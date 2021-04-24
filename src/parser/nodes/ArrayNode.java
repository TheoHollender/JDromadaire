package parser.nodes;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.Buffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.ArrayList;

import functions.base.array.ArrayFunctions;
import main.EntryPoint;
import parser.Node;
import parser.operators.ComparateOperator;
import parser.operators.IteratorOperator;
import parser.operators.ListOperator;
import variables.ClassNode;
import variables.VariableContext;

public class ArrayNode extends ClassNode implements ListOperator, IteratorOperator, ComparateOperator<ArrayNode> {

	public ArrayList<Object> array = new ArrayList<>();
	
	private static final FunctionNode add = new ArrayFunctions.AddFunction(-1, -1);
	private static final FunctionNode remove = new ArrayFunctions.RemoveFunction(-1, -1);
	private static final FunctionNode index = new ArrayFunctions.IndexFunction(-1, -1);
	private static final FunctionNode contains = new ArrayFunctions.ContainsFunction(-1, -1);
	
	public ArrayNode(int col, int line) {
		super(col, line);
		
		this.typeName = "array";
		
		isRoot = false;
		isSetable = true;
		
		set(new StringNode(-1, -1, "add"), add);
		set(new StringNode(-1, -1, "remove"), remove);
		set(new StringNode(-1, -1, "index"), index);
		set(new StringNode(-1, -1, "contains"), contains);
		set(new StringNode(-1, -1, "length"), new NumberNode(0,-1,-1));
		
		isSetable = false;
	}
	
	public Object evaluate(VariableContext context) {
		return this;
	}
	
	public void add(Object o) {
		this.isSetable = true;
		array.add(o);
		this.set(new StringNode(this.col, this.line, "length"), 
				new NumberNode(this.length(), this.col, this.line));
		this.isSetable = false;
	}
	
	public void remove(Object o) {
		this.isSetable = true;
		array.remove(o);
		this.set(new StringNode(this.col, this.line, "length"), 
				new NumberNode(this.length(), this.col, this.line));
		this.isSetable = false;
	}
	
	public boolean removeByIndex(int i) {
		if (i<0 || i>=this.array.size()) {
			EntryPoint.raiseErr("Index Out of bound Exception : tryed to remove index "+i+" for size "+this.array.size());
			return false;
		}
		this.isSetable = true;
		array.remove(i);
		this.set(new StringNode(this.col, this.line, "length"), 
				new NumberNode(this.length(), this.col, this.line));
		this.isSetable = false;
		return true;
	}

	@Override
	public boolean set(NumberNode n, Object o) {
		array.set((int) n.getNumber().intValue(), o);
		return true;
	}

	@Override
	public Object get(NumberNode n) {
		return array.get((int) n.getNumber().intValue());
	}

	@Override
	public int length() {
		return this.array.size();
	}
	
	public String toString() {
		return this.array.toString();
	}

	
	private class Iterator extends IteratorNode {

		private ArrayNode node;
		private int found = 0;
		public Iterator(int col, int line, ArrayNode arr) {
			super(col, line);
			node = arr;
		}
		
		public boolean hasNext() {
			return found < node.length();
		}
		
		public Object next() {
			found += 1;
			return node.get(new NumberNode(found - 1, -1, -1));
		}
		
	}
	
	@Override
	public IteratorNode getIterator(VariableContext context) {
		return new Iterator(col, line, this);
	}

	public void updateLength() {
		this.isSetable = true;
		this.set(new StringNode(this.col, this.line, "length"), 
				new NumberNode(this.length(), this.col, this.line));
		this.isSetable = false;
	}

	@Override
	public double compare(ArrayNode e) {
		if (e.length() < this.length()) {
			return -1;
		}
		if (e.length() > this.length()) {
			return -1;
		}
		
		for (int i = 0; i < e.length(); i++) {
			if (!e.array.get(i).equals(this.array.get(i))) {
				return -1;
			}
		}
		return 0;
	}
	
	public static Object undoComponentCast(Object base, Object end) {
		if (base instanceof Node) {
			Method m;
			try {
				m = base.getClass().getMethod("undoCast", new Class[] {Object.class, Object.class});
				return m.invoke(null, base, end);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return base.getClass().cast(end);
	}
	
	public static Object undoCast(Object base, Object end) {
		if (end instanceof Buffer) {
			end = ((Buffer) end).array();
		}
		
		if (base instanceof ArrayNode) {
			((ArrayNode) base).recoverNativeModifications(end);
			return base;
		}
		
		return base;
	}
	
	public void recoverNativeModifications(Object o) {
		for (int i=0; i<this.array.size(); i++) {
			this.array.set(i, undoComponentCast(array.get(i), Array.get(o, i)));
		}
	}
	
	public Object castTo(Class<?> type) {
		if (type.isArray()) {
			Object o = Array.newInstance(type.getComponentType(), this.array.size());
			
			for (int i=0; i<this.array.size(); i++)
	            Array.set(o, i, this.castItem(array.get(i), type.getComponentType()));
			
			return o;
		}
		
		if (type == FloatBuffer.class) {
			return this.castTo(float[].class);
		}
		if (type == IntBuffer.class) {
			return this.castTo(int[].class);
		}
		if (type == LongBuffer.class) {
			return this.castTo(long[].class);
		}
		if (type == DoubleBuffer.class) {
			return this.castTo(double[].class);
		}
		if (type == CharBuffer.class) {
			return this.castTo(char[].class);
		}
		
		return super.castTo(type);
	}

	private Object castItem(Object o, Class<?> componentType) {
		if (o instanceof Node) {
			return ((Node) o).castTo(componentType);
		}
		
		try {
			return componentType.cast(o);
		} catch (Exception e) {
			EntryPoint.raiseErr("Failed to cast component to "+componentType+" in native method");
			return null;
		}
	}

}
