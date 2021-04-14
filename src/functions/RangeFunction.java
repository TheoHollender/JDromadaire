package functions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import main.EntryPoint;
import parser.nodes.FunctionNode;
import parser.nodes.NumberNode;
import parser.nodes.StringNode;
import variables.VariableContext;

public class RangeFunction extends FunctionNode {

	public RangeFunction(int col, int line) {
		super(col, line);
	}

	public Object evaluate(VariableContext context, ArrayList<Object> args, HashMap<StringNode, Object> kwargs_entry) {
		BigDecimal debut = BigDecimal.ZERO;
		BigDecimal rng = BigDecimal.ONE;
		BigDecimal fin = BigDecimal.ONE;
		if(args.size()==1) {
			if (!(args.get(0) instanceof NumberNode)) {
	            EntryPoint.raiseErr("Expected number as first argument, got "+args.get(0).getClass().toString());
	            return null;
			}
			fin = ((NumberNode)args.get(0)).getNumber();
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
			debut = ((NumberNode)args.get(0)).getNumber();
			fin = ((NumberNode)args.get(1)).getNumber();
		}
		else if(args.size()==3) {
			if (!(args.get(0) instanceof NumberNode)) {
	            EntryPoint.raiseErr("Expected number as first argument, got "+args.get(0).getClass().toString());
	            return null;
			}
			if (!(args.get(1) instanceof NumberNode)) {
	            EntryPoint.raiseErr("Expected number as second argument, got "+args.get(1).getClass().toString());
	            return null;
			}
			if (!(args.get(2) instanceof NumberNode)) {
	            EntryPoint.raiseErr("Expected number as third argument, got "+args.get(2).getClass().toString());
	            return null;
			}
			debut = ((NumberNode)args.get(0)).getNumber();
			fin = ((NumberNode)args.get(1)).getNumber();
			rng = ((NumberNode)args.get(2)).getNumber();
			if(rng.equals(BigDecimal.ZERO)) {
				EntryPoint.raiseErr("Third argument cannot be a zero");
	            return null;
			}
		}
		else {
			EntryPoint.raiseErr("1,2 or 3 arguments expected, "+args.size()+" received");
			return null;
		}
		return new RangeNode(this.col, this.line, new RangeIterator(this.col, this.line, debut, fin, rng));
	}
	
}
