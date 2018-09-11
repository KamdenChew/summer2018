package game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class UIImageButton extends UIObject{

	private BufferedImage image;
	private BufferedImage hoverImage;
	private ClickListener clickListener;
	
	/**
	 * Constructs a new UIImageButton
	 * 
	 * @param x the x pixel coordinate for the upper left corner of the UIImageButton
	 * @param y the y pixel coordinate for the upper left corner of the UIImageButton
	 * @param width the number of pixels wide the UIImageButton is
	 * @param height the number of pixels tall the UIImageButton is
	 * @param image the image to display for the UIImageButton
	 * @param hoverImage the image to display for the UIImageButton when the mouse is hovered above the button
	 * @param clickListener the ClickListener that tells the UIImageButton what task to perform once clicked
	 */
	public UIImageButton(float x, float y, int width, int height, BufferedImage image, BufferedImage hoverImage, ClickListener clickListener) {
		super(x, y, width, height);
		this.image = image;
		this.hoverImage = hoverImage;
		this.clickListener = clickListener;
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void render(Graphics graphics) {
		if(hovering) {
			graphics.drawImage(hoverImage, (int) x, (int) y, width, height, null);
		} else {
			graphics.drawImage(image, (int) x, (int) y, width, height, null);
		}
	}

	@Override
	public void onClick() {
		clickListener.onClick();
	}

}
