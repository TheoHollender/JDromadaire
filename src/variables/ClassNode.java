package variables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import parser.Node;
import parser.nodes.FunctionNode;
import parser.nodes.NumberNode;
import parser.nodes.ReturnNode;
import parser.nodes.StringNode;
import parser.operators.ListOperator;

public class ClassNode extends Node implements ListOperator {

	public boolean isRoot = true;
	public String name;
	protected boolean isSetable = true;
	public ClassNode(int col, int line) {
		super(col, line);
	}
	
	public String toString() {
		Object str = this.objects.get("str");
		if (str instanceof FunctionNode) {
			FunctionNode stringify = (FunctionNode) str;
			ArrayList<Object> args = new ArrayList<Object>(1);
			args.add(this);
			VariableContext ctx = new VariableContext();
			Object d = stringify.evaluate(ctx, args);
			if (d instanceof StringNode) {
				return ((StringNode)d).getValue();
			}
			if (d instanceof ReturnNode) {
				return ((ReturnNode)d).evaluate(ctx).toString();
			}
			if (d!=null) {
				return d.toString();
			}
		}
		return "<class:ClassNode:"+this.name+">";
	}
	
	protected ClassNode(ClassNode other, ArrayList<Object> args) {
		super(other.col, other.line);
		isRoot = false;
		this.name = other.name;
		
		for(Entry<String, Object> dat:other.objects.entrySet())  {
			this.objects.put(dat.getKey(), dat.getValue());
		}
		
		Object init = this.objects.get("init");
		if (init instanceof FunctionNode) {
			FunctionNode constructor = (FunctionNode) init;
			args.add(0, this);
			constructor.evaluate(new VariableContext(), args);
		}
	}
	
	public void importContext(VariableContext context) {
		this.objects.putAll(context.values);
	}

	public Object createInstance(VariableContext context, ArrayList<Object> args) {
		if(!isRoot) {
			System.out.println("Can't create instance with sub child");
			return null;
		}
		return new ClassNode(this, args);
	}
	
	public HashMap<String, Object> objects = new HashMap<String, Object>();
	
	public Object evaluate(VariableContext context) {
		return this;
	}

	@Override
	public boolean set(NumberNode n, Object o) {return false;}

	@Override
	public boolean set(StringNode n, Object o) {
		if(!isSetable) {
			return false;
		}
		this.objects.put(n.getValue(), o);
		return true;
	}

	@Override
	public Object get(NumberNode n) {return null;}

	@Override
	public Object get(StringNode n) {
		return this.objects.get(n.getValue());
	}

	@Override
	public int length() {
		return 0;
	}

	
	
}
