package game;

import java.awt.Font;

public class Fonts {

	//Static Fonts to access in rendering methods of other classes
	public static Font font32;
	
	/**
	 * Initializes static Font assets for rendering
	 */
	public static void init() {
		font32 = FontLoader.loadFont("res/fonts/slkscr.ttf", 32);
	}
}
