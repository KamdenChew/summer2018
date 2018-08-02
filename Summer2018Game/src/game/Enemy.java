package game;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

public class Enemy extends Creature {

	Random rand = new Random();
	
	public Enemy(Game game, float x, float y, int coordinateX, int coordinateY) {
		super(game, x, y, coordinateX, coordinateY);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void tick() {
		CoordinatePair nextPosition = getMove();
		this.coordinateX = nextPosition.getX();
		this.coordinateY = nextPosition.getY();
	}

	@Override
	public void render(Graphics graphics) {
		graphics.drawImage(Assets.enemy, coordinateX * 50, coordinateY * 50, null);
	}
	
	//TODO Replace random move with some actually intelligent move
	public CoordinatePair getMove() {
		if(State.getState().isDungeonState()) {
			
			DungeonState dungeonState = (DungeonState) State.getState();
			Dungeon dungeon = dungeonState.getDungeon();
			
			Array2D<Integer> data = dungeon.getData();
			
			ArrayList<CoordinatePair> possibleMoves = new ArrayList<CoordinatePair>();
			
			// Add North Neighbor
			if (coordinateY > 0 && dungeon.isWalkable(coordinateX, coordinateY - 1)) {
				possibleMoves.add(new CoordinatePair(coordinateX, coordinateY - 1));
			} 

			// Add East Neighbor
			if (coordinateX < data.getNumColumns() - 1 && dungeon.isWalkable(coordinateX + 1, coordinateY)) {
				possibleMoves.add(new CoordinatePair(coordinateX + 1, coordinateY));
			} 

			// Add South Neighbor
			if (coordinateY < data.getNumRows() - 1 && dungeon.isWalkable(coordinateX, coordinateY + 1)) {
				possibleMoves.add(new CoordinatePair(coordinateX, coordinateY + 1));
			}

			// Add West Neighbor
			if (coordinateX > 0 && dungeon.isWalkable(coordinateX - 1, coordinateY)) {
				possibleMoves.add(new CoordinatePair(coordinateX - 1, coordinateY));
			}
			
			int randomIndex = rand.nextInt(possibleMoves.size());
			
			return possibleMoves.get(randomIndex);
			
		} else  {
			throw new IllegalStateException("Trying to move enemies while in an illegal state.");
		}
	}

}
