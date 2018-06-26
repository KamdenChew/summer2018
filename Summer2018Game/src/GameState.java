

import java.awt.Graphics;

public class GameState extends State{

	private Dungeon dungeon;
	private Array2D<Integer> data;
	
	public GameState(Dungeon dungeon) {
		this.dungeon = dungeon;
		this.data = dungeon.getData();
	}
	
	@Override
	public void update() {
		
	}

	@Override
	public void render(Graphics graphics) {
		int columns = data.getNumColumns();
		int rows = data.getNumRows();
		for(int x = 0; x < columns; x++) {
			for(int y = 0; y < rows; y++) {
				if(Math.abs(data.get(x, y)) == 1) {
					graphics.drawImage(Assets.wall, x * 50, y * 50, null);
				} else if(data.get(x, y) == 0) {
					graphics.drawImage(Assets.stone, x * 50, y * 50, null);
				} else if(data.get(x, y) == 2) {
					graphics.drawImage(Assets.dirt, x * 50, y * 50, null);
				}
			}
		}
	}

}
