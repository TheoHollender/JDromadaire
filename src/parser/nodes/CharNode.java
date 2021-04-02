package parser.nodes;

import parser.Node;
import parser.operators.EvaluateOperator;

public class CharNode extends Node implements EvaluateOperator {

	private char c;
	
	public CharNode(int col, int line, char c) {
		super(col, line);
		this.c=c;
	}
	
	public Object evaluate() {
		return this;
	}
	
	public char getValue() {
		return this.c;
	}
	
	public String toString() {
		return "\'"+String.valueOf(this.c)+"\'";
	}

	@Override
	public Object add(Object e) {
		if (e instanceof StringNode) {
			return new StringNode(this.col, this.line, this.c + ((StringNode)e).getValue());
		}
		if (e instanceof CharNode) {
			return new StringNode(this.col, this.line, this.c + "" + ((CharNode)e).getValue());
		}
		if (e instanceof NumberNode) {
			return new StringNode(this.col, this.line, this.c + String.valueOf(((NumberNode)e).getValue()));
		}
		return null;
	}

	@Override
	public Object substract(Object e) {return null;}

	@Override
	public Object multiply(Object e) {return null;}

	@Override
	public Object divide(Object e) {return null;}

	@Override
	public Object power(Object e) {return null;}

}
