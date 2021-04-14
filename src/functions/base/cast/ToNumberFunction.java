package functions.base.cast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.EntryPoint;
import main.Scanner;
import main.Token;
import main.TokenType;
import parser.nodes.FunctionNode;
import parser.nodes.NumberNode;
import parser.nodes.StringNode;
import variables.VariableContext;

public class ToNumberFunction extends FunctionNode {

	private int pCountMax;
	public ToNumberFunction(int col, int line, int pCountMax) {
		super(col, line);
		this.pCountMax = pCountMax;
	}
	
	public Object evaluate(VariableContext context, ArrayList<Object> args, HashMap<StringNode, Object> kwargs_entry) {
		if (args.size() != 1) {
			EntryPoint.raiseErr("Expected 1 argument in number cast, got "+args.size());
			return null;
		}
		
		Object d = args.get(0);
		if (d instanceof StringNode) {
			String s = ((StringNode)d).getValue();
			Scanner sc = new Scanner(s);
			List<Token> tokens = sc.scanTokens();
			if (tokens.size() != 2) {
				EntryPoint.raiseErr("Parsed bad count of Tokens");
				return null;
			}
			
			if (tokens.get(0).type != TokenType.NUMBER) {
				EntryPoint.raiseErr("The token isn't a number");
				return null;
			}
			
			d = new NumberNode((BigDecimal)tokens.get(0).value, this.col, this.line);
		}
		if (d instanceof NumberNode) {
			if (pCountMax >= 1) {
				return d;
			} else if (!(((NumberNode) d).isInt())) {
				return new NumberNode(((NumberNode)d).getNumber().setScale(0, BigDecimal.ROUND_FLOOR), ((NumberNode) d).col, ((NumberNode) d).line);
			}
			return d;
		}
		return new StringNode(-1, -1, args.get(0).toString());
	}

}
