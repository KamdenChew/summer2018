package game;

import java.awt.Graphics;
import java.util.ArrayList;

public class ConfirmTownState extends State {

	private State prevState;
	private int difficulty;
	
	private UIImageButton continueButton = new UIImageButton(game.getWidth() / 2 - 50, game.getHeight() / 2 - 75, 100, 50, Assets.exitToTown, Assets.exitToTownHover, new ClickListener(){

		@Override
		public void onClick() {
			TownState newState = null;
			
			if(difficulty == 0) {
				game.setNumPeacefulCompleted(game.getNumPeacefulCompleted() + 1);
				game.setScore(game.getScore() + 5);
				newState = new TownState(game, 1, 13, "Up");
			} else if(difficulty == 1) {
				game.setNumEasyCompleted(game.getNumEasyCompleted() + 1);
				game.setScore(game.getScore() + 20);
				newState = new TownState(game, 5, 13, "Up");
			} else if(difficulty == 2) {
				game.setNumMediumCompleted(game.getNumMediumCompleted() + 1);
				game.setScore(game.getScore() + 50);
				newState = new TownState(game, 9, 13, "Up");
			} else if(difficulty == 3) {
				game.setNumHardCompleted(game.getNumHardCompleted() + 1);
				game.setScore(game.getScore() + 200);
				newState = new TownState(game, 13, 13, "Up");
			}
			if(newState != null) {
				State.setState(newState);
			} else {
				System.exit(1);
			}
			
		}});
	
	private UIImageButton closeMenuButton = new UIImageButton(game.getWidth() / 2 - 50, game.getHeight() / 2 + 25, 100, 50, Assets.closeMenu, Assets.closeMenuHover, new ClickListener(){

		@Override
		public void onClick() {
			State.setState(prevState);
		}});
	
	/**
	 * Constructs a new ConfirmTownState
	 *
	 * @param game the Game object for this running instance
	 * @param prevState the previous state we were in, saved in case the user decides to close the menu, in which case we return the player to where they were
	 * @param difficulty the dungeon difficulty for the dungeon we are about to possibly exit from
	 */
	public ConfirmTownState(Game game, State prevState, int difficulty) {
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
