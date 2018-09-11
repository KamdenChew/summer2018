package game;

public class GameCamera {
	
	private Game game;
	private float xOffset, yOffset;
	
	/**
	 * Constructs a new GameCamera, used to figure out where to render things relative to the player
	 *
	 * @param game the Game object for this running instance
	 * @param xOffset the number of pixels offset horizontally from 0,0
	 * @param yOffset the number of pixels offset vertically from 0,0
	 */
	public GameCamera(Game game, float xOffset, float yOffset){
		this.game = game;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	
	/**
	 * Centers this camera on a given Entity
	 *
	 * @param entity the Entity to center the camera on 
	 */
	public void centerOnEntity(Entity entity){
		xOffset = entity.getX() - game.getWidth() / 2 + entity.getWidth() / 2;
		yOffset = entity.getY() - game.getHeight() / 2 + entity.getHeight() / 2;
	}
	
	/**
	 * Moves this camera by xAmt pixels horizontally and yAmt pixels vertically
	 *
	 * @param xAmt the number of pixels to add to the xOffset
	 * @param yAmt the number of pixels to add to the yOffset 
	 */
	public void move(float xAmt, float yAmt){
		xOffset += xAmt;
		yOffset += yAmt;
	}
	
	//Getters and Setters
	public float getXOffset() {
		return xOffset;
	}

	public void setXOffset(float xOffset) {
		this.xOffset = xOffset;
	}

	public float getYOffset() {
		return yOffset;
	}

	public void setYOffset(float yOffset) {
		this.yOffset = yOffset;
	}

}
