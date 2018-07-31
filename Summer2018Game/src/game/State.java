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
			game.setPlayer(((DungeonState) state).getPlayer());
		} else if(state instanceof TownState) {
			game.setPlayer(((TownState) state).getPlayer());
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
	
	/**
	 * Returns an int representing the difficulty. Intended to be used for saving
	 *  
	 *  @return difficulty the difficulty of the state that is being saved.
	 */
	public abstract int getDifficulty();
	
	public abstract ArrayList<UIObject> getUIOBjects();	
	
	public abstract Array2D<Integer> getData();
	
	public abstract Array2D<Boolean> getSeen();
	
	public abstract void tick();
	
	public abstract void render(Graphics graphics);
}
