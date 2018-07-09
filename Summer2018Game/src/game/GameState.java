package game;


import java.awt.Graphics;
import java.util.Random;

public class GameState extends State{

	private Dungeon dungeon;
	private Player player;
	private Random rand = new Random();
	
	public GameState(Game game) {
		super(game);
		this.game = game;
		this.dungeon = game.dungeon;
		
		CoordinatePair startCoordinates = null;
		Array2D<Integer> data = game.dungeon.getData();
		while(startCoordinates == null || data.get(startCoordinates.getX(), startCoordinates.getY()) != 0) {
			int randomColumn = rand.nextInt(data.getNumColumns() - 2)  + 1;
			int randomRow = rand.nextInt(data.getNumRows() - 2) + 1;
			startCoordinates = new CoordinatePair(randomColumn, randomRow);
		}
		System.out.println("Starting at: " + startCoordinates);
		this.player = new Player(game, startCoordinates.getX(), startCoordinates.getY());
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
				if(Math.abs(data.get(x, y)) == 1) {
					graphics.drawImage(Assets.wall, x * 50, y * 50, null);
				} else if(data.get(x, y) == 0) {
					graphics.drawImage(Assets.stone, x * 50, y * 50, null);
				} else if(data.get(x, y) != -2){//if(data.get(x, y) == 2) {
					graphics.drawImage(Assets.dirt, x * 50, y * 50, null);
				} 
			}
		}
		player.render(graphics);
	}

}
