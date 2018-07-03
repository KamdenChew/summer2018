package game;
public class CoordinatePair {
	
	// Represents a Cartesian 2D coordinate pair
	 
	private int x;
	private int y;
	
	/**
	 * Constructs a new CoordinatePair with a specified x and y coordinate
	 *
	 * @param x the x component of this CoordinatePair
	 * @param y the y component of this CoordinatePair
	 */
	public CoordinatePair(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * @return the x component of this CoordinatePair
	 */
	public int getX() {
		return this.x;
	}
	
	/**
	 * @return the number of columns in this Array2D
	 */
	public int getY() {
		return this.y;
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
	@Override
	public int hashCode() {
		return 8 * this.x + 6 * this.getY() + 37;
	}
	
	@Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        
        if (!(o instanceof CoordinatePair)) {
            return false;
        }
         
        CoordinatePair other = (CoordinatePair) o;
        return (this.x == other.getX() && this.y == other.getY());
    }
}
