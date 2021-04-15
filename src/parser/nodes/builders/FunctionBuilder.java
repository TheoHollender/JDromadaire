package parser.nodes.builders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import parser.Node;
import parser.nodes.FunctionNode;
import parser.nodes.StringNode;
import variables.VariableContext;

public class FunctionBuilder extends Node {

	private boolean agEnabled;
	private String agName;
	private boolean kwEnabled;
	private String kwName;
	private HashMap<String, String> expectedTypeVar;
	private String expectedReturnType;

	public FunctionBuilder(int col, int line, ArrayList<StringNode> args, HashMap<StringNode, Node> kwargs, String name, ArrayList<Node> evals, boolean agEnabled, String agName, boolean kwEnabled, String kwName, HashMap<String, String> expectedTypeVar, String expectedReturnType) {
		super(col, line);
		this.args = args;
		this.kwargs = kwargs;
		this.evaluators = evals;
		this.name = name;
		this.agEnabled = agEnabled;
		this.agName = agName;
		this.kwEnabled = kwEnabled;
		this.kwName = kwName;
		this.expectedTypeVar = expectedTypeVar;
		this.expectedReturnType = expectedReturnType;
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
		
		f.args_enabled = agEnabled;
		f.arg_name = agName;
		f.kwargs_enabled = kwEnabled;
		f.kwarg_name = kwName;
		f.expectedTypeVar = expectedTypeVar;
		f.expectedReturnType = expectedReturnType;
		
		ctx.setValue(name, f);
		
		return null;
	}
	
}
