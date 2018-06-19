import javax.swing.JFrame;

public class Display {
	private JFrame frame;
	private String title;
	private int width;
	private int height;
	
	public Display(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
		initializeDisplay();
	}
	
	private void initializeDisplay() {
		frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
