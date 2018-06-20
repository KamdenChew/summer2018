
public class Room {
	/*
	 * Represents a rectangular dungeon room
	 */
	
	private CoordinatePair origin;
	private int width;
	private int height;
	
	
	/**
	 * Constructs a new room with a specified origin location (x and y, the the most upper left coordinate) and dimensions width and height
	 *
	 * @param x the x component of this room's origin location
	 * @param y the y component of this room's origin location
	 */
	public Room(int x, int y, int width, int height) {
		this.origin = new CoordinatePair(x,y);
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Returns a boolean for whether or not the specified coordinates are within this room
	 *
	 * @param pair the CoordinatePair that is being checked to be within this room
	 * @return a boolean for whether or not pair is in this room
	 */
	public boolean containsCoordinatePair(CoordinatePair pair) {
		int x = origin.getX();
		int y = origin.getY();
		boolean contains = false;
		for(int c = x; c < x + width; c++) {
			for(int r = y; r < y + height; r++) {
				if(pair.getX() == c && pair.getY() == r) {
					contains = true;
				}
			}
		}
		return contains;
	}
	
	
}
