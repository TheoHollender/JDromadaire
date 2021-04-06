package functions;

import java.util.ArrayList;

import main.EntryPoint;
import parser.nodes.ArrayNode;
import parser.nodes.FunctionNode;
import parser.nodes.NumberNode;
import variables.VariableContext;

public class SumFunction extends FunctionNode {

	public SumFunction(int col, int line) {
		super(col, line);
	}

	public Object evaluate(VariableContext context, ArrayList<Object> args) {
		if(args.size()!=1) {
            EntryPoint.raiseErr("Expected one argument, got "+args.size());
            return null;
		}
		if (!(args.get(0) instanceof ArrayNode)) {
            EntryPoint.raiseErr("Expected array as first argument, got "+args.get(0).getClass().toString());
            return null;
		}
		double sum = 0;
		for(Object i:((ArrayNode) args.get(0)).array) {
			if (!(i instanceof NumberNode)) {
	            EntryPoint.raiseErr("Expected array of numbers, got "+i.getClass().toString());
	            return null;
			}
			sum+=((NumberNode)i).getDoubleValue();
		}
		return new NumberNode(sum,col,line);
	}
	
}
