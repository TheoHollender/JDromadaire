package functions;

import functions.files.FileNode;
import main.EntryPoint;
import parser.nodes.DictNode;
import variables.VariableContext;

public class FunctionImporter {

	public static void ImportFunctions() {
		VariableContext cont = EntryPoint.globalContext;
		
		cont.setValue("print", new PrintFunction(-2, -2));
		cont.setValue("help", new HelpFunction(-2,-2));
		cont.setValue("File", new FileNode(-2,-2));
	}
	
}
