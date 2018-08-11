package game;

import java.awt.Color;
import java.awt.Graphics;

public abstract class Creature extends Entity{
	
	protected int coordinateX;
	protected int coordinateY;
	protected int maxHealth = 10;
	protected int health;
	protected boolean facingUp = false, facingDown = true, facingLeft = false, facingRight = false;
	
	public Creature(Game game, float x, float y, int coordinateX, int coordinateY) {
		super(game, x, y);
		this.coordinateX = coordinateX;
		this.coordinateY = coordinateY;
		health = 10;
	}
	
	public int getCoordinateX() {
		return coordinateX;
	}

	public void setCoordinateX(int coordinateX) {
		this.coordinateX = coordinateX;
	}

	public int getCoordinateY() {
		return coordinateY;
	}

	public void setCoordinateY(int coordinateY) {
		this.coordinateY = coordinateY;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getHealth() {
		return health;
	}
	
	public void setFacingUp() {
		this.facingUp = true;
		this.facingDown = false;
		this.facingLeft = false;
		this.facingRight = false;
	}
	
	public void setFacingDown() {
		this.facingUp = false;
		this.facingDown = true;
		this.facingLeft = false;
		this.facingRight = false;
	}
	
	public void setFacingLeft() {
		this.facingUp = false;
		this.facingDown = false;
		this.facingLeft = true;
		this.facingRight = false;
	}
	
	public void setFacingRight() {
		this.facingUp = false;
		this.facingDown = false;
		this.facingLeft = false;
		this.facingRight = true;
	}
	
	public void updateDirectionFacing() {
		if(game.getKeyManager().up) {
			setFacingUp();
		} else if(game.getKeyManager().down) {
			setFacingDown();
		} else if(game.getKeyManager().left) {
			setFacingLeft();
		} else if(game.getKeyManager().right) {
			setFacingRight();
		}
	}
	
	public String getDirectionFacing() {
		if(this.facingUp) {
			return "Up";
		} else if(this.facingDown) {
			return "Down";
		} else if(this.facingLeft) {
			return "Left";
		} else {
			return "Right";
		}
	}
}
