package parser;

import parser.nodes.NumberNode;
import variables.VariableContext;

public class SetterNode extends Node {

	public Node left;
	public String name;
	public SetterNode(Node left, String name, int col, int line) {
		super(col, line);
		this.left = left;
		this.name = name;
	}
	
	public Object evaluate(VariableContext context) {
		Object value = this.left.evaluate(context);
		
		if (value != null) {
			context.setValue(this.name, value);
		}
		
		return null;
	}
}
