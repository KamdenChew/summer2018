package game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Enemy extends Creature {

	private Random rand = new Random();
	private Dungeon dungeon;
	private boolean isAggressive = true;
	
	
	/**
	 * Constructs a new Enemy
	 *
	 * @param game the Game object for this running instance
	 * @param x the x pixel coordinate for the upper left corner of the Creature
	 * @param y the y pixel coordinate for the upper left corner of the Creature
	 * @param coordinateX the x coordinate for where the Creature resides in the grid world
	 * @param coordinateY the y coordinate for where the Creature resides in the grid world
	 * @param currHealth the amount of health that this enemy has
	 * @param dungeon the Dungeon that this enemy belongs to
	 * @param direction String denoting which way this enemy is facing
	 */
	public Enemy(Game game, float x, float y, int coordinateX, int coordinateY, int currHealth, Dungeon dungeon, String direction) {
		super(game, x, y, coordinateX, coordinateY);
		this.health = currHealth;
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
		
		if(this.isAggressive && this.adjacentToPlayer(new CoordinatePair(this.coordinateX, this.coordinateY))) {
			this.facePlayer();
			this.setAttacking(true);
		} else {
			
			CoordinatePair nextPosition;
			
			//If the player can see us, then make a smart move
			if(this.isVisibleToPlayer()) {
				nextPosition = getBFSMove();
		
			//Otherwise make a random move
			} else {
				nextPosition = getRandomMove();
			}
			
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
				} else if(nextPosition.getX() > this.coordinateX){
					setFacingRight();
					this.nextCoordinateX = this.coordinateX + 1;
					this.nextX = this.x + 50;
				}
			}
		}
	}

	@Override
	public void render(Graphics graphics) {
		if(State.getState().isDungeonState()) {
			
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
	
	/**
	 * Renders this Enemy's health bar to the graphics object it is passed
	 * 
	 * @param graphics the Graphics object to draw the health bar too
	 * 
	 */
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
		graphics.fillRect((int) (this.x - player.getCamera().getXOffset()), (int) (this.y - player.getCamera().getYOffset()), numGreenPixels, 4);
		graphics.setColor(Color.red);
		graphics.fillRect((int) (this.x - player.getCamera().getXOffset()) + numGreenPixels, (int) (this.y - player.getCamera().getYOffset()), numRedPixels, 4);
	}
	
	/**
	 * Returns a random CoordinatePair that is adjacent to where the enemy is, 
	 * is walkable in the Dungeon this enemy belongs to, and won't collide with another Creature
	 */
	public CoordinatePair getRandomMove() {
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
				return null;
			}
			
			int randomIndex = rand.nextInt(possibleMoves.size());
			
			return possibleMoves.get(randomIndex);
			
		} else  {
			throw new IllegalStateException("Trying to move enemies while in an illegal state.");
		}
	}
	
	/**
	 * Returns a CoordinatePair that is adjacent to where the enemy is, 
	 * is walkable in the Dungeon this enemy belongs to, and won't collide with another Creature,
	 * obtained by using a Breadth First Search Algorithm with the goal to get adjacent to the player so it can attack
	 */
	public CoordinatePair getBFSMove() {
		if(State.getState().isDungeonState()) {
			
			DungeonState dungeonState = (DungeonState) State.getState();
			Dungeon dungeon = dungeonState.getDungeon();
			
			Array2D<Integer> data = dungeon.getData();
			
			Queue<ArrayList<CoordinatePair>> queue = new LinkedList<ArrayList<CoordinatePair>>();
			
			
			//Start by adding the first options for propagating through the dungeon
			CoordinatePair startPosition = new CoordinatePair(this.coordinateX, this.coordinateY);
			
			for(CoordinatePair neighbor: startPosition.getNeighbors()) {
				if(neighbor.getX() <= data.getNumColumns() && neighbor.getY() <= data.getNumRows() &&
						   neighbor.getX() >= 0 && neighbor.getY() >= 0 && 
						   (data.get(neighbor.getX(), neighbor.getY()) == 0 || data.get(neighbor.getX(), neighbor.getY()) == 2)) {
					ArrayList<CoordinatePair> path = new ArrayList<CoordinatePair>();
					path.add(neighbor);
					queue.add(path);
				}
			}
			
			//Keep track of where we have already explored to avoid running into infinite loops, since dungeons are like cyclical graphs.
			ArrayList<CoordinatePair> known = new ArrayList<CoordinatePair>();
			known.add(startPosition);
			
			//While we still have tiles to explore
			while(!queue.isEmpty()) {
				ArrayList<CoordinatePair> currPath = queue.remove();
				
				CoordinatePair currPosition = currPath.get(currPath.size() - 1);
				
				//This is the goal, get adjacent to the player so we can attack
				if(this.adjacentToPlayer(currPosition)) {
					
					//Return the first step on this path to get adjacent to the player if it is walkable
					if(dungeon.isWalkable(currPath.get(0).getX(), currPath.get(0).getY())) {
						return currPath.get(0);
						
					//If we can't take that position, then another enemy must be there, so let's just wait
					} else {
						return new CoordinatePair(this.coordinateX, this.coordinateY);
					}
					
				}
				
				//We just visited this position, so let's mark it as known if it isn't and add all it's adjacent paths
				if(!known.contains(currPosition)) {
					ArrayList<CoordinatePair> neighbors = currPosition.getNeighbors();
					while(!neighbors.isEmpty()) {
						int randomIndex = rand.nextInt(neighbors.size());
						CoordinatePair neighbor = neighbors.get(randomIndex);
						neighbors.remove(neighbor);
						
						//TODO possible trouble spot. Maybe only check if the data is 0 or 2?
						if(neighbor.getX() <= data.getNumColumns() && neighbor.getY() <= data.getNumRows() &&
						   neighbor.getX() >= 0 && neighbor.getY() >= 0 && 
						   (data.get(neighbor.getX(), neighbor.getY()) == 0 || data.get(neighbor.getX(), neighbor.getY()) == 2)) {
							
							ArrayList<CoordinatePair> newPath = new ArrayList<CoordinatePair>(currPath);
							newPath.add(neighbor);
							queue.add(newPath);
						}
					}
					
					//Mark position as known
					known.add(currPosition);
				}
			}
			
			//If we exit the while loop we couldn't find a path, so let's just pick a random move
			return this.getRandomMove();
			
		} else  {
			throw new IllegalStateException("Trying to move enemies while in an illegal state.");
		}
	}
	
	/**
	 * Returns true if the Player is directly adjacent to this enemy (either above, below, to the left, or to the right)
	 */
	private boolean adjacentToPlayer(CoordinatePair position) {
		Player player = dungeon.getPlayer();
		
		//If we are above the Player
		if(position.getX() == player.getNextCoordinateX() && position.getY() == player.getNextCoordinateY() - 1) {
			return true;
			
		//If we are below the Player
		} else if(position.getX() == player.getNextCoordinateX() && position.getY() == player.getNextCoordinateY() + 1) {
			return true;
			
		//If we are the the left of the Player
		} else if(position.getX() == player.getNextCoordinateX() - 1 && position.getY() == player.getNextCoordinateY()) {
			return true;
			
		//If we are to the right of the Player
		} else if(position.getX() == player.getNextCoordinateX() + 1 && position.getY() == player.getNextCoordinateY()) {
			return true;
			
		} 
		
		
		return false;
	}
	
	/**
	 * Sets this Enemy to be facing the player
	 */
	private void facePlayer() {
		Player player = dungeon.getPlayer();
		
		//If we are above the Player
		if(this.coordinateX == player.getNextCoordinateX() && this.coordinateY == player.getNextCoordinateY() - 1) {
			this.setFacingDown();
			
		//If we are below the Player
		} else if(this.coordinateX == player.getNextCoordinateX() && this.coordinateY == player.getNextCoordinateY() + 1) {
			this.setFacingUp();
			
		//If we are the the left of the Player
		} else if(this.coordinateX == player.getNextCoordinateX() - 1 && this.coordinateY == player.getNextCoordinateY()) {
			this.setFacingRight();
			
		//If we are to the right of the Player
		} else if(this.coordinateX == player.getNextCoordinateX() + 1 && this.coordinateY == player.getNextCoordinateY()) {
			this.setFacingLeft();
			
		} 
	}
	
	/**
	 * Returns true if this Enemy is being rendered such that the player can see them (i.e. within render distance of the player)
	 */
	private boolean isVisibleToPlayer() {
		Player player = dungeon.getPlayer();
		if(this.coordinateX >= player.getCoordinateX() - game.getRenderDistance() && this.coordinateX <= player.getCoordinateX() + game.getRenderDistance() &&
		   this.coordinateY >= player.getCoordinateY() - game.getRenderDistance() && this.coordinateY <= player.getCoordinateY() + game.getRenderDistance()) {
			return true;
		} 
		
		return false;
	}

}
