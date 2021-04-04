package functions;

import java.util.ArrayList;
import java.util.Scanner;

import parser.nodes.FunctionNode;
import parser.nodes.StringNode;
import variables.VariableContext;

public class InputFunction extends FunctionNode {

	public InputFunction(int col, int line) {
		super(col, line);
	}

	private static Scanner sc = new Scanner(System.in);
	
	public Object evaluate(VariableContext ctx, ArrayList<Object> args) {
		return new StringNode(-1, -1, sc.nextLine());
	}
	
}
