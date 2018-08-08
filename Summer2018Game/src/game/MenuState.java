package game;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuState extends State {

	private State prevState;
	
	private UIImageButton saveGameButton = new UIImageButton(game.getWidth() / 2 - 50, game.getHeight() / 2 - 75, 100, 50, Assets.saveGame, Assets.saveGameHover, new ClickListener(){

		@Override
		public void onClick() {
			if(game.isPrimaryWindow()) {
				System.out.println("Save Game Clicked!");
				game.setPrimaryWindow(false);
				SaveWindow saveWindow = new SaveWindow(game);
//				System.out.print("Please enter a name for your save file: ");
//				String fileName = game.getScanner().nextLine();
//				GameSaver.saveGame(game, fileName);
			}
		}});
	
	private UIImageButton closeMenuButton = new UIImageButton(game.getWidth() / 2 - 50, game.getHeight() / 2 + 25, 100, 50, Assets.closeMenu, Assets.closeMenuHover, new ClickListener(){

		@Override
		public void onClick() {
			if(game.isPrimaryWindow()) {
				System.out.println("Close Menu Clicked!");
				State.setState(prevState);
			}
		}});
	
	public MenuState(Game game, State prevState) {
		super(game);
		game.getMouseManager().addUIObject(saveGameButton);
		game.getMouseManager().addUIObject(closeMenuButton);
		this.prevState = prevState;
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
	
	public Array2D<Integer> getData() {
		if(prevState.isDungeonState()) {
			DungeonState dungeonState = (DungeonState) prevState;
			return dungeonState.getDungeon().getData();
		} else {
			TownState townState = (TownState) prevState;
			return townState.getData();
		}
	}
	
	public State getPrevState() {
		return prevState;
	}

	public Array2D<Boolean> getSeen() {
		if(prevState.isDungeonState()) {
			DungeonState dungeonState = (DungeonState) prevState;
			return dungeonState.getDungeon().getSeen();
		} else {
			TownState townState = (TownState) prevState;
			return townState.getSeen();
		}
	}

	@Override
	public int getDifficulty() {
		return prevState.getDifficulty();
	}
}
