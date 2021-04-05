package parser.nodes.statements;

import java.util.ArrayList;

import main.EntryPoint;
import parser.Node;
import parser.nodes.BooleanNode;
import parser.nodes.FunctionNode;
import parser.nodes.ReturnNode;
import parser.nodes.innerreturn.InnerRNode;
import variables.VariableContext;

public class IfNode extends Node {
	
	private FunctionNode innerNode;
	private Node expr;
	public Node elser;
	
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
			if (elser instanceof FunctionNode) {
				data = ((FunctionNode)elser).evaluate(context, new ArrayList<>());
			} else {
				data = elser.evaluate(context);
			}
		}
		
		if (data instanceof ReturnNode) {
			return data;
		}
		if (data instanceof InnerRNode) {
			return data;
		}
		
		return null;
		
	}
	
}
