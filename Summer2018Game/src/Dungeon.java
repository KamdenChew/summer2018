import java.util.Random;
public class Dungeon {
	/* Dungeons are represented with Array2D's of ints.
	 * -1's represent the dungeon outer boundary
	 * 0's represent a walkable path
	 * 1's represent a wall (not walkable)
	 * 2's represent an exit from the dungeon
	 */
	
	private static Random rand = new Random();
	private static int difficulty;
	private static Array2D<Integer> data;
	
	private static final int MIN_ROOM_DIMENSION = 3;
	
	//Room Dimension Variance is additive, meaning if the Min Room Dimension is 3, and the Room Dimension Variance is 2
	//then the Max Room Dimension is 5
	private static final int ROOM_DIMENSION_VARIANCE = 2;
	
	public Dungeon(int difficulty) {
		int numDungeonRows;
		int numDungeonColumns;
		if(difficulty == 0) {
			System.out.println("Difficulty set to non-hostile.");
			
			//Set Fixed size for non-hostile dungeons
			numDungeonRows = 10;
			numDungeonColumns = 10;
		} else if(difficulty == 1) {
			System.out.println("Difficulty set to easy.");
			
			//Set size to between 10x10 and 20x20 for easy dungeons
			numDungeonRows = rand.nextInt(11) + 10;
			numDungeonColumns = rand.nextInt(11) + 10;
		} else if(difficulty == 2) {
			System.out.println("Difficulty set to normal.");
			
			//Set size to between 20x20 and 35x35 for easy dungeons
			numDungeonRows = rand.nextInt(16) + 20;
			numDungeonColumns = rand.nextInt(16) + 20;
		} else if(difficulty == 3) {
			System.out.println("Difficulty set to hard.");
			
			//Set size to between 35x35 and 50x50 for easy dungeons
			numDungeonRows = rand.nextInt(16) + 35;
			numDungeonColumns = rand.nextInt(16) + 35;
		} else {
			throw new IllegalArgumentException("Difficulty should be an integer value from 0-3.");
		}
		
		System.out.println();
		this.difficulty = difficulty;
		data = new Array2D<Integer>(numDungeonRows + 2, numDungeonColumns + 2);
		generateDungeon(data);
	}
	
	private static void generateDungeon(Array2D<Integer> data) {
		setBoundaryWalls(data);
		setInnerWalls(data);
		generateRooms(data);
		setExit(data);
		System.out.println(data);
	}
	
	private static void setBoundaryWalls(Array2D<Integer> data) {
		//Set outer boundary tiles
		for(int x = 0; x < data.getNumColumns(); x++) {
			data.set(x, 0, -1);
			data.set(x, data.getNumRows() - 1, -1);
		}
				
		for(int y = 0; y < data.getNumRows(); y++) {
			data.set(0, y, -1);
			data.set(data.getNumColumns() - 1, y, -1);
		}
//		System.out.println("Step 1: Set all Outer Tiles to Boundary Walls:");
	}
	
	private static void setInnerWalls(Array2D<Integer> data) {
		//Set all tiles to walls
		for(int r = 1; r < data.getNumRows() - 1; r++) {
			for(int c = 1; c < data.getNumColumns() - 1; c++) {
				data.set(c, r, 1);
			}
		}
		
//		System.out.println("Step 2: Set all Inner Tiles to Walls:");
//		System.out.println(data);
	}
	
	private static void generateRooms(Array2D<Integer> data) {
		
		//Place one room guaranteed to ensure we have an escape/start room
		int roomWidth = rand.nextInt(ROOM_DIMENSION_VARIANCE) + MIN_ROOM_DIMENSION;
		int roomHeight = rand.nextInt(ROOM_DIMENSION_VARIANCE) + MIN_ROOM_DIMENSION;
		int startRoomColumn = rand.nextInt(data.getNumColumns() - roomWidth - 2) + 1;
		int startRoomRow = rand.nextInt(data.getNumRows() - roomHeight - 2) + 1;
		
		//Generate our guaranteed room
		for(int x = startRoomColumn; x < startRoomColumn + roomWidth; x++) {
			for(int y = startRoomRow; y < startRoomRow + roomHeight; y++) {
				data.set(x, y, 0);
			}
		}
//		System.out.println();
//		System.out.println("After Guaranteed room:");
//		System.out.println(data);
		
		
		
		int roomCountTries = 5 * difficulty;
		
		//If the game is set to non-hostile, give 3 tries
		if(difficulty == 0) {
			roomCountTries = 3;
		}
		
		int currTries = 0;
		while(currTries < roomCountTries) {
			roomWidth = rand.nextInt(ROOM_DIMENSION_VARIANCE) + MIN_ROOM_DIMENSION;
			roomHeight = rand.nextInt(ROOM_DIMENSION_VARIANCE) + MIN_ROOM_DIMENSION;
			
			int randomRow = rand.nextInt(data.getNumRows() - 1) + 1;
			int randomColumn = rand.nextInt(data.getNumColumns() - 1) + 1;
			
//			System.out.println("Parameters for Room Placement:");
//			System.out.println("RoomWidth: " + roomWidth);
//			System.out.println("RoomHeight: " + roomHeight);
//			System.out.println("RandomRow: " + randomRow);
//			System.out.println("RandomColumn: " + randomColumn);
//			System.out.println();
			
			//Preliminary check to see if the room will go out of bounds
			if(randomColumn + roomWidth > data.getNumColumns() || randomRow + roomHeight > data.getNumRows()) {
				currTries++;
//				System.out.println("(" + randomRow + ", " + randomColumn + ") was going to generate out of bounds");
//				System.out.println();
			} else  {
				//Check to see if placing the room would overlap with another room, if it does, then we fail and we add a try to our count
				boolean overlap = false;
				for(int x = randomColumn; x < randomColumn + roomWidth; x++) {
					for(int y = randomRow; y < randomRow + roomHeight; y++) {
						
						boolean neighborRoom = false;
						
						//Check all neighbors to see if we will end up creating a combined room (Unwanted)
						for(Integer tileVal: data.getOrderedNeighbors(x, y)) {
							if(tileVal != null && tileVal == 0) {
								neighborRoom = true;
							}
						}
						
						//If the tile is a walkable path, or we'll combine rooms, we want to set a flag
						if(data.get(x, y) == 0 || data.get(x, y) == -1 || neighborRoom) {
							overlap = true;
//							System.out.println("Found overlap at: (" + x + ", " + y + ")");
							break;
						}
					}
				}
				
				//If we overlapped, increment our currTries
				if(overlap) {
					currTries++;
					
				//Else, we add the room
				} else {
//					System.out.println("Found no overlap! Placing room.");
					for(int x = randomColumn; x < randomColumn + roomWidth; x++) {
						for(int y = randomRow; y < randomRow + roomHeight; y++) {
							data.set(x, y, 0);
						}
					}
					
					//Reset our counter to allow roomCountTries number of attempts to place the next room
					currTries = 0;
//					System.out.println("After placing room:");
//					System.out.println(data);
				}
			}
		}
//		System.out.println("Step 3: Randomly Generate Rooms:");
//		System.out.println(data);
	}
	
	private static void setExit(Array2D<Integer> data) {
		boolean exitPlaced = false;
		while(!exitPlaced) {
			int x = rand.nextInt(data.getNumColumns() - 2) + 1;
			int y = rand.nextInt(data.getNumRows() - 2) + 1;
			if(data.get(x, y) == 0) {
				exitPlaced = true;
				data.set(x, y, 2);
			}
		}
	}
}
