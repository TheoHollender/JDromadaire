package parser.nodes;

import java.io.File;
import java.util.ArrayList;

import functions.base.string.StringFunctions;
import functions.files.FileNode;
import main.EntryPoint;
import parser.Node;
import parser.operators.ComparateOperator;
import parser.operators.EvaluateOperator;
import parser.operators.ListOperator;
import variables.ClassNode;
import variables.VariableContext;

public class StringNode extends ClassNode implements EvaluateOperator,ListOperator,ComparateOperator<StringNode> {

	public boolean equals(Object o) {
		if (o instanceof StringNode) {
			return ((StringNode)o).getValue().equals(this.getValue());
		}
		return false;
	}

	public StringNode(StringNode other, ArrayList<Object> args) {
		super(other, args);
	}
	
	public Object createInstance(VariableContext context, ArrayList<Object> args) {
		if(!isRoot) {
			System.out.println("Can't create instance with sub child");
			return null;
		}
		return new StringNode(this, args);
	}
	
	public static final FunctionNode SPLIT = new StringFunctions.SplitFunction(-1, -1);
	public StringNode(int col, int line, String string) {
		super(col, line);
		this.typeName = "string";
		this.value = string;
		this.isRoot = false;
		
		this.objects.put("split", SPLIT);
	}
	private String value;

	public Object evaluate(VariableContext context) {
		return this;
	}

	public String getValue() {
		return this.value;
	}

	public String toString() {
		return '\''+this.value+'\'';
	}
	
	@Override
	public Object add(Object e) {
		if (e instanceof StringNode) {
			StringNode n = (StringNode) e;
			return new StringNode(n.col, n.line, this.getValue() + n.getValue());
		}
		if (e instanceof NumberNode) {
			NumberNode n = (NumberNode) e;
			return new StringNode(n.col, n.line, this.getValue() + n.getNumber().toString());
		}

		if (e instanceof CharNode) {
			return new StringNode(this.col, this.line, this.getValue() + ((CharNode)e).getValue());
		}
		return null;
	}

	@Override
	public Object substract(Object e) {return null;}

	@Override
	public Object multiply(Object e) {return null;}

	@Override
	public Object divide(Object e) {return null;}

	@Override
	public Object power(Object e) {return null;}

	@Override
	public boolean set(NumberNode n, Object o) {return false;}

	@Override
	public Object get(NumberNode n) {
		if (n.isInt() && n.isIntegerRange()) {
			if (this.value.length() < (int) n.getNumber().intValue() || (int) n.getNumber().intValue() < 0) {
				EntryPoint.raiseErr("Index out of range exception");
			}
			return new CharNode(this.col, this.line, this.value.charAt(n.getNumber().intValue()));
		} else {
			EntryPoint.raiseErr("Integer Object needed, received Float/Double");
		}
		return null;
	}

	@Override
	public int length() {
		return this.value.length();
	}
	
	@Override
	public double compare(StringNode e) {
		return this.value.compareTo(e.getValue());
	}
	
}
