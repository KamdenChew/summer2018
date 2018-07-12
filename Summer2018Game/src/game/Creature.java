package game;

public abstract class Creature extends Entity{
	
	protected int coordinateX;
	protected int coordinateY;
	protected int health;
	
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
}
