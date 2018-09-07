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
	
	/**
	 * Constructs a new DungeonState
	 *
	 * @param game the Game object for this running instance
	 * @param difficulty an int representing the difficulty level of the Dungeon
	 * @param currFloor int representing the floor number that we are currently on
	 * 
	 */
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
	
	/**
	 * Constructs a new DungeonState
	 *
	 * @param game the Game object for this running instance
	 * @param dungeon the Dungeon that the player is in
	 * 
	 */
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
			    		for(int i = 0; i < creatures.size(); i++) {
			        		Creature creature = creatures.get(i);
			        		
			        		if(creature.isAttacking() && creature instanceof Enemy) {
			        			player.decreaseHealth(2);
			        		} else if(creature.isAttacking() && creature instanceof Player) {
			        			player.handleAttack();
			        		}
			        		creature.setAttacking(false);
			        		
			        		
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
			        	for(int i = 0; i < creatures.size(); i++) {
			        		Creature creature = creatures.get(i);
			        		
			        		//This creature is attacking 
			        		if(creature.isAttacking()) {
			        			//For the first half of the animation move it in the direction it's facing
			        			if(stepsTaken < 25) {
			        				if(creature.facingUp) {
			        					creature.setY(creature.getY() - Creature.STEP_SIZE);
			        				} else if(creature.facingDown) {
			        					creature.setY(creature.getY() + Creature.STEP_SIZE);
			        				} else if(creature.facingLeft) {
			        					creature.setX(creature.getX() - Creature.STEP_SIZE);
			        				} else if(creature.facingRight) {
			        					creature.setX(creature.getX() + Creature.STEP_SIZE);
			        				}
			        			
			        			//For the second half of the animation move it back
			        			} else {
			        				if(creature.facingUp) {
			        					creature.setY(creature.getY() + Creature.STEP_SIZE);
			        				} else if(creature.facingDown) {
			        					creature.setY(creature.getY() - Creature.STEP_SIZE);
			        				} else if(creature.facingLeft) {
			        					creature.setX(creature.getX() + Creature.STEP_SIZE);
			        				} else if(creature.facingRight) {
			        					creature.setX(creature.getX() - Creature.STEP_SIZE);
			        				}
			        			}
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
	
	/**
	 * Renders the dungeon grid to the graphics object it is passed
	 * 
	 * @param graphics the Graphics object to draw the Dungeon too
	 * 
	 */
	private void drawDungeon(Graphics graphics) {
		for(int x = player.getNextCoordinateX() - game.getRenderDistance() - 1; x < player.getNextCoordinateX() + game.getRenderDistance() + 2; x++) {
			for(int y = player.getNextCoordinateY() - game.getRenderDistance() - 1; y < player.getNextCoordinateY() + game.getRenderDistance() + 2; y++) { 
				
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
	}
	
	/**
	 * Renders the player to the graphics object it is passed
	 * 
	 * @param graphics the Graphics object to draw the Player too
	 * 
	 */
	private void drawPlayer(Graphics graphics) {
		player.render(graphics);
	}
	
	/**
	 * Renders the enemies of the dungeon to the graphics object it is passed
	 * 
	 * @param graphics the Graphics object to draw the enemies too
	 * 
	 */
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
	
	//Getters
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
	
	public Dungeon getDungeon() {
		return this.dungeon;
	}
	
	public int getDifficulty() {
		return difficulty;
	}

	public Player getPlayer() {
		return player;
	}
}
