package test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import org.junit.Test;

import game.Array2D;
import game.CoordinatePair;
import game.Dungeon;
import game.Game;

public class DungeonTest {
	
	private Random rand = new Random();

	@Test
	public void testDungeonConstruction()  {
		boolean passed = true;
		Game game = new Game();
		for(int i = 0; i < 25000; i++) {
			Dungeon nonHostileDungeon = new Dungeon(game, 0);
			if(!isValidDungeon(nonHostileDungeon)) {
				System.out.println("Failed on nonHostile iteration: " + i);
				System.out.println(nonHostileDungeon.getData());
				passed = false;
				break;
			}
			
			Dungeon easyDungeon = new Dungeon(game, 1);
			if(!isValidDungeon(easyDungeon)) {
				System.out.println("Failed on easy iteration: " + i);
				System.out.println(easyDungeon.getData());
				passed = false;
				break;
			}
			
			Dungeon normalDungeon = new Dungeon(game, 2);
			if(!isValidDungeon(normalDungeon)) {
				System.out.println("Failed on normal iteration: " + i);
				System.out.println(normalDungeon.getData());
				passed = false;
				break;
			}
			
			Dungeon hardDungeon = new Dungeon(game, 3);
			if(!isValidDungeon(hardDungeon)) {
				System.out.println("Failed on hard iteration: " + i);
				System.out.println(hardDungeon.getData());
				passed = false;
				break;
			}
		}
		
		assert(passed);
	}
	
	public boolean isValidDungeon(Dungeon dungeon) {
//		System.out.println("Checking: ");
//		System.out.println(dungeon.getData());
		Array2D<Integer> data = dungeon.getData();
		HashSet<CoordinatePair> allWalkableTiles = new HashSet<CoordinatePair>();
		
		
		for(int x = 0; x < data.getNumColumns(); x++) {
			for(int y = 0; y < data.getNumRows(); y++) {
				
				//Add all path, room, and exit tiles to the walkable set.
				if(data.get(x, y) == 0 || data.get(x, y) == -2 || data.get(x, y) == 2) {
					allWalkableTiles.add(new CoordinatePair(x,y));
				}
			}
		}
		
		
		//Pick a random tile from the walkable set to start "walking" from
		int randomIndex = rand.nextInt(allWalkableTiles.size());
		CoordinatePair tile = (CoordinatePair) allWalkableTiles.toArray()[randomIndex];
		
		//Create a new set for all tiles we've visited on our "walk". At the end, this should be
		//equal to the allWalkableTiles
		HashSet<CoordinatePair> visited = new HashSet<CoordinatePair>();
		
		ArrayList<CoordinatePair> boundary = new ArrayList<CoordinatePair>();
		
		boundary.add(tile);
		//Keep "walking" until there are no tiles to walk from
		while(boundary.size() > 0) {
			//Pick the first tile on the boundary
			CoordinatePair boundaryTile = boundary.get(0);
				
			//Add all neighbors that are walkable and haven't already been visited to the boundary list
			for(CoordinatePair neighbor: data.getOrderedNeighbors(boundaryTile.getX(), boundaryTile.getY())) {
				int x = neighbor.getX();
				int y = neighbor.getY();
				if((data.get(x, y) == 0 || data.get(x, y) == -2 || data.get(x, y) == 2) && !visited.contains(neighbor) && !boundary.contains(neighbor)) {
					boundary.add(neighbor);
				}
			}
			
			//Now boundaryTile can be considered visited, and be taken out of the boundary set
			visited.add(boundaryTile);
			boundary.remove(boundaryTile);
		}
		
		return visited.equals(allWalkableTiles);
	}
}
