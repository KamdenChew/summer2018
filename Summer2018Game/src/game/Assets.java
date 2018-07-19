package game;


import java.awt.image.BufferedImage;

public class Assets {
	
	//For future use if using spritesheet
//	private static final int WIDTH = 50;
//	private static final int HEIGHT = 50;
	
	public static BufferedImage dirt, stone, wall, empty, player, newGame, newGameHover, loadGame, loadGameHover, saveGame, saveGameHover, closeMenu, closeMenuHover;
	
	public static void init() {
		dirt = ImageLoader.loadImage("/textures/dirt.png");
		stone = ImageLoader.loadImage("/textures/stone.png");
		wall = ImageLoader.loadImage("/textures/wall.png");
		empty = ImageLoader.loadImage("/textures/empty.png");
		player = ImageLoader.loadImage("/textures/player.png");
		newGame = ImageLoader.loadImage("/textures/NewGame.png");
		newGameHover = ImageLoader.loadImage("/textures/NewGameHover.png");
		loadGame = ImageLoader.loadImage("/textures/LoadGame.png");
		loadGameHover = ImageLoader.loadImage("/textures/LoadGameHover.png");
		saveGame = ImageLoader.loadImage("/textures/SaveGame.png");
		saveGameHover = ImageLoader.loadImage("/textures/SaveGameHover.png");
		closeMenu = ImageLoader.loadImage("/textures/CloseMenu.png");
		closeMenuHover = ImageLoader.loadImage("/textures/CloseMenuHover.png");
	}
	
}
