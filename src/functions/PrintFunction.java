package functions;

import java.util.ArrayList;

import parser.nodes.FunctionNode;
import variables.VariableContext;

public class PrintFunction extends FunctionNode {

	public PrintFunction(int col, int line) {
		super(col, line);
	}
	
	public Object evaluate(VariableContext context, ArrayList<Object> args) {
		for(Object o:args) {
			System.out.println(o);
		}
		
		return null;
	}

}
