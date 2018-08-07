package game;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class GameSaver {

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
			output.write(game.getPlayer().getCoordinateX() + " " + game.getPlayer().getCoordinateY() + "\n");
			
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
			output.write(State.getState().getDifficulty() + "\n");
			
			//Sixth line is the player seen tile data
			Array2D<Boolean> seen = menuState.getSeen();
			
			for(Boolean val: seen) {
				output.write(val + " ");
			}
			output.write("\n");
			
			//Seventh line is Enemies in the format X,Y,Health. If it's a TownState save, this line will say "NoEnemies"
			//TODO Save enemies
			
			//Eighth line is score
			output.write(game.getScore() + "\n");
			
			//Ninth line is numPeacefulCompleted
			output.write(game.getNumPeacefulCompleted() + "\n");
			
			//Tenth line is numEasyCompleted
			output.write(game.getNumEasyCompleted() + "\n");
			
			//Eleventh line is numMediumCompleted
			output.write(game.getNumMediumCompleted() + "\n");
			
			//Twelfth line is numHardCompleted
			output.write(game.getNumHardCompleted() + "\n");
			
			output.flush();
			output.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
