package parser.operators;

public interface BinaryOperator<E> {

	public Object or(E e);
	public Object and(E e);
	
}
