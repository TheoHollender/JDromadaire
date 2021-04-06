package parser.nodes.getters;

import main.EntryPoint;
import parser.Node;
import parser.nodes.NumberNode;
import parser.nodes.StringNode;
import parser.operators.ListOperator;
import variables.VariableContext;

public class ListGetterNode extends Node {

	private Node left;
	private Node index;

	public ListGetterNode(int col, int line, Node left, Node index) {
		super(col, line);
		this.left = left;
		this.index = index;
	}
	
	public Object evaluate(VariableContext context) {
		Object index = this.index.evaluate(context);
		Object evaluated = left.evaluate(context);
		
		if (evaluated instanceof ListOperator) {
			ListOperator op = (ListOperator)evaluated;
			if(index instanceof NumberNode) {
				if(((NumberNode) index).getValue() instanceof Integer &&
						(Integer)((NumberNode) index).getValue() >= 0 && (Integer)((NumberNode) index).getValue() < op.length()) {
					return op.get((NumberNode) index);
				} else {
					if(!(((NumberNode) index).getValue() instanceof Integer)) {
						EntryPoint.raiseErr("Integer Object needed, received Float/Double");
						return null;
					}
					EntryPoint.raiseErr("Index out of range exception");
					return null;
				}
			}else if (index instanceof StringNode) {
				return op.get((StringNode) index);
			}
			EntryPoint.raiseErr("List only support usage with numbers or strings");
		}else {
			EntryPoint.raiseErr(evaluated.getClass().toString() + " does not support list or dict usage");
		}
		
		return null;
	}
	
}
