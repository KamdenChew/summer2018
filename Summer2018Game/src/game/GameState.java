package game;


import java.awt.Graphics;
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
	public void tick() {
		player.tick();
	}

	@Override
	public void render(Graphics graphics) {
		Array2D<Integer> data = this.dungeon.getData();
		int columns = data.getNumColumns();
		int rows = data.getNumRows();
		for(int x = 0; x < columns; x++) {
			for(int y = 0; y < rows; y++) {
				int val = data.get(x, y);
				//If we've seen it, go ahead and render it normally
				if(dungeon.getSeen().get(x, y)) {
					if(Math.abs(val) == 1) {
						graphics.drawImage(Assets.wall, x * 50, y * 50, null);
					} else if(val == 0) {
						graphics.drawImage(Assets.stone, x * 50, y * 50, null);
					} else if(val != -2){//if(data.get(x, y) == 2) {
						graphics.drawImage(Assets.dirt, x * 50, y * 50, null);
					} 
					
				//If we haven't seen it, render darkness
				} else {
					graphics.drawImage(Assets.empty, x * 50, y * 50, null);
				}
				
			}
		}
		player.render(graphics);
	}

}
