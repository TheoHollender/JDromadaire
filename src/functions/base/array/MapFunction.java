package functions.base.array;

import java.util.ArrayList;

import main.EntryPoint;
import parser.nodes.ArrayNode;
import parser.nodes.FunctionNode;
import parser.nodes.NumberNode;
import parser.nodes.ReturnNode;
import variables.VariableContext;

public class MapFunction extends FunctionNode {

	public MapFunction(int col, int line) {
		super(col, line);
		// TODO Auto-generated constructor stub
	}
	
	public Object getReturn(Object o, VariableContext ctx) {
		if (o instanceof ReturnNode) {
			return ((ReturnNode)o).evaluate(ctx);
		}
		return o;
	}

	public Object evaluate(VariableContext ctx, ArrayList<Object> args) {
		if (args.size() != 2 && args.size() != 4) {
			EntryPoint.raiseErr("Expected 2 or 4 arguments, got "+args.size());
			return null;
		}
		
		if (!(args.get(0) instanceof FunctionNode)) {
			EntryPoint.raiseErr("Expected function as first argument, got "+args.get(0).getClass().toString());
			return null;
		}
		if (!(args.get(1) instanceof ArrayNode)) {
			EntryPoint.raiseErr("Expected array as second argument, got "+args.get(1).getClass().toString());
			return null;
		}
		
		
		FunctionNode f = (FunctionNode) args.get(0);
		ArrayNode barr = (ArrayNode) args.get(1);
		ArrayList<Object> datas = new ArrayList<Object>(barr.array.size());
		ArrayList<Object> dtTS = new ArrayList<Object>();

		int index = 0;
		if (args.size() == 4) {
			if (!(args.get(2) instanceof NumberNode)) {
				EntryPoint.raiseErr("Expected number as third argument, got "+args.get(2).getClass().toString());
				return null;
			}
			if(!(((NumberNode)args.get(2)).getValue() instanceof Integer)) {
				EntryPoint.raiseErr("Expected integer as third argument, got "+((NumberNode)args.get(2)).getValue().getClass().toString());
				return null;
			}
			if (!(args.get(3) instanceof ArrayNode)) {
				EntryPoint.raiseErr("Expected array as fourth argument, got "+args.get(3).getClass().toString());
				return null;
			}
			
			index = (int) ((NumberNode)args.get(2)).getValue();
			ArrayNode baseargs = (ArrayNode)args.get(3);
			for (int j = 0; j < baseargs.array.size(); j++) {
				dtTS.add(baseargs.array.get(j));
			}
		} else {
			dtTS.add(null);
		}
		
		for (int i = 0; i < barr.length(); i++) {
			dtTS.set(index, barr.get(new NumberNode(i, -1, -1)));
			datas.add(getReturn(f.evaluate(ctx, dtTS), ctx));
		}
		
		ArrayNode arr = new ArrayNode(this.col, this.line);
		arr.array = datas;
		arr.updateLength();
		return arr;
	}
	
}
