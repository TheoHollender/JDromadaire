package parser.nodes.getters;

import parser.Node;
import parser.operators.IteratorOperator;
import variables.VariableContext;

public class IteratorGetterNode extends Node {

	private Node left;
	public IteratorGetterNode(int col, int line, Node left) {
		super(col, line);
		this.left = left;
	}
	
	public Object evaluate(VariableContext context) {
		Object dat = left.evaluate(context);
		
		if (dat instanceof IteratorOperator) {
			return ((IteratorOperator)dat).getIterator(context);
		}
		
		return null;
	}

}
