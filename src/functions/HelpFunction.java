package functions;

import java.util.ArrayList;

import parser.nodes.FunctionNode;
import variables.VariableContext;

public class HelpFunction extends FunctionNode {

	public HelpFunction(int col, int line) {
		super(col, line);
	}

	public Object evaluate(VariableContext context, ArrayList<Object> args) {
		System.out.println("Java Implementation of Drom");
		System.out.println("Welcome to JDromadaire's help utility!\r\n"
				+ "\r\n"
				+ "There are also some important functions you need to know :"
				+ "\r\n - print: prints all arguments."
				+ "\r\n - help: opens help area");
		
		return null;
	}
	
}
