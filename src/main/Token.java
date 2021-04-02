package main;

public class Token {
	public final TokenType type;
	public final Object value;
	public final int col;
	public final int line;
	public Token (TokenType type, Object value, int col, int line) {
		this.type = type;
		this.value = value;
		this.col = col;
		this.line = line;
	}
	public Token (TokenType type, int col, int line) {
		this.type = type;
		this.value = null;
		this.col = col;
		this.line = line;
	}
	public String toString() {
		if (this.value != null) {
			return this.type + ":" + this.value;
		}
		return this.type.toString();
	}
}
