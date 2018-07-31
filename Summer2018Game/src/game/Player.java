package game;

import java.awt.Graphics;

public class Player extends Creature{
	private static final int NUM_TICKS_MOVEMENT_DELAY = 10;
	private static final int RENDER_DISTANCE = 3;
	private int tickDelay = 0;
	
	public Player(Game game, int coordinateX, int coordinateY) {
		super(game, coordinateX * 50, coordinateY * 50, coordinateX, coordinateY);
		this.coordinateX = coordinateX;
		this.coordinateY = coordinateY;
	}

	@Override
	public void tick() {
		if(tickDelay == 0 && State.getState().getData() != null) {
			int upVal = State.getState().getData().get(coordinateX, coordinateY - 1);
			int downVal = State.getState().getData().get(coordinateX, coordinateY + 1);
			int leftVal = State.getState().getData().get(coordinateX - 1, coordinateY);
			int rightVal = State.getState().getData().get(coordinateX + 1, coordinateY);
			
			if(game.getKeyManager().up && (Math.abs(upVal) != 1)) {
				y -= 50;
				this.coordinateY--;
				tickDelay = NUM_TICKS_MOVEMENT_DELAY;
				updateSeen();
				handleNewTile();
//				System.out.println("Player coordinates: " + new CoordinatePair(coordinateX, coordinateY));
			} else if(game.getKeyManager().down && (Math.abs(downVal) != 1)) {
				y += 50;
				this.coordinateY++;
				tickDelay = NUM_TICKS_MOVEMENT_DELAY;
				updateSeen();
				handleNewTile();
//				System.out.println("Player coordinates: " + new CoordinatePair(coordinateX, coordinateY));
			} else if(game.getKeyManager().left && (Math.abs(leftVal) != 1)) {
				x -= 50;
				this.coordinateX--;
				tickDelay = NUM_TICKS_MOVEMENT_DELAY;
				updateSeen();
				handleNewTile();
//				System.out.println("Player coordinates: " + new CoordinatePair(coordinateX, coordinateY));
			} else if(game.getKeyManager().right && (Math.abs(rightVal) != 1)) {
				x += 50;
				this.coordinateX++;
				tickDelay = NUM_TICKS_MOVEMENT_DELAY;
				updateSeen();
				handleNewTile();
//				System.out.println("Player coordinates: " + new CoordinatePair(coordinateX, coordinateY));
			}
		} else {
			tickDelay--;
		}
		
		
	}
	
	private void updateSeen() {
		if(State.getState().getData() != null) {
			for(int x = coordinateX - RENDER_DISTANCE; x <= coordinateX + RENDER_DISTANCE; x++) {
				for(int y = coordinateY - RENDER_DISTANCE; y <= coordinateY + RENDER_DISTANCE; y++) {
					
					//Set seen if it's in bounds
					if(x >= 0 &&
					   x < State.getState().getData().getNumColumns() &&
					   y >= 0 &&
					   y < State.getState().getData().getNumRows()) {
						State.getState().getSeen().set(x, y, true);
					}
				}
			}
		}
	}
	
	private void handleNewTile() {
		//If we are on the peaceful warp tile
		if(State.getState().getData() != null) {
			int tileVal = State.getState().getData().get(this.getCoordinateX(), this.getCoordinateY());
			
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

	@Override
	public void render(Graphics graphics) {
		graphics.drawImage(Assets.player, game.getRenderDistance() * 50, game.getRenderDistance() * 50, null);
	}
	
}
