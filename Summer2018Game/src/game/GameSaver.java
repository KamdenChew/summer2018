package game;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class GameSaver {

	public static void saveGame(Game game, String fileName) {
		String filePath = "./res/saves/" + fileName;
		File file = new File(filePath);
		if(file.exists()) {
			System.out.print("Are you sure you want to overwrite " + fileName+ "? (y/n) ");
			String response = game.getScanner().nextLine();
			while(!(response.equals("y") || response.equals("n"))) {
				System.out.print("Are you sure you want to overwrite " + fileName+ "? (y/n) ");
				response = game.getScanner().nextLine();
			
			}
			//String response is now either "y" or "n"
			if(response.equals("n")) {
				return;
			} 
			
			//Getting past the previous if implies they want to overwrite the file
		}
		FileWriter output = null;
		
		try {
			output = new FileWriter(file, false);
			if(game.getPlayer() == null) {
				System.out.println("Player was null");
			}
			
			//First line is the positions of the player
			output.write(game.getPlayer().getCoordinateX() + " " + game.getPlayer().getCoordinateY() + "\n");
			
			
			if(State.getState().getData() != null && State.getState().getSeen() != null) {
				Array2D<Integer> data = State.getState().getData();
				
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
				output.write(game.getDifficulty() + "\n");
				
				//Sixth line is the player seen tile data
				for(Boolean val: State.getState().getSeen()) {
					output.write(val + " ");
				}
				output.write("\n");
				
				//Seventh line is score
				output.write(game.getScore() + "\n");
				
				//Eighth line is numPeacefulCompleted
				output.write(game.getNumPeacefulCompleted() + "\n");
				
				//Ninth line is numEasyCompleted
				output.write(game.getNumEasyCompleted() + "\n");
				
				//Tenth line is numMediumCompleted
				output.write(game.getNumMediumCompleted() + "\n");
				
				//Eleventh line is numHardCompleted
				output.write(game.getNumHardCompleted() + "\n");
				
				output.flush();
				output.close();
				
			} else {
				System.out.println("getData or getSeen was null...");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
