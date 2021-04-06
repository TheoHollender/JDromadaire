package main;

import variables.VariableContext;

public class StackItem {
	
	public VariableContext ctx;
	public int col;
	public int line;
	public String name;
	
	public StackItem(VariableContext ctx) {
		this.ctx = ctx;
	}
	
	public void setInfo(int col, int line) {
		this.col = col;
		this.line = line;
	}
	
	public void throwException() {
		System.out.println("Exception in function "+name+" at line "+(line+1)+" at col "+(col+1));
	}
}
