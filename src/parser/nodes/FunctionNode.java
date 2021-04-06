package parser.nodes;

import java.util.ArrayList;

import evaluator.Evaluator;
import main.EntryPoint;
import parser.Node;
import variables.VariableContext;

public class FunctionNode extends Node {

	public ArrayList<Node> evaluators;
	public ArrayList<StringNode> arguments;
	public String name;
	public FunctionNode(int col, int line) {
		super(col, line);
	}

	public Object evaluate(VariableContext context) {
		return this;
	}
	
	public Object evaluate(VariableContext context, ArrayList<Object> args) {
		Object data = null;
		
		if (args.size() != arguments.size()) {
			System.out.println("Expected "+arguments.size()+" arguments, got "+args.size());
			EntryPoint.raiseNode(this);
			return null;
		}
		int i = 0;
		for(Object dat:args) {
			context.setValue(arguments.get(i).getValue(), dat);
			i += 1;
		}
		
		data = Evaluator.evaluate(this.evaluators, context, false);
		
		return data;
	}
	
}
