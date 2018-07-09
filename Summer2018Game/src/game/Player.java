package game;

import java.awt.Graphics;

public class Player extends Creature{
	private final int NUM_TICKS_MOVEMENT_DELAY = 10;
	
	private Game game;
	private int coordinateX;
	private int coordinateY;
	private int tickDelay = 0;
	
	public Player(Game game, int coordinateX, int coordinateY) {
		super(coordinateX * 50, coordinateY * 50, coordinateX, coordinateY);
		this.coordinateX = coordinateX;
		this.coordinateY = coordinateY;
		this.game = game;
	}

	@Override
	public void tick() {
		if(tickDelay == 0) {
			int upVal = this.game.dungeon.getData().get(coordinateX, coordinateY - 1);
			int downVal = this.game.dungeon.getData().get(coordinateX, coordinateY + 1);
			int leftVal = this.game.dungeon.getData().get(coordinateX - 1, coordinateY);
			int rightVal = this.game.dungeon.getData().get(coordinateX + 1, coordinateY);
			
			if(game.getKeyManager().up && (upVal == 0 || upVal == 2 || upVal == -2)) {
				y -= 50;
				coordinateY--;
				tickDelay = NUM_TICKS_MOVEMENT_DELAY;
			} else if(game.getKeyManager().down && (downVal == 0 || downVal == 2 || downVal == 22)) {
				y += 50;
				coordinateY++;
				tickDelay = NUM_TICKS_MOVEMENT_DELAY;
			} else if(game.getKeyManager().left && (leftVal == 0 || leftVal == 2 || leftVal == -2)) {
				x -= 50;
				coordinateX--;
				tickDelay = NUM_TICKS_MOVEMENT_DELAY;
			} else if(game.getKeyManager().right && (rightVal == 0 || rightVal == 2 || rightVal == -2)) {
				x += 50;
				coordinateX++;
				tickDelay = NUM_TICKS_MOVEMENT_DELAY;
			}
		} else {
			tickDelay--;
		}
		
		
	}

	@Override
	public void render(Graphics graphics) {
		graphics.drawImage(Assets.player, (int) x, (int) y, null);
	}
	
}
