package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class GameLoader {

	/**
	 * Loads a game instance from a specified save file. If the file can't be found, 
	 * then System.exit(1) is called
	 *
	 * @param game the Game object for this running instance
	 * @param fileName String name for the save file to be loaded
	 */
	public static void loadGame(Game game, String fileName) {
		String filePath = "./res/saves/" + fileName;
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
		Scanner lineScanner = new Scanner(positionString);
		int x = Integer.parseInt(lineScanner.next());
		int y = Integer.parseInt(lineScanner.next());
		int currHealth = Integer.parseInt(lineScanner.next());
		String direction = lineScanner.next();
		lineScanner.close();
		
		//Second line is the Array2D's width
		int width = Integer.parseInt(scanner.nextLine());
		
		//Third line is the Array2D's height
		int height = Integer.parseInt(scanner.nextLine());
		
		//Fourth line is the tile data
		String tileDataString = scanner.nextLine();
		Integer[] tileData = new Integer[width * height];
		lineScanner = new Scanner(tileDataString);
		int i = 0;
		while(lineScanner.hasNext()) {
			tileData[i] = Integer.parseInt(lineScanner.next());
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
		while(lineScanner.hasNext()) {
			String val = lineScanner.next();
			if(val.equals("true")) {
				seenData[i] = true;
			} else if(val.equals("false")) {
				seenData[i] = false;
			}
			i++;
		}
		Array2D<Boolean> seen = new Array2D<Boolean>(height, width, seenData);
		lineScanner.close();
		
		//Seventh line is Enemies in the format X,Y,Health. If it's a TownState save, this line will say "NoEnemies"
		String enemyString = scanner.nextLine();
		
		//Eighth line is the current floor number. This will be -1 if it's a town state.
		int currFloor = Integer.parseInt(scanner.nextLine());

		//TownState if difficulty is -1
			
		State loadedState = null;
		
		if(difficulty == -1) {
			loadedState = new TownState(game, x, y, direction);
			
		//If difficulty falls in our defined dungeon difficulty bounds, then it's a dungeon state
		} else if(difficulty >= 0 && difficulty <= 3) {
			Dungeon dungeon = new Dungeon(game, x, y, currHealth, difficulty, data, seen, height, width, direction, currFloor);
			ArrayList<Enemy> enemies = new ArrayList<Enemy>();
			
			//If we have enemy data to read
			if(!enemyString.equals("")) {
				String[] enemyData;
				enemyData = enemyString.split(" ");
				
				String[] params;
				for(String enemy: enemyData) {
					params = enemy.split(",");
					int coordinateX = Integer.parseInt(params[0]);
					int coordinateY = Integer.parseInt(params[1]);
					int health = Integer.parseInt(params[2]);
					String enemyDirection = params[3];
					enemies.add(new Enemy(game, coordinateX * 50, coordinateY * 50, coordinateX, coordinateY, health, dungeon, enemyDirection));
				}
			}
			
			dungeon.setEnemies(enemies);
			dungeon.getPlayer().setEnemies(enemies);
			
			loadedState = new DungeonState(game, dungeon);
		}
		
		
		
		//Eighth line is score
		game.setScore(Integer.parseInt(scanner.nextLine()));
		
		//Ninth line is numPeacefulCompleted
		game.setNumPeacefulCompleted(Integer.parseInt(scanner.nextLine()));
		
		//Tenth line is numEasyCompleted
		game.setNumEasyCompleted(Integer.parseInt(scanner.nextLine()));
		
		//Eleventh line is numMediumCompleted
		game.setNumMediumCompleted(Integer.parseInt(scanner.nextLine()));
		
		//Twelfth line is numHardCompleted
		game.setNumHardCompleted(Integer.parseInt(scanner.nextLine()));
		
		game.setSaveName(fileName);
		
		if(loadedState != null) {
			State.setState(loadedState);
		} else {
			System.exit(1);
		}
	}
}
