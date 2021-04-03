package parser.nodes.statements;

import java.util.ArrayList;

import main.EntryPoint;
import parser.Node;
import parser.nodes.BooleanNode;
import parser.nodes.FunctionNode;
import parser.nodes.IteratorNode;
import parser.nodes.ReturnNode;
import parser.nodes.getters.IteratorGetterNode;
import parser.nodes.innerreturn.BreakNode;
import parser.nodes.innerreturn.ContinueNode;
import parser.nodes.innerreturn.InnerRNode;
import variables.VariableContext;

public class ForNode extends Node {

	private FunctionNode f;
	
	private Node set;
	private Node comp;
	private Node adv;
	
	private IteratorGetterNode iterg;
	private String name;
	
	private int type = -1;
	public static final int ITERATOR = 0;
	public static final int EXPRESSIONS = 1;
	
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
		this.type = EXPRESSIONS;
	}
	
	public ForNode(int col, int line, FunctionNode n, IteratorGetterNode iterg, String name) {
		super(col, line);
		this.f = n;
		this.iterg = iterg;
		this.name = name;
		this.type = ITERATOR;
	}
	
	private int jcount = 0;
	public Object evaluate(VariableContext context) {
		
		jcount = 0;
		if (type == EXPRESSIONS) {
			set.evaluate(context);
			
			while(canContinue(context)) {
				Object data = null;
				if (jcount <= 0) {
					data = f.evaluate(context, new ArrayList<Object>());
				} else {
					jcount -= 1;
				}
				
				Object d0 = this.returnData(data);
				if (hasToReturn) {
					return d0;
				}
				
				adv.evaluate(context);
			}
		} else {
			IteratorNode iter = (IteratorNode) iterg.evaluate(context);
			
			if (iter != null) {
				while(iter.hasNext()) {
					context.setValue(name, iter.next());
					Object data = null;
					if (jcount <= 0) {
						data = f.evaluate(context, new ArrayList<Object>());
					} else {
						jcount -= 1;
					}
					
					Object d0 = this.returnData(data);
					if (hasToReturn) {
						return d0;
					}
				}
			}
		}
		
		return null;
		
	}

	private boolean hasToReturn = false;
	private Object returnData(Object data) {
		hasToReturn = true;
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
		
		hasToReturn = false;
		return null;
	}
	
}
