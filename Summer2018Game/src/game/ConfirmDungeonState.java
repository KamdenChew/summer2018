package game;

import java.awt.Graphics;
import java.util.ArrayList;

public class ConfirmDungeonState extends State {

	private State prevState;
	private int difficulty;
	
	private UIImageButton continueButton = new UIImageButton(game.getWidth() / 2 - 50, game.getHeight() / 2 - 75, 100, 50, Assets.continueToDungeon, Assets.continueToDungeonHover, new ClickListener(){

		@Override
		public void onClick() {
			System.out.println("Continue to Dungeon Clicked!");
			State.setState(new DungeonState(game, difficulty, 1));
		}});
	
	
	//TODO add assets for "Stay in Town" 
	private UIImageButton closeMenuButton = new UIImageButton(game.getWidth() / 2 - 50, game.getHeight() / 2 + 25, 100, 50, Assets.closeMenu, Assets.closeMenuHover, new ClickListener(){

		@Override
		public void onClick() {
			System.out.println("Close Menu Clicked!");
			State.setState(prevState);
		}});
	
	public ConfirmDungeonState(Game game, State prevState, int difficulty) {
		super(game);
		this.prevState = prevState;
		this.difficulty = difficulty;
	}

	@Override
	public ArrayList<UIObject> getUIOBjects() {
		ArrayList<UIObject> uiObjects = new ArrayList<UIObject>();
		uiObjects.add(continueButton);
		uiObjects.add(closeMenuButton);
		return uiObjects;
	}

	@Override
	public void tick() {
		continueButton.tick();
		closeMenuButton.tick();
	}

	@Override
	public void render(Graphics graphics) {
		continueButton.render(graphics);
		closeMenuButton.render(graphics);
	}

	@Override
	public int getDifficulty() {
		return this.difficulty;
	}

}
