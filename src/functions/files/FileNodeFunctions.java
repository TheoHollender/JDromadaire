package functions.files;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import main.EntryPoint;
import parser.nodes.BooleanNode;
import parser.nodes.FunctionNode;
import parser.nodes.StringNode;
import variables.VariableContext;

public class FileNodeFunctions {

	public static class ExistsFunction extends FunctionNode {
	
		public ExistsFunction(int col, int line) {
			super(col, line);
		}
		
		public Object evaluate(VariableContext context, ArrayList<Object> args) {
			if (args.size() == 0) {
				System.out.println("An error occured during exists, could not find this object");
				EntryPoint.raiseNode(this);
				return null;
			}
			
			if (!(args.get(0) instanceof FileNode)) {
				System.out.println("An error occured during exists, this object not instance of FileNode, instance of "+args.get(0).getClass().toString());
				EntryPoint.raiseNode(this);
				return null;
			}
			
			FileNode fn = (FileNode)args.get(0);
			File f = fn.file;
			
			return new BooleanNode(fn.col, fn.line, f.exists());
		}
		
	}
	
	public static class MkdirsFunction extends FunctionNode {
		
		public MkdirsFunction(int col, int line) {
			super(col, line);
		}
		
		public Object evaluate(VariableContext context, ArrayList<Object> args) {
			if (args.size() == 0) {
				System.out.println("An error occured during mkdir, could not find this object");
				EntryPoint.raiseNode(this);
				return null;
			}
			
			if (!(args.get(0) instanceof FileNode)) {
				System.out.println("An error occured during mkdir, this object not instance of FileNode, instance of "+args.get(0).getClass().toString());
				EntryPoint.raiseNode(this);
				return null;
			}
			
			FileNode fn = (FileNode)args.get(0);
			File f = fn.file;
			
			return new BooleanNode(fn.col, fn.line, f.mkdirs());
		}
		
	}
	
	public static class CreateFileFunction extends FunctionNode {
		
		public CreateFileFunction(int col, int line) {
			super(col, line);
		}
		
		public Object evaluate(VariableContext context, ArrayList<Object> args) {
			if (args.size() == 0) {
				System.out.println("An error occured during create_file, could not find this object");
				EntryPoint.raiseNode(this);
				return null;
			}
			
			if (!(args.get(0) instanceof FileNode)) {
				System.out.println("An error occured during create_file, this object not instance of FileNode, instance of "+args.get(0).getClass().toString());
				EntryPoint.raiseNode(this);
				return null;
			}
			
			FileNode fn = (FileNode)args.get(0);
			File f = fn.file;
			
			try {
				return new BooleanNode(fn.col, fn.line, f.createNewFile());
			} catch (IOException e) {
				System.out.println("An IOException error occured in the creation of the file");
				System.out.println("One of the directories in "+fn.file.getPath()+" might not exists or your programm might not have access to this location");
				EntryPoint.raiseNode(this);
			}
			return null;
		}
		
	}
	
	public static class DeleteFileFunction extends FunctionNode {
		
		public DeleteFileFunction(int col, int line) {
			super(col, line);
		}
		
		public Object evaluate(VariableContext context, ArrayList<Object> args) {
			if (args.size() == 0) {
				System.out.println("An error occured during delete_file, could not find this object");
				EntryPoint.raiseNode(this);
				return null;
			}
			
			if (!(args.get(0) instanceof FileNode)) {
				System.out.println("An error occured during delete_file, this object not instance of FileNode, instance of "+args.get(0).getClass().toString());
				EntryPoint.raiseNode(this);
				return null;
			}
			
			FileNode fn = (FileNode)args.get(0);
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
		
		public Object evaluate(VariableContext context, ArrayList<Object> args) {
			if (args.size() == 0) {
				System.out.println("An error occured during read_file, could not find this object");
				EntryPoint.raiseNode(this);
				return null;
			}
			
			if (!(args.get(0) instanceof FileNode)) {
				System.out.println("An error occured during read_file, this object not instance of FileNode, instance of "+args.get(0).getClass().toString());
				EntryPoint.raiseNode(this);
				return null;
			}
			
			FileNode fn = (FileNode)args.get(0);
			File f = fn.file;

			try {
				byte[] bytes = Files.readAllBytes(Paths.get(f.getAbsolutePath()));
				return new StringNode(fn.col, fn.line, new String(bytes));
			} catch (IOException e) {
				System.out.println("An error occured during reading of "+f.getAbsolutePath());
				System.out.println("The file or directories might not exist or you might not have access to these files");
				EntryPoint.raiseNode(this);
				return null;
			}
		}
		
	}
	
	public static class WriteFileFunction extends FunctionNode {
		
		public WriteFileFunction(int col, int line) {
			super(col, line);
		}
		
		public Object evaluate(VariableContext context, ArrayList<Object> args) {
			if (args.size() < 2) {
				System.out.println("An error occured during read_file, could not find this object");
				EntryPoint.raiseNode(this);
				return null;
			}
			
			if (!(args.get(0) instanceof FileNode)) {
				System.out.println("An error occured during write_file, this object not instance of FileNode, instance of "+args.get(0).getClass().toString());
				EntryPoint.raiseNode(this);
				return null;
			}
			if (!(args.get(1) instanceof StringNode)) {
				System.out.println("An error occured during write_file, arg number 0 object not instance of StringNode, instance of "+args.get(1).getClass().toString());
				EntryPoint.raiseNode(this);
				return null;
			}
			
			boolean append = false;
			if(args.size() > 2 && (args.get(2) instanceof BooleanNode)) {
				append = ((BooleanNode)args.get(2)).value;
			}
			
			FileNode fn = (FileNode)args.get(0);
			File f = fn.file;
			String str = ((StringNode)args.get(1)).getValue();

			try {
				FileWriter fw = new FileWriter(f, append);
				fw.write(str);
				fw.close();
				return new BooleanNode(fn.col, fn.line, true);
			} catch (IOException e) {
				System.out.println("An error occured during writing of "+f.getAbsolutePath());
				System.out.println("The file or directories might not exist or you might not have access to these files");
				EntryPoint.raiseNode(this);
				return new BooleanNode(fn.col, fn.line, false);
			}
		}
		
	}
	
}
