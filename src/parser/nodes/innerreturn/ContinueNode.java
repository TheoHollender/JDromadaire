package parser.nodes.innerreturn;

public class ContinueNode extends InnerRNode {

	private int base_ccount;
	private int base_jcount;
	
	private int ccount;
	private int jcount;
	
	public void reset() {
		this.ccount = this.base_ccount;
		this.jcount = this.base_jcount;
	}
	
	public ContinueNode(int col, int line, int ccount, int jcount) {
		super(col, line);
		this.base_ccount = ccount;
		this.base_jcount = jcount;
		this.reset();
	}
	
	public int getContinueCount() {
		return this.ccount;
	}
	public int getJumpCount() {
		return this.jcount;
	}
	public void decreaseContinueCount() {
		this.ccount -= 1;
	}

}
