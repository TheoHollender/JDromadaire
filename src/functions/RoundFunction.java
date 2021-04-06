package functions;

import java.util.ArrayList;

import main.EntryPoint;
import parser.nodes.ArrayNode;
import parser.nodes.FunctionNode;
import parser.nodes.NumberNode;
import variables.VariableContext;

public class RoundFunction extends FunctionNode {

	public RoundFunction(int col, int line) {
		super(col, line);
	}

	public Object evaluate(VariableContext context, ArrayList<Object> args) {
		double pre = 0;
		double nb = 1;
		if(args.size()==1) {
			if (!(args.get(0) instanceof NumberNode)) {
	            EntryPoint.raiseErr("Expected number as first argument, got "+args.get(0).getClass().toString());
	            return null;
			}
			nb = (double) ((NumberNode)args.get(0)).getDoubleValue();
		}
		else if(args.size()==2) {
			if (!(args.get(0) instanceof NumberNode)) {
	            EntryPoint.raiseErr("Expected number as first argument, got "+args.get(0).getClass().toString());
	            return null;
			}
			if (!(args.get(1) instanceof NumberNode)) {
	            EntryPoint.raiseErr("Expected number as second argument, got "+args.get(1).getClass().toString());
	            return null;
			}
			nb = (double) ((NumberNode)args.get(0)).getDoubleValue();
			pre = (double) ((NumberNode)args.get(1)).getDoubleValue();
		}
		else {
			EntryPoint.raiseErr("1 or 2 arguments expected, "+args.size()+" received");
			return null;
		}
		return new NumberNode(Math.round(nb*Math.pow(10, pre))/Math.pow(10, pre),col,line);
	}
	
}
