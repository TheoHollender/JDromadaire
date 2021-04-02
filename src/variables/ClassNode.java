package variables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import parser.Node;
import parser.nodes.FunctionNode;
import parser.nodes.NumberNode;
import parser.nodes.StringNode;
import parser.operators.ListOperator;

public class ClassNode extends Node implements ListOperator {

	public boolean isRoot = true;
	protected boolean isSetable = true;
	public ClassNode(int col, int line) {
		super(col, line);
	}
	
	protected ClassNode(ClassNode other, ArrayList<Object> args) {
		super(other.col, other.line);
		isRoot = false;
		
		for(Entry<String, Object> dat:other.objects.entrySet())  {
			this.objects.put(dat.getKey(), dat.getValue());
		}
	}

	public Object createInstance(VariableContext context, ArrayList<Object> args) {
		if(!isRoot) {
			System.out.println("Can't create instance with sub child");
			return null;
		}
		return new ClassNode(this, args);
	}
	
	public HashMap<String, Object> objects = new HashMap<String, Object>();
	
	public Object evaluate(VariableContext context) {
		return this;
	}
	
	public void add(Object o) {}

	@Override
	public boolean set(NumberNode n, Object o) {return false;}

	@Override
	public boolean set(StringNode n, Object o) {
		if(!isSetable) {
			return false;
		}
		this.objects.put(n.getValue(), o);
		return true;
	}

	@Override
	public Object get(NumberNode n) {return null;}

	@Override
	public Object get(StringNode n) {
		return this.objects.get(n.getValue());
	}

	@Override
	public int length() {
		return 0;
	}
	
	public String toString() {
		return this.objects.toString();
	}

	
	
}
