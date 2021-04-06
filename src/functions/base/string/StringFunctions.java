package functions.base.string;

import java.util.ArrayList;

import main.EntryPoint;
import parser.Node;
import parser.nodes.ArrayNode;
import parser.nodes.BooleanNode;
import parser.nodes.FunctionNode;
import parser.nodes.StringNode;
import variables.VariableContext;

public class StringFunctions {

	public static boolean argsFound = false;
	public static StringNode getFromArgs(ArrayList<Object> args, String name, Node n) {
		argsFound = true;
		if (args.size() == 0) {
			EntryPoint.raiseErr("An error occured during "+name+", could not find this object");
			argsFound = false;
			return null;
		}
		
		if (!(args.get(0) instanceof StringNode)) {
			EntryPoint.raiseErr("An error occured during "+name+", this object not instance of StringNode, instance of "+args.get(0).getClass().toString());
			argsFound = false;
			return null;
		}
		return (StringNode)args.get(0);
	}
	
	public static class SplitFunction extends FunctionNode {
		
		public SplitFunction(int col, int line) {
			super(col, line);
		}
		
		public Object evaluate(VariableContext context, ArrayList<Object> args) {
			StringNode str = getFromArgs(args, "add", this);
			if (!argsFound) {return new BooleanNode(-1,-1, false);}
			if (args.size() != 2) {
				EntryPoint.raiseErr("Received "+args.size()+" arguments, expected 2");
				return null;
			}
			if (!(args.get(1) instanceof StringNode)) {
				EntryPoint.raiseErr("Expected StringNode as second argument, got "+args.get(1).getClass().toString());
				return null;
			}
			if (((StringNode)args.get(1)).getValue().equals("")) {
				EntryPoint.raiseErr("Regular expression can't be empty(\"\")");
				return null;
			}
			
			String[] dat = str.getValue().split(((StringNode)args.get(1)).getValue());
			ArrayList<Object> arr = new ArrayList<Object>(dat.length);
			for(String s:dat) {
				arr.add(s);
			}
			
			ArrayNode array = new ArrayNode(-1, -1);
			array.array = arr;
			array.updateLength();
			
			return array;
		}
		
	}
	
}
