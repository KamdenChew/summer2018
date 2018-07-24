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
				System.out.println("Player coordinates: " + new CoordinatePair(coordinateX, coordinateY));
			} else if(game.getKeyManager().down && (Math.abs(downVal) != 1)) {
				y += 50;
				this.coordinateY++;
				tickDelay = NUM_TICKS_MOVEMENT_DELAY;
				updateSeen();
				System.out.println("Player coordinates: " + new CoordinatePair(coordinateX, coordinateY));
			} else if(game.getKeyManager().left && (Math.abs(leftVal) != 1)) {
				x -= 50;
				this.coordinateX--;
				tickDelay = NUM_TICKS_MOVEMENT_DELAY;
				updateSeen();
				System.out.println("Player coordinates: " + new CoordinatePair(coordinateX, coordinateY));
			} else if(game.getKeyManager().right && (Math.abs(rightVal) != 1)) {
				x += 50;
				this.coordinateX++;
				tickDelay = NUM_TICKS_MOVEMENT_DELAY;
				updateSeen();
				System.out.println("Player coordinates: " + new CoordinatePair(coordinateX, coordinateY));
			}
		} else {
			tickDelay--;
		}
		
		
	}
	
	private void updateSeen() {
		for(int x = coordinateX - RENDER_DISTANCE; x <= coordinateX + RENDER_DISTANCE; x++) {
			for(int y = coordinateY - RENDER_DISTANCE; y <= coordinateY + RENDER_DISTANCE; y++) {
				
				//Set seen if it's in bounds
				if(x >= 0 &&
				   x < game.dungeon.getSeen().getNumColumns() &&
				   y >= 0 &&
				   y < game.dungeon.getSeen().getNumRows()) {
					game.dungeon.getSeen().set(x, y, true);
				}
			}
		}
	}

	@Override
	public void render(Graphics graphics) {
		graphics.drawImage(Assets.player, game.getRenderDistance() * 50, game.getRenderDistance() * 50, null);
	}
	
}
