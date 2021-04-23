package variables;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;

import main.EntryPoint;
import parser.nodes.FunctionNode;
import parser.nodes.NumberNode;
import parser.nodes.StringNode;

public class NativeMethodNode extends FunctionNode {

	public boolean isStatic = false;
	
	public NativeMethodNode(int col, int line, String type, boolean isStatic, Method met) {
		super(col, line);
		this.typeName = type;
		this.isStatic = isStatic;
		this.method = met;
	}
	
	public Object evaluate(VariableContext context, ArrayList<Object> args, HashMap<StringNode, Object> kwargs_entry) {
		int i = 1;
		if (isStatic) {i=0;}
		
		if (method.getParameterCount()+i != args.size()) {
			EntryPoint.raiseErr("Expected "+method.getParameterCount()+" arguments in native method "+this.type());
		}
		
		Object[] objs = new Object[method.getParameterCount()];
		
		int j = 0;
		for (Parameter p:method.getParameters()) {
			if (p.getType().equals(Integer.class) ||
					p.getType().equals(int.class)) {
				if (args.get(i) instanceof NumberNode
						&& args.get(i) != null
						&& ((NumberNode)args.get(i)).isInt()
						&& ((NumberNode)args.get(i)).isIntegerRange()) {
					objs[j] = ((NumberNode)args.get(i)).getNumber().intValue();
				} else {
					EntryPoint.raiseErr("Expected non null Integer (]-2**31, 2**31[) as n°"+j+" argument, got "+args.get(i).getClass());
				}
			} else if (p.getType().equals(String.class)) {
				if (args.get(i) instanceof StringNode
						&& args.get(i) != null) {
					objs[j] = ((StringNode)args.get(i)).getValue();
				} else {
					EntryPoint.raiseErr("Expected non null String as n°"+j+" argument, got "+args.get(i).getClass());
				}
			} else if (args.get(i).getClass().isInstance(p.getType())) {
				objs[j] = args.get(i);
			} else {
				EntryPoint.raiseErr("Expected "+p.getType()+" as n°"+j+" argument, got "+args.get(i).getClass());
			}
			i++;
			j++;
		}

		try {
			if (isStatic) {
				return method.invoke(null, objs);
			} else {
				return method.invoke(args.get(0), objs);
			}
		} catch (Exception e) {
			EntryPoint.raiseErr("An unexpected Error occured in native method "+this.type());
		} 
		
		return null;
	}
	
	public Method method;
	
}
