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
		
		if(this.isAggressive && this.adjacentToPlayer(new CoordinatePair(this.coordinateX, this.coordinateY))) {
			this.facePlayer();
			this.setAttacking(true);
		} else {
			
			CoordinatePair nextPosition;
			
			//If the player can see us, then make a smart move
			if(this.isVisibleToPlayer()) {
				System.out.println("Making a smart move!");
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
				} else {
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
	
	public CoordinatePair getBFSMove() {
		if(State.getState().isDungeonState()) {
			
			DungeonState dungeonState = (DungeonState) State.getState();
			Dungeon dungeon = dungeonState.getDungeon();
			
			Array2D<Integer> data = dungeon.getData();
			
			Queue<Pair<CoordinatePair, ArrayList<CoordinatePair>>> queue = new LinkedList<Pair<CoordinatePair, ArrayList<CoordinatePair>>>();
			
			//Start by adding our current position with an empty list, representing the path to this point
			queue.add(new Pair<CoordinatePair, ArrayList<CoordinatePair>>(new CoordinatePair(this.coordinateX, this.coordinateY), new ArrayList<CoordinatePair>()));
			
			//Keep track of where we have already explored to avoid running into infinite loops, since dungeons are like cyclical graphs.
			ArrayList<CoordinatePair> known = new ArrayList<CoordinatePair>();
			
			//While we still have tiles to explore
			while(!queue.isEmpty()) {
				Pair<CoordinatePair, ArrayList<CoordinatePair>> curr = queue.remove();
				
				CoordinatePair currPosition = curr.getFirstElement();
				ArrayList<CoordinatePair> currPath = curr.getSecondElement();
				
				//This is the goal, get adjacent to the player so we can attack
				if(this.adjacentToPlayer(currPosition)) {
					currPath.add(currPosition);
					System.out.println("Here is my path!: " + currPath);
					System.out.println("currpos: " + currPosition);
					return currPath.get(0);
				}
				
				//We just visited this position, so let's mark it as known if it isn't and add all it's adjacent paths
				if(!known.contains(currPosition)) {
					ArrayList<CoordinatePair> neighbors = currPosition.getNeighbors();
					
					while(!neighbors.isEmpty()) {
						int randomIndex = rand.nextInt(neighbors.size());
						CoordinatePair neighbor = neighbors.get(randomIndex);
						neighbors.remove(neighbor);
						
						//TODO possible trouble spot. Maybe only check if the data is 0 or 2?
						if(neighbor.getX() < data.getNumColumns() - 2 && neighbor.getY() < data.getNumRows() - 2 &&
						   neighbor.getX() >= 0 && neighbor.getY() >= 0 &&
						   dungeon.isWalkable(neighbor.getX(), neighbor.getY()) && !known.contains(neighbor)) {
							
							ArrayList<CoordinatePair> newPath = new ArrayList<CoordinatePair>(currPath);
							newPath.add(currPosition);
							queue.add(new Pair<CoordinatePair, ArrayList<CoordinatePair>>(neighbor, newPath));
						}
					}
					
					//Mark position as known
					known.add(currPosition);
					System.out.println("Considered " + currPosition);
				}
			}
			
			//If we exit the while loop we couldn't find a path, so let's just pick a random move
			
			System.out.println("Had to default to random");
			return this.getRandomMove();
			
		} else  {
			throw new IllegalStateException("Trying to move enemies while in an illegal state.");
		}
	}
	
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
	
	private boolean isVisibleToPlayer() {
		Player player = dungeon.getPlayer();
		if(this.coordinateX >= player.getCoordinateX() - game.getRenderDistance() && this.coordinateX <= player.getCoordinateX() + game.getRenderDistance() &&
		   this.coordinateY >= player.getCoordinateY() - game.getRenderDistance() && this.coordinateY <= player.getCoordinateY() + game.getRenderDistance()) {
			return true;
		} 
		
		return false;
	}

}
