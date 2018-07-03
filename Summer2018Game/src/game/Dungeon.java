package game;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import org.omg.CORBA.SystemException;

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
	
	//Max path generation iterations controls how long we continue trying to propagate paths before we restart the generatePaths() method.
	//Max entrance iterations controls how many times we attempt to randomly place a path on the boundary of a room before we restart the generatePaths() method.
	//This is as a fail safe for any random path generations that get stuck
	private static final int MAX_PATH_GENERATION_ITERATIONS = 5000;
	private static final int MAX_ENTRANCE_ITERATIONS = 25;
	
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
	 * @throws IOException 
	 * 
	 */
	public Dungeon(int difficulty) throws IOException {
		int numDungeonRows;
		int numDungeonColumns;
		this.rooms = new ArrayList<Room>();
		if (difficulty == 0) {
//			System.out.println("Difficulty set to non-hostile.");

			// Set Fixed size for non-hostile dungeons
			numDungeonRows = 10;
			numDungeonColumns = 10;
		} else if (difficulty == 1) {
//			System.out.println("Difficulty set to easy.");

			// Set size to between 10x10 and 15x15 for easy dungeons
			numDungeonRows = rand.nextInt(6) + 10;
			numDungeonColumns = rand.nextInt(6) + 10;
		} else if (difficulty == 2) {
//			System.out.println("Difficulty set to normal.");

			// Set size to between 15x15 and 20x20 for easy dungeons
			numDungeonRows = rand.nextInt(6) + 15;
			numDungeonColumns = rand.nextInt(6) + 15;
		} else if (difficulty == 3) {
//			System.out.println("Difficulty set to hard.");

			// Set size to between 20x20 and 25x25 for easy dungeons
			numDungeonRows = rand.nextInt(6) + 20;
			numDungeonColumns = rand.nextInt(6) + 20;
		} else {
			throw new IllegalArgumentException("Difficulty should be an integer value from 0-3.");
		}

		// System.out.println();
		this.difficulty = difficulty;
		data = new Array2D<Integer>(numDungeonRows + 2, numDungeonColumns + 2);
		generateDungeon(data);
	}
	
	public Array2D<Integer> getData() { 
		return new Array2D<Integer>(this.data);
	}

	/**
	 * Edits the underlying Array2D structure of this dungeon to represent
	 * a randomly generated dungeon
	 *
	 * @param data and Array2D<Integer> intended to be edited to reflect a Dungeon
	 * @throws IOException 
	 * 
	 */
	private void generateDungeon(Array2D<Integer> data) throws IOException {
		
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
	 * using a randomized Prim's algorithm
	 * @requires Dungeon rooms, inner and boundary walls have all been placed in data
	 * @param data and Array2D<Integer> intended to be edited to reflect a Dungeon
	 * @throws IOException 
	 */
	private void generatePaths(Array2D<Integer> data) throws IOException {
		
//		System.out.println("Method Called!");
		
		Array2D<Integer> originalCopy = new Array2D<Integer>(data);
		
		PrintWriter out = new PrintWriter(new FileWriter("./debug.txt"));
		
		out.println("Initial:");
		out.println();
		out.println(data);
		out.println();
		
		//TODO fix visited flag, currently allows disjoint sets, remove iterator i and debug prints
		int i = 0;
		int j = 0;
		ArrayList<CoordinatePair> walls = new ArrayList<CoordinatePair>();
		HashSet<Room> visited = new HashSet<Room>();
		HashSet<Room> adjacentRooms = new HashSet<Room>();
		int randomIndex;
		
		//While we don't have all the rooms connected
		while(visited.size() < rooms.size() && i < MAX_PATH_GENERATION_ITERATIONS) {
			
//			System.out.println("1");
			
			out.println("Top of loop");
			out.println("Still rooms left to connect on iteration: " + i);
			//If we are out of options for this path, and haven't visited all the rooms, pick a new starting point
			if(walls.size() == 0) {
				
				out.println("Out of walls, picking new random room");
				//If we are starting a new path, reset the adjacentRooms to empty
				//We will add adjacent rooms as the path propagates
				adjacentRooms = new HashSet<Room>();
				
				//Find an unvisited random room
				randomIndex = rand.nextInt(rooms.size());
				Room randomRoom = null;
				while(randomRoom == null || visited.contains(randomRoom)) {
					
//					System.out.println("2");
					
					randomRoom = rooms.get(randomIndex);
					randomIndex = rand.nextInt(rooms.size());
				}
				
				//Pick a place to start creating paths from. This should be a tile
				//adjacent to the random room, shouldn't be on the boundary, and shouldn't
				//be adjacent to another path on the boundary
				CoordinatePair startTile = null;
				randomIndex = rand.nextInt(randomRoom.getBoundary().size());
				
				//Only check 100 random tiles on the boundary, if we can't find an eligible tile after that, there is high likelihood that
				//there are no eligible tiles.
				j = 0;
				while (j < 100 && (startTile == null || data.get(startTile.getX(), startTile.getY()) == -1 || !canBePath(data, startTile))) {
					
//					System.out.println("3");
					
					startTile = randomRoom.getBoundary().get(randomIndex);
					randomIndex = rand.nextInt(randomRoom.getBoundary().size());
					j++;
				}
				
				if(j == 100) {
					System.out.println("Couldn't find a starting point for the path");
					break;
				}
				
				//Set our first path tile
				data.set(startTile.getX(), startTile.getY(), 2);
				
				
				//Look through neighbors to find all adjacent walls
				for(CoordinatePair neighbor: data.getOrderedNeighbors(startTile.getX(), startTile.getY())) {
					
					//If the neighbor is a wall, it belongs in the walls list
					if(data.get(neighbor.getX(), neighbor.getY()) == 1) {
						walls.add(neighbor);
					}
				}
				
				//If we haven't visited any rooms yet, then add this room as the first visited room, otherwise
				//we will mark this room as visited later when it connects to other elements of the visited set
				if(visited.size() == 0) {
					visited.add(randomRoom);
					out.println("Added initial room to visited: " + randomRoom.getOrigin());
				}
			}
			
			//While there are still walls to propagate to
			while(walls.size() > 0) {
				
//				System.out.println("4");
				
				//Select a random wall
				randomIndex = rand.nextInt(walls.size());
				CoordinatePair randomWall = walls.get(randomIndex);
				
				
				boolean isValidPath = canBePath(data, randomWall);
				
				if(isValidPath) {
					
					//If we are adding this tile to our path, we must add all adjacent rooms to this tile to our adjacent rooms set for the path
					for(Room newAdjacentRoom: getAdjacentRooms(data, randomWall)) {
						adjacentRooms.add(newAdjacentRoom);
					}
					
					boolean connected = false;
					//Check if we connected to any visited rooms
					for(Room adjacentRoom: adjacentRooms) {
						if(visited.contains(adjacentRoom)) {
							connected = true;
							break;
						}
					}
					
					int prevSize = visited.size();
					
					if(connected) {
						//If we did, then we have now connected all the rooms that this path connects
						for(Room adjacentRoom: adjacentRooms) {
							visited.add(adjacentRoom);
						}
					}
					out.println("Added " + (visited.size() - prevSize) + " rooms to visited");
					out.println("visitied now contains: ");
					for(Room room: visited) {
						out.println(room.getOrigin());
					}
					
					//Set this wall to a path
					data.set(randomWall.getX(), randomWall.getY(), 2);
					
					//Look through neighbors to find all adjacent walls
					for(CoordinatePair neighbor: data.getOrderedNeighbors(randomWall.getX(), randomWall.getY())) {
						
						//If the neighbor is a wall, it belongs in the walls list
						if(data.get(neighbor.getX(), neighbor.getY()) == 1) {
							walls.add(neighbor);
						}
					}
				}
				
				//The randomWall was assigned to a path, remove from walls
				walls.remove(randomWall);
				out.println();
				out.println("i: " + i);
				out.println();
				out.println(data);
				i++;
			}
		}
		
		if(i == MAX_PATH_GENERATION_ITERATIONS || j == 100) {
			if(j == 100 && i != MAX_PATH_GENERATION_ITERATIONS) {
				System.out.println("Found a new case!");
			}
			
			System.out.print("Original Copy before call: ");
			System.out.println(originalCopy);
			System.out.println("Maximum Iteration Threshold Achieved");
			generatePaths(originalCopy);
			this.data = originalCopy;
			
			System.out.println("Original Copy after call: ");
			System.out.print(originalCopy);
			
			System.out.println("Data after call: ");
			System.out.println(this.data);
			
		}
		out.close();
	}
	
	private boolean canBePath(Array2D<Integer> data, CoordinatePair point) {
		
		//Check criteria for this wall becoming a path
		HashSet<Room> adjacentRooms = new HashSet<Room>();
		int numAdjacentPaths = 0;
		int numEntrances = 0;
		boolean exceedsThreeEntrances = false;
		boolean adjacentEntrance = false;
		boolean leadsToExit = false;
		
		//Get all the neighbors of this potential new path
		ArrayList<CoordinatePair> wallNeighbors = data.getOrderedNeighbors(point.getX(), point.getY());
		
		for(CoordinatePair neighbor: wallNeighbors) {
			
			//Count the number of adjacent paths that would be connected to this tile if it became a path
			if(neighbor != null && data.get(neighbor.getX(), neighbor.getY()) == 2) {
				numAdjacentPaths++;
			}
			
			if(data.get(neighbor.getX(), neighbor.getY()) == 3) {
				leadsToExit = true;
			}
			
			//If we are connecting to any rooms, add them to the adjacentRooms set
			if(data.get(neighbor.getX(), neighbor.getY()) == 0 || data.get(neighbor.getX(), neighbor.getY()) == 3) {
				
				//Find the room we are adjacent to and add it
				for(Room room: rooms) {
					if(room.contains(neighbor.getX(), neighbor.getY())) {
						adjacentRooms.add(room);
						
						//Count the number of entrances this room already has, and check if any would be adjacent to this potential entrance
						for(CoordinatePair tile: room.getBoundary()) {
							
							//A path on the boundary is equivalent to an entrance
							if(data.get(tile.getX(), tile.getY()) == 2) {
								numEntrances++;
								
								//Check if this entrance is adjacent to our new potential entrance
								for(CoordinatePair adjacent: wallNeighbors) {
									if(adjacent.equals(tile)) {
										adjacentEntrance = true;
									}
								}
							}
						}
					}
				}
				
				//If we already have 3 entrances, set our flag
				if(numEntrances >= 3) {
					exceedsThreeEntrances = true;
				}
			}
		}
		
		return (numAdjacentPaths < 2 && !exceedsThreeEntrances && !adjacentEntrance && !leadsToExit);
	}
	
	private HashSet<Room> getAdjacentRooms(Array2D<Integer> data, CoordinatePair point) {
		ArrayList<CoordinatePair> neighbors = data.getOrderedNeighbors(point.getX(), point.getY());
		
		HashSet<Room> adjacentRooms = new HashSet<Room>();
		
		for(CoordinatePair neighbor: neighbors) {
			
			//For every neighboring tile that is a room, find which room it is, and put it in the set
			if(data.get(neighbor.getX(), neighbor.getY()) == 0) {
				for(Room room: rooms) {
					
					if(room.contains(neighbor.getX(), neighbor.getY())) {
						adjacentRooms.add(room);
					}
				}
			}
		}
		
		return adjacentRooms;
	}
}
