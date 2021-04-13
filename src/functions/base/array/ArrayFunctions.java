package functions.base.array;

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
			EntryPoint.raiseErr("An error occured during "+name+", could not find this object");
			argsFound = false;
			return null;
		}
		
		if (!(args.get(0) instanceof ArrayNode)) {
			EntryPoint.raiseErr("An error occured during "+name+", this object not instance of ArrayNode, instance of "+args.get(0).getClass().toString());
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
			if ((args.get(1) instanceof NumberNode) && (((NumberNode)args.get(1)).isInt()) && (((NumberNode)args.get(1)).isIntegerRange())) {
				b = arr.removeByIndex(((NumberNode)args.get(1)).getNumber().intValue());
			} else {
				arr.remove(args.get(1));
			}
			
			return new BooleanNode(-1,-1, b);
		}
		
	}
	
	public static class IndexFunction extends FunctionNode {
		
		public IndexFunction(int col, int line) {
			super(col, line);
		}
		
		public Object evaluate(VariableContext context, ArrayList<Object> args) {
			ArrayNode arr = getFromArgs(args, "add", this);
			
			int i = -2;
			if (!argsFound && args.size() > 1) {
				EntryPoint.raiseErr("Missing arguments on index function");
				return null;
			} else {
				i = arr.array.indexOf(args.get(1));
			}
			
			return new NumberNode(i, -1,-1);
		}
		
	}
	
	public static class ContainsFunction extends FunctionNode {
		
		public ContainsFunction(int col, int line) {
			super(col, line);
		}
		
		public Object evaluate(VariableContext context, ArrayList<Object> args) {
			ArrayNode arr = getFromArgs(args, "add", this);
			
			boolean i = false;
			if (!argsFound && args.size() > 1) {
				EntryPoint.raiseErr("Missing arguments on contains function");
				return null;
			} else {
				 i = arr.array.contains(args.get(1));
			}
			
			return new BooleanNode(-1,-1, i);
		}
		
	}
	
}
