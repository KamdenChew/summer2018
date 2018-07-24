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
			
			if(game.getKeyManager().up && (upVal == 0 || upVal == 2 || upVal == -2)) {
				y -= 50;
				this.coordinateY--;
				tickDelay = NUM_TICKS_MOVEMENT_DELAY;
				updateSeen();
			} else if(game.getKeyManager().down && (downVal == 0 || downVal == 2 || downVal == -2)) {
				y += 50;
				this.coordinateY++;
				tickDelay = NUM_TICKS_MOVEMENT_DELAY;
				updateSeen();
			} else if(game.getKeyManager().left && (leftVal == 0 || leftVal == 2 || leftVal == -2)) {
				x -= 50;
				this.coordinateX--;
				tickDelay = NUM_TICKS_MOVEMENT_DELAY;
				updateSeen();
			} else if(game.getKeyManager().right && (rightVal == 0 || rightVal == 2 || rightVal == -2)) {
				x += 50;
				this.coordinateX++;
				tickDelay = NUM_TICKS_MOVEMENT_DELAY;
				updateSeen();
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
