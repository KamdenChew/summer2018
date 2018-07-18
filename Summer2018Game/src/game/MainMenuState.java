package game;
import java.awt.Dimension;
import java.awt.Graphics;

public class MainMenuState extends State {
	
	private UIImageButton newGameButton = new UIImageButton(125, 100, 100, 50, Assets.newGame, Assets.newGameHover, new ClickListener(){

		@Override
		public void onClick() {
			State.setState(game.getTownState());
		}});
	
	private UIImageButton loadGameButton = new UIImageButton(125, 200, 100, 50, Assets.loadGame, Assets.loadGameHover, new ClickListener(){
		
		@Override
		public void onClick() {
			System.out.println("Load Game Clicked!");
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

}
