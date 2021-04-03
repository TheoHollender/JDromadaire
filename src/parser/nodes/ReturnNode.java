package parser.nodes;

import parser.Node;
import variables.VariableContext;

public class ReturnNode extends Node {

	private Object o;
	public ReturnNode(int col, int line, Object value) {
		super(col, line);
		o = value;
	}

	public Object evaluate(VariableContext ctx) {
		return o;
	}
	
}
