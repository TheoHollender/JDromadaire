package functions;

import java.util.ArrayList;
import java.util.HashMap;

import main.EntryPoint;
import parser.nodes.FunctionNode;
import parser.nodes.NumberNode;
import parser.nodes.StringNode;
import variables.VariableContext;

public class OrdFunction extends FunctionNode {

	public OrdFunction(int col, int line) {
		super(col, line);
	}

	public Object evaluate(VariableContext context, ArrayList<Object> args, HashMap<StringNode, Object> kwargs_entry) {
		if(args.size()!=1) {
			EntryPoint.raiseErr("Expected 1 argument, got "+args.size());
            return null;
		}
		if (!(args.get(0) instanceof StringNode)) {
            EntryPoint.raiseErr("Expected string as first argument, got "+args.get(0).getClass().toString());
            return null;
		}
		String ch = (String) ((StringNode)args.get(0)).getValue();
		if(ch.length()!=1) {
            EntryPoint.raiseErr("Expected one character, got "+ch.length());
            return null;
		}
		return new NumberNode((int) ch.charAt(0), col, line);
	}
	
}