package game;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.Timer;

public class DungeonState extends State{

	private Dungeon dungeon;
	private Player player;
	private ArrayList<Enemy> enemies;
	private int difficulty;
	private GameCamera camera;
	private ArrayList<Creature> creatures = new ArrayList<Creature>();
	
	public DungeonState(Game game, int difficulty, int currFloor) {
		super(game);
		this.difficulty = difficulty;
		this.dungeon = new Dungeon(game, difficulty);
		this.dungeon.setCurrFloor(currFloor);
		this.player = dungeon.getPlayer();
		this.enemies = dungeon.getEnemies();
		this.camera = player.getCamera();
		this.creatures.add(player);
		for(Enemy enemy: enemies) {
			creatures.add(enemy);
		}
	}
	
	public DungeonState(Game game, Dungeon dungeon) {
		super(game);
		this.difficulty = dungeon.getDifficulty();
		this.dungeon = dungeon;
		this.player = dungeon.getPlayer();
		game.setPlayer(this.player);
		this.enemies = dungeon.getEnemies();
		this.camera = player.getCamera();
		this.creatures.add(player);
		for(Enemy enemy: enemies) {
			this.creatures.add(enemy);
		}
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
	@Override
	public void tick() {
		this.player.tick();
		if(game.getKeyManager().keyJustPressed(KeyEvent.VK_M)) {
			State.setState(new MenuState(game, this));
		}
	}

	@Override
	public void render(final Graphics graphics) {
		//If the player is not taking a turn, just render as usual.
		if(!player.hasTakenTurn()) {
			drawDungeon(graphics);
			drawPlayer(graphics);
			drawEnemies(graphics);
			Text.drawString(graphics, "Floor " + dungeon.getCurrFloor() + "/" + dungeon.getNumFloors(), game.getRenderDistance() * 50 + 25, 50, true, Color.cyan, Fonts.font32);
		//Otherwise we know all creatures in the dungeon will be taking a turn
		} else {
			
			Timer timer = new Timer(5, new ActionListener() {
			    private int stepsTaken;

			    @Override
			    public void actionPerformed(ActionEvent e) {
			    	if (stepsTaken == 50) {
			            for(Creature creature: creatures) {
			            	
			            	if(creature instanceof Player) {
			        			//Reset turn taken
			        			game.getPlayer().setTookTurn(false);
			        		}
			        		
			        		//Update coordinates that we've moved too
			        		creature.setCoordinateX(creature.getNextCoordinateX());
			        		creature.setCoordinateY(creature.getNextCoordinateY());
			        		
			        		if(creature instanceof Player) {
			        			game.getPlayer().handleNewTile();
			        		}
			            }
			    		((Timer)e.getSource()).stop();
			        } else {
			        	//Step each of the creatures
			        	for(Creature creature: creatures) {
			        		
			        		if(creature.isAttacking()) {
//				    				drawDungeonAndPlayer(graphics);
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
			    			if(creature instanceof Player) {
			    				camera.centerOnEntity(creature);
			    			}
			        	}
			        	
			        	drawDungeon(graphics);
			        	drawPlayer(graphics);
			        	drawEnemies(graphics);
			        	Text.drawString(graphics, "Floor " + dungeon.getCurrFloor() + "/" + dungeon.getNumFloors(), game.getRenderDistance() * 50 + 25, 50, true, Color.cyan, Fonts.font32);
			        	game.forceBs();
			        }
			    	stepsTaken++;
			    }
			});
			timer.start();
			while(timer.isRunning()) {
				
			}
		}
	}
	
	private void drawDungeon(Graphics graphics) {
		for(int x = player.getNextCoordinateX() - game.getRenderDistance() - 1; x < player.getNextCoordinateX() + game.getRenderDistance() + 2; x++) {
			for(int y = player.getNextCoordinateY() - game.getRenderDistance() - 1; y < player.getNextCoordinateY() + game.getRenderDistance() + 2; y++) { 
				
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
	}
	
	private void drawPlayer(Graphics graphics) {
		player.render(graphics);
	}
	
	private void drawEnemies(Graphics graphics) {
		for(Enemy enemy: enemies) {
			
			//Optimize by only rendering if it will be visible to the user
			if(enemy.getNextCoordinateX() >= player.getNextCoordinateX() - game.getRenderDistance() - 2 &&
			   enemy.getNextCoordinateX() <= player.getNextCoordinateX() + game.getRenderDistance() + 2 &&
			   enemy.getNextCoordinateY() >= player.getNextCoordinateY() - game.getRenderDistance() - 2 &&
			   enemy.getNextCoordinateY() <= player.getNextCoordinateY() + game.getRenderDistance() + 2) {
				enemy.render(graphics);
			}
		}
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
