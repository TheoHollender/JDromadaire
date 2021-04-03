package parser.nodes.innerreturn;

public class BreakNode extends InnerRNode {

	private int bcount;
	private int base_bcount;
	
	public BreakNode(int col, int line, int bcount) {
		super(col, line);
		this.base_bcount = bcount;
		this.bcount = bcount;
	}
	
	public int getBreakCount() {
		return bcount;
	}
	
	public void reset() {
		this.bcount = this.base_bcount;
	}
	
	public void decreaseBreakCount() {
		bcount -= 1;
	}

}
