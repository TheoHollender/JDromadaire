package parser.operators;

public interface EvaluateOperator<E> {

	public Object add(E e);
	public Object substract(E e);
	public Object multiply(E e);
	public Object divide(E e);
	public Object power(E e);
	
}
