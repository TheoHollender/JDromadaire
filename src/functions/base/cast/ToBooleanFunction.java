package functions.base.cast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.EntryPoint;
import main.Scanner;
import main.Token;
import main.TokenType;
import parser.nodes.BooleanNode;
import parser.nodes.FunctionNode;
import parser.nodes.NumberNode;
import parser.nodes.StringNode;
import variables.VariableContext;

public class ToBooleanFunction extends FunctionNode {

	public ToBooleanFunction(int col, int line) {
		super(col, line);
	}
	
	public String type() {
		return "boolean";
	}
	
	public Object evaluate(VariableContext context, ArrayList<Object> args, HashMap<StringNode, Object> kwargs_entry) {
		if (args.size() != 1) {
			EntryPoint.raiseErr("Expected 1 argument in number cast, got "+args.size());
			return null;
		}
		
		Object d = args.get(0);
		if (d instanceof StringNode) {
			if (((StringNode)d).getValue().contains("true")) {
				return new BooleanNode(((StringNode)d).col, ((StringNode)d).line, true);
			}
			return new BooleanNode(((StringNode)d).col, ((StringNode)d).line, false);
		}
		if (d instanceof NumberNode) {
			if (((NumberNode)d).getNumber().compareTo(BigDecimal.ONE) == 0) {
				return new BooleanNode(((NumberNode)d).col, ((NumberNode)d).line, true);
			}
			return new BooleanNode(((NumberNode)d).col, ((NumberNode)d).line, false);
		}
		if (d instanceof BooleanNode) {
			return d;
		}
		EntryPoint.raiseErr("Expected either string, number or boolean in boolean function, got "+d.getClass().toString());
		return null;
	}

}
