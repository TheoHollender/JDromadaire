package functions;

import functions.base.array.MapFunction;
import functions.base.cast.ToNumberFunction;
import functions.base.cast.ToStringFunction;
import functions.files.FileNode;
import functions.math.AbsFunction;
import functions.math.AcosFunction;
import functions.math.AcoshFunction;
import functions.math.AsinFunction;
import functions.math.AsinhFunction;
import functions.math.AtanFunction;
import functions.math.AtanhFunction;
import functions.math.CosFunction;
import functions.math.CoshFunction;
import functions.math.ExpFunction;
import functions.math.FacFunction;
import functions.math.LnFunction;
import functions.math.LogFunction;
import functions.math.RadiansFunction;
import functions.math.RootFunction;
import functions.math.SinFunction;
import functions.math.SinhFunction;
import functions.math.SqrtFunction;
import functions.math.TanFunction;
import functions.math.TanhFunction;
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
		cont.setValue("sqrt", new SqrtFunction(-2,-2));
		cont.setValue("abs", new AbsFunction(-2,-2));
		cont.setValue("pi", new NumberNode(Math.toRadians(180),-2,-2));
		cont.setValue("factorial", new FacFunction(-2,-2));
		cont.setValue("acos", new AcosFunction(-2,-2));
		cont.setValue("acosh", new AcoshFunction(-2,-2));
		cont.setValue("asin", new AsinFunction(-2,-2));
		cont.setValue("asinh", new AsinhFunction(-2,-2));
		cont.setValue("atan", new AtanFunction(-2,-2));
		cont.setValue("atanh", new AtanhFunction(-2,-2));
		cont.setValue("cos", new CosFunction(-2,-2));
		cont.setValue("cosh", new CoshFunction(-2,-2));
		cont.setValue("exp", new ExpFunction(-2,-2));
		cont.setValue("ln", new LnFunction(-2,-2));
		cont.setValue("log", new LogFunction(-2,-2));
		cont.setValue("radians", new RadiansFunction(-2,-2));
		cont.setValue("root", new RootFunction(-2,-2));
		cont.setValue("sin", new SinFunction(-2,-2));
		cont.setValue("sinh", new SinhFunction(-2,-2));
		cont.setValue("tan", new TanFunction(-2,-2));
		cont.setValue("tanh", new TanhFunction(-2,-2));
	}
	
}
