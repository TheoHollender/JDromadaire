package main;

import java.util.ArrayList;
import java.util.List;

public class Scanner {
	
	static final String INT_CHARS = "0123456789";
	static final String FLOAT_CHARS = "0123456789.";
	static final String IGNORE_CHARS = " \t\n";
	static final String NAME_FIRST_CHARS = "abcdefghijklmnopqrstuvwxyz_ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	static final String NAME_CHARS = ".abcdefghijklmnopqrstuvwxyz_ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	static String OPERATOR_CHARS = "=<>|&!";
	
	static final char[] ONECHARTOKEN_CHAR = new char[] {
		'+', '-', '*', '/', '^', '(', ')', '\n', ';', '=', '[', ']', ',',
		':', '{' , '}', '!'
	};
	static final TokenType[] ONECHARTOKEN_TYPE = new TokenType[] {
		TokenType.PLUS, TokenType.MINUS, TokenType.MUL, TokenType.DIV,
		TokenType.POW, TokenType.LPAREN, TokenType.RPAREN, TokenType.EOF,
		TokenType.EOF, TokenType.SET, TokenType.LHOOK, TokenType.RHOOK,
		TokenType.COMMA, TokenType.TWO_POINTS, TokenType.LCURLYBRACKET,
		TokenType.RCURLYBRACKET, TokenType.NOT,
	};
	static final String[] STRINGTOKEN_STRING = new String[] {
		"function", "return", "true", "false", "if", "for", "break",
		"continue", "class", "else", "import", "from",
	};
	static final TokenType[] STRINGTOKEN_TYPE = new TokenType[] {
		TokenType.FUNCTION, TokenType.RETURN, TokenType.TRUE,
		TokenType.FALSE, TokenType.IF, TokenType.FOR, TokenType.BREAK,
		TokenType.CONTINUE, TokenType.CLASS, TokenType.ELSE, 
		TokenType.IMPORT, TokenType.FROM,
	};
	static final String[] OPERATORTOKEN_STRING = new String[] {
		"==", "<=", ">=", "!=", "&&", "||", "<", ">"
	};
	static final TokenType[] OPERATORTOKEN_TYPE = new TokenType[] {
		TokenType.EQ, TokenType.INFEQ, TokenType.SUPEQ,
		TokenType.NOTEQ, TokenType.AND, TokenType.OR,
		TokenType.INF, TokenType.SUP,
	};
	
	
	final String source;
	
	char current_char = '_';
	private int line = 0;
	private int col = -1;
	private int idx = -1;
	private boolean advanceResult = false;
	public Scanner(String source) {
		this.source = source;
	}
	
	public boolean advance() {
		if (current_char == '\n') {
			this.line += 1;
			this.col = -1;
		}
		
		this.col += 1;
		this.idx += 1;
		
		if (this.source.length() > this.idx) {
			this.current_char = this.source.charAt(this.idx);
			
			advanceResult = true;
			return true;
		}
		advanceResult = false;
		return false;
	}

	final int NO_COMMENTARY = 0;
	final int COMMENTARY_LINE = 1;
	final int COMMENTARY_AREA = 2;
	private int getCommentary(int actual) {
		if (actual == NO_COMMENTARY && this.current_char == '/') {
			int cp_id = this.idx;
			int cp_col = this.col;
			int cp_line = this.line;
			this.advance();
			if (this.current_char == '/') {
				return COMMENTARY_LINE;
			}
			if (this.current_char == '*') {
				return COMMENTARY_AREA;
			}
			
			this.idx = cp_id - 1;
			this.col = cp_col - 1;
			this.line = cp_line;
			this.advance();
		} else if (actual == COMMENTARY_LINE && this.current_char == '\n') {
			this.advance();
			return NO_COMMENTARY;
		} else if (actual == COMMENTARY_AREA && this.current_char == '*') {
			int cp_id = this.idx;
			int cp_col = this.col;
			int cp_line = this.line;
			this.advance();
			if (this.current_char == '/') {
				this.advance();
				return NO_COMMENTARY;
			}
			
			this.idx = cp_id - 1;
			this.col = cp_col - 1;
			this.line = cp_line;
			this.advance();
		}
		
		return actual;
	}
	
	public List<Token> scanTokens() {
		ArrayList<Token> tokens = new ArrayList<>();
		
		boolean hasToAdvance = true;
		int index;
		this.advance();
		int commentary = NO_COMMENTARY;
		while(this.advanceResult) {
			Token data = null;
			hasToAdvance = true;
			commentary = getCommentary(commentary);
			
			if (commentary == NO_COMMENTARY) {
				if (INT_CHARS.contains(String.valueOf(this.current_char))) {
					data = this.scanNumber();
					hasToAdvance = false;
				} else if ((index = this.scanOperator()) != -1) {
					data = new Token(OPERATORTOKEN_TYPE[index], this.col, this.line);
					hasToAdvance = false;
				} else if (this.current_char == '\"') {
					int baseLine = this.line;
					int baseCol = this.col;
					data = this.scanString();
					if (data == null) {
						System.out.println("String Not Finished");
						EntryPoint.raiseToken(new Token(null, baseLine, baseCol));
						return null;
					}
				} else if (NAME_FIRST_CHARS.contains(String.valueOf(this.current_char))) {
					data = this.scanName();
					hasToAdvance = false;
				} else if ((index = this.scanCharIndex()) != -1) {
					data = new Token(ONECHARTOKEN_TYPE[index], this.col, this.line);
				} else if (this.scanPointName(tokens)) {
					hasToAdvance = false;
				} else if (!this.isCharIgnore()) {
					System.out.println("Unexpected Character : \'" + this.current_char + "\'");
					EntryPoint.raiseToken(new Token(null, this.col, this.line));
					return null;
				}
			}
			
			if (data != null) {
				tokens.add(data);
			}
			if(hasToAdvance) {
				this.advance();
			}
		}
		
		tokens.add(new Token(TokenType.EOF, this.col, this.line));
		
		return tokens;
	}

	private boolean scanPointName(ArrayList<Token> tokens) {
		if (this.current_char == '.') {
			this.advance();
			
			if (NAME_FIRST_CHARS.contains(String.valueOf(this.current_char))) {
				Token data = this.scanName();
				
				tokens.add(new Token(TokenType.LHOOK, this.col, this.line));
				tokens.add(new Token(TokenType.STRING, data.value, data.col, data.line));
				tokens.add(new Token(TokenType.RHOOK, this.col, this.line));
				
				return true;
			}
			EntryPoint.raiseToken(new Token(TokenType.NAME, this.col, this.line));
		} else {
			return false;
		}
		return false;
	}

	private Token scanName() {
		StringBuffer string = new StringBuffer();
		
		while(this.advanceResult && NAME_CHARS.contains(String.valueOf(this.current_char))) {
			string.append(this.current_char);
			this.advance();
		}
		
		String s = string.toString();
		// Check Known Tokens (class, function, ...)
		for(int i = 0; i < STRINGTOKEN_STRING.length; i++) {
			if (s.equals(STRINGTOKEN_STRING[i])) {
				return new Token(STRINGTOKEN_TYPE[i], this.col, this.line);
			}
		}
		
		return new Token(TokenType.NAME, s, this.col, this.line);
	}
	
	private int scanOperator() {
		int sv_idx = this.idx;
		int sv_col = this.col;
		int sv_line = this.line;

		StringBuffer string = new StringBuffer();
		
		while(this.advanceResult && OPERATOR_CHARS.contains(String.valueOf(this.current_char))) {
			string.append(this.current_char);
			this.advance();
		}
		
		String s = string.toString();
		// Check Known Tokens (class, function, ...)
		for(int i = 0; i < OPERATORTOKEN_STRING.length; i++) {
			if (s.equals(OPERATORTOKEN_STRING[i])) {
				return i;
			}
		}
		
		this.idx = sv_idx - 1;
		this.col = sv_col - 1;
		this.line = sv_line;
		this.advance();
		
		return -1;
	}

	private Token scanString() {
		StringBuffer string = new StringBuffer();
		
		char lchar = this.current_char;
		this.advance();
		while(this.advanceResult) {
			if (this.current_char == '\"' && lchar != '\\') {
				return new Token(TokenType.STRING, string.toString(), this.col, this.line);
			}
			
			string.append(this.current_char);
			
			lchar = this.current_char;
			this.advance();
		}
		return null;
	}

	private boolean isCharIgnore() {
		return IGNORE_CHARS.contains(String.valueOf(this.current_char));
	}

	private int scanCharIndex() {
		int i = 0;
		for(char c:ONECHARTOKEN_CHAR) {
			if (this.current_char == c) {
				return i;
			}
			i++;
		}
		
		return -1;
	}

	public Token scanNumber() {
		StringBuffer numberString = new StringBuffer();
		int dot_count = 0;
		
		while (this.advanceResult && FLOAT_CHARS.contains(String.valueOf(this.current_char))) {
			if (this.current_char == '.') {
				if (dot_count == 1) {
					break;
				}
				
				dot_count += 1;
			}
			numberString.append(this.current_char);
			this.advance();
		}
		
		try {
			if (dot_count == 0) {
				return new Token(TokenType.NUMBER, Integer.parseInt(numberString.toString()), this.col, this.line);
			}
			return new Token(TokenType.NUMBER, Float.parseFloat(numberString.toString()), this.col, this.line);
		} catch(NumberFormatException e) {
			System.out.println("The number you requested is too big for the format used by JDrom");
			System.out.println("Error at line "+line+" at col "+col);
			EntryPoint.raised = true;
			return null;
		}
	}
	
}
