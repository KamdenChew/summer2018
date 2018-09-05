package game;

import java.awt.Graphics;
import java.util.ArrayList;

public class RespawnState extends State {

	private UIImageButton respawnButton = new UIImageButton(game.getWidth() / 2 - 50, game.getHeight() / 2 - 25, 100, 50, Assets.respawn, Assets.respawnHover, new ClickListener(){

		@Override
		public void onClick() {
			if(game.isPrimaryWindow()) {
				State.setState(new TownState(game, 7, 7, "Down"));
			}
		}});
	
	public RespawnState(Game game) {
		super(game);
		game.setScore(game.getScore() - 100);
	}

	@Override
	public int getDifficulty() {
		return -1;
	}

	@Override
	public ArrayList<UIObject> getUIOBjects() {
		ArrayList<UIObject> uiObjects = new ArrayList<UIObject>();
		uiObjects.add(respawnButton);
		return uiObjects;
	}

	@Override
	public void tick() {
		respawnButton.tick();
		
	}

	@Override
	public void render(Graphics graphics) {
		respawnButton.render(graphics);
	}

}
