package functions;

import parser.nodes.IteratorNode;
import parser.nodes.NumberNode;

public class RangeIterator extends IteratorNode{

	private double counter;
	private double end;
	private double jump;
	public RangeIterator(int col, int line, double begin, double end, double jump) {
		super(col, line);
		this.counter = begin;
		this.end = end;
		this.jump = jump;
		// TODO Auto-generated constructor stub
	}
	public boolean hasNext() {
		if(this.jump>0) {
			return this.counter<this.end;
		}
		else {
			return this.counter>this.end;
		}
	}
    public Object next() {
    	this.counter+=this.jump;
    	return new NumberNode(this.counter - this.jump, this.col, this.line);
    }

}
