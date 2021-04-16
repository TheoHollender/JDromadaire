package functions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import main.EntryPoint;
import parser.nodes.ArrayNode;
import parser.nodes.FunctionNode;
import parser.nodes.NumberNode;
import parser.nodes.StringNode;
import variables.VariableContext;

public class RoundFunction extends FunctionNode {

	public RoundFunction(int col, int line) {
		super(col, line);
	}

	public Object evaluate(VariableContext context, ArrayList<Object> args, HashMap<StringNode, Object> kwargs_entry) {
		BigDecimal pre = BigDecimal.ZERO;
		BigDecimal nb = BigDecimal.ONE;
		if(args.size()==1) {
			if (!(args.get(0) instanceof NumberNode)) {
	            EntryPoint.raiseErr("Expected number as first argument, got "+args.get(0).getClass().toString());
	            return null;
			}
			nb = ((NumberNode)args.get(0)).getNumber();
		}
		else if(args.size()==2) {
			if (!(args.get(0) instanceof NumberNode)) {
	            EntryPoint.raiseErr("Expected number as first argument, got "+args.get(0).getClass().toString());
	            return null;
			}
			if (!(args.get(1) instanceof NumberNode)) {
	            EntryPoint.raiseErr("Expected number as second argument, got "+args.get(1).getClass().toString());
	            return null;
			}
			if (!(((NumberNode)args.get(1)).isInt() && ((NumberNode)args.get(1)).isIntegerRange())) {
				EntryPoint.raiseErr("Expected integer < 2^31 and > -2^31");
				return null;
			}
			nb = ((NumberNode)args.get(0)).getNumber();
			pre = ((NumberNode)args.get(1)).getNumber();
		}
		else {
			EntryPoint.raiseErr("1 or 2 arguments expected, "+args.size()+" received");
			return null;
		}
		
		BigDecimal d = nb.multiply(BigDecimal.TEN.pow(pre.intValue())).setScale(0, BigDecimal.ROUND_HALF_UP).divide(BigDecimal.TEN.pow(pre.intValue()));
		return new NumberNode(d, ((NumberNode)args.get(0)).col, ((NumberNode)args.get(0)).line);
	}
	
}
