package parser.nodes;

import parser.Node;
import parser.operators.BinaryOperator;
import parser.operators.ComparateOperator;
import parser.operators.UnaryOperator;
import variables.VariableContext;

public class BooleanNode extends Node implements UnaryOperator, BinaryOperator<BooleanNode>, ComparateOperator<BooleanNode> {

	public boolean value;
	public BooleanNode(int col, int line, boolean b) {
		super(col, line);
		this.value = b;
		this.typeName = "boolean";
	}
	
	public boolean equals(Object oth) {
		if (oth instanceof BooleanNode) {
			return value == ((BooleanNode)oth).value;
		}
		return false;
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

	@Override
	public double compare(BooleanNode e) {
		int a0 = 0;
		if (this.value) {a0 = 1;}
		int a1 = 0;
		if (e.value) {a1 = 1;}
		return a0 - a1;
	}

}
