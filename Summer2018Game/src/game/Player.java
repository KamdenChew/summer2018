package game;

import java.awt.Graphics;
import java.util.ArrayList;

public class Player extends Creature{
	private static final int NUM_TICKS_MOVEMENT_DELAY = 10;
	private static final int RENDER_DISTANCE = 3;
	private int tickDelay = 0;
	private boolean inDungeon = false;
	private Dungeon dungeon;
	private ArrayList<Enemy> enemies;
	
	public Player(Game game, int coordinateX, int coordinateY, boolean inDungeon, Dungeon dungeon) {
		super(game, coordinateX * 50, coordinateY * 50, coordinateX, coordinateY);
		this.coordinateX = coordinateX;
		this.coordinateY = coordinateY;
		this.inDungeon = inDungeon;
		this.dungeon = dungeon;
		if(this.dungeon != null) {
			this.enemies = dungeon.getEnemies();
		}
	}

	@Override
	public void tick() {
		if(tickDelay == 0 && (State.getState().isDungeonState() || State.getState().isTownState())) {
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
			
			if(game.getKeyManager().up && upWalkable) {
				y -= 50;
				this.coordinateY--;
				tickDelay = NUM_TICKS_MOVEMENT_DELAY;
				updateSeen();
				handleNewTile();
				if(inDungeon) {
					tickEnemies();
				}

			} else if(game.getKeyManager().down && downWalkable) {
				y += 50;
				this.coordinateY++;
				tickDelay = NUM_TICKS_MOVEMENT_DELAY;
				updateSeen();
				handleNewTile();
				if(inDungeon) {
					tickEnemies();
				}

			} else if(game.getKeyManager().left && leftWalkable) {
				x -= 50;
				this.coordinateX--;
				tickDelay = NUM_TICKS_MOVEMENT_DELAY;
				updateSeen();
				handleNewTile();
				if(inDungeon) {
					tickEnemies();
				}
				
			} else if(game.getKeyManager().right && rightWalkable) {
				x += 50;
				this.coordinateX++;
				tickDelay = NUM_TICKS_MOVEMENT_DELAY;
				updateSeen();
				handleNewTile();
				if(inDungeon) {
					tickEnemies();
				}
				
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
			
			//tileVal == -2 means we are on the dungeon exit
			if(tileVal == -2) {
				System.out.println("Stepped on exit");
				State.setState(new ConfirmTownState(game, State.getState(), State.getState().getDifficulty()));
				
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
		graphics.drawImage(Assets.player, game.getRenderDistance() * 50, game.getRenderDistance() * 50, null);
	}
	
}
