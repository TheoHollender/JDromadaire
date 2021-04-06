package parser.nodes;

import main.EntryPoint;
import parser.Node;
import parser.ValueNode;
import parser.operators.ComparateOperator;
import parser.operators.EvaluateOperator;
import parser.operators.UnaryOperator;
import variables.VariableContext;

public class NumberNode extends ValueNode implements EvaluateOperator<NumberNode>,UnaryOperator,ComparateOperator<NumberNode> {

	public NumberNode(Object value, int col, int line) {
		super(value, col, line);
	}
	
	public boolean equals(Object other) {
		if (other instanceof NumberNode) {
			return this.value == ((NumberNode)other).value;
		}
		return false;
	}
	
	public String toString() {
		return String.valueOf(this.getValue());
	}
	
	public Object getMaxPriority(NumberNode e, double value) {
		if (e.getValue() instanceof Double || this.getValue() instanceof Double) {
			return new NumberNode(value, col, line);
		}
		if (e.getValue() instanceof Float || this.getValue() instanceof Float) {
			return new NumberNode((float)value, col, line);
		}
		return new NumberNode((int)value, col, line);
	}
	public Object getMaxPriority(double value) {
		if (this.getValue() instanceof Double) {
			return new NumberNode(value, col, line);
		}
		if (this.getValue() instanceof Float) {
			return new NumberNode((float)value, col, line);
		}
		return new NumberNode((int)value, col, line);
	}
	
	public double getDoubleValue() {
		if (this.getValue() instanceof Integer) {
			return (double) (int) this.getValue();
		}else if (this.getValue() instanceof Float) {
			return (double) (float) this.getValue();
		}
		return (double) this.getValue();
	}

	public Node evaluate(VariableContext context) {
		return this;
	}
	
	@Override
	public Object add(NumberNode e) {
		return this.getMaxPriority(e, this.getDoubleValue() + e.getDoubleValue());
	}

	@Override
	public Object substract(NumberNode e) {
		return this.getMaxPriority(e, this.getDoubleValue() - e.getDoubleValue());
	}

	@Override
	public Object multiply(NumberNode e) {
		return this.getMaxPriority(e, this.getDoubleValue() * e.getDoubleValue());
	}

	@Override
	public Object divide(NumberNode e) {
		if (e.getDoubleValue() == 0) {
			EntryPoint.raiseErr("Division by Zero Error");
			return null;
		}
		if(this.getDoubleValue() % e.getDoubleValue() == 0) {
			return this.getMaxPriority(e, this.getDoubleValue() / e.getDoubleValue());
		}
		else {
			return this.getMaxPriority(new NumberNode(this.getDoubleValue() / e.getDoubleValue(),col,line), this.getDoubleValue() / e.getDoubleValue());
		}
	}

	@Override
	public Object power(NumberNode e) {
		return this.getMaxPriority(e, Math.pow(this.getDoubleValue(), e.getDoubleValue()));
	}

	@Override
	public Object invert() {
		return this.getMaxPriority(-this.getDoubleValue());
	}

	@Override
	public int compare(NumberNode e) {
		return (Integer)this.value - (Integer)e.value;
	}

}
