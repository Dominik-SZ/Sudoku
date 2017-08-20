package gui;

import logic.Sudoku;
import util.Direction;
import util.GameStatus;
import util.Keyboard;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

class GraphicalSudokuFieldFullyFunctional extends GraphicalSudokuField{

    private SudokuGUI parent;
    private Sudoku sudoku;

    /**
     * Creates a new GraphicalSudokuField object with all functionality.
     *
     * @param iCoord The i coordinate where the field is placed in its parents boardGraphic
     * @param jCoord The j coordinate where the field is placed in its parents boardGraphic
     * @param parent The parent GUI to which this field belongs
     */
    GraphicalSudokuFieldFullyFunctional(int iCoord, int jCoord, SudokuGUI parent) {
        super(parent.getStandardFont(), iCoord, jCoord);
        this.parent = parent;
        this.sudoku = parent.getSudoku();

        // 0 means erased field
        setEditable(sudoku.getStartValue(iCoord, jCoord) == 0);
        sudoku.setCurrentValue(sudoku.getStartValue(iCoord, jCoord), iCoord, jCoord, true);
        setFont(parent.getStandardFont());
        setHorizontalAlignment(JTextField.CENTER);

        // EDITABLE FIELDS:
        if (isEditable()) {
            setText("");
            setForeground(Color.blue);

            @SuppressWarnings("serial") Action toggleNoteMode = new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    toggleNoteMode();
                }
            };

            getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('q'), "toggleNoteMode");
            getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke('t'), "toggleNoteMode");
            getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(12, 0), "toggleNoteMode");
            getActionMap().put("toggleNoteMode", toggleNoteMode);

            // mouse listener
            addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent click) {
                    if (SwingUtilities.isRightMouseButton(click)) {
                        toggleNoteMode();

                        // handle right clicks
                        // update the current status
                        if (getFont().equals(parent.getStandardFont())) {
                            setForeground(parent.getFieldStandardColor());
                            try {
                                int newContent = Integer.parseInt(getText().trim());
                                sudoku.setCurrentValue(newContent, iCoord, jCoord, true);
                            } catch (NumberFormatException ex) {
                                sudoku.setCurrentValue(0, iCoord, jCoord, true);
                            }
                        } else if (getFont().equals(parent.getNoteFont())) {
                            setForeground(parent.getFieldNoteColor());
                            sudoku.setCurrentValue(0, iCoord, jCoord, true);
                        }

                        parent.updateRightPanel();
                    }

                }

                @Override
                public void mouseReleased(MouseEvent click) {

                    String fieldContent = getText().trim();
                    // left click
                    if (SwingUtilities.isLeftMouseButton(click) && click.getClickCount() == 2) {
                        // open the color chooser dialog
                        parent.showColorChooser((GraphicalSudokuField) click.getSource());
                    }

                    // right click
                    else if (SwingUtilities.isRightMouseButton(click)) {
                        requestFocus();
                        // reset the manual coloring
                        if (click.getClickCount() == 2) {
                            if (isDarkened()) {
                                setBackground(Color.WHITE.darker());
                            } else {
                                setBackground(Color.WHITE);
                            }
                        }

                        // update the game state in the sudoku
                        try {
                            int newContent = Integer.parseInt(fieldContent);
                            if (getFont().equals(parent.getStandardFont())) {
                                sudoku.setCurrentValue(0, iCoord, jCoord, true);
                                sudoku.setCurrentValue(newContent, iCoord, jCoord, true);
                            } else {
                                sudoku.setCurrentValue(0, iCoord, jCoord, true);
                            }
                        } catch (NumberFormatException ex) {
                            sudoku.setCurrentValue(0, iCoord, jCoord, true);
                        }

                        // check if the field should stop being painted red
                        if (getBackground().equals(Color.RED) && getFont().equals(parent.getNoteFont())) {
                            if (isDarkened()) {
                                setBackground(Color.WHITE.darker());
                            } else {
                                setBackground(Color.WHITE);
                            }
                        }

                        // update the inserted possibilities
                        if (parent.getPossibilityMode()) {
                            parent.showPossibilities();
                        }

                        // check if the game is finished
                        GameStatus currentStatus = sudoku.getStatus();
                        if (currentStatus != GameStatus.INCOMPLETE) {
                            parent.showStatus(currentStatus);
                        }

                        // check if the outline should be updated
                        int oldOutline = parent.getCurrentOutline();
                        try {
                            if (!fieldContent.equals("") && Integer.parseInt(fieldContent) == oldOutline) {
                                parent.setCurrentOutline(-1);
                                parent.outline(oldOutline);
                            }
                        } catch (NumberFormatException ignored) {
                        }

                    }
                }
            });

            // key listener
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent ev) {

                    parent.getKeyboard().setTyped(ev.getKeyCode());

                    String egg = parent.getEgg();
                    egg += ev.getKeyChar();
                    try {
                        egg = egg.substring(1, 12);
                    } catch (StringIndexOutOfBoundsException ignored) {
                    }
                    parent.setEgg(egg);

                    if (egg.equals("lorddominik")) {
                        try {
                            JFrame frame = new JFrame();
                            frame.setUndecorated(true);
                            BufferedImage bufferedImage = ImageIO.read(egg.getClass().getResourceAsStream("/icons/Grinsebacke882x882.jpg"));
                            ImageIcon image = new ImageIcon(bufferedImage);
                            JLabel label = new JLabel(image);
                            frame.add(label);
                            frame.setSize(image.getIconWidth(), image.getIconHeight());

                            frame.setLocationRelativeTo(null);
                            frame.requestFocus();
                            frame.setAlwaysOnTop(true);
                            frame.setVisible(true);
                        } catch (Exception ignored) {
                        }
                    }
                }

                @Override
                public void keyReleased(KeyEvent ev) {
                    // useful variables
                    String fieldContent = getText().trim();
                    int fieldNumber = 0;
                    try {
                        fieldNumber = Integer.parseInt(fieldContent);
                    } catch (NumberFormatException ignored) {
                    }
                    int correctNumber = sudoku.getSolutionValue(iCoord, jCoord);

                    // update the current state in the sudoku
                    // this must occur in the keyReleased method because the
                    // text is not updated yet in the keyPressed method
                    try {
                        int newContent = Integer.parseInt(fieldContent);
                        if (getFont().equals(parent.getStandardFont())) {
                            sudoku.setCurrentValue(0, iCoord, jCoord, true);
                            sudoku.setCurrentValue(newContent, iCoord, jCoord, true);
                        } else {
                            sudoku.setCurrentValue(0, iCoord, jCoord, true);
                        }
                    } catch (NumberFormatException ex) {
                        sudoku.setCurrentValue(0, iCoord, jCoord, true);
                    }

                    // update the inserted possibilities
                    if (parent.getPossibilityMode()) {
                        parent.showPossibilities();
                    }

                    // move focus stuff
                    parent.getKeyboard().released(ev.getKeyCode());
                    if (parent.getKeyboard().nonePressed()) {
                        Direction direction = parent.getKeyboard().getDirection();
                        parent.setKeyboard(new Keyboard());
                        int jumpWidth = parent.jumpWidth(iCoord, jCoord, direction);
                        parent.moveFocus(iCoord, jCoord, direction, jumpWidth);
                    }

                    parent.updateRightPanel();

                    // check if the game is finished
                    GameStatus currentStatus = sudoku.getStatus();
                    if (currentStatus != GameStatus.INCOMPLETE) {
                        parent.showStatus(currentStatus);
                    }

                    // check if the field should stop being painted red
                    if (getBackground().equals(Color.RED) && fieldNumber == correctNumber) {
                        if (isDarkened()) {
                            setBackground(Color.WHITE.darker());
                        } else {
                            setBackground(Color.WHITE);
                        }
                    }

                    // check if the outline should be updated
                    if (fieldNumber == parent.getCurrentOutline() || getBorder().equals(parent.getOutlineBorder()) && fieldNumber != parent.getCurrentOutline()) {
                        int oldOutline = parent.getCurrentOutline();
                        parent.setCurrentOutline(-1);
                        parent.outline(oldOutline);
                    }

                }

            });

        }

        // PRESET FIELDS:
        else {
            setText(parent.getSudoku().getStartValue(iCoord, jCoord) + "");
            setForeground(Color.BLACK);
            setEditable(false);
            setBackground(Color.WHITE);
            sudoku.setCurrentValue(sudoku.getStartValue(iCoord, jCoord), iCoord, jCoord, true);
        }
    }

    /**
     * Toggles the note mode of the inserted field. This means changing its font.
     */

    private void toggleNoteMode() {
        if (getFont().equals(parent.getStandardFont())) {
            setFont(parent.getNoteFont());
        } else {
            setFont(parent.getStandardFont());
        }
    }

}
