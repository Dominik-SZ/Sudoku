package util;

import java.awt.event.KeyEvent;

/**
 * This class is used to process direction inputs via <p>- WASD<br>- arrow keys<br>- shift + 1,2,3,4,6,7,8,9 on the num
 * block</p> If a key event "pressed" is detected, insert it to the setTyped() method and if it is setReleased, insert
 * it to the setReleased() method. Now you can assure that the last key has been released via nonePressed() and get the
 * direction if this is true.
 */
public class Keyboard {
    private int counter = 0;
    private boolean up = false;
    private boolean down = false;
    private boolean left = false;
    private boolean right = false;

    private boolean[] keyCodes = new boolean[512];

    /**
     * Marks a key to be pressed now. Use in the keyPressed() method of a KeyEventHandler.
     *
     * @param keyCode The key code of the button pressed
     */
    public void setTyped(int keyCode) {

        // only increase the counter if the button was not clicked yet
        if (!keyCodes[keyCode]) {
            keyCodes[keyCode] = true;
            counter++;
        }
        if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) {
            up = true;
        } else if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) {
            right = true;
        } else if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) {
            down = true;
        } else if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) {
            left = true;
        } else if (keyCode == KeyEvent.VK_PAGE_UP) {
            up = true;
            right = true;
        } else if (keyCode == KeyEvent.VK_PAGE_DOWN) {
            right = true;
            down = true;
        } else if (keyCode == KeyEvent.VK_END) {
            down = true;
            left = true;
        } else if (keyCode == KeyEvent.VK_HOME) {
            up = true;
            left = true;
        }
        // note that e.g KeyEvent.VK_PAGE_UP is only detected if NUM PAD 9 is pressed while shift is down
        // note that e.g. NUM PAD 8 while shift is down equals KeyEvent.VK_UP
    }

    /**
     * Marks a key to be released now. Use in the keyReleased() method of a KeyEventHandler.
     *
     * @param keyCode The key code of the button released
     */
    public void setReleased(int keyCode) {
        // only decrease the counter if the button was clicked already
        if (keyCodes[keyCode]) {
            keyCodes[keyCode] = false;
            counter--;
        }
    }

    /**
     * Returns if no buttons are pressed at the moment (counter == 0).
     *
     * @return if no button is pressed
     */
    public boolean nonePressed() {
        return (counter == 0);
    }

    /**
     * Return the direction entered by previous key presses.
     *
     * @return The current direction pressed
     */
    public Direction getDirection() {
        if (up && right) return Direction.UP_RIGHT;
        else if (right && down) return Direction.RIGHT_DOWN;
        else if (down && left) return Direction.DOWN_LEFT;
        else if (left && up) return Direction.LEFT_UP;
        else if (up) return Direction.UP;
        else if (right) return Direction.RIGHT;
        else if (down) return Direction.DOWN;
        else if (left) return Direction.LEFT;
        else return Direction.ERROR;
    }


}