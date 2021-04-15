package functions;

import java.util.ArrayList;
import java.util.HashMap;

import parser.nodes.FunctionNode;
import parser.nodes.StringNode;
import variables.VariableContext;

public class HelpFunction extends FunctionNode {

	public HelpFunction(int col, int line) {
		super(col, line);
	}

	public Object evaluate(VariableContext context, ArrayList<Object> args, HashMap<StringNode, Object> kwargs_entry) {
		System.out.println("Java Implementation of Drom");
		System.out.println("Welcome to JDromadaire's help utility!\r\n"
				+ "\r\n"
				+ "There are also some important functions you need to know :"
				+ "\r\n - print: prints all arguments."
				+ "\r\n - help: opens help area");
		
		return null;
	}
	
}
