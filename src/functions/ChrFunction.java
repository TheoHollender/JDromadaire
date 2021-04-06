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
			System.out.println("Expected 1 argument, got "+args.size());
			EntryPoint.raiseNode(this);
            return null;
		}
		if (!(args.get(0) instanceof NumberNode)) {
            System.out.println("Expected number as first argument, got "+args.get(0).getClass().toString());
            EntryPoint.raiseNode(this);
            return null;
		}
		if (!(((NumberNode)args.get(0)).getValue() instanceof Integer)) {
            System.out.println("Expected integer as first argument, got "+((NumberNode)args.get(0)).getValue() .getClass().toString());
            EntryPoint.raiseNode(this);
            return null;
		}
		if (((Integer)((NumberNode)args.get(0)).getValue())<0) {
			System.out.println("Expected positive Integer as first argument");
            EntryPoint.raiseNode(this);
            return null;
		}
		int ch = (int) ((NumberNode)args.get(0)).getValue();
		return new StringNode(col, line, String.valueOf((char) ch));
	}
	
}
