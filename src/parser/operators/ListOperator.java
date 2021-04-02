package parser.operators;

import parser.nodes.NumberNode;
import parser.nodes.StringNode;

public interface ListOperator {
	
	public boolean set(NumberNode n, Object o);
	public boolean set(StringNode n, Object o);
	public Object get(NumberNode n);
	public Object get(StringNode n);
	public int length();

}
