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
	
	public abstract void tick();
	
	public abstract void render(Graphics graphics);
	
	public abstract void onClick();
	
	public void onMouseMove(MouseEvent e) {
		updateHovering(e.getX(), e.getY());
	}
	
	public void updateHovering(int eX, int eY) {
		if(eX >= x && eX < x + width && eY >= y && eY < y + height) {
			hovering = true;
		} else {
			hovering = false;
		}
	}
	
	public void onMouseRelease(MouseEvent e) {
		if(hovering) {
			onClick();
		}
	}

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
