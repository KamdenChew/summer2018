package game;

import java.awt.Graphics;

public abstract class Entity {
	protected Game game;
	protected float x;
	protected float y;
	protected int width;
	protected int height;
	
	/**
	 * Constructs a new Entity
	 * 
	 * @param game the Game object for this running instance
	 * @param x the x pixel coordinate for the upper left corner of the Entity
	 * @param y the y pixel coordinate for the upper left corner of the Entity
	 */
	public Entity(Game game, float x, float y) {
		this.game = game;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Processes and updates this Entity's variables
	 */
	public abstract void tick();
	
	/**
	 * Renders this entity to the specified Graphics object
	 * 
	 * @param graphics the Graphics object to render this entity too
	 */
	public abstract void render(Graphics graphics);

	
	//Getters and Setters
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
