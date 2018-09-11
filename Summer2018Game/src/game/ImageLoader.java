package game;


import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader {
	
	/**
	 * Loads a BufferedImage from a specified file path.
	 * If the specified Image file can't be found, then System.exit(1) is called
	 * 
	 * @param path String for the file path to the Font file
	 * @return the BufferedImage loaded from the specified file path
	 */
	public static BufferedImage loadImage(String path) {
		try {
			return ImageIO.read(ImageLoader.class.getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}
}
