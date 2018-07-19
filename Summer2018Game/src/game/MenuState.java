package game;

import java.awt.Graphics;
import java.util.ArrayList;

public class MenuState extends State {

	private State prevState;
	
	private UIImageButton saveGameButton = new UIImageButton(125, 100, 100, 50, Assets.saveGame, Assets.saveGameHover, new ClickListener(){

		@Override
		public void onClick() {
			System.out.println("Save Game Clicked!");
		}});
	
	private UIImageButton closeMenuButton = new UIImageButton(125, 200, 100, 50, Assets.closeMenu, Assets.closeMenuHover, new ClickListener(){

		@Override
		public void onClick() {
			System.out.println("Close Menu Clicked!");
			State.setState(prevState);
		}});
	
	public MenuState(Game game, State prevState) {
		super(game);
		game.getMouseManager().addUIObject(saveGameButton);
		game.getMouseManager().addUIObject(closeMenuButton);
		this.prevState = prevState;
	}

	@Override
	public Array2D<Integer> getData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void tick() {
		saveGameButton.tick();
		closeMenuButton.tick();
	}

	@Override
	public void render(Graphics graphics) {
		saveGameButton.render(graphics);
		closeMenuButton.render(graphics);
	}

	@Override
	public ArrayList<UIObject> getUIOBjects() {
		ArrayList<UIObject> uiObjects = new ArrayList<UIObject>();
		uiObjects.add(saveGameButton);
		uiObjects.add(closeMenuButton);
		return uiObjects;
	}
}
