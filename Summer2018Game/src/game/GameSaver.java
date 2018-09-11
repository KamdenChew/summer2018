package game;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GameSaver {

	/**
	 * Saves a game instance to a specified save file
	 *
	 *@throws IllegalStateException if called when the game is not in a menu state
	 * @param game the Game object for this running instance
	 * @param fileName String name for the file to be saved too
	 */
	public static void saveGame(Game game, String fileName) {
		String filePath = "./res/saves/" + fileName;
		File file = new File(filePath);
		
		FileWriter output = null;
		
		try {
			output = new FileWriter(file, false);
			if(!State.getState().isMenuState()) {
				output.close();
				throw new IllegalStateException("Attempted to save from an illegal state.");
			}
			
			//We are in a menu state
			
			//First line is the positions of the player
			output.write(game.getPlayer().getCoordinateX() + " " + game.getPlayer().getCoordinateY() + " " + game.getPlayer().getHealth() + " " + game.getPlayer().getDirectionFacing() + "\n");
			
			MenuState menuState = (MenuState) State.getState();
			
			Array2D<Integer> data = menuState.getData();
			
			//Second line is the Array2D's width
			output.write(data.getNumColumns() + "\n");
			
			//Third line is the Array2D's height
			output.write(data.getNumRows() + "\n");
			
			//Fourth line is the tile data
			for(Integer val: data) {
				output.write(val + " ");
			}
			output.write("\n");
			
			//Fifth line is the difficulty (-1 if TownState)
			int difficulty = State.getState().getDifficulty();
			output.write(difficulty + "\n");
			
			//Sixth line is the player seen tile data
			Array2D<Boolean> seen = menuState.getSeen();
			
			for(Boolean val: seen) {
				output.write(val + " ");
			}
			output.write("\n");
			
			//Seventh line is Enemies in the format coordinateX,coordinateY,Health. If it's a TownState save, this line will say "NoEnemies"
			//Eighth line is the current floor number. This will be -1 if it's a town state.
			
			//Town State if difficulty is -1
			if(difficulty == -1) {
				output.write("NoEnemies");
				output.write("\n");
				
				output.write(-1 + "\n");
			
			//Otherwise saving a Dungeon State
			} else {
				DungeonState dungeonState = (DungeonState) menuState.getPrevState();
				Dungeon dungeon = dungeonState.getDungeon();
				ArrayList<Enemy> enemies = dungeon.getEnemies();
				for(Enemy enemy: enemies) {
					output.write(enemy.getCoordinateX() + "," + enemy.getCoordinateY() + "," + enemy.getHealth() + "," + enemy.getDirectionFacing() + " ");
				}
				
				output.write("\n");
				
				output.write(dungeon.getCurrFloor() + "\n");
			}
			
			//Ninth line is score
			output.write(game.getScore() + "\n");
			
			//Tenth line is numPeacefulCompleted
			output.write(game.getNumPeacefulCompleted() + "\n");
			
			//Eleventh line is numEasyCompleted
			output.write(game.getNumEasyCompleted() + "\n");
			
			//Twelfth line is numMediumCompleted
			output.write(game.getNumMediumCompleted() + "\n");
			
			//Thirteenth line is numHardCompleted
			output.write(game.getNumHardCompleted() + "\n");
			
			output.flush();
			output.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
