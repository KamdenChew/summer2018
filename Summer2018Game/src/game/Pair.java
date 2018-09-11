package game;

public class Pair<E, F> {
	private E firstElement;
	private F secondElement;
	
	/**
	 * Constructs a new Pair
	 *
	 * @param first the first Element in the Pair
	 * @param second the second Element in the Pair
	 */
	public Pair(E first, F second) {
		this.firstElement = first;
		this.secondElement = second;
	}
	
	//Getters
	public E getFirstElement() {
		return this.firstElement;
	}
	
	public F getSecondElement() {
		return this.secondElement;
	}
}
