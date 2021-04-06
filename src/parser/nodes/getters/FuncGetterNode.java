package parser.nodes.getters;

import java.util.ArrayList;

import main.EntryPoint;
import parser.Node;
import parser.nodes.FunctionNode;
import parser.nodes.ReturnNode;
import parser.nodes.innerreturn.BreakNode;
import parser.nodes.innerreturn.ContinueNode;
import parser.nodes.innerreturn.InnerRNode;
import variables.ClassNode;
import variables.VariableContext;

public class FuncGetterNode extends Node {

	private Node left;
	private ArrayList<Node> exprs;
	private boolean creCont = true;
	public FuncGetterNode(int col, int line, Node n, ArrayList<Node> exprs) {
		super(col, line);
		this.left = n;
		this.exprs = exprs;
	}
	public FuncGetterNode(int col, int line, Node n, ArrayList<Node> exprs, boolean creCont) {
		super(col, line);
		this.left = n;
		this.exprs = exprs;
		this.creCont = creCont;
	}

	public Object evaluate(VariableContext context) {
		ArrayList<Object> args = new ArrayList<Object>();
		Object thisObj = null;
		if (this.left instanceof GetterNode) {
			thisObj = ((GetterNode)this.left).evaluateLast(context);
		}
		if(thisObj instanceof ClassNode) {
			if (!((ClassNode)thisObj).isRoot) {
				args.add(thisObj);
			}
		}
		for(Node n:exprs) {
			args.add(n.evaluate(context));
		}
		
		Object lefte = this.left.evaluate(EntryPoint.globalContext);
		
		if (lefte instanceof FunctionNode) {
			FunctionNode leftf = (FunctionNode) lefte;
			
			if (creCont) {
				context = new VariableContext();
			}
			
			if (creCont) {
				EntryPoint.registerStack(context);
				EntryPoint.setStackName(leftf.name);
			}
			Object d = leftf.evaluate(context, args);
			if (creCont) {
				EntryPoint.unregisterStack(context);
			}
			
			if (d instanceof ReturnNode) {
				return ((ReturnNode)d).evaluate(context);
			}
			if (d instanceof InnerRNode) {
				if (d instanceof BreakNode) {
					EntryPoint.raiseErr("Too many breaks occured");
					((BreakNode)d).reset();
					return null;
				}
				if (d instanceof ContinueNode) {
					EntryPoint.raiseErr("Continue count too high");
					((ContinueNode)d).reset();
					return null;
				}
			}
			return d;
		} else if (lefte instanceof ClassNode) {
			return ((ClassNode)lefte).createInstance(new VariableContext(), args);
		} else {
			EntryPoint.raiseErr("Function could not be found");
		}
		
		return null;
	}

}
