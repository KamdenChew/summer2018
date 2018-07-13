package game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class UIImageButton extends UIObject{

	private BufferedImage image;
	private BufferedImage hoverImage;
	private ClickListener clickListener;
	
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
