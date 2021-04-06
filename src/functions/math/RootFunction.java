package functions.math;

import java.util.ArrayList;

import main.EntryPoint;
import parser.nodes.FunctionNode;
import parser.nodes.NumberNode;
import variables.VariableContext;

public class RootFunction extends FunctionNode {

	public RootFunction(int col, int line) {
		super(col, line);
	}

	public Object evaluate(VariableContext context, ArrayList<Object> args) {
		if(args.size()!=2) {
			EntryPoint.raiseErr("Expected 1 argument, got "+args.size());
            return null;
		}
		if (!(args.get(0) instanceof NumberNode) || !(args.get(1) instanceof NumberNode)) {
            EntryPoint.raiseErr("Expected number for arguments, got "+args.get(0).getClass().toString()+" and "+args.get(1).getClass().toString());
            return null;
		}
		double nb = (double) ((NumberNode)args.get(0)).getDoubleValue();
		double nb2 = (double) ((NumberNode)args.get(0)).getDoubleValue();
		return new NumberNode(Math.pow(nb,1/nb2),col,line);
	}
	
}
