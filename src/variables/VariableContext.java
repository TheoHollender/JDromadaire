package variables;

import java.util.ArrayList;
import java.util.HashMap;

import main.EntryPoint;
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
	public void importClass(ClassNode cs, ArrayList<String> strs) {
		if (strs.contains("*")) {
			values.putAll(cs.objects);
		} else {
			for (String s:strs) {
				if (cs.objects.containsKey(s)) {
					values.put(s, cs.objects.get(s));
				} else {
					EntryPoint.raiseErr("The requested name does not exist in import");
				}
			}
		}
	}

	public void createClasses(String name, Object data)  {
		String[] spl = name.split("\\.");
		
		if (spl.length > 1) {
			ListOperator act = null;
			if(!(this.values.getOrDefault(spl[0], null) instanceof ListOperator)) {
				ClassNode cl = new ClassNode(-1,-1);
				cl.name = spl[0];
				this.values.put(spl[0], cl);
				act = cl;
			} else {
				act = (ListOperator) this.values.get(spl[0]);
			}
			
			for(int i = 1; i < spl.length - 1; i++) {
				if(!(this.values.getOrDefault(spl[i], null) instanceof ListOperator)) {
					ClassNode cl = new ClassNode(-1,-1);
					cl.name = spl[i];
					this.values.put(spl[i], cl);
					act = cl;
				} else {
					act = (ListOperator) this.values.get(spl[i]);
				}
			}
		}
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
