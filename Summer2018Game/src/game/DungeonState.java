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
	private ArrayList<Creature> creatures = new ArrayList<Creature>();
	private long lastAnimationTime;
	
	public DungeonState(Game game, int difficulty, int floors) {
		super(game);
		this.dungeon = new Dungeon(game, difficulty, floors);
		this.difficulty = difficulty;
		this.player = dungeon.getPlayer();
		this.enemies = dungeon.getEnemies();
		this.camera = player.getCamera();
		this.creatures.add(player);
		for(Enemy enemy: enemies) {
			creatures.add(enemy);
		}
		this.lastAnimationTime = System.nanoTime();
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
			//If the player is not taking a turn, just render as usual.
			if(!player.hasTakenTurn()) {
				drawDungeonAndPlayer(graphics);
				
			//Otherwise we know all creatures in the dungeon will be taking a turn
			} else {
				
				for(int i = 0; i < 50; i = i + (int) Creature.STEP_SIZE) {
					
					//Set cap on animation rendering speed
					while(System.nanoTime() - this.lastAnimationTime < 6000000) { 
						
					}
					
					
					this.lastAnimationTime = System.nanoTime();
					
					
					for(Creature creature: creatures) {
						if(creature.isAttacking()) {
//							drawDungeonAndPlayer(graphics);
							creature.setAttacking(false);
						//This creature is moving up
						} else if(creature.getNextCoordinateY() < creature.getCoordinateY()) {
							creature.setY(creature.getY() - Creature.STEP_SIZE);
							
						//This creature is moving down
						} else if(creature.getNextCoordinateY() > creature.getCoordinateY()) {
							creature.setY(creature.getY() + Creature.STEP_SIZE);
							
						//This creature is moving left
						} else if(creature.getNextCoordinateX() < creature.getCoordinateX()) {
							creature.setX(creature.getX() - Creature.STEP_SIZE);
							
						//This creature is moving right
						} else if(creature.getNextCoordinateX() > creature.getCoordinateX()) {
							creature.setX(creature.getX() + Creature.STEP_SIZE);
							
						}
						drawDungeonAndPlayer(graphics);
						player.getCamera().centerOnEntity(player);
						game.forceBs();
					}
				}
				
				//Reset turn taken
				player.setTookTurn(false);
				
				//Update coordinates that we've moved too
				for(Creature creature: creatures) {
					creature.setCoordinateX(creature.getNextCoordinateX());
					creature.setCoordinateY(creature.getNextCoordinateY());
				}
				player.handleNewTile();
			}
	}
	
	public void drawDungeonAndPlayer(Graphics graphics) {
		for(int x = -game.getRenderDistance(); x < dungeon.getData().getNumColumns() + game.getRenderDistance(); x++) {
			for(int y = -game.getRenderDistance(); y < dungeon.getData().getNumRows() + game.getRenderDistance(); y++) {
				
				//If it's off the board just visualize it as a wall
				if(x < 0 || x >= dungeon.getData().getNumColumns() || y < 0 || y >= dungeon.getData().getNumRows()) {
//					System.out.println("graphics?: " + graphics);
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
	
	public ArrayList<Creature> getCreatures() {
		return creatures;
	}

	public GameCamera getCamera() {
		return camera;
	}

	@Override
	public ArrayList<UIObject> getUIOBjects() {
		return null;
	}
}
