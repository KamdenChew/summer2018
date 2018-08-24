package game;

import java.awt.Font;

public class Fonts {

	public static Font font32;
	
	public static void init() {
		font32 = FontLoader.loadFont("res/fonts/slkscr.ttf", 32);
	}
}
