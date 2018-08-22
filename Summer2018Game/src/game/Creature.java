package game;

public abstract class Creature extends Entity{
	
	public static final float STEP_SIZE = 1;
	protected int coordinateX;
	protected int coordinateY;
	protected float nextX;
	protected float nextY;
	protected int nextCoordinateX;
	protected int nextCoordinateY;
	protected int maxHealth = 10;
	protected int health;
	protected boolean facingUp = false, facingDown = true, facingLeft = false, facingRight = false, isAttacking = false;

	public Creature(Game game, float x, float y, int coordinateX, int coordinateY) {
		super(game, x, y);
		this.coordinateX = coordinateX;
		this.coordinateY = coordinateY;
		this.health = 10;
		this.width = 50;
		this.height = 50;
	}

	public void decreaseHealth(int damage) {
		if(this.health - damage < 0) {
			this.health = 0;
		} else {
			this.health = this.health - damage;
		}
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
	
	public int getHealth() {
		return health;
	}
	
	public float getNextX() {
		return nextX;
	}

	public void setNextX(float nextX) {
		this.nextX = nextX;
	}

	public float getNextY() {
		return nextY;
	}

	public void setNextY(float nextY) {
		this.nextY = nextY;
	}

	public int getNextCoordinateX() {
		return nextCoordinateX;
	}

	public void setNextCoordinateX(int nextCoordinateX) {
		this.nextCoordinateX = nextCoordinateX;
	}

	public int getNextCoordinateY() {
		return nextCoordinateY;
	}

	public void setNextCoordinateY(int nextCoordinateY) {
		this.nextCoordinateY = nextCoordinateY;
	}
	
	public boolean isAttacking() {
		return isAttacking;
	}

	public void setAttacking(boolean isAttacking) {
		this.isAttacking = isAttacking;
	}
	
	public void handleNewTile() {
		
	}
}
