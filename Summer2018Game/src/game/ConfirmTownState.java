package game;

import java.awt.Graphics;
import java.util.ArrayList;

public class ConfirmTownState extends State {

	private State prevState;
	private int difficulty;
	
	private UIImageButton continueButton = new UIImageButton(game.getWidth() / 2 - 50, game.getHeight() / 2 - 75, 100, 50, Assets.exitToTown, Assets.exitToTownHover, new ClickListener(){

		@Override
		public void onClick() {
			System.out.println("Continue to Town Clicked!");
			TownState newState = null;
			
			if(difficulty == 0) {
				game.setNumPeacefulCompleted(game.getNumPeacefulCompleted() + 1);
				newState = new TownState(game, 1, 13);
			} else if(difficulty == 1) {
				game.setNumEasyCompleted(game.getNumEasyCompleted() + 1);
				newState = new TownState(game, 5, 13);
			} else if(difficulty == 2) {
				game.setNumMediumCompleted(game.getNumMediumCompleted() + 1);
				newState = new TownState(game, 9, 13);
			} else if(difficulty == 3) {
				game.setNumHardCompleted(game.getNumHardCompleted() + 1);
				newState = new TownState(game, 13, 13);
			}
			if(newState != null) {
				State.setState(newState);
			} else {
				System.exit(1);
			}
			
		}});
	
	
	//TODO add assets for "Stay in Dungeon" 
	private UIImageButton closeMenuButton = new UIImageButton(game.getWidth() / 2 - 50, game.getHeight() / 2 + 25, 100, 50, Assets.closeMenu, Assets.closeMenuHover, new ClickListener(){

		@Override
		public void onClick() {
			System.out.println("Close Menu Clicked!");
			State.setState(prevState);
		}});
	
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
	public Array2D<Integer> getData() {
		return null;
	}

	@Override
	public Array2D<Boolean> getSeen() {
		return null;
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

}
