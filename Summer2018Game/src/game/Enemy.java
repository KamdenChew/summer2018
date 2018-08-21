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
		this.nextCoordinateX = this.coordinateX;
		this.nextCoordinateY = this.coordinateY;
	}

	@Override
	public void tick() {
		CoordinatePair nextPosition = getMove();
		
		if(nextPosition != null) {
			if(nextPosition.getY() < this.coordinateY) {
				setFacingUp();
				this.nextCoordinateY = this.coordinateY - 1;
				this.nextY = this.y - 50;
			} else if(nextPosition.getY() > this.coordinateY) {
				setFacingDown();
				this.nextCoordinateY = this.coordinateY + 1;
				this.nextY = this.y + 50;
			} else if(nextPosition.getX() < this.coordinateX) {
				setFacingLeft();
				this.nextCoordinateX = this.coordinateX - 1;
				this.nextX = this.x - 50;
			} else {
				setFacingRight();
				this.nextCoordinateX = this.coordinateX + 1;
				this.nextX = this.x + 50;
			}
		}
	}

	@Override
	public void render(Graphics graphics) {
		if(State.getState().isDungeonState()) {
			
			DungeonState dungeonState = (DungeonState) State.getState();
			Array2D<Integer> data = dungeonState.getDungeon().getData();
			Player player = game.getPlayer();
			
			//Player is always at renderDistance * 50, renderDistance * 50
			if(this.facingUp) {
				graphics.drawImage(Assets.enemyUp, (int) (this.x - player.getCamera().getXOffset()), (int) (this.y - player.getCamera().getYOffset()), null);
			} else if(this.facingDown) {
				graphics.drawImage(Assets.enemyDown, (int) (this.x - player.getCamera().getXOffset()), (int) (this.y - player.getCamera().getYOffset()), null);
			} else if(this.facingLeft) {
				graphics.drawImage(Assets.enemyLeft, (int) (this.x - player.getCamera().getXOffset()), (int) (this.y - player.getCamera().getYOffset()), null);
			} else if(this.facingRight) {
				graphics.drawImage(Assets.enemyRight, (int) (this.x - player.getCamera().getXOffset()), (int) (this.y - player.getCamera().getYOffset()), null);
			}
			renderHealthBar(graphics);
			
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
		graphics.fillRect((int) (this.x - player.getCamera().getXOffset()), (int) (this.y - player.getCamera().getYOffset()), numGreenPixels, 2);
		graphics.setColor(Color.red);
		graphics.fillRect((int) (this.x - player.getCamera().getXOffset()) + numGreenPixels, (int) (this.y - player.getCamera().getYOffset()), numRedPixels, 2);
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
//				System.out.println("Added north path to enemy moves");
			} 

			// Add East Neighbor
			if (coordinateX < data.getNumColumns() - 1 && dungeon.isWalkable(coordinateX + 1, coordinateY)) {
				possibleMoves.add(new CoordinatePair(coordinateX + 1, coordinateY));
//				System.out.println("Added east path to enemy moves");
			} 

			// Add South Neighbor
			if (coordinateY < data.getNumRows() - 1 && dungeon.isWalkable(coordinateX, coordinateY + 1)) {
				possibleMoves.add(new CoordinatePair(coordinateX, coordinateY + 1));
//				System.out.println("Added south path to enemy moves");
			}

			// Add West Neighbor
			if (coordinateX > 0 && dungeon.isWalkable(coordinateX - 1, coordinateY)) {
				possibleMoves.add(new CoordinatePair(coordinateX - 1, coordinateY));
//				System.out.println("Added west path to enemy moves");
			}
			
			//If there are no possible moves, stay here
			if(possibleMoves.size() == 0) {
//				System.out.println("No moves found!");
				return null;
			}
			
//			System.out.println("----------------------------------------------");
			
			int randomIndex = rand.nextInt(possibleMoves.size());
			
			return possibleMoves.get(randomIndex);
			
		} else  {
			throw new IllegalStateException("Trying to move enemies while in an illegal state.");
		}
	}

}
