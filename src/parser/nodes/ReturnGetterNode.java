package parser.nodes;

import parser.Node;
import variables.VariableContext;

public class ReturnGetterNode extends Node {

	private Node ex;
	public ReturnGetterNode(int col, int line, Node exp) {
		super(col, line);
		this.ex = exp;
	}

	public Object evaluate(VariableContext context) {
		Object dat = ex.evaluate(context);
		return new ReturnNode(this.col, this.line, dat);
	}
	
}
