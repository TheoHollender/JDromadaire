package parser.nodes;

import java.util.ArrayList;
import java.util.HashMap;

import main.EntryPoint;
import parser.Node;
import parser.operators.ListOperator;
import variables.VariableContext;

public class DictNode extends Node implements ListOperator {

	public HashMap<String, Object> objects = new HashMap<String, Object>();
	public boolean immutable = false;
	
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
		if (immutable) {
			EntryPoint.raiseErr("The Dict object that you requested is immutable");
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
