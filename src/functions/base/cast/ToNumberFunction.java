package functions.base.cast;

import java.util.ArrayList;
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
	
	public Object evaluate(VariableContext context, ArrayList<Object> args) {
		if (args.size() != 1) {
			System.out.println("Expected 1 argument in number cast, got "+args.size());
			EntryPoint.raiseNode(this);
			return null;
		}
		
		Object d = args.get(0);
		if (d instanceof StringNode) {
			String s = ((StringNode)d).getValue();
			Scanner sc = new Scanner(s);
			List<Token> tokens = sc.scanTokens();
			if (tokens.size() != 2) {
				System.out.println("Parsed bad count of Tokens");
				EntryPoint.raiseNode(this);
				return null;
			}
			
			if (tokens.get(0).type != TokenType.NUMBER) {
				System.out.println("The token isn't a number");
				EntryPoint.raiseNode(this);
				return null;
			}
			
			d = new NumberNode(tokens.get(0).value, this.col, this.line);
		}
		if (d instanceof NumberNode) {
			if (pCountMax >= 1) {
				return d;
			} else if (!(((NumberNode) d).getValue() instanceof Integer)) {
				return new NumberNode((int)Math.floor(((NumberNode) d).getDoubleValue()), ((NumberNode) d).col, ((NumberNode) d).line);
			}
			return d;
		}
		return new StringNode(-1, -1, args.get(0).toString());
	}

}