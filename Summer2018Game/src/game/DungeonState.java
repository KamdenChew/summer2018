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
	private GameCamera camera;
	
	public DungeonState(Game game, int difficulty, int floors) {
		super(game);
		this.dungeon = new Dungeon(game, difficulty, floors);
		this.difficulty = difficulty;
		this.player = dungeon.getPlayer();
		this.enemies = dungeon.getEnemies();
		this.camera = player.getCamera();
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
		this.camera = player.getCamera();
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
		
		//If we aren't moving this tick, just draw
		if(player.getCoordinateX() == player.getNextCoordinateX() && player.getCoordinateY() == player.getNextCoordinateY()) {
			System.out.println("if");
			drawDungeonAndPlayer(graphics);
		
		//We have somewhere to move, so let's animate it
		} else {
			System.out.println("else");

			//Moving up
			if(player.getNextCoordinateY() < player.getCoordinateY()) {
				for(float y = player.getY(); y < player.getNextY(); y = y - Player.STEP_SIZE) {
					player.setY(y);
					drawDungeonAndPlayer(graphics);
					Timer.waitFor(0);
				}
				player.setCoordinateY(player.getNextCoordinateY());
			//Moving down
			} else if(player.getNextCoordinateY() > player.getCoordinateY()) {
				for(float y = player.getY(); y < player.getNextY(); y = y + Player.STEP_SIZE) {
					player.setY(y);
					drawDungeonAndPlayer(graphics);
					Timer.waitFor(0);
				}
				player.setCoordinateY(player.getNextCoordinateY());
			//Moving left
			} else if(player.getNextCoordinateX() < player.getCoordinateX()) {
				for(float x = player.getX(); x < player.getNextX(); x = x - Player.STEP_SIZE) {
					player.setX(x);
					drawDungeonAndPlayer(graphics);
					Timer.waitFor(0);
				}
				player.setCoordinateX(player.getNextCoordinateX());
			//Moving right
			} else if(player.getNextCoordinateX() > player.getCoordinateX()) {
				for(float x = player.getX(); x < player.getNextX(); x = x + Player.STEP_SIZE) {
					player.setX(x);
					drawDungeonAndPlayer(graphics);
					Timer.waitFor(0);
				}
				player.setCoordinateX(player.getNextCoordinateX());
			}
		}
	}
	
	private void drawDungeonAndPlayer(Graphics graphics) {
		for(int x = -game.getRenderDistance(); x < dungeon.getData().getNumColumns() + game.getRenderDistance(); x++) {
			for(int y = -game.getRenderDistance(); y < dungeon.getData().getNumRows() + game.getRenderDistance(); y++) {
				
				//If it's off the board just visualize it as a wall
				if(x < 0 || x >= dungeon.getData().getNumColumns() || y < 0 || y >= dungeon.getData().getNumRows()) {
					graphics.drawImage(Assets.wall, (int) (x * 50 - camera.getXOffset()), (int) (y * 50 - camera.getYOffset()), null);
				} else {
					int val = dungeon.getData().get(x, y);
					if(Math.abs(val) == 1) {
						graphics.drawImage(Assets.wall, (int) (x * 50 - camera.getXOffset()), (int) (y * 50 - camera.getYOffset()), null);
					} else if(val == 0) {
						graphics.drawImage(Assets.stone, (int) (x *  50 - camera.getXOffset()), (int) (y * 50 - camera.getYOffset()), null);
					} else if(val == 2){
						graphics.drawImage(Assets.dirt, (int) (x * 50 - camera.getXOffset()), (int) (y * 50 - camera.getYOffset()), null);
					} else if(val == -2) {
						graphics.drawImage(Assets.exit, (int) (x * 50 - camera.getXOffset()), (int) (y * 50 - camera.getYOffset()), null);
					}
				}
			}
		}
		
		for(Enemy enemy: enemies) {
			enemy.render(graphics);
		}
		
		player.render(graphics);
	}

	public GameCamera getCamera() {
		return camera;
	}

	@Override
	public ArrayList<UIObject> getUIOBjects() {
		return null;
	}
}
