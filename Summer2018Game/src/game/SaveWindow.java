package game;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SaveWindow {
	private JFrame frame;
	private JButton saveButton;
	private JButton cancelButton;
	private JLabel saveNameLabel;
	private JLabel warningLabel;
	private JTextField textField;
	private ArrayList<String> fileNames;
	
	/**
	 * Constructs a new SaveWindow
	 * 
	 * @param game the Game object for this running instance
	 */
	public SaveWindow(final Game game) {
		
		//Store all known saves
		File saveFolder = new File("./res/saves");
		fileNames = new ArrayList<String>();
		for(String filName: saveFolder.list()) {
			fileNames.add(filName);
		}
		
		//Initialize save button
		this.saveButton = new JButton();
		saveButton.setText("Save");
		saveButton.setPreferredSize(new Dimension(100, 30));
		saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		saveButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if(textField.getText().length() > 0) {
					warningLabel.setText("");
					GameSaver.saveGame(game, textField.getText());
					game.setSaveName(textField.getText());
					frame.dispose();
					game.setPrimaryWindow(true);
				} else {
					warningLabel.setText("Please input a save name.");
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
		frame.setTitle("Save Game");
		frame.setPreferredSize(new Dimension(700, 200));
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    frame.addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosing(WindowEvent event) {
	            game.setPrimaryWindow(true);
	            frame.dispose();
	            game.setPrimaryWindow(true);
	        }
	    });
		
		//Initialize saveNameLabel, warningLabel, and text field
		if(game.getSaveName().equals("")) {
			this.saveNameLabel = new JLabel("This game has not been saved before, please enter a save name below.");
			this.textField = new JTextField(20);
			this.warningLabel = new JLabel("");
		} else {
			this.saveNameLabel = new JLabel("This game's last state was saved as " + game.getSaveName() + ".");
			this.textField = new JTextField(game.getSaveName(), 20);
			this.warningLabel = new JLabel("WARNING the save name " + game.getSaveName() + " is already in use. Saving as " + game.getSaveName() + " will overwrite the file.");
		}
		
		
		textField.addKeyListener(new KeyAdapter() {
	        @Override
	        public void keyTyped(KeyEvent e) {
	        	
	        	//Limit text to 20 characters
	        	if (textField.getText().length() >= 10 ) {
	            	e.consume();
	            }
	            
	        	//Make appropriate save warnings
	        	
	            //If doing a backspace or a delete, check the contents after action is performed
	            if(((int)e.getKeyChar() == KeyEvent.VK_BACK_SPACE || (int)e.getKeyChar() == KeyEvent.VK_DELETE) && fileNames.contains(textField.getText())) {
	            	warningLabel.setText("WARNING the save name " + textField.getText() + " is already in use. Saving as " + textField.getText() + " will overwrite the file.");
	           
	            //Otherwise see if we just appended to make a previously existing file name
	            } else {
	            	 if(fileNames.contains(textField.getText() + e.getKeyChar())) {
	 	            	warningLabel.setText("WARNING the save name " + (textField.getText() + e.getKeyChar()) + " is already in use. Saving as " + (textField.getText() + e.getKeyChar()) + " will overwrite the file.");
	 	            } else {
	 	            	warningLabel.setText("");
	 	            }
	            }
	        }
	    });
		
		//Add components to panels
		JPanel saveNameText = new JPanel();
		saveNameText.add(saveNameLabel);
		
		JPanel justTextField = new JPanel();
		justTextField.add(textField);
		
		JPanel warningText = new JPanel();
		warningText.add(warningLabel);
		
		JPanel buttons = new JPanel();
		buttons.add(saveButton);
		buttons.add(cancelButton);
		
		//Add panels to frame
		frame.add(saveNameText);
		frame.add(justTextField);
		frame.add(warningText);
		frame.add(buttons);
		
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.pack();
	}
	
	/**
	 * Makes this SaveWindow visible
	 */
	public void show() {
		frame.setVisible(true);
	}
}
