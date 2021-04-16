package parser.nodes.statements;

import main.EntryPoint;
import parser.Node;
import variables.VariableContext;

public class GlobalizerNode extends Node {

	private Node next;
	
	public GlobalizerNode(int col, int line, Node n) {
		super(col, line);
		next = n;
	}
	
	public Object evaluate(VariableContext ctx) {
		return next.evaluate(EntryPoint.globalContext);
	}

}
