package parser.nodes.builders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import main.EntryPoint;
import parser.Node;
import parser.nodes.DictNode;
import parser.nodes.StringNode;
import variables.VariableContext;

public class DictBuilder extends Node {

	public DictBuilder(int col, int line) {
		super(col, line);
	}


	private HashMap<Node, Node> exprs = new HashMap<Node,Node>();

	public void put(Node key, Node expr) {
		exprs.put(key, expr);
	}
	
	public void evaluateEntry(VariableContext ctx, Entry<Node,Node> data, HashMap<String,Object> map) {
		Object key_object = data.getKey().evaluate(ctx);
		String key;
		if (key_object instanceof StringNode) {
			key = ((StringNode) key_object).getValue();
		} else if (key_object instanceof String) {
			key = (String) key_object;
		} else {
			EntryPoint.raiseErr("Expected a String as key for dictionnary");
			return ;
		}
		
		map.put(key, data.getValue().evaluate(ctx));
	}
	
	public Object evaluate(VariableContext ctx) {
		DictNode dict = new DictNode(col, line);
		
		dict.objects = new HashMap<String,Object>(exprs.size());
		
		for (Entry<Node, Node> entry:exprs.entrySet()) {
			evaluateEntry(ctx, entry, dict.objects);
		}
		
		return dict;
	}
	
}
