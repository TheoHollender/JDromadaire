package functions.math;

import java.util.ArrayList;

import main.EntryPoint;
import parser.nodes.FunctionNode;
import parser.nodes.NumberNode;
import variables.VariableContext;

public class FacFunction extends FunctionNode {

	public FacFunction(int col, int line) {
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
		if (!(((NumberNode)args.get(0)).getValue() instanceof Integer)) {
            EntryPoint.raiseErr("Expected integer as first argument, got "+((NumberNode)args.get(0)).getValue() .getClass().toString());
            return null;
		}
		if (((Integer)((NumberNode)args.get(0)).getValue())<0 || ((Integer)((NumberNode)args.get(0)).getValue())>12) {
            EntryPoint.raiseErr("Expected positive Integer less than 12 as first argument");
            return null;
		}
		int nb = (int) ((NumberNode)args.get(0)).getValue();
		int fac = 1;
		for(int i=1;i<nb+1;i++) {
			fac*=i;
		}
		return new NumberNode(fac,col,line);
	}
	
}
