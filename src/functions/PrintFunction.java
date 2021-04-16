package functions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import parser.nodes.FunctionNode;
import parser.nodes.StringNode;
import variables.VariableContext;

public class PrintFunction extends FunctionNode {

	public PrintFunction(int col, int line) {
		super(col, line);
	}
	
	public Object evaluate(VariableContext context, ArrayList<Object> args, HashMap<StringNode, Object> kwargs_entry) {
		String end = "\n";
		String sep = " ";
		for(Entry<StringNode, Object> en:kwargs_entry.entrySet()) {
			if (en.getKey().getValue().equals("end")
					&& en.getValue() != null
					&& en.getValue() instanceof StringNode) {
				end = ((StringNode)en.getValue()).getValue();
			} else if (en.getKey().getValue().equals("sep")
					&& en.getValue() != null
					&& en.getValue() instanceof StringNode) {
				sep = ((StringNode)en.getValue()).getValue();
			}
		}
		
		StringBuffer strbf = new StringBuffer();
		boolean hadLast = false;
		for(Object o:args) {
			if (hadLast) {
				strbf.append(sep);
			}
			strbf.append(o);
			hadLast = true;
		}
		strbf.append(end);
		
		System.out.print(strbf.toString());
		
		return null;
	}

}
