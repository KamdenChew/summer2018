package game;


import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class GameState extends State{

	private static final int RENDER_DISTANCE = 3;
	private Dungeon dungeon;
	private Player player;
	private Random rand = new Random();
	
	public GameState(Game game) {
		super(game);
		this.dungeon = game.dungeon;
		
		CoordinatePair startCoordinates = null;
		Array2D<Integer> data = dungeon.getData();
		while(startCoordinates == null || data.get(startCoordinates.getX(), startCoordinates.getY()) != 0) {
			int randomColumn = rand.nextInt(data.getNumColumns() - 2)  + 1;
			int randomRow = rand.nextInt(data.getNumRows() - 2) + 1;
			startCoordinates = new CoordinatePair(randomColumn, randomRow);
		}
		this.player = new Player(game, startCoordinates.getX(), startCoordinates.getY());
		
		for(int x = player.getCoordinateX() - RENDER_DISTANCE; x <= player.getCoordinateX() + RENDER_DISTANCE; x++) {
			for(int y = player.getCoordinateY() - RENDER_DISTANCE; y <= player.getCoordinateY() + RENDER_DISTANCE; y++) {
				
				//If it's in bounds, mark nearby tiles as seen!
				if(x >= 0 &&
						   x < game.dungeon.getSeen().getNumColumns() &&
						   y >= 0 &&
						   y < game.dungeon.getSeen().getNumRows()) {
					dungeon.getSeen().set(x, y, true);
				}
			}
		}
	}
	
	@Override
	public Array2D<Integer> getData() {
		return this.dungeon.getData();
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
		Array2D<Integer> data = this.dungeon.getData();
		int columns = data.getNumColumns();
		int rows = data.getNumRows();
		int xOffSet = player.getCoordinateX() - RENDER_DISTANCE;
		int yOffSet = player.getCoordinateY() - RENDER_DISTANCE;
		for(int x = player.getCoordinateX() - RENDER_DISTANCE; x <= player.getCoordinateX() + RENDER_DISTANCE; x++) {
			for(int y = player.getCoordinateY() - RENDER_DISTANCE; y <= player.getCoordinateY() + RENDER_DISTANCE; y++) {

				//If it's off the board just visualize it as a wall
				if(x < 0 || x >= columns || y < 0 || y >= rows) {
					graphics.drawImage(Assets.wall, (x - xOffSet) * 50, (y - yOffSet) * 50, null);
					
				//Otherwise display the information of the dungeon
				} else {
					int val = data.get(x, y);
					if(Math.abs(val) == 1) {
						graphics.drawImage(Assets.wall, (x - xOffSet) * 50, (y - yOffSet) * 50, null);
					} else if(val == 0) {
						graphics.drawImage(Assets.stone, (x - xOffSet) * 50, (y - yOffSet) * 50, null);
					} else if(val != -2){//if(data.get(x, y) == 2) {
						graphics.drawImage(Assets.dirt, (x - xOffSet) * 50, (y - yOffSet) * 50, null);
					} 
					
//					//If we've seen it, go ahead and render it normally
//					if(dungeon.getSeen().get(x, y)) {
//						if(Math.abs(val) == 1) {
//							graphics.drawImage(Assets.wall, (x - RENDER_DISTANCE) * 50, (y - RENDER_DISTANCE) * 50, null);
//						} else if(val == 0) {
//							graphics.drawImage(Assets.stone, (x - RENDER_DISTANCE) * 50, (y - RENDER_DISTANCE) * 50, null);
//						} else if(val != -2){//if(data.get(x, y) == 2) {
//							graphics.drawImage(Assets.dirt, (x - RENDER_DISTANCE) * 50, (y - RENDER_DISTANCE) * 50, null);
//						} 
//						
//					//If we haven't seen it, render darkness
//					} else {
//						graphics.drawImage(Assets.empty, (x - RENDER_DISTANCE) * 50, (y - RENDER_DISTANCE) * 50, null);
//					}
				}
			}
		}
		player.render(graphics);
	}

	@Override
	public ArrayList<UIObject> getUIOBjects() {
		// TODO Auto-generated method stub
		return null;
	}
}
