package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Player extends Creature {
	public static final int NUM_TICKS_MOVEMENT_DELAY = 8;
	private static final int RENDER_DISTANCE = 3;
	private int tickDelay = 0;
	private boolean inDungeon = false, tookTurn = false;
	private Dungeon dungeon;
	private ArrayList<Enemy> enemies;
	private GameCamera camera;
	
	/**
	 * Constructs a new Creature
	 *
	 * @param game the Game object for this running instance
	 * @param x the x pixel coordinate for the upper left corner of the Player
	 * @param y the y pixel coordinate for the upper left corner of the Player
	 * @param coordinateX the x coordinate for where the Player resides in the grid world
	 * @param coordinateY the y coordinate for where the Player resides in the grid world
	 * @param currHealth the amount of health that this Player has
	 * @param inDungeon whether or not the Player is inside a DungeonState
	 * @param dungeon the Dungeon this Player is in
	 * @param direction String denoting which way this Player is facing
	 */
	public Player(Game game, int coordinateX, int coordinateY, int currHealth, boolean inDungeon, Dungeon dungeon, String direction) {
		super(game, coordinateX * 50, coordinateY * 50, coordinateX, coordinateY);
		this.maxHealth = 50;
		this.health = currHealth;
		this.coordinateX = coordinateX;
		this.coordinateY = coordinateY;
		this.nextCoordinateX = coordinateX;
		this.nextCoordinateY = coordinateY;
		this.inDungeon = inDungeon;
		this.dungeon = dungeon;
		if(inDungeon) {
			this.enemies = dungeon.getEnemies();
		}
		if(direction.equals("Up")) {
			setFacingUp();
		} else if(direction.equals("Down")) {
			setFacingDown();
		} else if(direction.equals("Left")) {
			setFacingLeft();
		} else if(direction.equals("Right")) {
			setFacingRight();
		}
		this.camera = new GameCamera(game, 0, 0);
		this.camera.centerOnEntity(this);
	}
	
	/**
	 * Updates the direction the player is facing based on player input
	 */
	public void updateDirectionFacing() {
		if(game.getKeyManager().up) {
			setFacingUp();
		} else if(game.getKeyManager().down) {
			setFacingDown();
		} else if(game.getKeyManager().left) {
			setFacingLeft();
		} else if(game.getKeyManager().right) {
			setFacingRight();
		}
	}

	@Override
	public void tick() {
		if(this.health == 0) {
			State.setState(new RespawnState(game));
			return;
		}
		
		if(tickDelay == 0) {
			updateDirectionFacing();
			Array2D<Integer> data;
			if(State.getState().isDungeonState()) {
				DungeonState dungeonState = (DungeonState) State.getState();
				data = dungeonState.getDungeon().getData();
			} else {
				TownState townState = (TownState) State.getState();
				data = townState.getData();
			}
			
			boolean upWalkable;
			boolean downWalkable;
			boolean leftWalkable;
			boolean rightWalkable;
			
			if(inDungeon) {
				upWalkable = dungeon.isWalkable(coordinateX, coordinateY - 1);
				downWalkable = dungeon.isWalkable(coordinateX, coordinateY + 1);
				leftWalkable = dungeon.isWalkable(coordinateX - 1, coordinateY);
				rightWalkable = dungeon.isWalkable(coordinateX + 1, coordinateY);
			} else {
				upWalkable = (Math.abs(data.get(coordinateX, coordinateY - 1)) != 1);
				downWalkable = (Math.abs(data.get(coordinateX, coordinateY + 1)) != 1);
				leftWalkable = (Math.abs(data.get(coordinateX - 1, coordinateY)) != 1);
				rightWalkable = (Math.abs(data.get(coordinateX + 1, coordinateY)) != 1);
			}
			
			//Check if each of the four direction keys are being pressed, the player can move that direction, and the user isn't also holding shift
			if(game.getKeyManager().up && upWalkable && !game.getKeyManager().beingPressed(KeyEvent.VK_SHIFT)) {
				setFacingUp();
				this.nextY = this.y - 50;
				this.nextCoordinateY = this.coordinateY - 1;
				
				tickDelay = NUM_TICKS_MOVEMENT_DELAY;
				updateSeen();
				tookTurn = true;
				if(inDungeon) {
					tickEnemies();
				}

			} else if(game.getKeyManager().down && downWalkable && !game.getKeyManager().beingPressed(KeyEvent.VK_SHIFT)) {
				setFacingDown();
				this.nextY = this.y + 50;
				this.nextCoordinateY = this.coordinateY + 1;
				
				tickDelay = NUM_TICKS_MOVEMENT_DELAY;
				updateSeen();
				tookTurn = true;
				if(inDungeon) {
					tickEnemies();
				}

			} else if(game.getKeyManager().left && leftWalkable && !game.getKeyManager().beingPressed(KeyEvent.VK_SHIFT)) {
				setFacingLeft();
				this.nextX = this.x - 50;
				this.nextCoordinateX = this.coordinateX - 1;
				
				tickDelay = NUM_TICKS_MOVEMENT_DELAY;
				updateSeen();
				tookTurn = true;
				if(inDungeon) {
					tickEnemies();
				}
				
			} else if(game.getKeyManager().right && rightWalkable && !game.getKeyManager().beingPressed(KeyEvent.VK_SHIFT)) {
				setFacingRight();
				this.nextX = this.x + 50;
				this.nextCoordinateX = this.coordinateX + 1;
				
				tickDelay = NUM_TICKS_MOVEMENT_DELAY;
				updateSeen();
				tookTurn = true;
				if(inDungeon) {
					tickEnemies();
				}
				
			//Space being pressed means an attempt at attacking
			} else if(game.getKeyManager().keyJustPressed(KeyEvent.VK_SPACE)) {
				tookTurn = true;
				isAttacking = true;
//				handleAttack();
				if(inDungeon) {
					tickEnemies();
				}
			}
		} else {
			tickDelay--;
		}
		
		//Center the camera
		if(State.getState().isDungeonState()) {
			DungeonState dungeonState = (DungeonState) State.getState();
			dungeonState.getCamera().centerOnEntity(this);
		} else if(State.getState().isTownState()) {
			TownState townState = (TownState) State.getState();
			townState.getCamera().centerOnEntity(this);
		}
	}

	/**
	 * Updates the Array2D of booleans that mark whether or not the Player has seen parts of the Dungeon
	 */
	private void updateSeen() {
		if(State.getState().isDungeonState() || State.getState().isTownState()) {
			for(int x = coordinateX - RENDER_DISTANCE; x <= coordinateX + RENDER_DISTANCE; x++) {
				for(int y = coordinateY - RENDER_DISTANCE; y <= coordinateY + RENDER_DISTANCE; y++) {
					
					Array2D<Integer> data;
					if(State.getState().isDungeonState()) {
						DungeonState dungeonState = (DungeonState) State.getState();
						data = dungeonState.getDungeon().getData();
					} else {
						TownState townState = (TownState) State.getState();
						data = townState.getData();
					}
					
					Array2D<Boolean> seen;
					if(State.getState().isDungeonState()) {
						DungeonState dungeonState = (DungeonState) State.getState();
						seen = dungeonState.getDungeon().getSeen();
					} else {
						TownState townState = (TownState) State.getState();
						seen = townState.getSeen();
					}
					
					//Set seen if it's in bounds
					if(x >= 0 &&
					   x < data.getNumColumns() &&
					   y >= 0 &&
					   y < data.getNumRows()) {
						seen.set(x, y, true);
					}
				}
			}
		}
	}
	
	/**
	 * Checks to see if the Player is standing on a tile that triggers an event
	 */
	public void handleNewTile() {
		//If we are on the peaceful warp tile
		if(State.getState().isDungeonState() || State.getState().isTownState()) {
			
			Array2D<Integer> data;
			if(State.getState().isDungeonState()) {
				DungeonState dungeonState = (DungeonState) State.getState();
				data = dungeonState.getDungeon().getData();
			} else {
				TownState townState = (TownState) State.getState();
				data = townState.getData();
			}
			
			int tileVal = data.get(this.getCoordinateX(), this.getCoordinateY());
			
			//tileVal == -2 means we are on the dungeon floor exit
			if(tileVal == -2) {
				if(dungeon.onLastFloor()) {
					State.setState(new ConfirmTownState(game, State.getState(), State.getState().getDifficulty()));
				} else {
					State.setState(new ConfirmNextFloorState(game, State.getState(), State.getState().getDifficulty()));
				}
				
			//tileVal = -3 means we are on the peaceful warp
			} else if(tileVal == -3) {
				State.setState(new ConfirmDungeonState(game, State.getState(), 0));
				
			//tileVal = -4 means we are on the easy warp
			} else if(tileVal == -4) {
				State.setState(new ConfirmDungeonState(game, State.getState(), 1));
				
			//tileVal = -5 means we are on the medium warp
			} else if(tileVal == -5) {
				State.setState(new ConfirmDungeonState(game, State.getState(), 2));
				
			//tileVal = -6 means we are on the hard warp
			} else if(tileVal == -6) {
				State.setState(new ConfirmDungeonState(game, State.getState(), 3));
			}
		}
	}
	
	/**
	 * Handles distributing damage to enemies when this Player performs an attack action
	 */
	public void handleAttack() {
		boolean facingEnemy = false;
		Enemy target = null;
		
		//Check if we are facing an enemy and save the enemy if we are
		if(facingUp) {
			for(Enemy enemy: enemies) {
				if(enemy.getCoordinateX() == this.coordinateX && enemy.getCoordinateY() == this.coordinateY - 1) {
					facingEnemy = true;
					target = enemy;
				}
			}
		} else if(facingDown) {
			for(Enemy enemy: enemies) {
				if(enemy.getCoordinateX() == this.coordinateX && enemy.getCoordinateY() == this.coordinateY + 1) {
					facingEnemy = true;
					target = enemy;
				}
			}
		} else if(facingLeft) {
			for(Enemy enemy: enemies) {
				if(enemy.getCoordinateX() == this.coordinateX - 1 && enemy.getCoordinateY() == this.coordinateY) {
					facingEnemy = true;
					target = enemy;
				}
			}
		} else if(facingRight) {
			for(Enemy enemy: enemies) {
				if(enemy.getCoordinateX() == this.coordinateX + 1 && enemy.getCoordinateY() == this.coordinateY) {
					facingEnemy = true;
					target = enemy;
				}
			}
		}
		
		if(facingEnemy) {
			
			//Deal damage to target
			target.decreaseHealth(2);
			
			//If target lost all health, remove it.
			if(target.getHealth() == 0) {
				enemies.remove(target);
				this.dungeon.getEnemies().remove(target);
				DungeonState dungeonState = (DungeonState) State.getState();
				dungeonState.getCreatures().remove(target);
			}
		}
	}
	
	/**
	 * Calls the tick methods of all the enemies in the Dungeon that this Player is in
	 */
	private void tickEnemies() {
		for(Enemy enemy: enemies) {
			enemy.tick();
		}
	}

	@Override
	public void render(Graphics graphics) {
		//Draw the player
		if(this.facingUp) {
			graphics.drawImage(Assets.playerUp, game.getRenderDistance() * 50, game.getRenderDistance() * 50, null);
		} else if(this.facingDown) {
			graphics.drawImage(Assets.playerDown, game.getRenderDistance() * 50, game.getRenderDistance() * 50, null);
		} else if(this.facingLeft) {
			graphics.drawImage(Assets.playerLeft, game.getRenderDistance() * 50, game.getRenderDistance() * 50, null);
		} else if(this.facingRight) {
			graphics.drawImage(Assets.playerRight, game.getRenderDistance() * 50, game.getRenderDistance() * 50, null);
		}
		
		renderHealthBar(graphics);
	}
	
	/**
	 * Renders this Player's health bar to the graphics object it is passed
	 * 
	 * @param graphics the Graphics object to draw the health bar too
	 * 
	 */
	public void renderHealthBar(Graphics graphics) {
		if(inDungeon) {
			
			//Find scaling value
			int pixelsPerHealth = 50 / maxHealth;
			
			//Represents amount of health the player has lost
			int numRedPixels = pixelsPerHealth * (maxHealth - health);
			
			//Represents amount of health the player has
			int numGreenPixels = 50 - numRedPixels;
			
			//Draw the Health Bar
			graphics.setColor(Color.green);
			graphics.fillRect(game.getRenderDistance() * 50, game.getRenderDistance() * 50, numGreenPixels, 4);
			graphics.setColor(Color.red);
			graphics.fillRect(game.getRenderDistance() * 50 + numGreenPixels, game.getRenderDistance() * 50, numRedPixels, 4);
		
		}
	}
	
	//Getters and Setters
	public GameCamera getCamera() {
		return camera;
	}

	public void setEnemies(ArrayList<Enemy> enemies) {
		this.enemies = enemies;
	}
	
	public boolean hasTakenTurn() {
		return tookTurn;
	}

	public void setTookTurn(boolean tookTurn) {
		this.tookTurn = tookTurn;
	}
}
