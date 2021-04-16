package main;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;

import evaluator.Evaluator;
import functions.FunctionImporter;
import libs.FileImporter;
import parser.Node;
import parser.OpNode;
import parser.Parser;
import parser.nodes.NumberNode;
import variables.VariableContext;

public class EntryPoint {
	static boolean hadError = false;

	public static void main(String[] args) throws Exception{
		// EntryPoint of Dromadaire Java Interpreter
		FunctionImporter.ImportFunctions();
		if (args.length > 1) {
			System.out.println("Usage: jlox [script]");
			System.exit(64);
		} else if (args.length == 1) {
			runAndSetPath(args[0]);
		} else {
			runPrompt();
		}
	}
	
	public static void runFile(String path) throws IOException {
		byte[] bytes = Files.readAllBytes(Paths.get(path));
	    run(new String(bytes, Charset.defaultCharset()).replaceAll("\r", ""), false);
	    
	    if (hadError) System.exit(65);
	}
	
	public static void runPrompt() throws IOException{
		System.out.println("JDrom Console - Alpha [0.0.1]");
		InputStreamReader input = new InputStreamReader(System.in);
	    BufferedReader reader = new BufferedReader(input);

	    for (;;) { 
	      System.out.print("> ");
	      String line = reader.readLine();
	      if (line == null) break;
	      String[] spl = line.split(" ");
	      if (spl[0].equals("RUN")) {
	    	  runAndSetPath(spl[1]);
	    	  continue;
	      }
	      run(line, true);
	      hadError = false;
	    }
	}
	
	public static final String extension = ".dmd";
	public static String relativePath = "./";
	public static void runAndSetPath(String fname) {
		File f = new File(fname);
		FileImporter.underLoad.add(f.getName().replace(".dmd", ""));
		try {
			relativePath = (f.getParentFile().getCanonicalPath());
			runFile(fname);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean raised = false;
	public static VariableContext globalContext = new VariableContext();
	
	public static void raiseToken(Token tok) {
		raised = true;
		System.out.println("Error at line "+(tok.line+1)+" at col "+(tok.col+1));
		throw new RuntimeInterpreterException();
	}
	
	public static void run(String source, boolean printAll) {
		try {
			
		raised = false;
		Scanner scanner = new Scanner(source);
	    List<Token> tokens = scanner.scanTokens();
	    if (tokens == null || raised) {
	    	return ;
	    }

	    Parser p = new Parser();
	    ArrayList<Node> evNodes = p.parse(tokens, null);
	    if (evNodes == null || raised) {
	    	return ;
	    }
	    
	    registerStack(globalContext);
	    setStackName("<module>");
	    Evaluator.evaluate(evNodes, globalContext, printAll);
	    unregisterStack(globalContext);

		} catch (RuntimeInterpreterException e) {
			stack.clear();
		}
	}


	public static ArrayList<StackItem> stack = new ArrayList<StackItem>();
	public static void registerStack(VariableContext ctx) {
		stack.add(new StackItem(ctx));
	}
	public static void unregisterStack(VariableContext ctx) {
		stack.remove(stack.size() - 1);
	}
	public static void setStackDat(int col, int line) {
		stack.get(stack.size() - 1).setInfo(col, line);
	}
	public static void setStackName(String name) {
		stack.get(stack.size() - 1).name = name;
	}

	public static void raiseErr(String string) {
		for(StackItem it:stack) {
			it.throwException();
		}
		System.out.println("Exception : "+string);
		throw new RuntimeInterpreterException();
	}
	
}
