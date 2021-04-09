package functions;

import functions.base.array.MapFunction;
import functions.base.cast.ToNumberFunction;
import functions.base.cast.ToStringFunction;
import functions.files.FileNode;
import libs.LibLoader;
import main.EntryPoint;
import parser.nodes.NumberNode;
import variables.VariableContext;

public class FunctionImporter {

	public static void ImportFunctions() {
		VariableContext cont = EntryPoint.globalContext;
		
		cont.setValue("print", new PrintFunction(-2, -2));
		cont.setValue("help", new HelpFunction(-2,-2));
		cont.setValue("input", new InputFunction(-2,-2));
		cont.setValue("map", new MapFunction(-2,-2));
		cont.setValue("str", new ToStringFunction(-2,-2));
		cont.setValue("int", new ToNumberFunction(-2,-2, 0));
		cont.setValue("number", new ToNumberFunction(-2,-2, 1));
		cont.setValue("File", new FileNode(-2,-2));
		cont.setValue("range", new RangeFunction(-2,-2));
		cont.setValue("sum", new SumFunction(-2,-2));
		cont.setValue("chr", new ChrFunction(-2,-2));
		cont.setValue("ord", new OrdFunction(-2,-2));

		// Math
		cont.setValue("round", new RoundFunction(-2,-2));
		
		LibLoader.registerModule("openns", "libs/lib_openns.jar");
		LibLoader.registerModule("math", "libs/math.jar");
		LibLoader.registerModule("time", "libs/time.jar");
		LibLoader.registerModule("random", "libs/random.jar");
	}
	
}
