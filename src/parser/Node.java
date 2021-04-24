package parser;

import java.util.ArrayList;

import main.EntryPoint;
import variables.VariableContext;

public class Node {
	
	public String typeName = null;
	public String type() {
		if (typeName != null) {
			return typeName;
		}
		return this.getClass().toString();
	}
	public ArrayList<Node> parents = new ArrayList<Node>();
	public boolean isInType(String s) {
		if (s.equals("any")) {
			return true;
		}
		
		if (typeName.equals(s)) {
			return true;
		}
		
		for(Node p:parents) {
			if (p.isInType(s)) {
				return true;
			}
		}
		return false;
	}
	
	public int col;
	public int line;
	
	public Node(int col, int line) {
		this.col = col;
		this.line = line;
	}

	public Object evaluate(VariableContext context) {
		if (this instanceof OpNode) {
			return ((OpNode) this).OpEvaluate(context);
		}
		if (this instanceof UnaryOpNode) {
			return ((UnaryOpNode) this).OpEvaluate(context);
		}
		if (this instanceof ValueNode) {
			return ((ValueNode) this).getValue();
		}
		
		return null;
	}

	public static Object undoCast(Object base, Object end) {
		return end;
	}
	
	public void recoverNativeModifications(Object o) {
		
	}

	public Object castTo(Class<?> type) {
		if (type == this.getClass()) {
			return this;
		}
		
		EntryPoint.raiseErr("Native cast error : Couldn't cast "+this.getClass()+" to "+type);
		return null;
	}
	
}
