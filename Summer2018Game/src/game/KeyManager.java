package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener{

	private final int MAX_KEY_CODE = 256;
	private boolean[] keys;
	private boolean[] justPressed;
	private boolean[] cantPress;
	
	public boolean up;
	public boolean down;
	public boolean left;
	public boolean right;
	public boolean space;
	
	/**
	 * Constructs a new KeyManager
	 */
	public KeyManager() {
		keys = new boolean [MAX_KEY_CODE];
		justPressed = new boolean [MAX_KEY_CODE];
		cantPress = new boolean [MAX_KEY_CODE];
	}
	
	/**
	 * Processes and updates this KeyManager's variables
	 */
	public void tick() {
		for(int i = 0; i < keys.length; i++) {
			if(cantPress[i] && !keys[i]) {
				cantPress[i] = false;
			} else if(justPressed[i]) {
				cantPress[i] = true;
				justPressed[i] = false; 
			}
			if(!cantPress[i] && keys[i]) {
				justPressed[i] = true;
			}
		}
		
		this.up = keys[KeyEvent.VK_W];
		this.down = keys[KeyEvent.VK_S];
		this.left = keys[KeyEvent.VK_A];
		this.right = keys[KeyEvent.VK_D];
		this.space = keys[KeyEvent.VK_SPACE];
	}
	
	/**
	 * Returns whether or not the given key was just pressed
	 * 
	 * @param keyCode the keyCode to check to see if the corresponding key was just pressed
	 */
	public boolean keyJustPressed(int keyCode){
		if(keyCode < 0 || keyCode >= keys.length) {
			return false;
		} else {
			return justPressed[keyCode];
		}
	}
	
	/**
	 * Returns whether or not the given key is being pressed
	 * 
	 * @param keyCode the keyCode to check to see if the corresponding key is being pressed
	 */
	public boolean beingPressed(int keyCode) {
		if(keyCode < 0 || keyCode >= keys.length) {
			return false;
		} else {
			return keys[keyCode];
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() >=0 && e.getKeyCode() < keys.length) {
			keys[e.getKeyCode()] = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() >=0 && e.getKeyCode() < keys.length) {
			keys[e.getKeyCode()] = false;
		}
	}

}
