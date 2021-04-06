package functions.math;

import java.util.ArrayList;

import main.EntryPoint;
import parser.nodes.FunctionNode;
import parser.nodes.NumberNode;
import variables.VariableContext;

public class SqrtFunction extends FunctionNode {

	public SqrtFunction(int col, int line) {
		super(col, line);
	}

	public Object evaluate(VariableContext context, ArrayList<Object> args) {
		if(args.size()!=1) {
			EntryPoint.raiseErr("Expected 1 argument, got "+args.size());
            return null;
		}
		if (!(args.get(0) instanceof NumberNode)) {
            EntryPoint.raiseErr("Expected number as first argument, got "+args.get(0).getClass().toString());
            return null;
		}
		double nb = (double) ((NumberNode)args.get(0)).getDoubleValue();
		return new NumberNode(Math.sqrt(nb),col,line);
	}
	
}
