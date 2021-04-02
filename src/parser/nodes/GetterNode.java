package parser.nodes;

import parser.Node;
import variables.VariableContext;

public class GetterNode extends Node {

	private final String name;
	public GetterNode(String name, int col, int line) {
		super(col, line);
		this.name = name;
	}
	
	public Object evaluate(VariableContext context) {
		return context.getValue(this.name);
	}

	public Object evaluateLast(VariableContext context) {
		return context.getLastValue(this.name);
	}
	
}
