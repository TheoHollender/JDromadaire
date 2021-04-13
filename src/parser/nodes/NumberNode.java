package parser.nodes;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import ch.obermuhlner.math.big.BigDecimalMath;
import main.EntryPoint;
import parser.Node;
import parser.ValueNode;
import parser.operators.ComparateOperator;
import parser.operators.EvaluateOperator;
import parser.operators.UnaryOperator;
import variables.VariableContext;

public class NumberNode extends Node implements EvaluateOperator<NumberNode>,UnaryOperator,ComparateOperator<NumberNode> {

	public NumberNode(BigDecimal value, int col, int line) {
		super(col, line);
		this.number = value;
	}
	
	public NumberNode(Object value, int col, int line) {
		super(col, line);
		if (value instanceof Integer) {
			number = BigDecimal.valueOf((int) value);
		}
		if (value instanceof Long) {
			number = BigDecimal.valueOf((long) value);
		}
		if (value instanceof Float) {
			number = BigDecimal.valueOf((float) value);
		}
		if (value instanceof Double) {
			number = BigDecimal.valueOf((double) value);
		}
		if (value instanceof String) {
			number = new BigDecimal((String)value);
		}
	}

	private BigDecimal number;
	public BigDecimal getNumber() {
		return number;
	}
	public boolean isInt() {
		return number.stripTrailingZeros().scale() <= 0;
	}
	public boolean isIntegerRange() {
		return number.compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) < 0
				&& number.compareTo(BigDecimal.valueOf(Integer.MIN_VALUE)) > 0;
	}
	public boolean isLong() {
		return number.stripTrailingZeros().scale() <= 0;
	}
	public boolean isLongRange() {
		return number.compareTo(BigDecimal.valueOf(Long.MAX_VALUE)) < 0
				&& number.compareTo(BigDecimal.valueOf(Long.MIN_VALUE)) > 0;
	}
	
	public boolean equals(Object other) {
		if (other instanceof NumberNode) {
			return this.getNumber().equals(((NumberNode) other).getNumber());
		}
		return false;
	}
	
	public String toString() {
		return String.valueOf(this.getNumber());
	}
	
	public Node evaluate(VariableContext context) {
		return this;
	}
	
	@Override
	public Object add(NumberNode e) {
		return new NumberNode(this.getNumber().add(e.getNumber()), this.col, this.line);
	}

	@Override
	public Object substract(NumberNode e) {
		return new NumberNode(this.getNumber().subtract(e.getNumber()), this.col, this.line);
	}

	@Override
	public Object multiply(NumberNode e) {
		return new NumberNode(this.getNumber().multiply(e.getNumber()), this.col, this.line);
	}

	public static final int MAX_DIVIDE_SCALE = 100;
	
	@Override
	public Object divide(NumberNode e) {
		if (e.getNumber().equals(BigDecimal.ZERO)) {
			EntryPoint.raiseErr("Division by Zero Error");
			return null;
		}
		int scaleMax = Math.max(e.getNumber().scale(), this.getNumber().scale());
		BigDecimal n = this.getNumber().divide(e.getNumber(), scaleMax + MAX_DIVIDE_SCALE, RoundingMode.HALF_UP).stripTrailingZeros();
		return new NumberNode(n.setScale(Math.max(0, n.scale())), this.col, this.line);
	}

	@Override
	public Object power(NumberNode e) {
		return new NumberNode(BigDecimalMath.pow(this.getNumber(), e.getNumber(), MathContext.DECIMAL64), this.col, this.line);
	}

	@Override
	public Object invert() {
		return new NumberNode(this.getNumber().negate(), this.col, this.line);
	}

	@Override
	public double compare(NumberNode e) {
		return (this.getNumber().subtract(e.getNumber())).divide(this.getNumber().subtract(e.getNumber()).abs()).doubleValue();
	}

}
