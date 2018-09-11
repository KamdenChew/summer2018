package game;
import java.awt.Graphics;
import java.util.ArrayList;

public abstract class State {
	
	private static State currentState = null;
	
	protected static Game game;
	
	/**
	 * Constructs a new State
	 * @param passedGame the Game object for this running instance
	 */
	public State(Game passedGame) {
		game = passedGame;
	}
	
	/**
	 * Sets the currentState to the given State, and forces a UI update to any UIObjects in the new given State
	 * 
	 * @param state the State to switch too
	 */
	public static void setState(State state) {
		
		currentState = state;
		
		if(state instanceof DungeonState) {
			game.setPlayer(((DungeonState) state).getPlayer());
		} else if(state instanceof TownState) {
			game.setPlayer(((TownState) state).getPlayer());
		}
		
		if(state.getUIOBjects() != null) {
			for(UIObject object: state.getUIOBjects()) {
				object.updateHovering(game.getMouseManager().mouseX, game.getMouseManager().mouseY);
			}
		}
	}
	
	/**
	 * Returns an int representing the difficulty. Intended to be used for saving, specifically for Dungeon classification
	 *  
	 *  @return difficulty the difficulty of the state that is being saved.
	 */
	public abstract int getDifficulty();
	
	/**
	 * Returns the current State
	 */
	public static State getState() {
		return currentState;
	}
	
	/**
	 * Returns whether or not the current State is a DungeonState
	 */
	public boolean isDungeonState() {
		if(this instanceof DungeonState) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns whether or not the current State is a TownState
	 */
	public boolean isTownState() {
		if(this instanceof TownState) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns whether or not the current State is a MenuState
	 */
	public boolean isMenuState() {
		if(this instanceof MenuState) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns all the UIObjects associated with this State
	 */
	public abstract ArrayList<UIObject> getUIOBjects();	
	
	/**
	 * Processes and updates this State's variables
	 */
	public abstract void tick();
	
	/**
	 * Renders this State to the specified Graphics object
	 * 
	 * @param graphics the Graphics object to render this State too
	 */
	public abstract void render(Graphics graphics);
}
