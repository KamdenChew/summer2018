import java.util.ArrayList;
import java.util.List;

public class Room {
	/*
	 * Represents a rectangular 2D dungeon room
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
	 * @return a CoordinatePair representing the upperleft most point of the room
	 */
	public CoordinatePair getOrigin() {
		return this.origin;
	}
	
	/**
	 * Returns a boolean for whether or not the specified coordinates are within this room
	 *
	 * @param x the x coordinate of the point to check
	 * @param y the y coordinate of the point to check
	 * @return a boolean for whether or not (x, y) is in this room
	 */
	public boolean contains(int x, int y) {
		int originX = origin.getX();
		int originY = origin.getY();
		boolean contains = false;
		for(int c = originX; c < originX + width; c++) {
			for(int r = originY; r < originY + height; r++) {
				if(x == c && y == r) {
					contains = true;
				}
			}
		}
		return contains;
	}
	
	/**
	 * @return a list of CoordinatePairs that surround the entirety of the room.
	 */
	public List<CoordinatePair> getBoundary() {
		List<CoordinatePair> boundary = new ArrayList<CoordinatePair>();
		for(int c = origin.getX(); c < origin.getX() + width; c++) {
			boundary.add(new CoordinatePair(c, origin.getY() - 1));
			boundary.add(new CoordinatePair(c, origin.getY() + height));
		}
		for(int r = origin.getY(); r < origin.getY() + height; r++) {
			boundary.add(new CoordinatePair(origin.getX() - 1, r));
			boundary.add(new CoordinatePair(origin.getX() + width, r));
		}
		
		return boundary;
	}
	
	
}
