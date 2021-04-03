package parser.operators;

import parser.nodes.IteratorNode;
import variables.VariableContext;

public interface IteratorOperator {

	public IteratorNode getIterator(VariableContext context);
	
}
