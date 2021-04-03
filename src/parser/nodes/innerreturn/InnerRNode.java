package parser.nodes.innerreturn;

import parser.Node;
import variables.VariableContext;

public class InnerRNode extends Node {

	public InnerRNode(int col, int line) {
		super(col, line);
	}
	
	public Object evaluate(VariableContext context) {
		return this;
	}

}
