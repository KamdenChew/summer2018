package game;

import java.awt.Graphics;

public abstract class Entity {
	protected Game game;
	protected float x;
	protected float y;
	
	public Entity(Game game, float x, float y) {
		this.game = game;
		this.x = x;
		this.y = y;
	}
	
	public abstract void tick();
	
	public abstract void render(Graphics graphics);
}
