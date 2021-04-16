package parser.nodes;

import parser.Node;

public class IteratorNode extends Node {

	public IteratorNode(int col, int line) {
		super(col, line);
		this.typeName = "iterator";
	}
	
	public boolean hasNext() {return false;}
	public Object next() {return null;}

}
