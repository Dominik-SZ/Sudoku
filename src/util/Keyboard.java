package util;

import java.awt.event.KeyEvent;

public class Keyboard {
	private int counter= 0;
	private boolean up = false;
	private boolean down = false;
	private boolean left = false;
	private boolean right = false;
	
	private boolean[] keyCodes = new boolean[512];
	
	public void setTyped(int keyCode) {
		
		// only increase the counter if the button was not clicked yet
		if (!keyCodes[keyCode]) {
			keyCodes[keyCode] = true;
			counter++;
		}
		if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) {
			up= true;
		} else if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) {
			right= true;
		} else if(keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S){
			down= true;
		} else if(keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A){
			left= true;
		} else if(keyCode == KeyEvent.VK_PAGE_UP){
			up = true;
			right = true;
		} else if(keyCode == KeyEvent.VK_PAGE_DOWN){
			right = true;
			down = true;
		} else if(keyCode == KeyEvent.VK_END){
			down = true;
			left = true;
		} else if(keyCode == KeyEvent.VK_HOME){
			up= true;
			left= true;
		}
	}
	
	public void released(int keyCode) {
		keyCodes[keyCode] = false;
		counter--;
	}
	
	/**
	 * Returns if no buttons are pressed at the moment (counter == 0).
	 * 
	 * @return	if no button is pressed
	 */
	public boolean nonePressed() {
		return (counter == 0);
	}
	
	public Direction getDirection() {
		if(up && right)			return Direction.UP_RIGHT;
		else if(right && down)	return Direction.RIGHT_DOWN;
		else if(down && left)	return Direction.DOWN_LEFT;
		else if(left && up)		return Direction.LEFT_UP;
		else if(up)				return Direction.UP;
		else if(right)			return Direction.RIGHT;
		else if(down)			return Direction.DOWN;
		else if(left)			return Direction.LEFT;
		else					return Direction.ERROR;
	}
}