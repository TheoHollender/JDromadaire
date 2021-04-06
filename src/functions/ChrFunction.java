package functions;

import java.util.ArrayList;

import main.EntryPoint;
import parser.nodes.FunctionNode;
import parser.nodes.NumberNode;
import parser.nodes.StringNode;
import variables.VariableContext;

public class ChrFunction extends FunctionNode {

	public ChrFunction(int col, int line) {
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
		if (((Integer)((NumberNode)args.get(0)).getValue())<0) {
            EntryPoint.raiseErr("Expected positive Integer as first argument");
            return null;
		}
		int ch = (int) ((NumberNode)args.get(0)).getValue();
		return new StringNode(col, line, String.valueOf((char) ch));
	}
	
}
