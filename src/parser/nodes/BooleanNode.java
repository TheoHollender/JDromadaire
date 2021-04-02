package parser.nodes;

import parser.Node;
import parser.operators.BinaryOperator;
import parser.operators.UnaryOperator;
import variables.VariableContext;

public class BooleanNode extends Node implements UnaryOperator, BinaryOperator<BooleanNode> {

	public boolean value;
	public BooleanNode(int col, int line, boolean b) {
		super(col, line);
		this.value = b;
	}
	
	public String toString() {
		return String.valueOf(this.value);
	}
	
	public Object evaluate(VariableContext context) {
		return this;
	}
	
	@Override
	public Object invert() {
		return new BooleanNode(this.col, this.line, !this.value);
	}

	@Override
	public Object or(BooleanNode e) {
		return new BooleanNode(this.col, this.line, this.value || e.value);
	}

	@Override
	public Object and(BooleanNode e) {
		return new BooleanNode(this.col, this.line, this.value && e.value);
	}

}
