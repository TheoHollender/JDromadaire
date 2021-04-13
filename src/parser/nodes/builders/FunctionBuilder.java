package parser.nodes.builders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import parser.Node;
import parser.nodes.FunctionNode;
import parser.nodes.StringNode;
import variables.VariableContext;

public class FunctionBuilder extends Node {

	public FunctionBuilder(int col, int line, ArrayList<StringNode> args, HashMap<StringNode, Node> kwargs, String name, ArrayList<Node> evals) {
		super(col, line);
		this.args = args;
		this.kwargs = kwargs;
		this.evaluators = evals;
		this.name = name;
	}
	
	private HashMap<StringNode, Node> kwargs;
	private ArrayList<StringNode> args;
	private ArrayList<Node> evaluators;
	private String name;
	
	public Object evaluate(VariableContext ctx) {
		FunctionNode f = new FunctionNode(this.col, this.line);
		
		f.name = name;
		f.evaluators = evaluators;
		f.arguments = args;
		
		HashMap<StringNode, Object> kw = new HashMap<>(kwargs.size());
		for (Entry<StringNode, Node> en:kwargs.entrySet()) {
			kw.put(en.getKey(), en.getValue().evaluate(ctx));
		}
		
		f.kwargs = kw;
		
		ctx.setValue(name, f);
		
		return null;
	}
	
}
