package game;

public class Pair<E, F> {
	private E firstElement;
	private F secondElement;
	
	public Pair(E first, F second) {
		this.firstElement = first;
		this.secondElement = second;
	}
	
	public E getFirstElement() {
		return this.firstElement;
	}
	
	public F getSecondElement() {
		return this.secondElement;
	}
}
