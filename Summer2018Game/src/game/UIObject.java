package game;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

public abstract class UIObject {
	
	protected float x;
	protected float y;
	protected int width;
	protected int height;
	protected boolean hovering = false;
	
	public UIObject(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Processes and updates this UIObject's variables
	 */
	public abstract void tick();
	
	/**
	 * Renders this UIObject to the specified Graphics object
	 * 
	 * @param graphics the Graphics object to render this UIObject too
	 */
	public abstract void render(Graphics graphics);
	
	/**
	 * Tells this UIObject what method to perform when the user's mouse clicks on it.
	 */
	public abstract void onClick();
	
	/**
	 * Updates whether or not the user's mouse is hovering over this UIObject, given a mouse event
	 * 
	 * @param e a MouseEvent to tell us the position of the user's mouse
	 */
	public void onMouseMove(MouseEvent e) {
		updateHovering(e.getX(), e.getY());
	}
	
	/**
	 * Updates whether or not the user's mouse is hovering over this UIObject
	 * 
	 * @param eX the x pixel offset of the user's mouse
	 * @param eY the y pixel offset of the user's mouse
	 */
	public void updateHovering(int eX, int eY) {
		if(eX >= x && eX < x + width && eY >= y && eY < y + height) {
			hovering = true;
		} else {
			hovering = false;
		}
	}
	
	/**
	 * Tells this UIObject to perform it's onClick method, but only if the user's mouse is hovering over this UIObject
	 */
	public void onMouseRelease(MouseEvent e) {
		if(hovering) {
			onClick();
		}
	}

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

	public boolean isHovering() {
		return hovering;
	}

	public void setHovering(boolean hovering) {
		this.hovering = hovering;
	}
}
