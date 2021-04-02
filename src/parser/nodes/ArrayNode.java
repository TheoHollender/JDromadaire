package parser.nodes;

import java.util.ArrayList;

import parser.Node;
import parser.operators.ListOperator;
import variables.VariableContext;

public class ArrayNode extends Node implements ListOperator {

	private ArrayList<Object> objects = new ArrayList<>();
	
	public ArrayNode(int col, int line) {
		super(col, line);
	}
	
	public Object evaluate(VariableContext context) {
		return this;
	}
	
	public void add(Object o) {
		objects.add(o);
	}

	@Override
	public boolean set(NumberNode n, Object o) {
		objects.set((int) n.getValue(), o);
		return true;
	}

	@Override
	public boolean set(StringNode n, Object o) {return false;}

	@Override
	public Object get(NumberNode n) {
		return objects.get((int) n.getValue());
	}

	@Override
	public Object get(StringNode n) {return null;}

	@Override
	public int length() {
		return this.objects.size();
	}
	
	public String toString() {
		return this.objects.toString();
	}

}
