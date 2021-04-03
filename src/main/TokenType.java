package main;

public enum TokenType {
	
	// Single Characters                    // =
	COMMA, PLUS, MINUS, MUL, DIV, POW, EOF, SET,
	LPAREN, RPAREN, LHOOK, RHOOK, TWO_POINTS,
	LCURLYBRACKET, RCURLYBRACKET, NOT,
	
	// Name
	NAME,
	
	// Comparators
	EQ, INF, SUP, INFEQ, SUPEQ, NOTEQ,
	
	// Boolean Operators
	AND, OR,
	
	// Base Objects
	STRING, NUMBER, TRUE, FALSE,
	
	// String Objects
	FUNCTION, RETURN, IF, FOR, BREAK, CONTINUE,

}
