package functions;

import parser.Node;
import parser.nodes.IteratorNode;
import parser.operators.IteratorOperator;
import variables.VariableContext;

public class RangeNode extends Node implements IteratorOperator{

	private RangeIterator range;
	public RangeNode(int col, int line, RangeIterator it ) {
		super(col, line);
		this.range = it;
		// TODO Auto-generated constructor stub
	}
	@Override
	public IteratorNode getIterator(VariableContext context) {
		// TODO Auto-generated method stub
		return this.range;
	}

}
