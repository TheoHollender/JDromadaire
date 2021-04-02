package functions.files;

import java.io.File;
import java.util.ArrayList;

import main.EntryPoint;
import parser.nodes.StringNode;
import variables.ClassNode;
import variables.VariableContext;

public class FileNode extends ClassNode {
	
	File file;
	public FileNode(int col, int line) {
		super(col, line);
		this.objects.put("filename", null);
		this.objects.put("exists", new FileNodeFunctions.ExistsFunction(-1, -1));
		this.objects.put("mkdir", new FileNodeFunctions.MkdirsFunction(-1, -1));
		this.objects.put("create", new FileNodeFunctions.CreateFileFunction(-1, -1));
		this.objects.put("read", new FileNodeFunctions.ReadFileFunction(-1, -1));
		this.objects.put("write", new FileNodeFunctions.WriteFileFunction(-1, -1));
		this.objects.put("delete", new FileNodeFunctions.DeleteFileFunction(-1, -1));
		isSetable = false;
	}
	
	public FileNode(FileNode other, ArrayList<Object> args) {
		super(other, args);
		if(args.size() > 0 && (args.get(0) instanceof StringNode)) {
			this.objects.put("filename", ((StringNode)args.get(0)).getValue());
		} else {
			System.out.println("Missing filename parameter");
			EntryPoint.raiseNode(this);
		}
		
		String fname = ((StringNode)args.get(0)).getValue();
		file = new File(fname);
		isSetable = false;
	}

	public Object createInstance(VariableContext context, ArrayList<Object> args) {
		if(!isRoot) {
			System.out.println("Can't create instance with sub child");
			return null;
		}
		return new FileNode(this, args);
	}
	
	public String toString() {
		return "File: \""+this.file.getPath()+"\"";
	}
	
}
