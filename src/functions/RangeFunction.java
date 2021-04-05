package functions;

import java.util.ArrayList;

import main.EntryPoint;
import parser.nodes.ArrayNode;
import parser.nodes.FunctionNode;
import parser.nodes.NumberNode;
import variables.VariableContext;

public class RangeFunction extends FunctionNode {

	public RangeFunction(int col, int line) {
		super(col, line);
	}

	public Object evaluate(VariableContext context, ArrayList<Object> args) {
		int debut = 0;
		int rng = 1;
		int fin = 1;
		if(args.size()==1) {
			if (!(args.get(0) instanceof NumberNode)) {
	            System.out.println("Expected number as first argument, got "+args.get(0).getClass().toString());
	            EntryPoint.raiseNode(this);
	            return null;
			}
			fin = (int) ((NumberNode)args.get(0)).getValue();
		}
		else if(args.size()==2) {
			if (!(args.get(0) instanceof NumberNode)) {
	            System.out.println("Expected number as first argument, got "+args.get(0).getClass().toString());
	            EntryPoint.raiseNode(this);
	            return null;
			}
			if (!(args.get(1) instanceof NumberNode)) {
	            System.out.println("Expected number as second argument, got "+args.get(0).getClass().toString());
	            EntryPoint.raiseNode(this);
	            return null;
			}
			debut = (int) ((NumberNode)args.get(0)).getValue();
			fin = (int) ((NumberNode)args.get(1)).getValue();
		}
		else if(args.size()==3) {
			if (!(args.get(0) instanceof NumberNode)) {
	            System.out.println("Expected number as first argument, got "+args.get(0).getClass().toString());
	            EntryPoint.raiseNode(this);
	            return null;
			}
			if (!(args.get(1) instanceof NumberNode)) {
	            System.out.println("Expected number as second argument, got "+args.get(0).getClass().toString());
	            EntryPoint.raiseNode(this);
	            return null;
			}
			if (!(args.get(2) instanceof NumberNode)) {
	            System.out.println("Expected number as third argument, got "+args.get(0).getClass().toString());
	            EntryPoint.raiseNode(this);
	            return null;
			}
			debut = (int) ((NumberNode)args.get(0)).getValue();
			fin = (int) ((NumberNode)args.get(1)).getValue();
			rng = (int) ((NumberNode)args.get(2)).getValue();
		}
		else {
			System.out.println("1,2 or 3 arguments expected, "+args.size()+" received");
			EntryPoint.raiseNode(this);
			return null;
		}
		return new RangeNode(this.col, this.line, new RangeIterator(this.col, this.line, debut, fin, rng));
	}
	
}
