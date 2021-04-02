package parser.nodes;

import java.util.ArrayList;

import main.EntryPoint;
import parser.Node;
import variables.VariableContext;

public class IfNode extends Node {
	
	private FunctionNode innerNode;
	private Node expr;
	private Node elser;
	
	public IfNode(int col, int line, FunctionNode in, Node exp) {
		super(col, line);
		this.innerNode = in;
		this.expr = exp;
	}
	
	public Object evaluate(VariableContext context) {
		
		Object data = null;
		
		Object ev = expr.evaluate(context);
		
		if (!(ev instanceof BooleanNode)) {
			System.out.println("Expected boolean in IF statement, got "+ev.getClass().toString());
			EntryPoint.raiseNode(expr);
			return null;
		}
		
		if (((BooleanNode)ev).value) {
			data = innerNode.evaluate(context, new ArrayList<>());
		} else if (elser != null) {
			data = elser.evaluate(context);
		}
		
		if (data instanceof ReturnNode) {
			return data;
		}
		
		return null;
		
	}
	
}
