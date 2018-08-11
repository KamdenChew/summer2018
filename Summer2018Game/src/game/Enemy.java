package game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

public class Enemy extends Creature {

	private Random rand = new Random();
	private Dungeon dungeon;
	
	public Enemy(Game game, float x, float y, int coordinateX, int coordinateY, int health, Dungeon dungeon, String direction) {
		super(game, x, y, coordinateX, coordinateY);
		this.health = health;
		if(direction.equals("Up")) {
			setFacingUp();
		} else if(direction.equals("Down")) {
			setFacingDown();
		} else if(direction.equals("Left")) {
			setFacingLeft();
		} else if(direction.equals("Right")) {
			setFacingRight();
		}
		this.dungeon = dungeon;
	}

	@Override
	public void tick() {
		CoordinatePair nextPosition = getMove();
		
		//If we are moving right, face right
		if(nextPosition.getY() < this.coordinateY) {
			setFacingUp();
		} else if(nextPosition.getY() > this.coordinateY) {
			setFacingDown();
		} else if(nextPosition.getX() < this.coordinateX) {
			setFacingLeft();
		} else {
			setFacingRight();
		}
		this.coordinateX = nextPosition.getX();
		this.coordinateY = nextPosition.getY();
	}

	@Override
	public void render(Graphics graphics) {
		if(State.getState().isDungeonState()) {
			
			DungeonState dungeonState = (DungeonState) State.getState();
			Array2D<Integer> data = dungeonState.getDungeon().getData();
			
			int columns = data.getNumColumns();
			int rows = data.getNumRows();
			
			Player player = game.getPlayer();
			
			int xOffSet = player.getCoordinateX() - game.getRenderDistance();
			int yOffSet = player.getCoordinateY() - game.getRenderDistance();
			
			//If we are in the rendered region, render relative to player.
			if(this.coordinateX >= player.getCoordinateX() - game.getRenderDistance() &&
			   this.coordinateX <= player.getCoordinateX() + game.getRenderDistance() &&
			   this.coordinateY >= player.getCoordinateY() - game.getRenderDistance() &&
			   this.coordinateY <= player.getCoordinateY() + game.getRenderDistance()) {
				
				//Player is always at renderDistance * 50, renderDistance * 50
				if(this.facingUp) {
					graphics.drawImage(Assets.enemyUp, game.getRenderDistance() * 50 - (player.getCoordinateX() - this.coordinateX) * 50, game.getRenderDistance() * 50 - (player.getCoordinateY() - this.coordinateY) * 50, null);
				} else if(this.facingDown) {
					graphics.drawImage(Assets.enemyDown, game.getRenderDistance() * 50 - (player.getCoordinateX() - this.coordinateX) * 50, game.getRenderDistance() * 50 - (player.getCoordinateY() - this.coordinateY) * 50, null);
				} else if(this.facingLeft) {
					graphics.drawImage(Assets.enemyLeft, game.getRenderDistance() * 50 - (player.getCoordinateX() - this.coordinateX) * 50, game.getRenderDistance() * 50 - (player.getCoordinateY() - this.coordinateY) * 50, null);
				} else if(this.facingRight) {
					graphics.drawImage(Assets.enemyRight, game.getRenderDistance() * 50 - (player.getCoordinateX() - this.coordinateX) * 50, game.getRenderDistance() * 50 - (player.getCoordinateY() - this.coordinateY) * 50, null);
				}
				renderHealthBar(graphics);
			}
		}
	}
	
	public void renderHealthBar(Graphics graphics) {
		
		//Find scaling value
		int pixelsPerHealth = 50 / maxHealth;
		
		//Represents amount of health the player has lost
		int numRedPixels = pixelsPerHealth * (maxHealth - health);
		
		//Represents amount of health the player has
		int numGreenPixels = 50 - numRedPixels;
		
		Player player = dungeon.getPlayer();
		
		//Draw the Health Bar
		graphics.setColor(Color.green);
		graphics.fillRect(game.getRenderDistance() * 50 - (player.getCoordinateX() - this.coordinateX) * 50, game.getRenderDistance() * 50 - (player.getCoordinateY() - this.coordinateY) * 50, numGreenPixels, 2);
		graphics.setColor(Color.red);
		graphics.fillRect(game.getRenderDistance() * 50 - (player.getCoordinateX() - this.coordinateX) * 50 + numGreenPixels, game.getRenderDistance() * 50 - (player.getCoordinateY() - this.coordinateY) * 50, numRedPixels, 2);
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
			
			//If there are no possible moves, stay here
			if(possibleMoves.size() == 0) {
				return new CoordinatePair(this.coordinateX, this.coordinateY);
			}
			
			int randomIndex = rand.nextInt(possibleMoves.size());
			
			return possibleMoves.get(randomIndex);
			
		} else  {
			throw new IllegalStateException("Trying to move enemies while in an illegal state.");
		}
	}

}
