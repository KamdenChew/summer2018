package game;
import java.awt.Graphics;
import java.util.ArrayList;

public class MainMenuState extends State {
	
	private UIImageButton newGameButton = new UIImageButton(game.getWidth() / 2 - 50, game.getHeight() / 2 - 75, 100, 50, Assets.newGame, Assets.newGameHover, new ClickListener(){

		@Override
		public void onClick() {
			if(game.isPrimaryWindow()) {
				State.setState(new TownState(game, 7, 7, "Down"));
			}
		}});
	
	private UIImageButton loadGameButton = new UIImageButton(game.getWidth() / 2 - 50, game.getHeight() / 2 + 25, 100, 50, Assets.loadGame, Assets.loadGameHover, new ClickListener(){
		
		@Override
		public void onClick() {
			if(game.isPrimaryWindow()){
				game.setPrimaryWindow(false);
				LoadWindow loadWindow = new LoadWindow(game);
				loadWindow.show();
			}
		}});
	
	/**
	 * Constructs a new MainMenuState
	 *
	 * @param game the Game object for this running instance
	 */
	public MainMenuState(Game game) {
		super(game);
	}
	
	@Override
	public void tick() {
		newGameButton.tick();
		loadGameButton.tick();
	}

	@Override
	public void render(Graphics graphics) {
		newGameButton.render(graphics);
		loadGameButton.render(graphics);
	}

	@Override
	public ArrayList<UIObject> getUIOBjects() {
		ArrayList<UIObject> uiObjects = new ArrayList<UIObject>();
		uiObjects.add(newGameButton);
		uiObjects.add(loadGameButton);
		return uiObjects;
	}

	@Override
	public int getDifficulty() {
		return -1;
	}
}
