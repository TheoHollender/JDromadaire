package functions;

import parser.nodes.IteratorNode;
import parser.nodes.NumberNode;

public class RangeIterator extends IteratorNode{

	private int counter;
	private int end;
	private int jump;
	public RangeIterator(int col, int line, int begin, int end, int jump) {
		super(col, line);
		this.counter = begin;
		this.end = end;
		this.jump = jump;
		// TODO Auto-generated constructor stub
	}
	public boolean hasNext() {
		return this.counter<this.end;
	}
    public Object next() {
    	this.counter+=this.jump;
    	return new NumberNode(this.counter - this.jump, this.col, this.line);
    }

}
