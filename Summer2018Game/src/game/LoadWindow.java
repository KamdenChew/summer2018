package game;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LoadWindow {
	private JFrame frame;
	private JButton loadButton;
	private JButton cancelButton;
	private JLabel label;
	private String[] fileNames;
	private JComboBox box;
	private String selected = "";
	
	/**
	 * Constructs a new LoadWindow
	 * 
	 * @param game the Game object for this running instance
	 */
	public LoadWindow(final Game game) {
		
		//Initialize load button
		this.loadButton = new JButton();
		loadButton.setText("Load");
		loadButton.setPreferredSize(new Dimension(100, 30));
		loadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		loadButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if(!selected.equals("")) {
					GameLoader.loadGame(game, selected);
					frame.dispose();
					game.setPrimaryWindow(true);
				}
			}
		});
		
		//Initialize cancel button
		this.cancelButton = new JButton();
		cancelButton.setText("Cancel");
		cancelButton.setPreferredSize(new Dimension(100, 30));
		cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		cancelButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				game.setPrimaryWindow(true);
			}
		});
		
		//Initialize frame
		frame = new JFrame();
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.setTitle("Load Game");
		frame.setPreferredSize(new Dimension(300, 200));
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    frame.addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosing(WindowEvent event) {
	            game.setPrimaryWindow(true);
	            frame.dispose();
	        }
	    });
		
		//Get save files for drop down menu
		File saveFolder = new File("./res/saves");
		fileNames = saveFolder.list();
		if(fileNames.length > 0) {
			this.label = new JLabel("Load " +  fileNames[0] + "?");
			selected = fileNames[0];
		} else {
			this.label = new JLabel("No Saves Found");
		}
		
		//Initialize drop down menu of save names
		this.box = new JComboBox<String>(fileNames);
		
		this.box.addItemListener(new ItemListener() {
					
			public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED) {
						selected = fileNames[box.getSelectedIndex()];
						label.setText("Load " + selected +  "?");
					}
			}
		});
		
		//Add components to panels
		JPanel justBox = new JPanel();
		justBox.add(box);
		
		JPanel justText = new JPanel();
		justText.add(label);
		
		JPanel buttons = new JPanel();
		buttons.add(loadButton);
		buttons.add(cancelButton);
		
		//Add panels to frame
		frame.add(justBox);
		frame.add(justText);
		frame.add(buttons);
		
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.pack();
	}
	
	/**
	 * Makes this LoadWindow visible
	 */
	public void show() {
		frame.setVisible(true);
	}
}
