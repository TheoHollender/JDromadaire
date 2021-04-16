package functions;

import java.util.ArrayList;
import java.util.HashMap;

import main.EntryPoint;
import parser.Node;
import parser.nodes.FunctionNode;
import parser.nodes.StringNode;
import variables.VariableContext;

public class TypeFunction extends FunctionNode {

	public TypeFunction(int col, int line) {
		super(col, line);
	}
	
	public Object evaluate(VariableContext ctx, ArrayList<Object> args, HashMap<StringNode, Object> kwargs) {
		if (args.size() != 1) {
			EntryPoint.raiseErr("Expected one arguments in type, got "+args.size());
			return null;
		}
		if (!(args.get(0) instanceof Node)) {
			EntryPoint.raiseErr("Expected Node as first argument, got "+args.get(0));
			return null;
		}
		
		return new StringNode(-1, -1, ((Node)args.get(0)).type());
	}
	

}
