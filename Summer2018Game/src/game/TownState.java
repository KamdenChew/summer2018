package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.Timer;

public class TownState extends State{
	
	private Array2D<Integer> data = new Array2D<Integer>(15, 15, new Integer[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1, 1,0,0,0,0,0,0,0,0,0,0,0,0,0,1, 1,0,0,0,0,0,0,0,0,0,0,0,0,0,1, 1,0,0,0,0,0,0,0,0,0,0,0,0,0,1, 1,0,0,0,0,0,0,0,0,0,0,0,0,0,1, 1,0,0,0,0,0,0,0,0,0,0,0,0,0,1, 1,0,0,0,0,0,0,0,0,0,0,0,0,0,1, 1,0,0,0,0,0,0,0,0,0,0,0,0,0,1, 1,0,0,0,0,0,0,0,0,0,0,0,0,0,1, 1,0,0,0,0,0,0,0,0,0,0,0,0,0,1, 1,0,0,0,0,0,0,0,0,0,0,0,0,0,1, 1,0,0,0,0,0,0,0,0,0,0,0,0,0,1, 1,0,0,0,0,0,0,0,0,0,0,0,0,0,1, 1,-3,0,0,0,-4,0,0,0,-5,0,0,0,-6,1, 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1});
	private Player player;
	private GameCamera camera;
	private ArrayList<Creature> creatures;
	
	public TownState(Game game, int x, int y, String direction) {
		super(game);
		if(x < 1 || x >= data.getNumColumns() - 1 || y < 1 || y > data.getNumRows() - 1) {
			throw new IllegalArgumentException("Player position must fall between x: 1-" + (data.getNumColumns() - 1) + " y: 1-" + (data.getNumRows() - 1));
		}
		this.player = new Player(game, x, y, 50, false, null, direction);
		game.setPlayer(this.player);
		this.camera = player.getCamera();
		this.creatures = new ArrayList<Creature>();
		this.creatures.add(player);
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
		
		if(game.getKeyManager().keyJustPressed(KeyEvent.VK_M)) {
			State.setState(new MenuState(game, this));
		}
	}

	@Override
	public void render(final Graphics graphics) {
		//If the player is not taking a turn, just render as usual.
		if(!player.hasTakenTurn()) {
			drawTown(graphics);
			drawPlayer(graphics);
			Text.drawString(graphics, "Score: " + game.getScore(), game.getRenderDistance() * 50 + 25, 50, true, Color.cyan, Fonts.font32);
			
		//Otherwise we know all creatures in the dungeon will be taking a turn
		} else {
			
			Timer timer = new Timer(5, new ActionListener() {
			    private int stepsTaken;

			    @Override
			    public void actionPerformed(ActionEvent e) {
			    	if (stepsTaken == 50) {
			    		for(int i = 0; i < creatures.size(); i++) {
			        		Creature creature = creatures.get(i);
			        		
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
			        		
			        		if(creature.isAttacking()) {
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
			        	
			        	drawTown(graphics);
			        	drawPlayer(graphics);
			        	Text.drawString(graphics, "Score: " + game.getScore(), game.getRenderDistance() * 50 + 25, 50, true, Color.cyan, Fonts.font32);
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
	
	private void drawTown(Graphics graphics) {
		for(int x = player.getNextCoordinateX() - game.getRenderDistance() - 1; x < player.getNextCoordinateX() + game.getRenderDistance() + 2; x++) {
			for(int y = player.getNextCoordinateY() - game.getRenderDistance() - 1; y < player.getNextCoordinateY() + game.getRenderDistance() + 2; y++) { 
				
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
	}
	
	private void drawPlayer(Graphics graphics) {
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
