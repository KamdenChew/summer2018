package game;

import java.awt.Graphics;
import java.util.ArrayList;

public class ConfirmDungeonState extends State {

	private State prevState;
	private int difficulty;
	
	private UIImageButton continueButton = new UIImageButton(game.getWidth() / 2 - 50, game.getHeight() / 2 - 75, 100, 50, Assets.continueToDungeon, Assets.continueToDungeonHover, new ClickListener(){

		@Override
		public void onClick() {
			State.setState(new DungeonState(game, difficulty, 1));
		}});
	
	
	private UIImageButton closeMenuButton = new UIImageButton(game.getWidth() / 2 - 50, game.getHeight() / 2 + 25, 100, 50, Assets.closeMenu, Assets.closeMenuHover, new ClickListener(){

		@Override
		public void onClick() {
			State.setState(prevState);
		}});
	
	/**
	 * Constructs a new ConfirmDungeonState 
	 *
	 * @param game the Game object for this running instance
	 * @param prevState the previous state we were in, saved in case the user decides to close the menu, in which case we return the player to where they were
	 * @param difficulty the dungeon difficulty for the corresponding warp we have stepped on
	 */
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
