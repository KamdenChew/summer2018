package game;
import java.awt.Graphics;
import java.io.File;
import java.util.ArrayList;

public class MainMenuState extends State {
	
	private UIImageButton newGameButton = new UIImageButton(game.getWidth() / 2 - 50, game.getHeight() / 2 - 75, 100, 50, Assets.newGame, Assets.newGameHover, new ClickListener(){

		@Override
		public void onClick() {
			State.setState(game.getTownState());
		}});
	
	private UIImageButton loadGameButton = new UIImageButton(game.getWidth() / 2 - 50, game.getHeight() / 2 + 25, 100, 50, Assets.loadGame, Assets.loadGameHover, new ClickListener(){
		
		@Override
		public void onClick() {
			System.out.println("Load Game Clicked!");
			
			//TODO remove console input to program and replace with UI
			System.out.print("Enter a saved game name: ");
			String fileName = game.getScanner().nextLine();
			File file = new File("./res/saves/" + fileName);
			while(!file.exists()) {
				System.out.print("Sorry, file not found. Try again? (y/n) ");
				String tryAgain =game.getScanner().nextLine();
				while(!(tryAgain.equals("y") || tryAgain.equals("n"))) {
					System.out.print("Sorry, file not found. Try again? (y/n) ");
					tryAgain = game.getScanner().nextLine();
				}
				
				//String response is now either "y" or "n"
				if(tryAgain.equals("n")) {
					return;
				}
				
				//Getting past the previous if implies they want to try again
				System.out.print("Enter a saved game name: ");
				fileName = game.getScanner().nextLine();
				file = new File("./res/saves/" + fileName);
			}
			
			
			GameLoader.loadGame(game, fileName);
		}});
	
	public MainMenuState(Game game) {
		super(game);
		this.game.getMouseManager().addUIObject(newGameButton);
		this.game.getMouseManager().addUIObject(loadGameButton);
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
	public Array2D<Integer> getData() {
		return null;
	}

	@Override
	public ArrayList<UIObject> getUIOBjects() {
		ArrayList<UIObject> uiObjects = new ArrayList<UIObject>();
		uiObjects.add(newGameButton);
		uiObjects.add(loadGameButton);
		return uiObjects;
	}

	@Override
	public Array2D<Boolean> getSeen() {
		return null;
	}
}
