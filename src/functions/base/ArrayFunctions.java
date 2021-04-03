package functions.base;

import java.util.ArrayList;

import main.EntryPoint;
import parser.Node;
import parser.nodes.ArrayNode;
import parser.nodes.BooleanNode;
import parser.nodes.FunctionNode;
import parser.nodes.NumberNode;
import variables.VariableContext;

public class ArrayFunctions {

	public static boolean argsFound = false;
	public static ArrayNode getFromArgs(ArrayList<Object> args, String name, Node n) {
		argsFound = true;
		if (args.size() == 0) {
			System.out.println("An error occured during "+name+", could not find this object");
			EntryPoint.raiseNode(n);
			argsFound = false;
			return null;
		}
		
		if (!(args.get(0) instanceof ArrayNode)) {
			System.out.println("An error occured during "+name+", this object not instance of ArrayNode, instance of "+args.get(0).getClass().toString());
			EntryPoint.raiseNode(n);
			argsFound = false;
			return null;
		}
		return (ArrayNode)args.get(0);
	}
	
	public static class AddFunction extends FunctionNode {
		
		public AddFunction(int col, int line) {
			super(col, line);
		}
		
		public Object evaluate(VariableContext context, ArrayList<Object> args) {
			ArrayNode arr = getFromArgs(args, "add", this);
			if (!argsFound && args.size() > 1) {return new BooleanNode(-1,-1, false);}
			arr.add(args.get(1));
			
			return new BooleanNode(-1,-1, true);
		}
		
	}
	
	public static class RemoveFunction extends FunctionNode {
		
		public RemoveFunction(int col, int line) {
			super(col, line);
		}
		
		public Object evaluate(VariableContext context, ArrayList<Object> args) {
			ArrayNode arr = getFromArgs(args, "add", this);
			if (!argsFound && args.size() > 1) {return new BooleanNode(-1,-1, false);}
			boolean b = true;
			if ((args.get(1) instanceof NumberNode) && (((NumberNode)args.get(1)).getValue() instanceof Integer)) {
				b = arr.removeByIndex((int) ((NumberNode)args.get(1)).getValue());
			} else {
				arr.remove(args.get(1));
			}
			
			return new BooleanNode(-1,-1, b);
		}
		
	}
	
}
