package parser;

import main.EntryPoint;
import main.TokenType;
import parser.operators.UnaryOperator;
import variables.VariableContext;

public class UnaryOpNode extends Node {
	
	final TokenType operator;
	private Node left;
	public UnaryOpNode(TokenType op, Node left, int col, int line) {
		super(col, line);
		operator = op;
		this.left = left;
	}
	

	public Object OpEvaluate(VariableContext context) {
		Object leftv = left.evaluate(context);
		if (leftv == null) {
			return null;
		}
		
		Object data = null;
		try {
			if (operator == TokenType.MINUS || operator == TokenType.NOT) {
				data = (((UnaryOperator) leftv).invert());
			}
		} catch(Exception e) {
			
		}
		
		if (data == null && !EntryPoint.raised) {
			System.out.println("Unary Operation \'"+operator+"\' not allowed for "+leftv.getClass());
			System.out.println("Error at line "+left.line);
		}
		
		return data;
	}
	
}
