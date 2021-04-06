package parser;

import main.EntryPoint;
import main.TokenType;
import parser.nodes.BooleanNode;
import parser.operators.BinaryOperator;
import parser.operators.ComparateOperator;
import parser.operators.EvaluateOperator;
import variables.VariableContext;

public class OpNode extends Node {
	
	public static final TokenType[] COMPARE_TOKENS = new TokenType[] {
		TokenType.EQ, TokenType.INF, TokenType.NOTEQ, TokenType.SUP,
		TokenType.SUPEQ, TokenType.INFEQ,
	};
	
	final TokenType operator;
	private Node left;
	private Node right;
	public OpNode(TokenType op, Node left, Node right, int col, int line) {
		super(col, line);
		operator = op;
		this.left = left;
		this.right = right;
	}
	
	public Object OpEvaluate(VariableContext context) {
		Object leftv = left.evaluate(context);
		Object rightv = right.evaluate(context);
		if (leftv == null || rightv == null) {
			return null;
		}
		
		Object data = null;
		try {
			if (operator == TokenType.PLUS) {
				data = ((EvaluateOperator) leftv).add(rightv);
			}else if (operator == TokenType.MINUS) {
				data = ((EvaluateOperator) leftv).substract(rightv);
			}else if (operator == TokenType.MUL) {
				data = ((EvaluateOperator) leftv).multiply(rightv);
			}else if (operator == TokenType.DIV) {
				data = ((EvaluateOperator) leftv).divide(rightv);
			}else if (operator == TokenType.POW) {
				data = ((EvaluateOperator) leftv).power(rightv);
			}
			
			boolean isCompareToken = false;
			for (TokenType compare_token:COMPARE_TOKENS) {
				if(compare_token == operator) {
					isCompareToken = true;
					break;
				}
			}
			
			if(isCompareToken) {
				double compare = ((ComparateOperator)leftv).compare(rightv);
				
				boolean result = false;
				if (operator == TokenType.NOTEQ) {result = (compare != 0);}
				else if (operator == TokenType.SUP) {result = (compare > 0);}
				else if (operator == TokenType.SUPEQ) {result = (compare >= 0);}
				else if (operator == TokenType.EQ) {result = (compare == 0);}
				else if (operator == TokenType.INFEQ) {result = (compare <= 0);}
				else if (operator == TokenType.INF) {result = (compare < 0);}
				
				data = new BooleanNode(left.col, left.line, result);
			}
			
			if(operator == TokenType.OR) {
				data = ((BinaryOperator)leftv).or(rightv);
			} else if(operator == TokenType.AND) {
				data = ((BinaryOperator)leftv).and(rightv);
			}
		} catch(Exception e) {
			
		}
		
		if (data == null && !EntryPoint.raised) {
			System.out.println("Operation \'"+operator+"\' not allowed between "+leftv.getClass()+" and "+rightv.getClass());
			System.out.println("Error at line "+left.line);
		}
		
		return data;
	}
	
}
