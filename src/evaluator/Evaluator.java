package evaluator;

import java.util.ArrayList;

import main.EntryPoint;
import parser.Node;
import parser.nodes.ReturnNode;
import variables.VariableContext;

public class Evaluator {

	public static Object evaluate(ArrayList<Node> nodes, VariableContext context, boolean printEq) {
		for(Node n:nodes) {
			if (n != null) {
				Object value = n.evaluate(context);
				if(value != null) {
					if (value instanceof ReturnNode) {
						return ((ReturnNode)value).evaluate(context);
					}
					if(printEq) {
						System.out.println(value);
					}
				}
			}
		}
		return null;
	}
	
}
