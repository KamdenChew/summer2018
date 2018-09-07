package game;
import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Display {
	private JFrame frame;
	private Canvas canvas;
	private String title;
	private int width;
	private int height;

	/**
	 * Constructs a new Display
	 *
	 * @param title String to put as the title for the JFrame
	 * @param width int for pixel width of the JFrame
	 * @param height int for pixel height of the JFrame
	 */
	public Display(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
		initializeDisplay();
	}

	/**
	 * Initializes and makes the JFrame visible. JFrame is non-resizable for simplicity.
	 */
	private void initializeDisplay() {
		frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);

		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(width, height));
		canvas.setMinimumSize(new Dimension(width, height));
		canvas.setMaximumSize(new Dimension(width, height));
		canvas.setFocusable(false);

		frame.add(canvas);
		frame.pack();
		frame.setVisible(true);
	}
	
	//Getters
	public Canvas getCanvas() {
		return this.canvas;
	}
	
	public JFrame getFrame() {
		return this.frame;
	}
}
