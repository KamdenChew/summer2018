package game;


import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class DungeonState extends State{

	private Dungeon dungeon;
	private Player player;
	private Random rand = new Random();
	private int difficulty;
	
	public DungeonState(Game game, int difficulty) {
		super(game);
		System.out.println("Set difficulty to: " + difficulty);
		this.dungeon = new Dungeon(difficulty);
		this.difficulty = difficulty;
		CoordinatePair startCoordinates = null;
		Array2D<Integer> data = dungeon.getData();
		while(startCoordinates == null || data.get(startCoordinates.getX(), startCoordinates.getY()) != 0) {
			int randomColumn = rand.nextInt(data.getNumColumns() - 2)  + 1;
			int randomRow = rand.nextInt(data.getNumRows() - 2) + 1;
			startCoordinates = new CoordinatePair(randomColumn, randomRow);
		}
		this.player = new Player(game, startCoordinates.getX(), startCoordinates.getY());
		game.setPlayer(this.player);
		
		for(int x = player.getCoordinateX() - game.getRenderDistance(); x <= player.getCoordinateX() + game.getRenderDistance(); x++) {
			for(int y = player.getCoordinateY() - game.getRenderDistance(); y <= player.getCoordinateY() + game.getRenderDistance(); y++) {
				
				//If it's in bounds, mark nearby tiles as seen!
				if(x >= 0 &&
						   x < dungeon.getSeen().getNumColumns() &&
						   y >= 0 &&
						   y < dungeon.getSeen().getNumRows()) {
					dungeon.getSeen().set(x, y, true);
				}
			}
		}
		game.setPlayer(this.player);
	}
	
	public int getDifficulty() {
		return difficulty;
	}

	public Player getPlayer() {
		return player;
	}

	public DungeonState(Game game, int x, int y, int difficulty, Array2D<Integer> data, Array2D<Boolean> seen, int numDungeonRows, int numDungeonColumns) {
		super(game);
		this.difficulty = difficulty;
		this.dungeon = new Dungeon(difficulty, data, seen, numDungeonRows, numDungeonColumns);
		this.player = new Player(game, x, y);
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
		int xOffSet = player.getCoordinateX() - game.getRenderDistance();
		int yOffSet = player.getCoordinateY() - game.getRenderDistance();
		for(int x = player.getCoordinateX() - game.getRenderDistance(); x <= player.getCoordinateX() + game.getRenderDistance(); x++) {
			for(int y = player.getCoordinateY() - game.getRenderDistance(); y <= player.getCoordinateY() + game.getRenderDistance(); y++) {

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
					} else if(val == 2){
						graphics.drawImage(Assets.dirt, (x - xOffSet) * 50, (y - yOffSet) * 50, null);
					} else if(val == -2) {
						graphics.drawImage(Assets.exit, (x - xOffSet) * 50, (y - yOffSet) * 50, null);
					}
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

	@Override
	public Array2D<Boolean> getSeen() {
		return this.dungeon.getSeen();
	}
}
