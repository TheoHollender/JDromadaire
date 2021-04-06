package parser.nodes;

import java.util.ArrayList;

import functions.base.array.ArrayFunctions;
import main.EntryPoint;
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
		array.set((int) n.getValue(), o);
		return true;
	}

	@Override
	public Object get(NumberNode n) {
		return array.get((int) n.getValue());
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
	public int compare(ArrayNode e) {
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

}
