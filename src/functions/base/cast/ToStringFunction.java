package functions.base.cast;

import java.util.ArrayList;

import main.EntryPoint;
import parser.nodes.FunctionNode;
import parser.nodes.StringNode;
import variables.VariableContext;

public class ToStringFunction extends FunctionNode {

	public ToStringFunction(int col, int line) {
		super(col, line);
	}

	public Object evaluate(VariableContext context, ArrayList<Object> args) {
		if (args.size() != 1) {
			EntryPoint.raiseErr("Expected 1 argument in str, got "+args.size());
			return null;
		}
		
		Object d = args.get(0).toString();
		if (d instanceof StringNode) {
			return d;
		}
		return new StringNode(-1, -1, args.get(0).toString());
	}
	
}
