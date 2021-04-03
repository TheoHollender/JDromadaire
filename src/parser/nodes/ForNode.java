package parser.nodes;

import java.util.ArrayList;

import main.EntryPoint;
import parser.Node;
import parser.nodes.innerreturn.BreakNode;
import parser.nodes.innerreturn.ContinueNode;
import parser.nodes.innerreturn.InnerRNode;
import variables.VariableContext;

public class ForNode extends Node {

	private FunctionNode f;
	private Node set;
	private Node comp;
	private Node adv;
	
	public boolean canContinue(VariableContext ctx) {
		Object dat = comp.evaluate(ctx);
		
		if (dat instanceof BooleanNode) {
			return ((BooleanNode)dat).value;
		}
		
		return false;
	}
	
	public ForNode(int col, int line, FunctionNode n, Node exprSet, Node exprComp, Node exprAdv) {
		super(col, line);
		this.f = n;
		this.set = exprSet;
		this.comp = exprComp;
		this.adv = exprAdv;
	}
	
	public Object evaluate(VariableContext context) {
		
		set.evaluate(context);
		
		int jcount = 0;
		while(canContinue(context)) {
			Object data = null;
			if (jcount == 0) {
				data = f.evaluate(context, new ArrayList<Object>());
			} else {
				jcount -= 1;
			}
			
			if(data instanceof ReturnNode) {
				return data;
			}
			
			if (data instanceof InnerRNode) {
				if (data instanceof BreakNode) {
					BreakNode bn = (BreakNode) data;
					if(bn.getBreakCount() > 0) {
						if (bn.getBreakCount() > 1) {
							bn.decreaseBreakCount();
							return bn;
						}
						bn.reset();
						return null;
					}
				} else if (data instanceof ContinueNode) {
					ContinueNode cn = (ContinueNode) data;
					if (cn.getContinueCount() > 0) {
						if (cn.getContinueCount() > 1) {
							cn.decreaseContinueCount();
							return cn;
						}
						cn.reset();
						jcount = cn.getJumpCount() - 1;
					}
				} else {
					return data;
				}
			}
			
			adv.evaluate(context);
		}
		
		return null;
		
	}

}
