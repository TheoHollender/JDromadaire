package functions;

import java.math.BigDecimal;

import parser.nodes.IteratorNode;
import parser.nodes.NumberNode;

public class RangeIterator extends IteratorNode{

	private BigDecimal counter;
	private BigDecimal end;
	private BigDecimal jump;
	public RangeIterator(int col, int line, BigDecimal debut, BigDecimal fin, BigDecimal rng) {
		super(col, line);
		this.counter = debut;
		this.end = fin;
		this.jump = rng;
		// TODO Auto-generated constructor stub
	}
	public boolean hasNext() {
		if(this.jump.compareTo(BigDecimal.ZERO) > 0) {
			return this.counter.compareTo(this.end) < 0;
		}
		else {
			return this.counter.compareTo(this.end) > 0;
		}
	}
    public Object next() {
    	this.counter = this.counter.add(this.jump);
    	return new NumberNode(this.counter.subtract(this.jump), this.col, this.line);
    }

}
