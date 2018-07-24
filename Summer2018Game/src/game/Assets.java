package game;


import java.awt.image.BufferedImage;

public class Assets {
	
	//For future use if using spritesheet
//	private static final int WIDTH = 50;
//	private static final int HEIGHT = 50;
	
	public static BufferedImage dirt, stone, wall, empty, exit, player, newGame, newGameHover, loadGame, loadGameHover, saveGame, saveGameHover, 
								closeMenu, closeMenuHover, peacefulWarp, easyWarp, mediumWarp, hardWarp, continueToDungeon, continueToDungeonHover,
								exitToTown, exitToTownHover;
	
	public static void init() {
		dirt = ImageLoader.loadImage("/textures/Dirt.png");
		stone = ImageLoader.loadImage("/textures/Stone.png");
		wall = ImageLoader.loadImage("/textures/Wall.png");
		empty = ImageLoader.loadImage("/textures/Empty.png");
		exit = ImageLoader.loadImage("/textures/Exit.png");
		player = ImageLoader.loadImage("/textures/Player.png");
		newGame = ImageLoader.loadImage("/textures/NewGame.png");
		newGameHover = ImageLoader.loadImage("/textures/NewGameHover.png");
		loadGame = ImageLoader.loadImage("/textures/LoadGame.png");
		loadGameHover = ImageLoader.loadImage("/textures/LoadGameHover.png");
		saveGame = ImageLoader.loadImage("/textures/SaveGame.png");
		saveGameHover = ImageLoader.loadImage("/textures/SaveGameHover.png");
		closeMenu = ImageLoader.loadImage("/textures/CloseMenu.png");
		closeMenuHover = ImageLoader.loadImage("/textures/CloseMenuHover.png");
		peacefulWarp = ImageLoader.loadImage("/textures/PeacefulWarp.png");
		easyWarp = ImageLoader.loadImage("/textures/EasyWarp.png");
		mediumWarp = ImageLoader.loadImage("/textures/MediumWarp.png");
		hardWarp = ImageLoader.loadImage("/textures/HardWarp.png");
		continueToDungeon = ImageLoader.loadImage("/textures/ContinueToDungeon.png");
		continueToDungeonHover = ImageLoader.loadImage("/textures/ContinueToDungeonHover.png");
		exitToTown = ImageLoader.loadImage("/textures/ExitToTown.png");
		exitToTownHover = ImageLoader.loadImage("/textures/ExitToTownHover.png");
	}
	
}
