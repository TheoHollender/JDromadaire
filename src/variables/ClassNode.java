package variables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import main.EntryPoint;
import parser.Node;
import parser.nodes.FunctionNode;
import parser.nodes.NumberNode;
import parser.nodes.ReturnNode;
import parser.nodes.StringNode;
import parser.operators.ListOperator;

public class ClassNode extends Node implements ListOperator {

	public int getIntegerValue(ArrayList<Object> args, int index) {
		if (args.get(index) instanceof NumberNode
				&& ((NumberNode)args.get(index)) != null
				&& ((NumberNode)args.get(index)).isInt()
				&& ((NumberNode)args.get(index)).isIntegerRange()){
			return ((NumberNode)args.get(index)).getNumber().intValue();
		}
		EntryPoint.raiseErr("Expected integer as argument n°"+index+", got "+args.get(index).getClass());
		return -1;
	}
	
	public String getStringValue(ArrayList<Object> args, int index) {
		if (args.get(index) instanceof StringNode
				&& ((StringNode)args.get(index)) != null){
			return ((StringNode)args.get(index)).getValue();
		}
		EntryPoint.raiseErr("Expected string as argument n°"+index+", got "+args.get(index).getClass());
		return "";
	}
	
	public void checkSize(ArrayList<Object> args, int... sizes) {
		boolean b = false;
		
		for (int size:sizes) {
			if (size == args.size()) {
				b = true;
				break;
			}
		}
		
		if (!b) {
			StringBuffer strbf = new StringBuffer();
			for(int i = 0; i < sizes.length; i++) {
				if (i != sizes.length - 1) {
					if (i != 0) {
						strbf.append(", ");
					}
					strbf.append(sizes[i]);
				} else {
					if (i != 0) {
						strbf.append(" or ");
					}
					strbf.append(sizes[i]);
				}
			}
			EntryPoint.raiseErr("Expected "+strbf.toString()+" arguments, got "+args.size());
		}
	}
	
	public boolean isRoot = true;
	public String name;
	protected boolean isSetable = true;
	public ClassNode(int col, int line) {
		super(col, line);
	}
	
	public String type() {
		if (this.typeName != null) {
			return this.typeName;
		}
		return name;
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
