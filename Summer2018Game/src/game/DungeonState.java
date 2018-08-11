package game;


import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class DungeonState extends State{

	private Dungeon dungeon;
	private Player player;
	private ArrayList<Enemy> enemies;
	private int difficulty;
	
	public DungeonState(Game game, int difficulty, int floors) {
		super(game);
		this.dungeon = new Dungeon(game, difficulty, floors);
		this.difficulty = difficulty;
		this.player = dungeon.getPlayer();
		this.enemies = dungeon.getEnemies();
	}
	
	public Dungeon getDungeon() {
		return this.dungeon;
	}
	
	public int getDifficulty() {
		return difficulty;
	}

	public Player getPlayer() {
		return player;
	}
	
	public DungeonState(Game game, Dungeon dungeon) {
		super(game);
		this.difficulty = dungeon.getDifficulty();
		this.dungeon = dungeon;
		this.player = dungeon.getPlayer();
		game.setPlayer(this.player);
		this.enemies = dungeon.getEnemies();
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
		for(Enemy enemy: enemies) {
			enemy.render(graphics);
		}
	}

	@Override
	public ArrayList<UIObject> getUIOBjects() {
		return null;
	}
}
