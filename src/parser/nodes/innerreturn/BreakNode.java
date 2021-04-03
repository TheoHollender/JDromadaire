package parser.nodes.innerreturn;

public class BreakNode extends InnerRNode {

	private int bcount;
	
	public BreakNode(int col, int line, int bcount) {
		super(col, line);
		this.bcount = bcount;
	}
	
	public int getBreakCount() {
		return bcount;
	}
	
	public void decreaseBreakCount() {
		bcount -= 1;
	}

}
