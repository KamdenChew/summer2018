package game;

import java.awt.Graphics;

public abstract class Entity {
	protected float x;
	protected float y;
	
	public Entity(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public abstract void tick();
	
	public abstract void render(Graphics graphics);
}
