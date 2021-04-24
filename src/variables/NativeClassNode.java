package variables;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import main.EntryPoint;

public class NativeClassNode extends ClassNode {
	
	private Class cl;

	public NativeClassNode(int col, int line, Class cl) {
		super(col, line);
		this.cl = cl;
	}

	public NativeClassNode build(String baseType) {
		for (Method m:cl.getMethods()) {
			NativeMethodNode node;
			if (Modifier.isStatic(m.getModifiers())) {
				node = new NativeMethodNode(-1, -1, baseType+"."+m.getName(), true, m);
			} else {
				node = new NativeMethodNode(-1, -1, baseType+"."+m.getName(), false, m);
			}
			
			if (objects.containsKey(m.getName())) {
				objects.put(m.getName(), node.setAlternative(objects.get(m.getName())));
			} else {
				objects.put(m.getName(), node);
			}
		}
		
		return this;
	}
	
}
