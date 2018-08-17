package swingGUI;

import swingGUI.util.Direction;
import swingGUI.util.Keyboard;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class GraphicalSudokuFieldCustomInput extends GraphicalSudokuField {

    /**
     * Creates a graphical sudoku field without any additional functionality except for focus switching. This can be
     * used to insert custom Sudokus.
     *
     * @param iCoord The i coordinate at which this field is placed
     * @param jCoord The j coordinate at which this field is placed
     * @param parent    The parent swingGUI of this field
     */
    GraphicalSudokuFieldCustomInput(int iCoord, int jCoord, SudokuGUI parent) {
        super(parent.getStandardFont(), iCoord, jCoord);
        setFont(parent.getStandardFont());
        setHorizontalAlignment(JTextField.CENTER);

        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent ev) {
                parent.getKeyboard().setTyped(ev.getKeyCode());
                if (ev.getKeyCode() == KeyEvent.VK_ENTER) {
                    parent.activate();
                }
            }

            @Override
            public void keyReleased(KeyEvent ev) {

                // move focus stuff
                parent.getKeyboard().setReleased(ev.getKeyCode());
                if (parent.getKeyboard().nonePressed()) {
                    Direction direction = parent.getKeyboard().getDirection();
                    parent.setKeyboard(new Keyboard());
                    int jumpWidth = parent.jumpWidth(iCoord, jCoord, direction);
                    parent.moveFocus(iCoord, jCoord, direction, jumpWidth);
                }
            }
        });
    }
}