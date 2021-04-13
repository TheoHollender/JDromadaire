package parser.nodes.builders;

import java.util.ArrayList;

import parser.Node;
import parser.nodes.ArrayNode;
import variables.VariableContext;

public class ArrayBuilder extends Node {

	public ArrayBuilder(int col, int line) {
		super(col, line);
	}

	private ArrayList<Node> exprs = new ArrayList<Node>();

	public void add(Node expr) {
		exprs.add(expr);
	}
	
	public Object evaluate(VariableContext ctx) {
		ArrayNode node = new ArrayNode(this.col, this.line);
		
		node.array = new ArrayList<Object>(exprs.size());
		
		for (Node n:exprs) {
			node.array.add(n.evaluate(ctx));
		}
		
		node.updateLength();
		
		return node;
	}
	
}
