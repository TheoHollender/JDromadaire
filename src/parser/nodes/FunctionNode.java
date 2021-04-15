package parser.nodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import evaluator.Evaluator;
import main.EntryPoint;
import parser.Node;
import variables.VariableContext;

public class FunctionNode extends Node {

	public ArrayList<Node> evaluators = new ArrayList();
	public ArrayList<StringNode> arguments = new ArrayList();
	public HashMap<StringNode, Object> kwargs = new HashMap<>();
	public String name = "";
	public boolean kwargs_enabled = false;
	public boolean args_enabled = false;
	public String kwarg_name = "";
	public String arg_name = "";
	public HashMap<String, String> expectedTypeVar = new HashMap();
	public String expectedReturnType = "";
	public FunctionNode(int col, int line) {
		super(col, line);
		this.typeName = "function";
	}

	public Object evaluate(VariableContext context) {
		return this;
	}
	
	public Object evaluate(VariableContext context, ArrayList<Object> args) {
		Object data = null;
		
		DictNode dn = new DictNode(col, line);
		ArrayNode an = new ArrayNode(col, line);
		
		if (args.size() > arguments.size() && !args_enabled) {
			EntryPoint.raiseErr("Too many arguments in function");
		}
		
		boolean[] found = new boolean[arguments.size()];
		
		for (Entry<StringNode, Object> en:kwargs.entrySet()) {
			if (arguments.contains(en.getKey())) {
				found[arguments.indexOf(en.getKey())] = true;
				context.setValue(en.getKey().getValue(), en.getValue());
			} else {
				if (kwargs_enabled) {
					dn.set(en.getKey(), en.getValue());
				} else {
					EntryPoint.raiseErr("Name doesn't exists in function kwargs "+en.getKey().name);
				}
			}
		}
		
		int i = 0;
		for(Object dat:args) {
			if (i < arguments.size()) {
				context.setValue(arguments.get(i).getValue(), dat);
			} else {
				an.add(dat);
			}
			i += 1;
		}
		
		for (;i<found.length; i++) {
			if (!found[i]) {
				EntryPoint.raiseErr("Missing argument "+i+"in function "+name);
			}
		}
		
		if (args_enabled) {
			context.setValue(arg_name, an);
		}
		if (kwargs_enabled) {
			context.setValue(kwarg_name, dn);
		}
		
		data = Evaluator.evaluate(this.evaluators, context, false);
		
		return data;
	}
	
	
	
	public Object evaluate(VariableContext context, ArrayList<Object> args, HashMap<StringNode, Object> kwargs_entry) {
		Object data = null;
		
		DictNode dn = new DictNode(col, line);
		ArrayNode an = new ArrayNode(col, line);
		
		if (args.size() > arguments.size() && !args_enabled) {
			EntryPoint.raiseErr("Too many arguments in function");
		}
		
		boolean[] found = new boolean[arguments.size()];
		
		for (Entry<StringNode, Object> en:kwargs.entrySet()) {
			if (arguments.contains(en.getKey())) {
				found[arguments.indexOf(en.getKey())] = true;
				context.setValue(en.getKey().getValue(), en.getValue());
				evaluateKwargs(en, context);
			} else {
				if (kwargs_enabled) {
					dn.set(en.getKey(), en.getValue());
				} else {
					EntryPoint.raiseErr("Name doesn't exists in function kwargs "+en.getKey().name);
				}
			}
		}
		for (Entry<StringNode, Object> en:kwargs_entry.entrySet()) {
			if (arguments.contains(en.getKey())) {
				found[arguments.indexOf(en.getKey())] = true;
				context.setValue(en.getKey().getValue(), en.getValue());
				evaluateKwargs(en, context);
			} else {
				if (kwargs_enabled) {
					dn.set(en.getKey(), en.getValue());
				} else {
					EntryPoint.raiseErr("Name doesn't exists in function kwargs "+en.getKey().name);
				}
			}
		}
		
		int i = 0;
		for(Object dat:args) {
			if (i < arguments.size()) {
				context.setValue(arguments.get(i).getValue(), dat);
				evaluateArgs(arguments.get(i).getValue(), dat, context);
			} else {
				an.add(dat);
			}
			i += 1;
		}
		
		for (;i<found.length; i++) {
			if (!found[i]) {
				EntryPoint.raiseErr("Missing argument "+i+"in function "+name);
			}
		}
		
		if (args_enabled) {
			context.setValue(arg_name, an);
		}
		if (kwargs_enabled) {
			context.setValue(kwarg_name, dn);
		}
		
		data = Evaluator.evaluate(this.evaluators, context, false);

		if (data instanceof ReturnNode && !expectedReturnType.equals("")) {
			ReturnNode rn = (ReturnNode) data;
			Object o = rn.evaluate(context);
			
			if (o instanceof Node) {
				Node ndata = (Node) o;
				Object evTyper = context.getValue(expectedReturnType);
				if (evTyper == null) {
					evTyper = EntryPoint.globalContext.getValue(expectedReturnType);
				}
				
				if (evTyper instanceof Node) {
					String type = ((Node) evTyper).type();
					
					if (!ndata.isInType(type)) {
						EntryPoint.raiseErr("Expected "+type+" as return, got "+ndata.type());
					}
				}
			}
		}
		
		return data;
	}

	private void evaluateArgs(String value, Object dat, VariableContext context) {
		if (dat instanceof Node) {
			Node typeTester = (Node) dat;
			
			if (expectedTypeVar.containsKey(value)) {
				String s = expectedTypeVar.get(value);
				Object o = context.getValue(s);
				if (o == null) {
					o = EntryPoint.globalContext.getValue(s);
				}
				if (o instanceof Node) {
					String type = ((Node)o).type();
					
					if (!typeTester.isInType(type)) {
						EntryPoint.raiseErr("Expected "+type+" as "+value+" argument, got "+typeTester.type());
					}
				}
			}
		}
	}
	
	public void evaluateKwargs(Entry<StringNode, Object> en, VariableContext context) {
		evaluateArgs(en.getKey().getValue(), en.getValue(), context);
	}
	
}
