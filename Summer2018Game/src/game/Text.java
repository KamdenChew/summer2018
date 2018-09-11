package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class Text {
	
	/**
	 * Renders a given text in a given font to a given Graphics object at a a specified location in a specified color
	 * 
	 * @param graphics the Graphics object to render this text too
	 * @param text the String text to be rendered to the Graphics object
	 * @param xPos the horizontal pixel offset from 0,0 to render too
	 * @param yPos the vertical pixel offset from 0,0 to render too
	 * @param center boolean, if true then center the text on xPos,yPos, otherwise xPos,yPos is the upper left hand pixel of the text
	 * @param color the Color to render the text in
	 * @param font the Font to render the text in
	 */
	public static void drawString(Graphics g, String text, int xPos, int yPos, boolean center, Color color, Font font){
		g.setColor(color);
		g.setFont(font);
		int x = xPos;
		int y = yPos;
		if(center){
			FontMetrics fm = g.getFontMetrics(font);
			x = xPos - fm.stringWidth(text) / 2;
			y = (yPos - fm.getHeight() / 2) + fm.getAscent();
		}
		g.drawString(text, x, y);
	}

}
