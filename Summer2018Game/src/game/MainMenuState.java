package game;
import java.awt.Dimension;
import java.awt.Graphics;

public class MainMenuState extends State {
	
	private UIImageButton startButton = new UIImageButton(200, 225, 100, 50, Assets.newGame, Assets.newGameHover, new ClickListener(){

		@Override
		public void onClick() {
			State.setState(game.getGameState());
			int width = game.dungeon.getData().getNumColumns() * 50;
			int height = game.dungeon.getData().getNumRows() * 50;
			game.getDisplay().getFrame().setSize(width, height);
			System.out.println(new CoordinatePair(game.getDisplay().getFrame().getWidth(), game.getDisplay().getFrame().getHeight()));
			//TODO make sure display size is correct
		}});
	
	public MainMenuState(Game game) {
		super(game);
		this.game.getMouseManager().addUIObject(startButton);
	}
	
	@Override
	public void tick() {
		startButton.tick();
	}

	@Override
	public void render(Graphics graphics) {
		startButton.render(graphics);
	}

}
