package libs;

import java.util.ArrayList;
import java.util.HashMap;

import parser.Node;
import variables.ClassNode;

public interface LibExporter {
	
	public HashMap<String,ClassNode> exportClasses();

}
