package game;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class TownState extends State{
	
	private static final int RENDER_DISTANCE = 3;
	private Array2D<Integer> data = new Array2D<Integer>(6, 6, new Integer[]{1,1,1,1,1,1,1,0,0,0,0,1,1,0,0,0,0,1,1,0,0,0,0,1,1,0,0,0,0,1,1,1,1,1,1,1});
	private Player player;
	
	
	public TownState(Game game) {
		super(game);
		this.player = new Player(game, 2, 2);
	}
	
	@Override
	public Array2D<Integer> getData() {
		return this.data;
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
	
//Code for rendering a map
//	//If we've seen it, go ahead and render it normally
//	if(dungeon.getSeen().get(x, y)) {
//		if(Math.abs(val) == 1) {
//			graphics.drawImage(Assets.wall, (x - RENDER_DISTANCE) * 50, (y - RENDER_DISTANCE) * 50, null);
//		} else if(val == 0) {
//			graphics.drawImage(Assets.stone, (x - RENDER_DISTANCE) * 50, (y - RENDER_DISTANCE) * 50, null);
//		} else if(val != -2){//if(data.get(x, y) == 2) {
//			graphics.drawImage(Assets.dirt, (x - RENDER_DISTANCE) * 50, (y - RENDER_DISTANCE) * 50, null);
//		} 
//		
//	//If we haven't seen it, render darkness
//	} else {
//		graphics.drawImage(Assets.empty, (x - RENDER_DISTANCE) * 50, (y - RENDER_DISTANCE) * 50, null);
//	}

}
