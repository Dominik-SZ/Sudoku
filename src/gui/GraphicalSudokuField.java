package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import logic.Sudoku;
import utilities.Direction;
import utilities.Keyboard;
import utilities.GameStatus;

/**
 * GraphicalSudokuField extends JTextField and adds a flag if it is darkened, i and j
 * Coordinates as well as a restriction on inputs: Only numbers and whitespaces
 * are accepted. Other inputs have no effect. Furthermore all necessary
 * listeners are added in the constructor requiring a SudokuGUI parent.
 */
public class GraphicalSudokuField extends JTextField {
	/** only inserted to prevent warnings */
	private static final long serialVersionUID = 8902929711409056538L;
	private boolean darkened;
	private int iCoord;
	private int jCoord;
	private SudokuGUI parent;
	private Sudoku sudoku;

	/**
	 * Creates a new GraphicalSudokuField object with all functionality.
	 * 
	 * @param iCoord
	 *            The i coordinate where the field is placed in its parents
	 *            boardGraphic
	 * @param jCoord
	 *            The j coordinate where the field is placed in its parents
	 *            boardGraphic
	 * @param parent
	 *            The parent GUI to which this field belongs
	 */
	GraphicalSudokuField(int iCoord, int jCoord, SudokuGUI parent) {
		super();
		this.iCoord = iCoord;
		this.jCoord = jCoord;
		this.setDarkened(false);
		this.parent = parent;
		this.sudoku = parent.getSudoku();

		// 0 means erased field
		setEditable(sudoku.getBoard(iCoord, jCoord) == 0);
		sudoku.insertCurrentValue(sudoku.getBoard(iCoord, jCoord), iCoord, jCoord);
		setFont(parent.getStandardFont());
		setHorizontalAlignment(JTextField.CENTER);

		// EDITABLE FIELDS:
		if (isEditable()) {
			setText("");
			setForeground(Color.blue);

			@SuppressWarnings("serial")
			Action toggleNoteMode = new AbstractAction() {

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
								sudoku.insertCurrentValue(newContent, iCoord, jCoord);
							} catch (NumberFormatException ex) {
								sudoku.insertCurrentValue(0, iCoord, jCoord);
							}
						} else if (getFont().equals(parent.getNoteFont())) {
							setForeground(parent.getFieldNoteColor());
							sudoku.insertCurrentValue(0, iCoord, jCoord);
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
								sudoku.removeCurrentValue(iCoord, jCoord);
								sudoku.insertCurrentValue(newContent, iCoord, jCoord);
							} else {
								sudoku.removeCurrentValue(iCoord, jCoord);
							}
						} catch (NumberFormatException ex) {
							sudoku.removeCurrentValue(iCoord, jCoord);
						}

						// check if the field should stop being painted red
						if (getBackground().equals(Color.RED) && getFont().equals(parent.getNoteFont())) {
							if (darkened) {
								setBackground(Color.WHITE.darker());
							} else {
								setBackground(Color.WHITE);
							}
						}
						
						// update the inserted possibilities
						if(parent.getPossibilityMode()){
							parent.showPossibilities();
						}

						// check if the game is finished
						GameStatus currentStatus = sudoku.getStatus();
						if (currentStatus == GameStatus.CORRECT || currentStatus == GameStatus.INCORRECT) {
							parent.showStatus(currentStatus);
						}

						// check if the outline should be updated
						int oldOutline = parent.getCurrentOutline();
						try{
							if (!fieldContent.equals("") && Integer.parseInt(fieldContent) == oldOutline) {
								parent.setCurrentOutline(-1);
								parent.outline(oldOutline);
							}
						} catch (NumberFormatException ex){
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
					} catch (StringIndexOutOfBoundsException ex) {
					}
					parent.setEgg(egg);

					if (egg.equals("lorddominik")) {
						try {
							JFrame frame = new JFrame();
							frame.setUndecorated(true);
							BufferedImage bufferedImage = ImageIO
									.read(egg.getClass().getResourceAsStream("/icons/Grinsebacke882x882.jpg"));
							ImageIcon image = new ImageIcon(bufferedImage);
							JLabel label = new JLabel(image);
							frame.add(label);
							frame.setSize(image.getIconWidth(), image.getIconHeight());

							frame.setLocationRelativeTo(null);
							frame.requestFocus();
							frame.setAlwaysOnTop(true);
							frame.setVisible(true);
						} catch (Exception e) {
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
					} catch (NumberFormatException ex) {
					}
					int correctNumber = sudoku.getSolutionValue(getICoord(), getJCoord());
					
					// update the current state in the sudoku
					// this must occur in the keyReleased method because the
					// text is not updated yet in the keyPressed method
					try {
						int newContent = Integer.parseInt(fieldContent);
						if (getFont().equals(parent.getStandardFont())) {
							sudoku.removeCurrentValue(iCoord, jCoord);
							sudoku.insertCurrentValue(newContent, iCoord, jCoord);
						} else {
							sudoku.removeCurrentValue(iCoord, jCoord);
						}
					} catch (NumberFormatException ex) {
						sudoku.removeCurrentValue(iCoord, jCoord);
					}
					
					// update the inserted possibilities
					if(parent.getPossibilityMode()){
						parent.showPossibilities();
					}

					// move focus stuff
					parent.getKeyboard().released(ev.getKeyCode());
					if (parent.getKeyboard().nonePressed()) {
						Direction direction = parent.getKeyboard().getDirection();
						parent.setKeyboard(new Keyboard());
						int jumpWidth = parent.jumpWidth(getICoord(), getJCoord(), direction);
						parent.moveFocus(getICoord(), getJCoord(), direction, jumpWidth);
					}

					parent.updateRightPanel();

					// check if the game is finished
					GameStatus currentStatus = sudoku.getStatus();
					if (currentStatus == GameStatus.CORRECT || currentStatus == GameStatus.INCORRECT) {
						parent.showStatus(currentStatus);
					}

					// check if the field should stop being painted red
					if (getBackground().equals(Color.RED) && fieldNumber == correctNumber) {
						if (darkened) {
							setBackground(Color.WHITE.darker());
						} else {
							setBackground(Color.WHITE);
						}
					}

					// check if the outline should be updated
					if (fieldNumber == parent.getCurrentOutline() || getBorder().equals(parent.getOutlineBorder())
							&& fieldNumber != parent.getCurrentOutline()) {
						int oldOutline = parent.getCurrentOutline();
						parent.setCurrentOutline(-1);
						parent.outline(oldOutline);
					}

				}

			});

		}

		// PRESET FIELDS:
		else {
			setText(parent.getSudoku().getBoard(iCoord, jCoord) + "");
			setForeground(Color.BLACK);
			setEditable(false);
			setBackground(Color.WHITE);
			sudoku.insertCurrentValue(sudoku.getBoard(iCoord, jCoord), iCoord, jCoord);
		}
	}

	/**
	 * Creates a new GraphicalSudokuField without any additional functionality.
	 */
	GraphicalSudokuField(int i, int j) {
		this.iCoord = i;
		this.jCoord = j;
	}

	/**
	 * Toggles the note mode of the inserted field. This means changing its
	 * font.
	 */
	private void toggleNoteMode() {
		if (getFont().equals(parent.getStandardFont())) {
			setFont(parent.getNoteFont());
		} else {
			setFont(parent.getStandardFont());
		}
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

	@Override
	protected Document createDefaultModel() {
		return new OnlyNumbersDocument();
	}

	boolean isDarkened() {
		return darkened;
	}

	private void setDarkened(boolean darkened) {
		this.darkened = darkened;
	}

	/**
	 * Allows only for numbers and whitespaces to be inserted in the attached
	 * component.
	 */
	static class OnlyNumbersDocument extends PlainDocument {
		/** only inserted to prevent warnings */
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