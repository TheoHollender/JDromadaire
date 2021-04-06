package functions;

import java.util.ArrayList;

import main.EntryPoint;
import parser.nodes.FunctionNode;
import parser.nodes.NumberNode;
import parser.nodes.StringNode;
import variables.VariableContext;

public class OrdFunction extends FunctionNode {

	public OrdFunction(int col, int line) {
		super(col, line);
	}

	public Object evaluate(VariableContext context, ArrayList<Object> args) {
		if(args.size()!=1) {
			System.out.println("Expected 1 argument, got "+args.size());
			EntryPoint.raiseNode(this);
            return null;
		}
		if (!(args.get(0) instanceof StringNode)) {
            System.out.println("Expected string as first argument, got "+args.get(0).getClass().toString());
            EntryPoint.raiseNode(this);
            return null;
		}
		String ch = (String) ((StringNode)args.get(0)).getValue();
		return new NumberNode((int) ch.charAt(0), col, line);
	}
	
}