package game;

public abstract class Creature extends Entity{
	
	protected int coordinateX;
	protected int coordianteY;
	protected int health;
	
	public Creature(float x, float y, int coordinateX, int coordinateY) {
		super(x, y);
		this.coordinateX = coordinateX;
		this.coordianteY = coordinateY;
		health = 10;
	}
	
	public int getHealth() {
		return health;
	}
}
