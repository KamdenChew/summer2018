package game;
import java.awt.Graphics;
import java.util.ArrayList;

public abstract class State {
	
	private static State currentState = null;
	
	protected static Game game;
	
	public State(Game game) {
		this.game = game;
	}
	
	public static void setState(State state) {
		currentState = state;
		if(state instanceof DungeonState) {
			//TODO don't hardcode difficulty, set for debugging
			System.out.println("We're in a dungeon now");
			game.setDifficulty(1);
			game.setPlayer(((DungeonState) state).getPlayer());
		} else if(state instanceof TownState) {
			System.out.println("We're in a town now");
			game.setDifficulty(-1);
			game.setPlayer(new Player(game, 7,7));
		}
		game.getMouseManager().removeUIObjects();
		if(state.getUIOBjects() != null) {
			for(UIObject object: state.getUIOBjects()) {
				game.getMouseManager().addUIObject(object);
			}
		}
	}
	
	public static State getState() {
		return currentState;
	}
	
	public abstract ArrayList<UIObject> getUIOBjects();	
	
	public abstract Array2D<Integer> getData();
	
	public abstract Array2D<Boolean> getSeen();
	
	public abstract void tick();
	
	public abstract void render(Graphics graphics);
}
