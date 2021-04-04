package variables;

import java.util.HashMap;

import parser.nodes.StringNode;
import parser.operators.ListOperator;

public class VariableContext {

	public HashMap<String, Object> values = new HashMap<String, Object>();
	
	public void setValue(String name, Object object) {
		String[] names = name.split("\\.");
		
		Object l = get(names, names.length - 1);

		if (names.length == 1) {
			values.put(name, object);
		} else {
			if( l != null && l instanceof ListOperator ) {
				((ListOperator)l).set(new StringNode(-1, -1, names[names.length - 1]), object);
			}
		}
	}
	public Object getValue(String name) {
		String[] names = name.split("\\.");
		
		return get(names, names.length);
	}
	public Object getLastValue(String name) {
		String[] names = name.split("\\.");
		
		return get(names, names.length - 1);
	}
	
	private Object get(String[] name, int lastIndex) {
		if (lastIndex == 0) {
			return this;
		}
		
		if(name[0].equals("")) {
			return null;
		}
		
		Object dat = values.get(name[0]);
		
		for (int i = 1; i < Math.min(name.length, lastIndex); i ++) {
			if(name[0].equals("")) {
				return null;
			}
			
			if( dat != null && dat instanceof ListOperator ) {
				dat = ((ListOperator)dat).get(new StringNode(-1,-1,name[i]));
			} else if(dat != null) {
				System.out.println("Object "+name[i-1]+" can't be used as list: "+dat);
				return null;
			}
		}
		
		return dat;
	}
}
