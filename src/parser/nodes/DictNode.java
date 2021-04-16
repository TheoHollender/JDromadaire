package parser.nodes;

import java.util.ArrayList;
import java.util.HashMap;

import parser.Node;
import parser.operators.ListOperator;
import variables.VariableContext;

public class DictNode extends Node implements ListOperator {

	private HashMap<String, Object> objects = new HashMap<String, Object>();
	
	public DictNode(int col, int line) {
		super(col, line);
		this.typeName = "array";
	}
	
	public Object evaluate(VariableContext context) {
		return this;
	}
	
	public void add(Object o) {}

	@Override
	public boolean set(NumberNode n, Object o) {return false;}

	@Override
	public boolean set(StringNode n, Object o) {
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
