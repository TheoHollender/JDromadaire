package parser.nodes;

import java.util.ArrayList;

import main.EntryPoint;
import parser.Node;
import parser.nodes.innerreturn.BreakNode;
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
		
		while(canContinue(context)) {
			Object data = f.evaluate(context, new ArrayList<Object>());
			
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
						return null;
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
