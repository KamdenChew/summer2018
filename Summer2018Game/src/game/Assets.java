package game;


import java.awt.image.BufferedImage;

public class Assets {
	
	//For future use if using spritesheet
//	private static final int WIDTH = 50;
//	private static final int HEIGHT = 50;
	
	public static BufferedImage dirt, stone, wall, empty, player;
	
	public static void init() {
		dirt = ImageLoader.loadImage("/textures/dirt.png");
		stone = ImageLoader.loadImage("/textures/stone.png");
		wall = ImageLoader.loadImage("/textures/wall.png");
		empty = ImageLoader.loadImage("/textures/empty.png");
		player = ImageLoader.loadImage("/textures/player.png");
	}
	
}
