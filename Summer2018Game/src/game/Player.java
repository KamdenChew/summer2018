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
	
	public Player(Game game, int coordinateX, int coordinateY, boolean inDungeon, Dungeon dungeon, String direction) {
		super(game, coordinateX * 50, coordinateY * 50, coordinateX, coordinateY);
		this.maxHealth = 50;
		this.health = 50;
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

	public GameCamera getCamera() {
		return camera;
	}

	public void setEnemies(ArrayList<Enemy> enemies) {
		this.enemies = enemies;
	}

	@Override
	public void tick() {
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
			
			//TODO animate walking
			//Check if each of the four direction keys are being pressed, the player can move that direction, and the user isn't also holding shift
			if(game.getKeyManager().up && upWalkable && !game.getKeyManager().beingPressed(KeyEvent.VK_SHIFT)) {
				setFacingUp();
				this.nextY = this.y - 50;
				this.nextCoordinateY = this.coordinateY - 1;
				
//				//TODO remove temp code
//				this.y = this.nextY;
//				this.coordinateY = this.nextCoordinateY;
				
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
				
//				//TODO remove temp code
//				this.y = this.nextY;
//				this.coordinateY = this.nextCoordinateY;
				
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
				
//				//TODO remove temp code
//				this.x = this.nextX;
//				this.coordinateX = this.nextCoordinateX;
				
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

//				//TODO remove temp code
//				this.x = this.nextX;
//				this.coordinateX = this.nextCoordinateX;
				
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
	
	public boolean hasTakenTurn() {
		return tookTurn;
	}

	public void setTookTurn(boolean tookTurn) {
		this.tookTurn = tookTurn;
	}

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
	
	@Override
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
				System.out.println("Stepped on exit");
				if(dungeon.onLastFloor()) {
					State.setState(new ConfirmTownState(game, State.getState(), State.getState().getDifficulty()));
				} else {
					State.setState(new ConfirmNextFloorState(game, State.getState(), State.getState().getDifficulty()));
				}
				
			//tileVal = -3 means we are on the peaceful warp
			} else if(tileVal == -3) {
				System.out.println("Stepped on peaceful warp");
				State.setState(new ConfirmDungeonState(game, State.getState(), 0));
				
			//tileVal = -4 means we are on the easy warp
			} else if(tileVal == -4) {
				System.out.println("Stepped on easy warp");
				State.setState(new ConfirmDungeonState(game, State.getState(), 1));
				
			//tileVal = -5 means we are on the medium warp
			} else if(tileVal == -5) {
				System.out.println("Stepped on medium warp");
				State.setState(new ConfirmDungeonState(game, State.getState(), 2));
				
			//tileVal = -6 means we are on the hard warp
			} else if(tileVal == -6) {
				System.out.println("Stepped on hard warp");
				State.setState(new ConfirmDungeonState(game, State.getState(), 3));
			}
		}
	}
	
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
			//TODO animate attack
			
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
	
}
