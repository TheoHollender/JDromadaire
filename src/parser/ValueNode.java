package parser;

public class ValueNode extends Node {

	public Object value;
	public ValueNode(Object value, int col, int line) {
		super(col, line);
		this.value = value;
	}
	
	public Object getValue() {
		return this.value;
	}

}
