package variables;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import main.EntryPoint;
import main.RuntimeInterpreterException;
import parser.Node;
import parser.nodes.FunctionNode;
import parser.nodes.NumberNode;
import parser.nodes.StringNode;

public class NativeMethodNode extends FunctionNode {

	public boolean isStatic = false;
	public NativeMethodNode alternative = null;
	
	public NativeMethodNode(int col, int line, String type, boolean isStatic, Method met) {
		super(col, line);
		this.typeName = type;
		this.isStatic = isStatic;
		this.method = met;
	}
	
	public Object evaluateInner(VariableContext context, ArrayList<Object> args, HashMap<StringNode, Object> kwargs_entry) {
		int i = 1;
		if (isStatic) {i=0;}
		
		if (method.getParameterCount()+i != args.size()) {
			this.raiseErr("Expected "+method.getParameterCount()+" arguments in native method "+this.type());
		}
		
		Object[] objs = new Object[method.getParameterCount()];
		
		int j = 0;
		for (Parameter p:method.getParameters()) {
			if (args.get(i) instanceof Node) {
				objs[j] = ((Node)args.get(i)).castTo(p.getType());
			}
			
			i++;
			j++;
		}

		Object d;
		try {
			if (isStatic) {
				d = toUsableData(method.invoke(null, objs));
			} else {
				d = toUsableData(method.invoke(args.get(0), objs));
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.raiseErr("An unexpected Error occured in native method "+this.type());
			return null;
		}
		
		i = 1;
		if (isStatic) {i=0;}
		
		j = 0;
		for (Parameter p:method.getParameters()) {
			if (args.get(i) instanceof Node) {
				((Node)args.get(i)).recoverNativeModifications(objs[j]);
			}
			i++;
			j++;
		}
			
		return d;
	}
	
	public boolean raised = false;
	public String msg;
	public void raiseErr(String msg) {
		raised = true;
		this.msg = msg;
		throw new RuntimeInterpreterException();
	}
	
	public Object evaluate(VariableContext context, ArrayList<Object> args, HashMap<StringNode, Object> kwargs_entry) {
		raised = false;
		try {
			return this.evaluateInner(context, args, kwargs_entry);
		} catch (RuntimeInterpreterException e) {
			boolean b = false;
			if (alternative != null) {
				try {
					return alternative.evaluate(context, args, kwargs_entry);
				} catch(RuntimeInterpreterException e2) {
					EntryPoint.raiseErr(msg);
				}
			} else {
				EntryPoint.raiseErr(msg);
			}
		}
		return null;
	}
	
	public Method method;

	public NativeMethodNode setAlternative(Object object) {
		if (object instanceof NativeMethodNode) {
			this.alternative = (NativeMethodNode) object;
			return this;
		}
		EntryPoint.raiseErr("Proposed method alternative doesn't exist");
		return this;
	}
	
	public Object toUsableData(Object data) {
		if (data instanceof Integer) {
			return new NumberNode(BigDecimal.valueOf((int)data), -1, -1);
		}
		if (data instanceof Long) {
			return new NumberNode(BigDecimal.valueOf((long)data), -1, -1);
		}
		if (data instanceof Float) {
			return new NumberNode(BigDecimal.valueOf((float)data), -1, -1);
		}
		if (data instanceof Double) {
			return new NumberNode(BigDecimal.valueOf((double)data), -1, -1);
		}
		if (data instanceof String) {
			return new StringNode(-1, -1, (String)data);
		}
		
		return data;
	}
	
}
