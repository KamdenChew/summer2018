package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Player extends Creature {
	private static final int NUM_TICKS_MOVEMENT_DELAY = 8;
	private static final int RENDER_DISTANCE = 3;
	private int tickDelay = 0;
	private boolean inDungeon = false;
	private Dungeon dungeon;
	private ArrayList<Enemy> enemies;
	
	public Player(Game game, int coordinateX, int coordinateY, boolean inDungeon, Dungeon dungeon, String direction) {
		super(game, coordinateX * 50, coordinateY * 50, coordinateX, coordinateY);
		this.coordinateX = coordinateX;
		this.coordinateY = coordinateY;
		this.inDungeon = inDungeon;
		this.dungeon = dungeon;
		if(this.dungeon != null) {
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
			
			if(game.getKeyManager().up && upWalkable && !game.getKeyManager().beingPressed(KeyEvent.VK_SHIFT)) {
				setFacingUp();
				y -= 50;
				this.coordinateY--;
				tickDelay = NUM_TICKS_MOVEMENT_DELAY;
				updateSeen();
				if(inDungeon) {
					tickEnemies();
				}
				handleNewTile();

			} else if(game.getKeyManager().down && downWalkable && !game.getKeyManager().beingPressed(KeyEvent.VK_SHIFT)) {
				setFacingDown();
				y += 50;
				this.coordinateY++;
				tickDelay = NUM_TICKS_MOVEMENT_DELAY;
				updateSeen();
				if(inDungeon) {
					tickEnemies();
				}
				handleNewTile();

			} else if(game.getKeyManager().left && leftWalkable && !game.getKeyManager().beingPressed(KeyEvent.VK_SHIFT)) {
				setFacingLeft();
				x -= 50;
				this.coordinateX--;
				tickDelay = NUM_TICKS_MOVEMENT_DELAY;
				updateSeen();
				if(inDungeon) {
					tickEnemies();
				}
				handleNewTile();
				
			} else if(game.getKeyManager().right && rightWalkable && !game.getKeyManager().beingPressed(KeyEvent.VK_SHIFT)) {
				setFacingRight();
				x += 50;
				this.coordinateX++;
				tickDelay = NUM_TICKS_MOVEMENT_DELAY;
				updateSeen();
				if(inDungeon) {
					tickEnemies();
				}
				handleNewTile();
				
			}
		} else {
			tickDelay--;
		}
		
		
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
	
	private void handleNewTile() {
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
		
		//Find scaling value
		int pixelsPerHealth = 50 / maxHealth;
		
		//Represents amount of health the player has lost
		int numRedPixels = pixelsPerHealth * (maxHealth - health);
		
		//Represents amount of health the player has
		int numGreenPixels = 50 - numRedPixels;
		
		//Draw the Health Bar
		graphics.setColor(Color.green);
		graphics.fillRect(game.getRenderDistance() * 50, game.getRenderDistance() * 50, numGreenPixels, 2);
		graphics.setColor(Color.red);
		graphics.fillRect(game.getRenderDistance() * 50 + numGreenPixels, game.getRenderDistance() * 50, numRedPixels, 2);
	}
	
}
