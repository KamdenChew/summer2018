package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GameLoader {

	public static void loadGame(Game game, String filePath) {
		File file = new File(filePath);
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		//First line is the positions of the player
		String positionString = scanner.nextLine();
		int x = Integer.parseInt(positionString.substring(0,1));
		int y = Integer.parseInt(positionString.substring(2,3));
		System.out.println("X: " + x);
		System.out.println("Y: " + y);
		
		//Second line is the Array2D's width
		int width = Integer.parseInt(scanner.nextLine());
		
		//Third line is the Array2D's height
		int height = Integer.parseInt(scanner.nextLine());
		
		//Fourth line is the tile data
		String tileDataString = scanner.nextLine();
		Scanner lineScanner = new Scanner(tileDataString);
		Integer[] tileData = new Integer[width * height];
		lineScanner.close();
		lineScanner = new Scanner(tileDataString);
		int i = 0;
		while(scanner.hasNext()) {
			tileData[i] = Integer.parseInt(scanner.next());
			i++;
		}
		Array2D<Integer> data = new Array2D<Integer>(height, width, tileData);
		lineScanner.close();
		
		//Fifth line is the difficulty (-1 if TownState)
		int difficulty = Integer.parseInt(scanner.nextLine());
		
		//Sixth line is the player seen tile data
		String seenDataString = scanner.nextLine();
		lineScanner = new Scanner(seenDataString);
		Boolean[] seenData = new Boolean[width * height];
		i = 0;
		while(scanner.hasNext()) {
			String val = scanner.next();
			if(val == "T") {
				seenData[i] = true;
			} else if(val == "F") {
				seenData[i] = false;
			}
			i++;
		}
		Array2D<Boolean> seen = new Array2D<Boolean>(height, width, seenData);
		lineScanner.close();

		//TownState if difficulty is -1
			
		State loadedState = null;
		
		if(difficulty == -1) {
			loadedState = new MenuState(game, new TownState(game, x, y));
			System.out.println("Town State loaded");
			
		//If difficulty falls in our defined dungeon difficulty bounds, then it's a dungeon state
		} else if(difficulty >= 0 && difficulty <= 3) {
			loadedState = new DungeonState(game, x, y, difficulty, data, seen, height, width);
			System.out.println("Dungeon State loaded");
		}
		
		//Seventh line is score
		game.setScore(Integer.parseInt(scanner.nextLine()));
		
		//Eighth line is numPeacefulCompleted
		game.setNumPeacefulCompleted(Integer.parseInt(scanner.nextLine()));
		
		//Ninth line is numEasyCompleted
		game.setNumEasyCompleted(Integer.parseInt(scanner.nextLine()));
		
		//Tenth line is numMediumCompleted
		game.setNumMediumCompleted(Integer.parseInt(scanner.nextLine()));
		
		//Eleventh line is numHardCompleted
		game.setNumHardCompleted(Integer.parseInt(scanner.nextLine()));
		
		if(loadedState != null) {
			State.setState(loadedState);
		} else {
			System.exit(1);
		}
	}
}
