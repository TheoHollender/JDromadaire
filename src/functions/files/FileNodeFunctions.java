package functions.files;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import main.EntryPoint;
import parser.Node;
import parser.nodes.BooleanNode;
import parser.nodes.FunctionNode;
import parser.nodes.StringNode;
import variables.VariableContext;

public class FileNodeFunctions {

	public static boolean argsFound = false;
	public static FileNode getFromArgs(ArrayList<Object> args, String name, Node n) {
		argsFound = true;
		if (args.size() == 0) {
			EntryPoint.raiseErr("An error occured during "+name+", could not find this object");
			argsFound = false;
			return null;
		}
		
		if (!(args.get(0) instanceof FileNode)) {
			EntryPoint.raiseErr("An error occured during "+name+", this object not instance of FileNode, instance of "+args.get(0).getClass().toString());
			argsFound = false;
			return null;
		}
		return (FileNode)args.get(0);
	}
	
	public static class ExistsFunction extends FunctionNode {
	
		public ExistsFunction(int col, int line) {
			super(col, line);
		}
		
		public Object evaluate(VariableContext context, ArrayList<Object> args, HashMap<StringNode, Object> kwargs_entry) {
			FileNode fn = getFromArgs(args, "exists", this);
			if (!argsFound) {return null;}
			File f = fn.file;
			
			return new BooleanNode(fn.col, fn.line, f.exists());
		}
		
	}
	
	public static class MkdirsFunction extends FunctionNode {
		
		public MkdirsFunction(int col, int line) {
			super(col, line);
		}
		
		public Object evaluate(VariableContext context, ArrayList<Object> args, HashMap<StringNode, Object> kwargs_entry) {
			FileNode fn = getFromArgs(args, "mkdir", this);
			if (!argsFound) {return null;}
			File f = fn.file;
			
			return new BooleanNode(fn.col, fn.line, f.mkdirs());
		}
		
	}
	
	public static class CreateFileFunction extends FunctionNode {
		
		public CreateFileFunction(int col, int line) {
			super(col, line);
		}
		
		public Object evaluate(VariableContext context, ArrayList<Object> args, HashMap<StringNode, Object> kwargs_entry) {
			FileNode fn = getFromArgs(args, "create_file", this);
			if (!argsFound) {return null;}
			
			File f = fn.file;
			
			try {
				return new BooleanNode(fn.col, fn.line, f.createNewFile());
			} catch (IOException e) {
				EntryPoint.raiseErr("An IOException error occured in the creation of the file\nOne of the directories in \"+fn.file.getPath()+\" might not exists or your programm might not have access to this location");
			}
			return null;
		}
		
	}
	
	public static class DeleteFileFunction extends FunctionNode {
		
		public DeleteFileFunction(int col, int line) {
			super(col, line);
		}
		
		public Object evaluate(VariableContext context, ArrayList<Object> args, HashMap<StringNode, Object> kwargs_entry) {
			FileNode fn = getFromArgs(args, "delete", this);
			if (!argsFound) {return null;}
			File f = fn.file;
			
			if (f.isDirectory()) {
				String[]entries = f.list();
				for(String s: entries){
				    File currentFile = new File(f.getPath(),s);
				    currentFile.delete();
				}
				return new BooleanNode(fn.col, fn.line, f.delete());
			}
			
			return new BooleanNode(fn.col, fn.line, f.delete());
		}
		
	}
	
	public static class ReadFileFunction extends FunctionNode {
		
		public ReadFileFunction(int col, int line) {
			super(col, line);
		}
		
		public Object evaluate(VariableContext context, ArrayList<Object> args, HashMap<StringNode, Object> kwargs_entry) {
			FileNode fn = getFromArgs(args, "read_file", this);
			if (!argsFound) {return null;}
			
			File f = fn.file;

			try {
				byte[] bytes = Files.readAllBytes(Paths.get(f.getAbsolutePath()));
				return new StringNode(fn.col, fn.line, new String(bytes));
			} catch (IOException e) {
				EntryPoint.raiseErr("An IOException error occured in the creation of the file\nOne of the directories in \"+fn.file.getPath()+\" might not exists or your programm might not have access to this location");
				return null;
			}
		}
		
	}
	
	public static class WriteFileFunction extends FunctionNode {
		
		public WriteFileFunction(int col, int line) {
			super(col, line);
		}
		
		public Object evaluate(VariableContext context, ArrayList<Object> args, HashMap<StringNode, Object> kwargs_entry) {
			FileNode fn = getFromArgs(args, "exists", this);
			if (!argsFound || args.size() < 2) {return null;}
			
			if (!(args.get(1) instanceof StringNode)) {
				EntryPoint.raiseErr("An error occured during write_file, arg number 0 object not instance of StringNode, instance of "+args.get(1).getClass().toString());
				return null;
			}
			
			boolean append = false;
			if(args.size() > 2 && (args.get(2) instanceof BooleanNode)) {
				append = ((BooleanNode)args.get(2)).value;
			}
			
			File f = fn.file;
			String str = ((StringNode)args.get(1)).getValue();

			try {
				FileWriter fw = new FileWriter(f, append);
				fw.write(str);
				fw.close();
				return new BooleanNode(fn.col, fn.line, true);
			} catch (IOException e) {
				EntryPoint.raiseErr("An IOException error occured in the creation of the file\nOne of the directories in \"+fn.file.getPath()+\" might not exists or your programm might not have access to this location");
				return new BooleanNode(fn.col, fn.line, false);
			}
		}
		
	}
	
}
