package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import logic.Sudoku;
import util.Direction;
import util.Keyboard;
import util.GameStatus;

/**
 * GraphicalSudokuField extends JTextField and adds a flag if it is darkened, i and j Coordinates as well as a
 * restriction on inputs: Only numbers and whitespaces are accepted. Other inputs have no effect. Furthermore all
 * necessary listeners are added in the constructor requiring a SudokuGUI parent.
 */
public class GraphicalSudokuField extends JTextField {

    //TODO: move darkened functionality to GraphicalSudokuFieldFullyFunctional
    private boolean darkened;
    private int iCoord;
    private int jCoord;

    /**
     * Creates a new GraphicalSudokuField without any additional functionality.
     *
     * @param font The font for this field
     * @param i    The i coordinate at which the field is placed
     * @param j    The j coordinate at which the field is placed
     */
    GraphicalSudokuField(Font font, int i, int j) {
        setFont(font);
        this.iCoord = i;
        this.jCoord = j;
        this.setDarkened(false);
        setHorizontalAlignment(JTextField.CENTER);
    }

    /**
     * Darkens this GraphicalSudokuField if it is not already darker.
     *
     * @return If this method did something
     */
    boolean darken() {
        if (darkened) {
            return false;
        } else {
            Color oldColor = getBackground();
            setBackground(oldColor.darker());
            darkened = true;
            return true;
        }
    }

    /**
     * If the field is darkened, this is undone.
     */
    void removeDarkening() {
        if (darkened) {
            Color oldColor = getBackground();
            setBackground(oldColor.brighter());
            // sometimes this very light gray occurs. I don't know why.
            if (getBackground().getRGB() == -65794) {
                setBackground(Color.WHITE);
            }
            darkened = false;
        }
    }

    private int getICoord() {
        return iCoord;
    }

    private int getJCoord() {
        return jCoord;
    }

    boolean isDarkened() {
        return darkened;
    }

    private void setDarkened(boolean darkened) {
        this.darkened = darkened;
    }

    //------------------------------------------------------------------------------------------------------------------
    @Override
    protected Document createDefaultModel() {
        return new OnlyNumbersDocument();
    }

    /**
     * Allows only for numbers and whitespaces to be inserted in the attached component.
     */
    static class OnlyNumbersDocument extends PlainDocument {
        /**
         * only inserted to prevent warnings
         */
        private static final long serialVersionUID = 5997722226368407519L;

        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            if (str == null) {
                return;
            }

            char[] chars = str.toCharArray();
            boolean ok = true;

            for (int i = 0; i < chars.length; i++) {
                if (chars[i] != ' ') {
                    try {
                        Integer.parseInt(String.valueOf(chars[i]));
                    } catch (NumberFormatException exc) {
                        // no number
                        ok = false;
                        break;
                    }
                }
            }

            if (ok) {
                super.insertString(offs, new String(chars), a);
            }
        }
    }

}