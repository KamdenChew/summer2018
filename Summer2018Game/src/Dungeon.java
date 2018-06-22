import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class Dungeon {
	/*
	 * Dungeons are represented with Array2D's of ints. -1's represent the
	 * dungeon outer boundary, 0's represent a walkable path, 1's represent a wall
	 * (not walkable), 2's represent a walkable path, 3's represent an exit from the dungeon
	 */

	private Random rand = new Random();
	private int difficulty;
	private Array2D<Integer> data;
	private ArrayList<Room> rooms;
	
	//Room Count Coefficient controls how many attempts we give to generating rooms. See generateRooms()
	private static final int ROOM_COUNT_COEFFICIENT = 5;

	private static final int MIN_ROOM_DIMENSION = 3;

	// Room Dimension Variance is additive, meaning if the Min Room Dimension is
	// 3, and the Room Dimension Variance is 2
	// then the Max Room Dimension is 5
	private static final int ROOM_DIMENSION_VARIANCE = 2;

	
	/**
	 * Constructs a new Dungeon with a specified difficulty level
	 *
	 * @param difficulty an int representing the difficulty level of the Dungeon
	 */
	public Dungeon(int difficulty) {
		int numDungeonRows;
		int numDungeonColumns;
		this.rooms = new ArrayList<Room>();
		if (difficulty == 0) {
			System.out.println("Difficulty set to non-hostile.");

			// Set Fixed size for non-hostile dungeons
			numDungeonRows = 10;
			numDungeonColumns = 10;
		} else if (difficulty == 1) {
			System.out.println("Difficulty set to easy.");

			// Set size to between 10x10 and 20x20 for easy dungeons
			numDungeonRows = rand.nextInt(11) + 10;
			numDungeonColumns = rand.nextInt(11) + 10;
		} else if (difficulty == 2) {
			System.out.println("Difficulty set to normal.");

			// Set size to between 20x20 and 35x35 for easy dungeons
			numDungeonRows = rand.nextInt(16) + 20;
			numDungeonColumns = rand.nextInt(16) + 20;
		} else if (difficulty == 3) {
			System.out.println("Difficulty set to hard.");

			// Set size to between 35x35 and 50x50 for easy dungeons
			numDungeonRows = rand.nextInt(16) + 35;
			numDungeonColumns = rand.nextInt(16) + 35;
		} else {
			throw new IllegalArgumentException("Difficulty should be an integer value from 0-3.");
		}

		// System.out.println();
		this.difficulty = difficulty;
		data = new Array2D<Integer>(numDungeonRows + 2, numDungeonColumns + 2);
		generateDungeon(data);
	}

	/**
	 * Edits the underlying Array2D structure of this dungeon to represent
	 * a randomly generated dungeon
	 *
	 * @param data and Array2D<Integer> intended to be edited to reflect a Dungeon
	 */
	private void generateDungeon(Array2D<Integer> data) {
		
		//Step 1: Add boundary walls
		setBoundaryWalls(data);
		
		//Step 2: Set all internal data in the Array2D to walls 
		setInnerWalls(data);
		
		//Step 3: Overwrite some of the walls to be randomly generated rooms
		generateRooms(data);
		
		//Step 4: Put an exit from the dungeon in one of the rooms
		setExit(data);
		
		//Step 5: Add paths to our dungeon connecting all of our rooms
		generatePaths(data);
	}

	/**
	 * Edits the underlying Array2D structure of this dungeon by adding boundary walls
	 *
	 * @param data and Array2D<Integer> intended to be edited to reflect a Dungeon
	 */
	private void setBoundaryWalls(Array2D<Integer> data) {
		// Set outer boundary tiles
		for (int x = 0; x < data.getNumColumns(); x++) {
			data.set(x, 0, -1);
			data.set(x, data.getNumRows() - 1, -1);
		}

		for (int y = 0; y < data.getNumRows(); y++) {
			data.set(0, y, -1);
			data.set(data.getNumColumns() - 1, y, -1);
		}
	}
	
	/**
	 * Edits the underlying Array2D structure of this dungeon by setting all internal data to walls
	 * 
	 * @param data and Array2D<Integer> intended to be edited to reflect a Dungeon
	 */
	private void setInnerWalls(Array2D<Integer> data) {
		// Set all tiles to walls
		for (int r = 1; r < data.getNumRows() - 1; r++) {
			for (int c = 1; c < data.getNumColumns() - 1; c++) {
				data.set(c, r, 1);
			}
		}

	}

	/**
	 * Edits the underlying Array2D structure of this dungeon by adding rooms
	 * @requires internal and boundary walls have already been placed in data
	 * @param data an Array2D<Integer> intended to be edited to reflect a Dungeon
	 */
	private void generateRooms(Array2D<Integer> data) {

		// Place one room guaranteed to ensure we have an escape/start room
		int roomWidth = rand.nextInt(ROOM_DIMENSION_VARIANCE) + MIN_ROOM_DIMENSION;
		int roomHeight = rand.nextInt(ROOM_DIMENSION_VARIANCE) + MIN_ROOM_DIMENSION;
		int startRoomColumn = rand.nextInt(data.getNumColumns() - roomWidth - 2) + 1;
		int startRoomRow = rand.nextInt(data.getNumRows() - roomHeight - 2) + 1;

		// Generate our guaranteed room
		for (int x = startRoomColumn; x < startRoomColumn + roomWidth; x++) {
			for (int y = startRoomRow; y < startRoomRow + roomHeight; y++) {
				data.set(x, y, 0);
			}
		}
		
		//Save our guaranteed room
		rooms.add(new Room(startRoomColumn, startRoomRow, roomWidth, roomHeight));

		int roomCountTries = ROOM_COUNT_COEFFICIENT * difficulty;

		// If the game is set to non-hostile, give 3 tries
		if (difficulty == 0) {
			roomCountTries = 3;
		}

		int currTries = 0;
		while (currTries < roomCountTries) {
			roomWidth = rand.nextInt(ROOM_DIMENSION_VARIANCE) + MIN_ROOM_DIMENSION;
			roomHeight = rand.nextInt(ROOM_DIMENSION_VARIANCE) + MIN_ROOM_DIMENSION;

			int randomRow = rand.nextInt(data.getNumRows() - 1) + 1;
			int randomColumn = rand.nextInt(data.getNumColumns() - 1) + 1;

			// Preliminary check to see if the room will go out of bounds
			if (randomColumn + roomWidth > data.getNumColumns() || randomRow + roomHeight > data.getNumRows()) {
				currTries++;

			} else {
				// Check to see if placing the room would overlap with another
				// room, if it does, then we fail and we add a try to our count
				boolean overlap = false;
				for (int x = randomColumn; x < randomColumn + roomWidth; x++) {
					for (int y = randomRow; y < randomRow + roomHeight; y++) {

						boolean neighborRoom = false;

						// Check all neighbors to see if we will end up creating
						// a combined room (Unwanted)
						for (CoordinatePair neighbor : data.getOrderedNeighbors(x, y)) {
							Integer tileVal = null;
							if(neighbor != null) {
								tileVal = data.get(neighbor.getX(), neighbor.getY());
							} 
							
							if (tileVal != null && tileVal == 0) {
								neighborRoom = true;
							}
						}

						// If the tile is a walkable path, or we'll combine
						// rooms, we want to set a flag
						if (data.get(x, y) == 0 || data.get(x, y) == -1 || neighborRoom) {
							overlap = true;
							break;
						}
					}
				}

				// If we overlapped, increment our currTries
				if (overlap) {
					currTries++;

				// Else, we add the room
				} else {
					
					//Save the coordinates of this room
					rooms.add(new Room(randomColumn, randomRow, roomWidth, roomHeight));
					
					//Apply the change to our Array2D 
					for (int x = randomColumn; x < randomColumn + roomWidth; x++) {
						for (int y = randomRow; y < randomRow + roomHeight; y++) {
							data.set(x, y, 0);
						}
					}

					// Reset our counter to allow roomCountTries number of
					// attempts to place the next room
					currTries = 0;
				}
			}
		}
	}
	
	/**
	 * Edits the underlying Array2D structure of this dungeon by adding an exit in a room.
	 * @requires Dungeon rooms have already been placed in data
	 * @param data and Array2D<Integer> intended to be edited to reflect a Dungeon
	 */
	private void setExit(Array2D<Integer> data) {
		//Add an exit in an existing room
		
		boolean exitPlaced = false;
		while (!exitPlaced) {
			int x = rand.nextInt(data.getNumColumns() - 2) + 1;
			int y = rand.nextInt(data.getNumRows() - 2) + 1;
			if (data.get(x, y) == 0) {
				exitPlaced = true;
				data.set(x, y, 3);
			}
		}
	}
	
	/**
	 * Edits the underlying Array2D structure of this dungeon by adding paths
	 * @requires Dungeon rooms, inner and boundary walls have all been placed in data
	 * @param data and Array2D<Integer> intended to be edited to reflect a Dungeon
	 */
	private void generatePaths(Array2D<Integer> data) {
		HashSet<Room> visited = new HashSet<Room>();
		Room initialRoom = rooms.get(0);
		visited.add(initialRoom);
		
		int randomIndex = rand.nextInt(initialRoom.getBoundary().size());
		CoordinatePair currTile = null;
		
		//Pick a place to start creating paths from. This should be a tile
		//adjacent to a room, but shouldn't be on the outer boundary.
		while (currTile == null || data.get(currTile.getX(), currTile.getY()) == -1) {
			currTile = initialRoom.getBoundary().get(randomIndex);
		}
		
		data.set(currTile.getX(), currTile.getY(), 2);
		
		//While we haven't connected all of the rooms, propagate out path
//		while(visited.size() != rooms.size()) {
//			
//		}
		
	}
}
