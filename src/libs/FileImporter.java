package libs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import main.EntryPoint;
import main.Token;
import variables.ClassNode;
import variables.VariableContext;

public class FileImporter {

	public static HashMap<String, ClassNode> modules = new HashMap();
	public static ArrayList<String> underLoad = new ArrayList();
	
	public static ClassNode importFile(String name, Token t) {
		File f = new File(EntryPoint.relativePath+"/"+name.replace(".", "/")+EntryPoint.extension);
		File dir = new File(EntryPoint.relativePath+"/"+name.replace(".", "/"));
		if (dir.isDirectory()) {
			return importFile(name+".__init__", t);
		}
		if (modules.containsKey(name))  {
			return modules.get(name);
		}
		if (underLoad.contains(name)) {
			EntryPoint.raiseErr("Recursive import not allowed, tryed to import "+name);
			return null;
		}
		
		if (!f.exists()) {
			System.out.println("The file "+f+" does not exist");
			EntryPoint.raiseToken(t);
			return null;
		}
		
		underLoad.add(name);
		VariableContext gbSave = EntryPoint.globalContext;
		ClassNode cs = new ClassNode(-1, -1);
		EntryPoint.globalContext = new VariableContext();
		try {
			EntryPoint.runFile(f.getAbsolutePath());
			cs.importContext(EntryPoint.globalContext);
			EntryPoint.globalContext = gbSave;
			
			modules.put(name, cs);
			
			underLoad.remove(name);
			
			return cs;
		} catch (IOException e) {
			EntryPoint.raiseErr("An internal error occured during import of "+name);
		}
		
		return null;
	}
	
}
