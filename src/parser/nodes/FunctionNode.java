package parser.nodes;

import java.util.ArrayList;

import evaluator.Evaluator;
import parser.Node;
import variables.VariableContext;

public class FunctionNode extends Node {

	public ArrayList<Node> evaluators;
	public ArrayList<StringNode> arguments;
	public FunctionNode(int col, int line) {
		super(col, line);
	}

	public Object evaluate(VariableContext context) {
		return this;
	}
	
	public void registerStack(VariableContext ctx) {
		
	}
	public void unregisterStack(VariableContext ctx) {
		
	}
	
	public Object evaluate(VariableContext context, ArrayList<Object> args) {
		Object data = null;
		
		int i = 0;
		for(Object dat:args) {
			context.setValue(arguments.get(i).getValue(), dat);
			i += 1;
		}
		
		data = Evaluator.evaluate(this.evaluators, context, false);
		
		return data;
	}
	
}
