package game;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Dungeon {
	/*
	 * Dungeons are represented with Array2D's of ints. -1's represent the
	 * dungeon outer boundary, 0's represent a walkable room, 1's represent a wall
	 * (not walkable), 2's represent a walkable path, -2's represent an exit from the dungeon
	 */

	private Random rand = new Random();
	private int difficulty;
	private Array2D<Integer> data;
	private Array2D<Boolean> seen;
	private ArrayList<Room> rooms;
	private int numDungeonRows;
	private int numDungeonColumns;
	private int currFloor;
	private int numFloors;
	private Game game;
	private Player player;
	private ArrayList<Enemy> enemies;
	
	//Room Count Coefficient controls how many attempts we give to generating rooms. See generateRooms()
	private static final int ROOM_COUNT_COEFFICIENT = 8;

	// Room Dimension Variance is additive, meaning if the Min Room Dimension is
	// 3, and the Room Dimension Variance is 2
	// then the Max Room Dimension is 5
	private static final int MIN_ROOM_DIMENSION = 3;
	private static final int ROOM_DIMENSION_VARIANCE = 2;

	
	/**
	 * Constructs a new Dungeon with a specified difficulty level
	 *
	 * @param difficulty an int representing the difficulty level of the Dungeon
	 *  @throws IllegalArgumentException if not passes a valid difficulty int
	 * 
	 */
	public Dungeon(Game game, int difficulty)  {
		this.game = game;
		this.enemies = new ArrayList<Enemy>();
		this.rooms = new ArrayList<Room>();
		this.currFloor = 1;
		
		if (difficulty == 0) {

			// Set Fixed size for non-hostile dungeons
			this.numDungeonRows = 10;
			this.numDungeonColumns = 10;
			this.numFloors = 5;
			
		} else if (difficulty == 1) {

			// Set size to between 10x10 and 15x15 for easy dungeons
			this.numDungeonRows = rand.nextInt(6) + 10;
			this.numDungeonColumns = rand.nextInt(6) + 10;
			this.numFloors = 8;
			
		} else if (difficulty == 2) {

			// Set size to between 15x15 and 20x20 for medium dungeons
			this.numDungeonRows = rand.nextInt(6) + 15;
			this.numDungeonColumns = rand.nextInt(6) + 15;
			this.numFloors = 12;
			
		} else if (difficulty == 3) {

			// Set size to between 20x20 and 25x25 for hard dungeons
			this.numDungeonRows = rand.nextInt(6) + 20;
			this.numDungeonColumns = rand.nextInt(6) + 20;
			this.numFloors = 20;
			
		} else {
			throw new IllegalArgumentException("Difficulty should be an integer value from 0-3.");
		}

		this.difficulty = difficulty;
		this.data = new Array2D<Integer>(numDungeonRows + 2, numDungeonColumns + 2);
		this.seen = new Array2D<Boolean>(numDungeonRows + 2, numDungeonColumns + 2);
		initializeSeen();
		
		generateDungeon(data);
	}

	//TODO add javadoc and also add rooms as paramater to this constructor?
	public Dungeon(Game game, int x, int y, int difficulty, Array2D<Integer> data, Array2D<Boolean> seen, int numDungeonRows, int numDungeonColumns, String direction, int currFloor) {
		this.game = game;
		this.difficulty = difficulty;
		if(difficulty == 0) {
			this.numFloors = 5;
		} else if(difficulty == 1) {
			this.numFloors = 8;
		} else if(difficulty == 2) {
			this.numFloors = 12;
		} else if(difficulty == 3) {
			this.numFloors = 20;
		}
		this.data = data;
		this.seen = seen;
		this.numDungeonColumns = numDungeonColumns;
		this.numDungeonRows = numDungeonRows;
		this.currFloor = currFloor;
		this.player = new Player(game, x, y, true, this, direction);
		setSeen();
	}
	
	/**
	 * Initializes all values of the seen Array2D to false
	 */
	public void initializeSeen() {
		for(int x = 0; x < this.numDungeonColumns + 2; x++) {
			for(int y = 0; y < this.numDungeonRows + 2; y++) {
				this.seen.set(x, y, false);
			}
		}
	}
	
	public int getCurrFloor() {
		return this.currFloor;
	}

	public void setCurrFloor(int currFloor) {
		this.currFloor = currFloor;
	}
	
	public Array2D<Integer> getData() { 
		return new Array2D<Integer>(this.data);
	}
	
	public Array2D<Boolean> getSeen() {
		return this.seen;
	}
	
	public int getNumFloors() {
		return numFloors;
	}

	/**
	 * Edits the underlying Array2D structure of this dungeon to represent
	 * a randomly generated dungeon
	 *
	 * @param data and Array2D<Integer> intended to be edited to reflect a Dungeon
	 *  
	 * 
	 */
	private void generateDungeon(Array2D<Integer> data)  {
		
		//Step 1: Add boundary walls
		setBoundaryWalls(data);
		
		//Step 2: Set all internal data in the Array2D to walls 
		setInnerWalls(data);
		
		//Step 3: Overwrite some of the walls to be randomly generated rooms
		generateRooms(data);
		
		//Step 4: Put an exit from the dungeon in one of the rooms
		setExit(data);
		
		//Step 5: Add paths to our dungeon, disjoint from the rooms
		generatePaths(data, null, 2);
		
		//Step 6: Connect all of our paths and rooms
		connect(data);
		
		//Step 7: Create a Player and put them in the Dungeon
		setPlayer();
		
		//Step 8: Mark the nearby regions of the Player starting position as seen
		setSeen();
		
		//Step 9: Add Enemies to the Dungeon proportional to difficulty
		addEnemies(difficulty * 3);
		
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
		// Set all inner tiles to walls
		for (int r = 1; r < data.getNumRows() - 1; r++) {
			for (int c = 1; c < data.getNumColumns() - 1; c++) {
				data.set(c, r, 1);
			}
		}

	}

	/**
	 * Edits the underlying Array2D structure of this dungeon by adding rooms
	 * 
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

		int tries = 0;
		while (tries < roomCountTries) {
			roomWidth = rand.nextInt(ROOM_DIMENSION_VARIANCE) + MIN_ROOM_DIMENSION;
			roomHeight = rand.nextInt(ROOM_DIMENSION_VARIANCE) + MIN_ROOM_DIMENSION;

			int randomRow = rand.nextInt(data.getNumRows() - 2) + 1;
			int randomColumn = rand.nextInt(data.getNumColumns() - 2) + 1;

			// Preliminary check to see if the room will go out of bounds
			if (randomColumn + roomWidth > data.getNumColumns() || randomRow + roomHeight > data.getNumRows()) {
				tries++;

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

						// If the tile is a walkable path we'll combine
						// rooms, we want to set a flag
						if (data.get(x, y) == 0 || data.get(x, y) == -1 || neighborRoom) {
							overlap = true;
							break;
						}
					}
				}

				// If we overlapped, increment our tries
				if (overlap) {
					tries++;

				// Else, we check to see if the room will end up with a 2 wide gap between rooms (this will be important to avoid for path generation later)
				} else {
					
					//Save the room parameters so we can now check for the 2 wide gap.
					Room attempt = new Room(randomColumn, randomRow, roomWidth, roomHeight);
					boolean twoWideGap = false;
					
					//There's a 2 wide gap if the neighbors of any of the boundary tiles are on the boundary of another room.
					for(CoordinatePair thisRoomBoundaryTile: attempt.getBoundary()) {
						
						for(CoordinatePair neighbor: data.getOrderedNeighbors(thisRoomBoundaryTile.getX(), thisRoomBoundaryTile.getY())) {
							
							for(Room otherRoom: rooms) {
								
								if(otherRoom.getBoundary().contains(neighbor)) {
									twoWideGap = true;
								}
							}
						}
						
					}
					
					if(twoWideGap) {
						tries++;
					} else {
						rooms.add(attempt);
						
						//Apply the change to our Array2D 
						for (int x = randomColumn; x < randomColumn + roomWidth; x++) {
							for (int y = randomRow; y < randomRow + roomHeight; y++) {
								data.set(x, y, 0);
							}
						}

						// Reset our counter to allow roomCountTries number of
						// attempts to place the next room
						tries = 0;
					}
				}
			}
		}
	}
	
	/**
	 * Edits the underlying Array2D structure of this dungeon by adding an exit in a room.
	 * 
	 * @requires Boundary walls, inner walls, and Dungeon rooms have already been placed in data
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
				data.set(x, y, -2);
			}
		}
	}
	
	/**
	 * Edits the underlying Array2D structure of this dungeon by adding paths
	 * using a randomized Prim's algorithm
	 * 
	 * @requires Dungeon rooms, inner and boundary walls have all been placed in data
	 * @param data an Array2D<Integer> intended to be edited to reflect a Dungeon
	 * @param validPaths a set of CoordinatePairs that can be validly turned to paths
	 * @param identifier an int for identifying a disjoint set from the rest of the walkable tiles, the identifiers will later be used to connect the sets
	 */
	private void generatePaths(Array2D<Integer> data, HashSet<CoordinatePair> validPaths, int identifier)  {
		
		//If this is the initial call, then we won't know validPath tiles
		if(validPaths == null) {
			validPaths = new HashSet<CoordinatePair>();
			for(int x = 1; x < data.getNumColumns() - 1; x++) {
				for(int y = 1; y < data.getNumRows() - 1; y++) {
					CoordinatePair potentialPath = new CoordinatePair(x, y);
					if(canBePath(data, potentialPath)) {
						validPaths.add(potentialPath);
					}
				}
			}
		}
		
		int randomIndex;
		CoordinatePair start = null;
		while(start == null || !canBePath(data, start)) {
			randomIndex = rand.nextInt(validPaths.size());
			start = (CoordinatePair) validPaths.toArray()[randomIndex];
		}
		
		ArrayList<CoordinatePair> walls = new ArrayList<CoordinatePair>();
		data.set(start.getX(), start.getY(), identifier);
		validPaths.remove(start);
		for(CoordinatePair neighbor: data.getOrderedNeighbors(start.getX(), start.getY())) {
			if(canBePath(data, neighbor)) {
				if(!walls.contains(neighbor)) {
					walls.add(neighbor);
				}
			}
		}
		
		while(!walls.isEmpty()) {
			
			//Pick a random wall from the wall list
			randomIndex = rand.nextInt(walls.size());
			CoordinatePair randomWall = walls.get(randomIndex);
			
			//If it can be a path, add all neighboring walls to wall list, and set the wall to a path
			if(canBePath(data, randomWall)) {
				for(CoordinatePair neighbor: data.getOrderedNeighbors(randomWall.getX(), randomWall.getY())) {
					if(neighbor != null && canBePath(data, neighbor) && !walls.contains(neighbor)) {
						walls.add(neighbor);
					}
				}
				data.set(randomWall.getX(), randomWall.getY(), identifier);
			}
			walls.remove(randomWall);
			validPaths.remove(randomWall);
		}
		
		//If there's still places we can put paths, call generatePaths again
		if(!validPaths.isEmpty()) {
			generatePaths(data, validPaths, identifier + 1);
		}

	}
	
	/**
	 * Checks a given CoordinatePair location in an Array2D of a Dungeon to see if it can be a path.
	 * A tile can be a path if it isn't adjacent to more than 1 other path, it is not adjacent to a room,
	 * it isn't adjacent to the exit, and it is currently an inner wall tile
	 * 
	 * @requires Dungeon rooms, inner and boundary walls have all been placed in data
	 * @param data an Array2D<Integer> that represents a Dungeon
	 * @param point a CoordinatePair representing the coordinates within data that we are checking for path eligibility
	 * @return a boolean value representing if this CoordinatePair in the given Array2D can be a path.
	 */
	private boolean canBePath(Array2D<Integer> data, CoordinatePair point) {
		
		//Check criteria for this wall becoming a path
		int numAdjacentPaths = 0;
		boolean adjacentExit = false;
		boolean adjacentRoom = false;
		boolean isInnerWall = false;
		
		if(data.get(point.getX(), point.getY()) == 1) {
			isInnerWall = true;
		}
		
		//Get all the neighbors of this potential new path
		ArrayList<CoordinatePair> wallNeighbors = data.getOrderedNeighbors(point.getX(), point.getY());
		
		for(CoordinatePair neighbor: wallNeighbors) {
			//Count the number of adjacent paths that would be connected to this tile if it became a path
			if(neighbor != null && data.get(neighbor.getX(), neighbor.getY()) >= 2) {
				numAdjacentPaths++;
			}
			
			//If we are connecting to the exit, flag it
			if(neighbor != null && data.get(neighbor.getX(), neighbor.getY()) == -2) {
				adjacentExit = true;
			}
			
			//If we are connecting to any rooms flag it
			if(neighbor != null && data.get(neighbor.getX(), neighbor.getY()) == 0) {
				adjacentRoom = true;
			}
		}
		
		return (numAdjacentPaths < 2 && !adjacentExit && !adjacentRoom && isInnerWall);
	}
	
	/**
	 * Edits the underlying Array2D structure of this dungeon by connecting paths to rooms and other paths, making the Dungeon complete.
	 * 
	 * @requires Dungeon rooms, paths, inner and boundary walls have all been placed in data
	 * @param data an Array2D<Integer> intended to be edited to reflect a Dungeon
	 */
	private void connect(Array2D<Integer> data) {
		
		//Find all the possible connectors
		ArrayList<CoordinatePair> connectors = new ArrayList<CoordinatePair>();
		
		HashSet<Integer> uniquePathIdentifiers = new HashSet<Integer>();
		
		for(int x = 1; x < data.getNumColumns() - 1; x++) {
			for(int y = 1; y < data.getNumRows() - 1; y++) {
				
				//Save all unique path values for later use
				if(data.get(x, y) >= 2) {
					uniquePathIdentifiers.add(data.get(x, y));
				}
				
				//A connector is an inner wall that has at least 2 disjoint walkable sets as neighbors
				HashSet<Integer> neighboringPathIdentifiers = new HashSet<Integer>();
				HashSet<Room> uniqueRooms = new HashSet<Room>();
				if(data.get(x, y) == 1) {
					for(CoordinatePair neighbor: data.getOrderedNeighbors(x, y)) {
						if(neighbor != null) {
							int val = data.get(neighbor.getX(), neighbor.getY());
							
							//If adjacent to a path, save it's identifier
							if(val >= 2) {
								neighboringPathIdentifiers.add(val);
							}
							
							//If adjacent to a room, save it
							if(val == 0 || val == -2) {
								for(Room room: rooms) {
									if(room.contains(neighbor.getX(), neighbor.getY())) {
										uniqueRooms.add(room);
									}
								}
							}
							
						}
					}
				}
				if(neighboringPathIdentifiers.size() + uniqueRooms.size() >= 2) {
					connectors.add(new CoordinatePair(x, y));
				}
			}
		}
		
		HashSet<Room> visitedRooms = new HashSet<Room>();
		HashSet<Integer> visitedPathIdentifiers = new HashSet<Integer>();
		
		//Pick a random room to count as our first visited
		int randomIndex = rand.nextInt(rooms.size());
		Room randomRoom = rooms.get(randomIndex);
		visitedRooms.add(randomRoom);
		
		while(!connectors.isEmpty()) {

			//Find a connector that's adjacent to something we've visited
			CoordinatePair possibleConnector = null;
			boolean adjacentToVisited = false;
			boolean adjacentToUnvisited = false;
			while(possibleConnector == null || !(adjacentToVisited && adjacentToUnvisited)) {
				randomIndex = rand.nextInt(connectors.size());
				possibleConnector = (CoordinatePair) connectors.toArray()[randomIndex];
				adjacentToVisited = false;
				adjacentToUnvisited = false;
				
				//Check if adjacent to a visited path
				for(CoordinatePair neighbor: data.getOrderedNeighbors(possibleConnector.getX(), possibleConnector.getY())) {
					if(visitedPathIdentifiers.contains(data.get(neighbor.getX(), neighbor.getY()))) {
						adjacentToVisited = true;
					} else if(!visitedPathIdentifiers.contains(data.get(neighbor.getX(), neighbor.getY())) && data.get(neighbor.getX(), neighbor.getY()) >= 2){
						adjacentToUnvisited = true;
					}
				}
				
				//Check if adjacent to a visited room
				for(Room room: rooms) {
					//TODO replace iterations over boundaries to contains like this
					if(visitedRooms.contains(room) && room.getBoundary().contains(possibleConnector)) {
						adjacentToVisited = true;
					} else if(!visitedRooms.contains(room) && room.getBoundary().contains(possibleConnector)) {
						adjacentToUnvisited = true;
					}
				}
			}
			
			//Set new connectors to -4, we will set all unique paths and connectors to 2 after we are all connected
			data.set(possibleConnector.getX(), possibleConnector.getY(), -4);
			
			//We now have a valid connector, add the new adjacent paths and rooms
			
			for(CoordinatePair neighbor: data.getOrderedNeighbors(possibleConnector.getX(), possibleConnector.getY())) {
				
				
				int val = data.get(neighbor.getX(), neighbor.getY());
				
				
				//If we haven't seen the adjacent room before, add to visitedRooms
				if(val == 0) {
					for(Room room: rooms) {
						if(room.contains(neighbor.getX(), neighbor.getY()) && !visitedRooms.contains(room)) {
							visitedRooms.add(room);
						}
					}
				}
				
				//If we haven't seen the adjacent path before, add to newPathIdentifiers
				if((val >= 2) && !visitedPathIdentifiers.contains(val)) {
					visitedPathIdentifiers.add(val);
				}
			} 
			
			//Remove all connectors that only connect known sections
			boolean connectsToNew = false;
			for(int i = 0; i < connectors.size(); i++) {
				
				CoordinatePair connector = connectors.get(i);
				for(CoordinatePair neighbor: data.getOrderedNeighbors(connector.getX(), connector.getY())) {
					int val = data.get(neighbor.getX(), neighbor.getY());
					if((val >= 2) && !visitedPathIdentifiers.contains(val)) {
						connectsToNew = true;
					}
					
					if(val == 0) {
						for(Room room: rooms) {
							if(room.contains(neighbor.getX(), neighbor.getY()) && !visitedRooms.contains(room)) {
								connectsToNew = true;
							}
						}
					}
				}
				
				if(!connectsToNew) {
					connectors.remove(connector);
					i--;
				}
			}
		}	
		
		//Set all connectors and paths to value 2
		for(int x = 1; x < data.getNumColumns() - 1; x++) {
			for(int y = 1; y < data.getNumRows() - 1; y++) {
				if(data.get(x, y) == -4 || data.get(x, y) > 2) {
					data.set(x, y, 2);
				}
			}
		}
	}
	
	private void setPlayer() {
		CoordinatePair startCoordinates = null;
		while(startCoordinates == null || data.get(startCoordinates.getX(), startCoordinates.getY()) != 0) {
			int randomColumn = rand.nextInt(data.getNumColumns() - 2)  + 1;
			int randomRow = rand.nextInt(data.getNumRows() - 2) + 1;
			startCoordinates = new CoordinatePair(randomColumn, randomRow);
		}
		this.player = new Player(game, startCoordinates.getX(), startCoordinates.getY(), true, this, "Down");
		game.setPlayer(this.player);
	}
	
	private void setSeen() {
	//Set the nearby tile values to seen
		for(int x = player.getCoordinateX() - game.getRenderDistance(); x <= player.getCoordinateX() + game.getRenderDistance(); x++) {
			for(int y = player.getCoordinateY() - game.getRenderDistance(); y <= player.getCoordinateY() + game.getRenderDistance(); y++) {
				
				//If it's in bounds, mark nearby tiles as seen!
				if(x >= 0 &&
						   x < seen.getNumColumns() &&
						   y >= 0 &&
						   y < seen.getNumRows()) {
					seen.set(x, y, true);
				}
			}
		}
	}
	
	/**
	 * Returns the Player for this Dungeon
	 */
	public Player getPlayer() {
		return player;
	}

	private void addEnemies(int numEnemies) {
		
		ArrayList<CoordinatePair> validLocations = new ArrayList<>();
	
		for(int x = 0; x < data.getNumColumns(); x++) {
			for(int y = 0; y < data.getNumRows(); y++) {
				int manhattanDistanceToPlayer = Math.abs(x - player.getCoordinateX()) + Math.abs(y - player.getCoordinateY());
				if(isWalkable(x,y) && manhattanDistanceToPlayer > 8) {
					validLocations.add(new CoordinatePair(x,y));
				}
			}
		}
		int randomIndex;
		
		while(numEnemies > 0) {
			if(validLocations.size() < 1) {
				break;
			}
			randomIndex = rand.nextInt(validLocations.size());
			CoordinatePair location = validLocations.get(randomIndex);
			Enemy newEnemy = new Enemy(game, location.getX() * 50, location.getY() * 50, location.getX(), location.getY(), 10, this, "Down");
			enemies.add(newEnemy);
			numEnemies--;
			validLocations.remove(location);
		}
	}
	
	public ArrayList<Enemy> getEnemies() {
		return this.enemies;
	}
	
	public void setEnemies(ArrayList<Enemy> enemies) {
		this.enemies = enemies;
	}
	
	/**
	 * Checks if we have a generated dungeon, makes sure the given coordinates are either a path or a room, and that the player isn't standing there.
	 * 
	 * @return a boolean representing if an entity can move to the given coordinates
	 */
	public boolean isWalkable(int x, int y) {
		
		boolean occupied = false;
		
		//Check if the player will be moved into that same location on this turn
		if(x == player.getNextCoordinateX() && y == player.getNextCoordinateY()) {
			occupied = true;
		}
		for(Enemy enemy: enemies) {
			if(x == enemy.getNextCoordinateX() && y == enemy.getNextCoordinateY()) {
				occupied = true;
			}
		}
		
		return (data != null && (data.get(x, y) == 0 || data.get(x, y) == 2 || data.get(x, y) == -2) && !occupied);
	}
	
	public int getDifficulty() {
		return difficulty;
	}

	public boolean onLastFloor() {
		return (currFloor == numFloors);
	}
}
