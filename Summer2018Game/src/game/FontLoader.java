package game;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

public class FontLoader {
	
	/**
	 * Loads a Font from a specified file path with a specified size
	 * If the specified font file can't be found, then System.exit(1) is called
	 * 
	 * @param path String for the file path to the Font file
	 * @param size float representing the Font size to be loaded
	 * @return the Font loaded from the specified file path and with the specified size
	 */
	public static Font loadFont(String path, float size){
		try {
			return Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(Font.PLAIN, size);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

}

