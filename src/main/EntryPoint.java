package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import evaluator.Evaluator;
import functions.FunctionImporter;
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
			runFile(args[0]);
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
	    	  runFile(spl[1]);
	    	  continue;
	      }
	      run(line, true);
	      hadError = false;
	    }
	}
	
	public static boolean raised = false;
	public static VariableContext globalContext = new VariableContext();
	
	public static void raiseToken(Token tok) {
		raised = true;
		System.out.println("Error at line "+(tok.line+1)+" at col "+(tok.col+1));
	}
	public static void raiseNode(Node node) {
		raised = true;
		System.out.println("Error at line "+(node.line+1)+" at col "+(node.col+1));
	}
	
	public static void run(String source, boolean printAll) {
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
	    
	    Evaluator.evaluate(evNodes, globalContext, printAll);
	}

}
