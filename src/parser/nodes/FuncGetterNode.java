package parser.nodes;

import java.util.ArrayList;

import main.EntryPoint;
import parser.Node;
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
				leftf.registerStack(context);
			}
			Object d = leftf.evaluate(context, args);
			if (creCont) {
				leftf.unregisterStack(context);
			}
			return d;
		} else if (lefte instanceof ClassNode) {
			return ((ClassNode)lefte).createInstance(new VariableContext(), args);
		} else {
			System.out.println("Function could not be found");
			EntryPoint.raiseNode(left);
		}
		
		return null;
	}

}
