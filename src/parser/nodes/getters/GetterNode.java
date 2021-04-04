package parser.nodes.getters;

import main.EntryPoint;
import parser.Node;
import variables.VariableContext;

public class GetterNode extends Node {

	private final String name;
	public GetterNode(String name, int col, int line) {
		super(col, line);
		this.name = name;
	}
	
	public Object evaluate(VariableContext context) {
		Object d = context.getValue(this.name);
		if (d!=null) {
			return d;
		}
		return EntryPoint.globalContext.getValue(this.name);
	}

	public Object evaluateLast(VariableContext context) {
		Object d = context.getLastValue(this.name);
		if (d!=null) {
			return d;
		}
		return EntryPoint.globalContext.getLastValue(this.name);
	}
	
}
