package game;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class TownState extends State{
	
	private Array2D<Integer> data = new Array2D<Integer>(15, 15, new Integer[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1, 1,0,0,0,0,0,0,0,0,0,0,0,0,0,1, 1,0,0,0,0,0,0,0,0,0,0,0,0,0,1, 1,0,0,0,0,0,0,0,0,0,0,0,0,0,1, 1,0,0,0,0,0,0,0,0,0,0,0,0,0,1, 1,0,0,0,0,0,0,0,0,0,0,0,0,0,1, 1,0,0,0,0,0,0,0,0,0,0,0,0,0,1, 1,0,0,0,0,0,0,0,0,0,0,0,0,0,1, 1,0,0,0,0,0,0,0,0,0,0,0,0,0,1, 1,0,0,0,0,0,0,0,0,0,0,0,0,0,1, 1,0,0,0,0,0,0,0,0,0,0,0,0,0,1, 1,0,0,0,0,0,0,0,0,0,0,0,0,0,1, 1,0,0,0,0,0,0,0,0,0,0,0,0,0,1, 1,-3,0,0,0,-4,0,0,0,-5,0,0,0,-6,1, 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1});
	private Player player;
	private GameCamera camera;
	private Random rand = new Random();
	private long lastAnimationTime;
	
	public TownState(Game game, int x, int y, String direction) {
		super(game);
		if(x < 1 || x >= data.getNumColumns() - 1 || y < 1 || y > data.getNumRows() - 1) {
			throw new IllegalArgumentException("Player position must fall between x: 1-" + (data.getNumColumns() - 1) + " y: 1-" + (data.getNumRows() - 1));
		}
		this.player = new Player(game, x, y, false, null, direction);
		game.setPlayer(this.player);
		this.camera = player.getCamera();
		this.lastAnimationTime = System.nanoTime();
	}
	
	public GameCamera getCamera() {
		return camera;
	}

	public Player getPlayer() {
		return player;
	}

	public Array2D<Integer> getData() {
		return this.data;
	}

	@Override
	public void tick() {
		this.player.tick();
		
		if(this.game.getKeyManager().keyJustPressed(KeyEvent.VK_M)) {
			State.setState(new MenuState(this.game, this));
		}
	}

	@Override
	public void render(Graphics graphics) {
		if(player.getCoordinateX() == player.getNextCoordinateX() && player.getCoordinateY() == player.getNextCoordinateY()) {
			drawTownAndPlayer(graphics);
		} else {
			
			
			for(int i = 0; i < 50; i = i + (int) Player.STEP_SIZE) {
				
				//Set cap on animation rendering speed
				while(System.nanoTime() - this.lastAnimationTime < 6000000) { 
					
				}
				
				this.lastAnimationTime = System.nanoTime();
				
				//Moving up
				if(player.getNextCoordinateY() < player.getCoordinateY()) {
					player.setY(player.getY() - Player.STEP_SIZE);
					
				//Moving down
				} else if(player.getNextCoordinateY() > player.getCoordinateY()) {
					player.setY(player.getY() + Player.STEP_SIZE);
					
				//Moving left
				} else if(player.getNextCoordinateX() < player.getCoordinateX()) {
					player.setX(player.getX() - Player.STEP_SIZE);
					
				//Moving right
				} else if(player.getNextCoordinateX() > player.getCoordinateX()) {
					player.setX(player.getX() + Player.STEP_SIZE);
					
				}
				
				//Draw the updated movement
				player.getCamera().centerOnEntity(player);
				drawTownAndPlayer(graphics);
				game.forceBs();
//				Timer.waitFor(2);
			}
			
			//Set the new coordinates and handle the new tile after animation is complete
			player.setCoordinateX(player.getNextCoordinateX());
			player.setCoordinateY(player.getNextCoordinateY());
			player.handleNewTile();
			
		}
	}
	
	public void drawTownAndPlayer(Graphics graphics) {
		for(int x = -game.getRenderDistance(); x < data.getNumColumns() + game.getRenderDistance(); x++) {
			for(int y = -game.getRenderDistance(); y < data.getNumRows() + game.getRenderDistance(); y++) {
				//If it's off the board just visualize it as a wall
				if(x < 0 || x >= data.getNumColumns() || y < 0 || y >= data.getNumRows()) {
					graphics.drawImage(Assets.wall, (int) (x * 50 - camera.getXOffset()), (int) (y * 50 - camera.getYOffset()), null);
				} else {
					int val = data.get(x, y);
					if(Math.abs(val) == 1) {
						graphics.drawImage(Assets.wall, (int) (x * 50 - camera.getXOffset()), (int) (y * 50 - camera.getYOffset()), null);
					} else if(val == 0) {
						graphics.drawImage(Assets.stone, (int) (x * 50 - camera.getXOffset()), (int) (y * 50 - camera.getYOffset()), null);
					} else if(val == 2){
						graphics.drawImage(Assets.dirt, (int) (x * 50 - camera.getXOffset()), (int) (y * 50 - camera.getYOffset()), null);
					} else if(val == -3) {
						graphics.drawImage(Assets.peacefulWarp, (int) (x * 50 - camera.getXOffset()), (int) (y * 50 - camera.getYOffset()), null);
					} else if(val == -4) {
						graphics.drawImage(Assets.easyWarp, (int) (x * 50 - camera.getXOffset()), (int) (y * 50 - camera.getYOffset()), null);
					} else if(val == -5) {
						graphics.drawImage(Assets.mediumWarp, (int) (x * 50 - camera.getXOffset()), (int) (y * 50 - camera.getYOffset()), null);
					} else if(val == -6) {
						graphics.drawImage(Assets.hardWarp, (int) (x * 50 - camera.getXOffset()), (int) (y * 50 - camera.getYOffset()), null);
					}
				}
			}
		}
		player.render(graphics);
	}

	@Override
	public ArrayList<UIObject> getUIOBjects() {
		return null;
	}

	public Array2D<Boolean> getSeen() {
		Array2D<Boolean> seen = new Array2D<Boolean>(this.data.getNumColumns(), this.data.getNumRows());
		//Everything is considered visible in town
		for(int c = 0; c < seen.getNumColumns(); c++) {
			for(int r = 0; r < seen.getNumRows(); r++) {
				seen.set(c, r, true);
			}
		}
		
		return seen;
	}

	@Override
	public int getDifficulty() {
		return -1;
	}
	
//Code for rendering a map
//	//If we've seen it, go ahead and render it normally
//	if(dungeon.getSeen().get(x, y)) {
//		if(Math.abs(val) == 1) {
//			graphics.drawImage(Assets.wall, (x - game.getRenderDistance()) * 50, (y - game.getRenderDistance()) * 50, null);
//		} else if(val == 0) {
//			graphics.drawImage(Assets.stone, (x - game.getRenderDistance()) * 50, (y - game.getRenderDistance()) * 50, null);
//		} else if(val != -2){//if(data.get(x, y) == 2) {
//			graphics.drawImage(Assets.dirt, (x - game.getRenderDistance()) * 50, (y - game.getRenderDistance()) * 50, null);
//		} 
//		
//	//If we haven't seen it, render darkness
//	} else {
//		graphics.drawImage(Assets.empty, (x - game.getRenderDistance()) * 50, (y - game.getRenderDistance()) * 50, null);
//	}

}
