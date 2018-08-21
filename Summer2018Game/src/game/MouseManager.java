package game;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class MouseManager implements MouseListener, MouseMotionListener{

	private boolean leftPressed;
	private boolean rightPressed;
	public int mouseX;
	public int mouseY;
	
	private boolean isLeftPressed() {
		return leftPressed;
	}
	
	private boolean isRightPressed() {
		return rightPressed;
	}
	
	private int getMouseX() {
		return mouseX;
	}
	
	private int getMouseY() {
		return mouseY;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		
		if(State.getState() != null) {
			ArrayList<UIObject> uiObjects = State.getState().getUIOBjects();
			if(uiObjects != null) {
				for(UIObject object: uiObjects) {
					object.onMouseMove(e);
				}	
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			leftPressed = true;
		} else if(e.getButton() == MouseEvent.BUTTON3) {
			rightPressed = true;
		}
	}

	@SuppressWarnings("unused")
	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			leftPressed = false;
		} else if(e.getButton() == MouseEvent.BUTTON3) {
			rightPressed = false;
		}
		if(State.getState() != null) {
			ArrayList<UIObject> uiObjects = State.getState().getUIOBjects();
			if(uiObjects != null) {
				UIObject[] uiObjectArray =  uiObjects.toArray(new UIObject[uiObjects.size()]);
				for(UIObject object: uiObjectArray) {
					object.onMouseRelease(e);
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
