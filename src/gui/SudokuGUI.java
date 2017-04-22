package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;

import gui.SudokuGUI;
import logic.Sudoku;
import logic.SudokuMain;
import logic.SudokuSolver;
import utilities.Direction;
import utilities.Keyboard;
import utilities.ComponentPrinter;
import utilities.Coordinate;
import utilities.DarkenStatus;
import utilities.GameStatus;

/**
 * This Class makes it possible to create a GUI for a calculated Sudoku in which
 * it can be solved. The GUI is based on the Java Swing framework.
 * 
 * @author Dominik
 *
 */
public class SudokuGUI extends JFrame {
	/** only inserted to prevent warnings */
	private static final long serialVersionUID = -5611312691569909674L;
	/** the length of the Sudoku. Additionally saved to save access. */
	private int length;
	/** the standard font for the Sudoku fields */
	private Font standardFont;
	/** the font for the fields if they are in note mode */
	private Font noteFont;
	/** the fonts of the labels */
	private Font labelFont;
	/** the logical sudoku object which is displayed and played on */
	private Sudoku sudoku;
	/** the main window frame */
	private JFrame mainFrame;
	/** the top part of the main frame */
	private JMenuBar menuBar;
	/** the center part of the main frame */
	private JPanel centerPanel;
	/** the right part of the main frame */
	private JPanel rightPanel;
	/** the bottom part of the main frame */
	private JPanel bottomPanel;
	/** the left part of the main frame */
	private JPanel leftPanel;
	/** the display on the right showing each number plus "er" */
	private JButton[] numberDisplay;
	/** the display showing how often each number has been inserted */
	private JTextField[] amountDisplay;
	/** the graphic board the player uses */
	private SudokuField[][] boardGraphic;
	/** all paintable components */
	private LinkedList<Component> paintable;
	/** the keyboard object helping especially with diagonal movement */
	private Keyboard keyboard;
	/** the starting time of the window in nanoseconds */
	private long startTime;
	/** a timer which updates the timeDisplay once a second */
	private Timer timer;
	/** the label with the time display */
	private JLabel timerLabel;
	private String egg;
	/** the foreground color of the components except for buttons */
	private Color foregroundColor;
	/** the background color of the components except for buttons */
	private Color backgroundColor;
	/** the standard color of the editable Sudoku fields of the boardGraphic */
	private Color fieldStandardColor;
	/** the note color of the editable Sudoku fields of the boardGraphic */
	private Color fieldNoteColor;
	/**
	 * the button which determines if the possibilities are automatically shown
	 */
	private JRadioButton showPossibilitiesButton;
	/**
	 * the radio button used to select if the amountDisplay counts downwards
	 */
	private JRadioButton countDownwardsButton;
	/** determines how the outline method darkens the affected fields */
	private DarkenStatus darkenMode;
	/** the gap between the displayed blocks of the Sudoku */
	private int gap = 5; //
	/** the border of the outlined fields */
	private Border outlineBorder; //
	/** the standard border of the fields */
	private Border standardBorder; //
	/** the current number which is outlined. -1 means none */
	private int currentOutline;
	/**
	 * The coordinates of the fields, which are currently highlighted based of
	 * the highlightPossibilityPairs method
	 */
	private LinkedList<Coordinate> possibilityPairsCoordinates;
	/** the colors currently used by the highlightPossibilityPairs method */
	private HashSet<Color> usedBackgroundColors;
	/** maps the indices of the color boxes to their respective color */
	private HashMap<Integer, Color> indexToColor;
	/** used to choose the foreground color of the gui overlay */
	JComboBox<String> foregroundBox;
	/** used to choose the background color of the gui overlay */
	JComboBox<String> backgroundBox;
	/** used to choose the extend of darkening when outlining */
	JComboBox<String> darkenBox;
	/** used to choose the standard foreground color of the sudoku fields */
	JComboBox<String> fieldStandardColorBox;
	/** used to choose the note foreground color of the sudoku fields */
	JComboBox<String> fieldNoteColorBox;

	// -------------------------------------------------------------------------

	// Constructor
	public SudokuGUI(Sudoku sudoku) {
		this.sudoku = sudoku;
		this.keyboard = new Keyboard();
		this.paintable = new LinkedList<Component>();
		length = sudoku.getLength();
		egg = "";
		standardFont = new Font("SansSerif", Font.BOLD, 30);
		noteFont = new Font("SansSerif", Font.BOLD, 15);
		if (length <= 4) {
			labelFont = new Font("SansSerif", Font.PLAIN, 12);
		} else {
			labelFont = new Font("SansSerif", Font.PLAIN, 16);
		}
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		standardBorder = new EtchedBorder(10);
		outlineBorder = new CompoundBorder(new LineBorder(Color.BLACK, 4), standardBorder);
		boardGraphic = new SudokuField[length][length];
		darkenMode = DarkenStatus.PARTLY;
		currentOutline = -1;
		possibilityPairsCoordinates = new LinkedList<Coordinate>();

		usedBackgroundColors = new HashSet<Color>();

		indexToColor = new HashMap<Integer, Color>();
		indexToColor.put(0, Color.BLACK);
		indexToColor.put(1, Color.DARK_GRAY);
		indexToColor.put(2, Color.GRAY);
		indexToColor.put(3, Color.WHITE);
		indexToColor.put(4, Color.PINK);
		indexToColor.put(5, Color.RED);
		indexToColor.put(6, Color.ORANGE);
		indexToColor.put(7, Color.YELLOW);
		indexToColor.put(8, Color.GREEN);
		indexToColor.put(9, new Color(0, 153, 0));
		indexToColor.put(10, Color.BLUE);

		mainFrame = new JFrame("Sudoku");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			LinkedList<Image> images = new LinkedList<Image>();
			images.add(ImageIO.read(mainFrame.getClass().getResourceAsStream("/icons/sudokuIcon128x128.png")));
			images.add(ImageIO.read(mainFrame.getClass().getResourceAsStream("/icons/sudokuIcon64x64.png")));
			images.add(ImageIO.read(mainFrame.getClass().getResourceAsStream("/icons/sudokuIcon32x32.png")));
			images.add(ImageIO.read(mainFrame.getClass().getResourceAsStream("/icons/sudokuIcon16x16.png")));
			mainFrame.setIconImages(images);
		} catch (IOException e) {
			e.printStackTrace();
		}

		mainFrame.setLayout(new BorderLayout());
		mainFrame.setMinimumSize(new Dimension(37 * length + 470, 37 * length + 260));
		mainFrame.setMaximumSize(new Dimension((int) screenSize.getWidth(), (int) screenSize.getHeight() - 43));
		mainFrame.setPreferredSize(new Dimension(length * 70 + 470, length * 70 + 260));
		int startWidth = (int) Math.min(mainFrame.getPreferredSize().getWidth(), mainFrame.getMaximumSize().getWidth());
		int startHeight = (int) Math.min(mainFrame.getPreferredSize().getHeight(),
				mainFrame.getMaximumSize().getHeight());
		mainFrame.setSize(new Dimension(startWidth, startHeight));

		// Debug outputs
		// System.out.println("min: " + mainFrame.getMinimumSize().getWidth() +
		// " " + mainFrame.getMinimumSize().getHeight());
		// System.out.println("max: " + mainFrame.getMaximumSize().getWidth() +
		// " " + mainFrame.getMaximumSize().getHeight());
		// System.out.println("preferred: " +
		// mainFrame.getPreferredSize().getWidth() + " " +
		// mainFrame.getPreferredSize().getHeight());
		// System.out.println("actual: " + mainFrame.getWidth() + " " +
		// mainFrame.getHeight());

		// build all the secondary panels
		buildTopPanel();
		buildCenterPanel();
		buildRightPanel();
		buildBottomPanel();
		buildLeftPanel();

		updateForeground();
		updateBackground();

		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null); // center the frame

		// make the tooltips last longer
		ToolTipManager.sharedInstance().setDismissDelay(15000);
	}

	/** generates the top panel of the main frame */
	private void buildTopPanel() {

		menuBar = new JMenuBar();
		// menuBar.setPreferredSize(new Dimension(0, 50));
		mainFrame.add(menuBar, BorderLayout.NORTH);

		// generate the menu
		JMenu fileMenu = new JMenu("Datei");
		fileMenu.setMnemonic(KeyEvent.VK_D);
		menuBar.add(fileMenu);

		JMenu helpMenu = new JMenu("Hilfe");
		helpMenu.setMnemonic(KeyEvent.VK_H);
		menuBar.add(helpMenu);

		JMenuItem newSudokuItem = new JMenuItem("Neues Sudoku");
		try {
			BufferedImage newSudokuImage = ImageIO
					.read(mainFrame.getClass().getResourceAsStream("/icons/newFileIcon.png"));
			newSudokuItem.setIcon(new ImageIcon(newSudokuImage));
		} catch (IOException ex) {
		}
		newSudokuItem.setMnemonic('N');
		newSudokuItem.setToolTipText("Ersetzt das momentane Sudoku durch ein neues.");
		newSudokuItem.setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK));
		newSudokuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int answer = JOptionPane.showConfirmDialog(null,
						"Bist du sicher, dass du mit einem neuen Sudoku starten m�chtest? "
								+ "Das momentane Sudoku wird nicht gespeichert.",
						"Neues Sudoku best�tigen", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				if (answer == JOptionPane.OK_OPTION) {
					mainFrame.dispose();
					String oldLength = length + "";
					String oldDifficulty = sudoku.getDifficulty() + "";
					SudokuMain.main(new String[] { oldLength, oldDifficulty });
				}
			}
		});
		fileMenu.add(newSudokuItem);

		JMenuItem saveItem = new JMenuItem("Speichern");
		try {
			BufferedImage saveImage = ImageIO.read(mainFrame.getClass().getResourceAsStream("/icons/saveIcon.png"));
			saveItem.setIcon(new ImageIcon(saveImage));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		saveItem.setMnemonic('S');
		saveItem.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));
		saveItem.setToolTipText("Speichere das Sudoku in einer Textdatei.");

		final SudokuGUI gui = this;
		saveItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int returnVal = chooser.showDialog(mainFrame, "Speichern");

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String path = chooser.getSelectedFile().getAbsolutePath();
					FileIO.save(gui, sudoku, path);
				}
			}
		});
		fileMenu.add(saveItem);

		JMenuItem loadItem = new JMenuItem("Laden");
		try {
			BufferedImage loadIcon = ImageIO.read(mainFrame.getClass().getResourceAsStream("/icons/loadIcon.png"));
			loadItem.setIcon(new ImageIcon(loadIcon));
		} catch (IOException ex) {
		}
		loadItem.setMnemonic('L');
		loadItem.setAccelerator(KeyStroke.getKeyStroke('L', InputEvent.CTRL_DOWN_MASK));
		loadItem.setToolTipText("Lade ein gespeichertes Sudoku.");
		loadItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int returnVal = chooser.showDialog(mainFrame, "Laden");

				String path;

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					path = chooser.getSelectedFile().getAbsolutePath();
					if (FileIO.load(path)) {
						mainFrame.dispose();
					}

				}
			}
		});
		fileMenu.add(loadItem);

		JMenuItem printItem = new JMenuItem("Drucken");
		try {
			BufferedImage printImage = ImageIO.read(mainFrame.getClass().getResourceAsStream("/icons/printIcon.png"));
			printItem.setIcon(new ImageIcon(printImage));
		} catch (IOException ex) {
		}
		printItem.setMnemonic('D');
		printItem.setAccelerator(KeyStroke.getKeyStroke('D', InputEvent.CTRL_DOWN_MASK));
		printItem.setToolTipText("Drucke das aktuelle Sudoku auf eine DIN A4 Seite skaliert aus.");
		printItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				print();
			}
		});
		fileMenu.add(printItem);

		JMenuItem helpFillItem = new JMenuItem("noch nicht implementiert");
		helpMenu.add(helpFillItem);

		// create the rest panel
		JPanel restPanel = new JPanel();
		// restPanel.setPreferredSize(new Dimension(0, topHeight));
		restPanel.setLayout(new BoxLayout(restPanel, BoxLayout.X_AXIS));
		paintable.add(restPanel);
		menuBar.add(restPanel);

		restPanel.add(Box.createHorizontalGlue()); // a spacer

		JLabel difficultyLabel = new JLabel("Schwierigkeitsgrad: " + sudoku.getDifficulty());
		// difficultyLabel.setPreferredSize(new Dimension(0, topHeight));
		difficultyLabel.setFont(labelFont);
		paintable.add(difficultyLabel);
		restPanel.add(difficultyLabel);

		restPanel.add(Box.createHorizontalGlue()); // a spacer

		JLabel lengthLabel = new JLabel("L�nge: " + length);
		// lengthLabel.setPreferredSize(new Dimension(0, topHeight));
		lengthLabel.setFont(labelFont);
		paintable.add(lengthLabel);
		restPanel.add(lengthLabel);

		restPanel.add(Box.createHorizontalGlue()); // a spacer

		JLabel fillInfoLabel = new JLabel(
				sudoku.count() + " von " + length * length + " Feldern sind zu Beginn bef�llt.");
		// fillInfoLabel.setPreferredSize(new Dimension(0, topHeight));
		fillInfoLabel.setFont(labelFont);
		paintable.add(fillInfoLabel);
		restPanel.add(fillInfoLabel);

		restPanel.add(Box.createHorizontalGlue()); // a spacer

		timerLabel = new JLabel();
		// timerLabel.setPreferredSize(new Dimension(0, topHeight));
		timerLabel.setFont(labelFont);
		ActionListener timerListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				long nanoSeconds = System.nanoTime() - startTime;
				long seconds = nanoSeconds / (long) 1000000000;
				long minutes = seconds / 60;
				seconds = seconds % 60;
				long hours = minutes / 60;
				minutes = minutes % 60;

				if (hours == 0) { // less than an hour
					if (seconds < 10) {
						if (minutes < 10) {
							timerLabel.setText("Zeit gespielt: 0" + minutes + ":0" + seconds);
						} else {
							timerLabel.setText("Zeit gespielt: " + minutes + ":0" + seconds);
						}
					} else {
						if (minutes < 10) {
							timerLabel.setText("Zeit gespielt: 0" + minutes + ":" + seconds);
						} else {
							timerLabel.setText("Zeit gespielt: " + minutes + ":" + seconds);
						}
					}
				} else { // more than an hour
					if (seconds < 10) {
						if (minutes < 10) {
							timerLabel.setText("Zeit gespielt: " + hours + ":0" + minutes + ":0" + seconds);
						} else {
							timerLabel.setText("Zeit gespielt: " + hours + ":" + minutes + ":0" + seconds);
						}
					} else {
						if (minutes < 10) {
							timerLabel.setText("Zeit gespielt: 0" + hours + ":0" + minutes + ":" + seconds);
						} else {
							timerLabel.setText("Zeit gespielt: " + +minutes + ":" + seconds);
						}
					}
				}
			}
		};
		timer = new Timer(1000, timerListener); // update the time once a second
		paintable.add(timerLabel);
		restPanel.add(timerLabel);

		restPanel.add(Box.createHorizontalGlue()); // a spacer
	}

	/** Generates the right panel of the main frame */
	private void buildRightPanel() {
		rightPanel = new JPanel();
		mainFrame.add(BorderLayout.EAST, rightPanel);
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		paintable.add(rightPanel);

		// generate the main panel of the rightPanel
		JPanel mainPanel = new JPanel();
		rightPanel.add(mainPanel);
		mainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		mainPanel.setBorder(new EmptyBorder(20, 15, 5, 15)); // create padding
		paintable.add(mainPanel);

		JPanel numberPanel = new JPanel(new GridLayout(length, 1));
		paintable.add(numberPanel);
		mainPanel.add(numberPanel);

		JPanel amountPanel = new JPanel(new GridLayout(length, 1));
		paintable.add(amountPanel);
		mainPanel.add(amountPanel);

		numberDisplay = new JButton[length];
		amountDisplay = new JTextField[length];
		Dimension preferredDimension = new Dimension(50, 0);
		for (int i = 1; i <= length; i++) {
			// add a new number button
			JButton numberButton = new JButton(i + "er: ");
			numberButton.setToolTipText("Hebt alle " + numberButton.getText().substring(0, 3) + " hervor.");
			numberButton.setMinimumSize(preferredDimension);
			numberButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			numberButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ev) {
					JButton button = (JButton) ev.getSource();
					String text = button.getText();
					// cutting off the "er"
					text = text.substring(0, text.length() - 4);
					int number = Integer.parseInt(text);

					outline(number);
				}
			});
			numberDisplay[i - 1] = numberButton;
			numberPanel.add(numberButton);

			// add a new amount field
			JTextField amountField = new JTextField();
			amountField.setPreferredSize(preferredDimension);
			amountField.setHorizontalAlignment(JLabel.CENTER);
			amountField.setBorder(standardBorder);
			amountField.setEditable(false);
			amountField.setFont(noteFont);
			amountField.setForeground(Color.BLACK);
			amountDisplay[i - 1] = amountField;
			amountPanel.add(amountField);
		}

		// the count downwards radio button
		countDownwardsButton = new JRadioButton("Runterz�hlen");
		rightPanel.add(countDownwardsButton);
		countDownwardsButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		countDownwardsButton.setBorder(new EmptyBorder(0, 15, 0, 10));
		countDownwardsButton.setFont(labelFont);
		countDownwardsButton.setToolTipText(
				"Entscheidet, ob die rechte Anzeige" + "die Anzahl der eingef�gten Zahlen hochz�hlt oder ob die"
						+ "noch fehlenden Zahlen angezeigt werden, also runtergez�hlt wird.");
		countDownwardsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		countDownwardsButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				updateRightPanel();
			}
		});
		paintable.add(countDownwardsButton);
	}

	/** Generates the bottom panel of the main frame. */
	private void buildBottomPanel() {
		bottomPanel = new JPanel();
		paintable.add(bottomPanel);

		// check for mistakes button
		JButton checkForMistakesButton = new JButton("Fehlersuche");
		checkForMistakesButton.setToolTipText("Sucht nach falsch bef�llten "
				+ "Feldern und hinterlegt diese rot. Doppelklick macht dies " + "r�ckg�ngig.");
		checkForMistakesButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		checkForMistakesButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent click) {
				if (click.getClickCount() == 2) {
					// clear all red painted fields
					for (int i = 0; i < length; i++) {
						for (int j = 0; j < length; j++) {
							SudokuField currentField = boardGraphic[i][j];
							if (currentField.getBackground().equals(Color.RED)) {
								currentField.setBackground(Color.WHITE);
							} else if (currentField.getBackground().equals(Color.RED.darker())) {
								currentField.setBackground(Color.WHITE.darker());
							}
						}
					}
				} else {
					checkForMistakes();
				}
			}
		});
		bottomPanel.add(checkForMistakesButton);

		// show possibilities button
		showPossibilitiesButton = new JRadioButton("M�glichkeiten anzeigen");
		showPossibilitiesButton.setToolTipText("Zeigt in allen leeren Feldern und Notizfeldern die dortigen "
				+ "M�glichkeiten an, wenn ausgew�hlt und aktualisiert diese bei neuen Eintr�gen.");
		showPossibilitiesButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		showPossibilitiesButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent click) {
				JRadioButton possibilityButton = (JRadioButton) click.getSource();
				if (possibilityButton.isSelected()) {
					showPossibilities();
				} else {
					removePossibilities();
				}
			}
		});

		bottomPanel.add(showPossibilitiesButton);

		// highlight possibility pairs button
		JButton highlightPossibilityPairsButton = new JButton("M�glichkeitspaare");
		highlightPossibilityPairsButton
				.setToolTipText("Hebt eingetragene " + "M�glichkeitspaare, die mindestens 2 mal vorkommen, farblich "
						+ "hervor. Doppelklick macht dies r�ckg�ngig.");
		highlightPossibilityPairsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		highlightPossibilityPairsButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent click) {
				if (click.getClickCount() == 2) {
					// double click to undo
					clearPossibilityPairHighlighting();
				} else {
					highlightPossibilityPairs();
				}
			}
		});
		bottomPanel.add(highlightPossibilityPairsButton);

		// solution button
		JButton solutionButton = new JButton("L�sung");
		solutionButton.setToolTipText("�ffnet ein kleines zus�tzliches Fenster, in dem die L�sung des Sudokus "
				+ "nachgeschlagen werden kann.");
		solutionButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		solutionButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SolutionWindow solutionWindow = new SolutionWindow(sudoku.getBoardSolved(), sudoku.getBoard(),
						fieldStandardColor);
				solutionWindow.setVisible(true);
			}
		});
		bottomPanel.add(solutionButton);

		// reset button
		JButton resetButton = new JButton("Reset");
		resetButton.setToolTipText("Setzt das Sudoku auf den Ausgangszustand zur�ck.");
		resetButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		resetButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int answer = JOptionPane.showConfirmDialog(null,
						"Bist du sicher, dass du das Sudoku resetten m�chtest? Alle deine Eintr�ge "
								+ "und Einf�rbungen werden entfernt.",
						"Reset best�tigen", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				if (answer == JOptionPane.OK_OPTION) {
					for (int i = 0; i < length; i++) {
						for (int j = 0; j < length; j++) {
							// check if the original Sudoku's field is empty
							if (sudoku.getBoard(i, j) == 0) {
								boardGraphic[i][j].setText("");
								boardGraphic[i][j].setBackground(Color.WHITE);
							}
						}
					}

					sudoku.resetCurrentState();
					updateRightPanel();
					outline(-1);
					startTime = System.nanoTime();
					timer.start();
				}
			}
		});
		bottomPanel.add(resetButton);

		// add the bottom panel to the main frame
		mainFrame.add(BorderLayout.SOUTH, bottomPanel);
	}

	/** Generates the left panel of the main frame. */
	private void buildLeftPanel() {
		leftPanel = new JPanel();
		leftPanel.setPreferredSize(new Dimension(170, centerPanel.getHeight()));
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		mainFrame.add(BorderLayout.WEST, leftPanel);
		paintable.add(leftPanel);

		String[] colorStrings = { "schwarz", "dunkelgrau", "grau", "wei�", "pink", "rot", "orange", "gelb", "gr�n",
				"dunkelgr�n", "blau", "mehr Farben" };

		ActionListener boxListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox<String> selectedBox = (JComboBox<String>) e.getSource();
				int selectedIndex = selectedBox.getSelectedIndex();

				Color previousColor = Color.WHITE;
				switch (selectedBox.getName()) {
				case "foreground":
					previousColor = foregroundColor;
					break;
				case "background":
					previousColor = backgroundColor;
					break;
				case "fieldStandardColor":
					previousColor = fieldStandardColor;
					break;
				case "fieldNoteColor":
					previousColor = fieldNoteColor;
					break;
				}

				Color selectedColor;
				if (selectedIndex == 11) { // "mehr Farben"
					selectedColor = JColorChooser.showDialog(mainFrame, "W�hle deine gew�nschte Farbe", previousColor);
					if (selectedColor == null) { // abbrechen button pressed
						selectedColor = previousColor;
					}
				} else {
					selectedColor = indexToColor.get(selectedIndex);
				}

				switch (selectedBox.getName()) {
				case "foreground":
					foregroundColor = selectedColor;
					updateForeground();
					break;
				case "background":
					backgroundColor = selectedColor;
					updateBackground();
					break;
				case "fieldStandardColor":
					fieldStandardColor = selectedColor;
					updateFieldStandard();
					break;
				case "fieldNoteColor":
					fieldNoteColor = selectedColor;
					updateFieldNote();
					break;
				}
			}
		};
		// -------------------------------------------------------------------------------------------------
		JLabel foregroundColorLabel = new JLabel("Schriftfarbe");
		foregroundColorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		foregroundColorLabel.setFont(labelFont);
		paintable.add(foregroundColorLabel);

		foregroundBox = new JComboBox<String>(colorStrings);

		foregroundBox.setName("foreground");
		foregroundBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		foregroundBox.addActionListener(boxListener);
		foregroundBox.setEditable(true);
		foregroundBox.setSelectedIndex(3); // standard choice (white)

		putIntoPanel(foregroundColorLabel, foregroundBox);
		// -------------------------------------------------------------------------------------------------
		JLabel backgroundColorLabel = new JLabel("Hintergrundfarbe");
		backgroundColorLabel.setFont(labelFont);
		paintable.add(backgroundColorLabel);

		backgroundBox = new JComboBox<String>(colorStrings);
		backgroundBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		backgroundBox.setName("background");
		backgroundBox.addActionListener(boxListener);
		backgroundBox.setEditable(true);
		backgroundBox.setSelectedIndex(1); // standard choice (dark gray)
		putIntoPanel(backgroundColorLabel, backgroundBox);
		// -------------------------------------------------------------------------------------------------
		JLabel darkenLabel = new JLabel("Verdunklungsgrad");
		darkenLabel.setFont(labelFont);
		paintable.add(darkenLabel);

		String[] darkenOptions = { "gar nicht", "teilweise", "vollst�ndig" };
		darkenBox = new JComboBox<String>(darkenOptions);

		darkenBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		darkenBox.setToolTipText("Entscheidet inwieweit das herausstellen von"
				+ " Zahlen durch die rechten Kn�pfe die dadurch blockierten Felder verdunkelt.");
		darkenBox.setSelectedIndex(1); // standard choice (partly)
		darkenBox.setEditable(true);
		darkenBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				switch (darkenBox.getSelectedIndex()) {
				case (0):
					darkenMode = DarkenStatus.NONE;
					break;
				case (1):
					darkenMode = DarkenStatus.PARTLY;
					break;
				case (2):
					darkenMode = DarkenStatus.COMPLETE;
					break;
				}
				int lastOutline = currentOutline;
				// to prevent the method from undoing the last outline
				currentOutline = -1;
				outline(lastOutline);
			}
		});
		putIntoPanel(darkenLabel, darkenBox);
		// -------------------------------------------------------------------------------------------------
		JLabel fieldStandardColorLabel = new JLabel("Standardzahlenfarbe");

		fieldStandardColorLabel.setFont(labelFont);
		paintable.add(fieldStandardColorLabel);

		fieldStandardColorBox = new JComboBox<String>(colorStrings);

		fieldStandardColorBox.setName("fieldStandardColor");
		fieldStandardColorBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		fieldStandardColorBox.addActionListener(boxListener);
		fieldStandardColorBox.setEditable(true);
		fieldStandardColorBox.setSelectedIndex(10); // standard choice (blue)
		putIntoPanel(fieldStandardColorLabel, fieldStandardColorBox);
		// -------------------------------------------------------------------------------------------------
		JLabel fieldNoteColorLabel = new JLabel("Notizzahlenfarbe");

		fieldNoteColorLabel.setFont(labelFont);
		paintable.add(fieldNoteColorLabel);

		fieldNoteColorBox = new JComboBox<String>(colorStrings);

		fieldNoteColorBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		fieldNoteColorBox.setName("fieldNoteColor");
		fieldNoteColorBox.addActionListener(boxListener);
		fieldNoteColorBox.setEditable(true);
		fieldNoteColorBox.setSelectedIndex(10); // standard choice (blue)
		putIntoPanel(fieldNoteColorLabel, fieldNoteColorBox);
	}

	/**
	 * Puts the inserted components into a JPanel, which has a left and right
	 * padding of 10px and a top and bottom padding of 20px. The panel itself is
	 * added to the paintable list and to the left panel.
	 * 
	 * @param describer
	 *            the describing component attached
	 * @param component
	 *            the component to become nested and added
	 */
	private void putIntoPanel(Component describer, Component component) {
		int xPadding = 10;
		int yPadding = 20;
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		paintable.add(mainPanel);

		// top padding
		mainPanel.add(Box.createVerticalGlue());
		// mainPanel.add(Box.createRigidArea(new Dimension(0, yPadding)));

		// first component
		if (describer != null) {
			JPanel panel1 = new JPanel();
			panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
			panel1.setAlignmentX(Component.LEFT_ALIGNMENT);
			panel1.setPreferredSize(new Dimension(100, 40));
			panel1.setMaximumSize(new Dimension(170, 50));
			panel1.add(Box.createRigidArea(new Dimension(xPadding, 0)));
			panel1.add(describer);
			panel1.add(Box.createRigidArea(new Dimension(xPadding, 0)));
			paintable.add(panel1);
			mainPanel.add(panel1);

			// internal spacer
			mainPanel.add(Box.createVerticalGlue());
			// mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		}

		JPanel panel2 = new JPanel();
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
		panel2.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel2.setPreferredSize(new Dimension(100, 40));
		panel2.setMaximumSize(new Dimension(170, 50));
		panel2.add(Box.createRigidArea(new Dimension(xPadding, 0)));
		panel2.add(component);
		panel2.add(Box.createRigidArea(new Dimension(xPadding, 0)));
		paintable.add(panel2);
		mainPanel.add(panel2);

		// bottom padding
		mainPanel.add(Box.createRigidArea(new Dimension(0, yPadding)));

		leftPanel.add(mainPanel);
	}

	/**
	 * Generates the centerPanel of the mainFrame and the boardGraphic of the
	 * Window, which contains the SudokuFields on which the game is played.
	 */
	private void buildCenterPanel() {
		centerPanel = new JPanel(new GridLayout(sudoku.getBlockLength(), sudoku.getBlockLength(), gap, gap));
		paintable.add(centerPanel);

		// generating the blocks
		for (int blockNumber = 0; blockNumber < length; blockNumber++) {
			JPanel block = new JPanel();
			block.setLayout(new GridLayout(sudoku.getBlockLength(), sudoku.getBlockLength()));

			int iStartValue = sudoku.getBlockLength() * (blockNumber / sudoku.getBlockLength());
			int jStartValue = sudoku.getBlockLength() * (blockNumber % sudoku.getBlockLength());
			for (int i = iStartValue; i < iStartValue + sudoku.getBlockLength(); i++) {
				for (int j = jStartValue; j < jStartValue + sudoku.getBlockLength(); j++) {
					// giving each block its fields
					SudokuField field = new SudokuField(i, j, this);
					field.setBorder(standardBorder);

					boardGraphic[i][j] = field;
					block.add(field);
				}
			}
			centerPanel.add(BorderLayout.CENTER, block); // adding the blocks to
															// the center panel
		}
		mainFrame.add(BorderLayout.CENTER, centerPanel); // adding the panel to
															// the main frame
	}

	/**
	 * Sets the window visible or hides it depending on the input. It also
	 * starts the timer to display how long the player has played and sets the
	 * Focus in a middle field.
	 */
	public void setVisible(boolean arg) {
		updateRightPanel();
		mainFrame.setVisible(arg);
		startTime = System.nanoTime();
		timer.start();
		setFocusInMiddle();
	}

	/**
	 * Counts the current amount of the particular numbers inserted in the
	 * Sudoku and updates the display. Originally inserted fields are also
	 * regarded by doing so.
	 */
	void updateRightPanel() {
		boolean downwards = countDownwardsButton.isSelected();
		for (int number = 1; number <= length; number++) {
			int amount = 0;
			for (int i = 0; i < length; i++) {
				for (int j = 0; j < length; j++) {
					String text = boardGraphic[i][j].getText();
					text = text.trim();
					try {
						if (Integer.parseInt(text) == number && boardGraphic[i][j].getFont().equals(standardFont)) {
							amount++;
						}
					} catch (NumberFormatException e) {
					} catch (NullPointerException f) {
					}
				}
			}
			JTextField actualTextField = amountDisplay[number - 1];
			if (downwards) {
				actualTextField.setText(String.valueOf(length - amount));
			} else {
				actualTextField.setText(String.valueOf(amount));
			}
			// test if the number is inserted too often
			if (Integer.parseInt(actualTextField.getText().trim()) > length
					|| Integer.parseInt(actualTextField.getText().trim()) < 0) {
				actualTextField.setForeground(Color.RED);
			} else {
				actualTextField.setForeground(Color.BLACK);
			}
		}
	}

	/**
	 * Outlines all displayed Sudoku fields with the inserted number if their
	 * input is a final input (they have the standardFont). All fields of the
	 * boardGraphic are checked by doing so. All other outlines are undone and
	 * if the inserted number is already outlined, it is also undone.
	 * 
	 * @param newOutline
	 *            The number whose holding fields shall become outlined
	 */
	void outline(int newOutline) {
		// remove previous darkening
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				boardGraphic[i][j].removeDarkening();
			}
		}

		// handle the borders and new darkening
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				SudokuField currentField = boardGraphic[i][j];
				String text = currentField.getText().trim();

				try {
					if (!text.equals("") && !boardGraphic[i][j].getFont().equals(noteFont)
							&& Integer.parseInt(text) == newOutline && currentOutline != newOutline) {

						boardGraphic[i][j].setBorder(outlineBorder);
						darken(i, j);

					} else {
						boardGraphic[i][j].setBorder(standardBorder);
					}
				} catch (NumberFormatException e) {
					// occurs for instance if the field contains the text "1 3"
					boardGraphic[i][j].setBorder(standardBorder);
				}
			}
		}

		if (currentOutline == newOutline) {
			currentOutline = -1;
		} else {
			currentOutline = newOutline;
		}
	}

	/**
	 * Darkens the blocked fields depending on the darken status.
	 * 
	 * @param iCoord
	 *            The i coordinate of the based field
	 * @param jCoord
	 *            The j coordinate of the based field
	 */
	private void darken(int iCoord, int jCoord) {

		if (darkenMode != DarkenStatus.NONE) {

			Collection<Coordinate> blocked = sudoku.getBlocked(new Coordinate(iCoord, jCoord));

			if (darkenMode == DarkenStatus.COMPLETE) {
				blocked.addAll(sudoku.getFilled());
			}

			blocked.forEach(coord -> boardGraphic[coord.getICoord()][coord.getJCoord()].darken());
		}
	}

	/**
	 * Updates the foreground color of all components in the "paintable" list.
	 * The attribute foregroundColor is used to determine the color.
	 */
	private void updateForeground() {
		for (Component i : paintable) {
			i.setForeground(foregroundColor);
		}
	}

	/**
	 * Updates the background color of all components in the "paintable" list.
	 * The attribute backgroundColor is used to determine the color.
	 */
	private void updateBackground() {
		for (Component i : paintable) {
			i.setBackground(backgroundColor);
		}
	}

	/**
	 * Updates the foreground color of all editable Sudoku fields in the
	 * boardGraphic with standard font. The attribute fieldStandardColor is used
	 * to determine the color.
	 */
	private void updateFieldStandard() {
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				SudokuField currentField = boardGraphic[i][j];
				if (currentField.isEditable() && currentField.getFont().equals(standardFont)) {
					currentField.setForeground(fieldStandardColor);
				}
			}
		}
	}

	/**
	 * Updates the foreground color of all editable Sudoku fields in the
	 * boardGraphic with note font. The attribute fieldNoteColor is used to
	 * determine the color.
	 */
	private void updateFieldNote() {
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				SudokuField currentField = boardGraphic[i][j];
				if (currentField.getFont().equals(noteFont)) {
					currentField.setForeground(fieldNoteColor);
				}
			}
		}
	}

	/**
	 * Determines the width to the next editable field in the specified
	 * direction of the specified field.
	 * 
	 * @param iCoord
	 *            The iCoord of the current field
	 * @param jCoord
	 *            The jCoord of the current field
	 * @param direction
	 *            The direction in which to look (0 means top, 1 means top
	 *            right,...)
	 * @return The distance in fields to the next editable field. 0 if there are
	 *         none in this direction.
	 */
	int jumpWidth(int iCoord, int jCoord, Direction direction) {
		int jumpWidth = 0;
		try {
			do {
				switch (direction) {
				case UP:
					iCoord--;
					break;
				case UP_RIGHT:
					iCoord--;
					jCoord++;
					break;
				case RIGHT:
					jCoord++;
					break;
				case RIGHT_DOWN:
					iCoord++;
					jCoord++;
					break;
				case DOWN:
					iCoord++;
					break;
				case DOWN_LEFT:
					iCoord++;
					jCoord--;
					break;
				case LEFT:
					jCoord--;
					break;
				case LEFT_UP:
					iCoord--;
					jCoord--;
					break;
				default:
					break;
				}
				jumpWidth++;
			} while (!boardGraphic[iCoord][jCoord].isEditable());
		} catch (IndexOutOfBoundsException e) {
			return 0; // Can't jump in this direction over all non-editable
						// fields without leaving the board
		}
		return jumpWidth;
	}

	/**
	 * Tries to set the focus in the middle of the boardGraphic. If that field
	 * is not editable, other editable fields are searched, which are as close
	 * as possible to the middle.
	 */
	private void setFocusInMiddle() {
		int middle = (length - 1) / 2; // try the middle field
		if (getBoardGraphic(middle, middle).isEditable()) {
			getBoardGraphic(middle, middle).requestFocus();
			return;
		}

		for (int distance = 1; distance < middle; distance++) { // increase the
																// radius
			for (Direction d : Direction.values()) { // search in a circle
														// starting top, going
														// clockwise
				if (jumpWidth(middle, middle, d) == distance) {
					moveFocus(middle, middle, d, distance);
					return;
				}
			}
		}
	}

	/**
	 * Moves the focus in the specified direction by the specified distance.
	 * 
	 * @param iCoord
	 *            The iCoord of the field which had the focus until now
	 * @param jCoord
	 *            The jCoord of the field which had the focus until now
	 * @param direction
	 *            The direction in which the focus should move. (0 means top, 1
	 *            means top right,...)
	 * @param jumpWidth
	 *            The distance in fields how far the focus should move.
	 */
	void moveFocus(int iCoord, int jCoord, Direction direction, int jumpWidth) {
		switch (direction) {
		case UP:
			boardGraphic[iCoord - jumpWidth][jCoord].requestFocus();
			break;
		case UP_RIGHT:
			boardGraphic[iCoord - jumpWidth][jCoord + jumpWidth].requestFocus();
			break;
		case RIGHT:
			boardGraphic[iCoord][jCoord + jumpWidth].requestFocus();
			break;
		case RIGHT_DOWN:
			boardGraphic[iCoord + jumpWidth][jCoord + jumpWidth].requestFocus();
			break;
		case DOWN:
			boardGraphic[iCoord + jumpWidth][jCoord].requestFocus();
			break;
		case DOWN_LEFT:
			boardGraphic[iCoord + jumpWidth][jCoord - jumpWidth].requestFocus();
			break;
		case LEFT:
			boardGraphic[iCoord][jCoord - jumpWidth].requestFocus();
			break;
		case LEFT_UP:
			boardGraphic[iCoord - jumpWidth][jCoord - jumpWidth].requestFocus();
			break;
		default:
			break;
		}
	}

	/**
	 * Opens an appropriate message window, which tells the user the inserted
	 * status.
	 * 
	 * @param status
	 *            The status which will be told to the user
	 */
	void showStatus(GameStatus status) {
		if (status == GameStatus.INCOMPLETE) {
			JOptionPane.showMessageDialog(null, "Das Sudoku ist noch nicht vollst�ndig bef�llt.", "Da fehlt noch was!",
					JOptionPane.INFORMATION_MESSAGE);

		} else if (status == GameStatus.CORRECT) {
			timer.stop();
			// play the success sound
			try {
				Clip clip = AudioSystem.getClip();
				AudioInputStream inputStream = AudioSystem
						.getAudioInputStream(clip.getClass().getResourceAsStream("/utilities/success.wav"));
				clip.open(inputStream);
				clip.start();
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
				System.out.println("Error trying to play sound!");
				e.printStackTrace();
			}

			// show the success pop up window
			String time = timerLabel.getText().substring(15, timerLabel.getText().length());
			JOptionPane.showMessageDialog(null, "Alles richtig! Gl�ckwunsch!!\r\nBen�tigte Zeit:" + time, "Gewonnen!",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "Sudoku ist noch nicht korrekt bef�llt.", "Stimmt so nicht ganz",
					JOptionPane.INFORMATION_MESSAGE);
		}

	}

	/**
	 * Opens a pop up window at the inserted field, in which the user can choose
	 * the background color for this field.
	 * 
	 * @param field
	 *            The chosen field where the window is opened and which is
	 *            affected
	 */
	void showColorChooser(SudokuField field) {
		Border oldBorder = field.getBorder(); // save the original border
		Border selectedBorder = new CompoundBorder(new LineBorder(Color.ORANGE, 2), standardBorder);
		field.setBorder(selectedBorder); // highlight the selected field

		JColorChooser cc = new JColorChooser(field.getBackground());
		cc.setPreviewPanel(new JPanel());
		AbstractColorChooserPanel[] chooserPanels = { new ColorChooserPanel(field) };
		cc.setChooserPanels(chooserPanels);

		// invoked if the user closes the dialog with OK
		ActionListener okListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent okPressed) {
				field.setBackground(cc.getColor());

				field.setBorder(oldBorder);
			}

		};

		// invoked if the user closes the dialog with Cancel
		ActionListener cancelListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				field.setBorder(oldBorder);
			}

		};

		JDialog ccDialog = JColorChooser.createDialog(field, "W�hle deine gew�nschte Farbe f�r dieses Feld", true, cc,
				okListener, cancelListener);

		// set the dialog to the right position
		int oldY = ccDialog.getY();
		int newY = oldY - ccDialog.getHeight() / 2 - 35;
		if (newY < 0) {
			newY = oldY + ccDialog.getHeight() / 2 + 40;
		}
		ccDialog.setLocation(ccDialog.getX(), newY);

		ccDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		ccDialog.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent arg0) {
				field.setBorder(oldBorder);
			}

		});

		ccDialog.setVisible(true);
	}

	/**
	 * Iterates through the Sudoku and searches for incorrectly filled fields.
	 * If it finds one, the background color is changed to RED. If the fields
	 * are filled correctly even though their background color is red, their
	 * background color is changed to white
	 */
	private void checkForMistakes() {
		Collection<Coordinate> mistakes = sudoku.getMistakes();

		mistakes.forEach(coord -> {
			SudokuField currentField = boardGraphic[coord.getICoord()][coord.getJCoord()];
			if (currentField.isDarkened()) {
				currentField.setBackground(Color.RED.darker());
			} else {
				currentField.setBackground(Color.RED);
			}
		});

	}

	/**
	 * Prints the centerPanel of this window as big as possible while being
	 * small enough to fit one page. The user is asked in a dialog window which
	 * printer he/she wants to use.
	 */
	private void print() {
		PrinterJob job = PrinterJob.getPrinterJob();
		job.setPrintable(new ComponentPrinter(centerPanel));

		if (job.printDialog()) {
			try {
				job.print();
			} catch (PrinterException e) {
				e.printStackTrace();
				System.out.println("Print problem occured.");
			}
		}
	}

	/**
	 * This method takes all fields of the boardGraphic with standard font into
	 * account and writes, depending on these, the remaining possibilities for
	 * each field into it.
	 */
	void showPossibilities() {
		// iterate all fields
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				// save the current field because it is often used
				SudokuField currentField = boardGraphic[i][j];

				if (currentField.getText().equals("") || currentField.getFont().equals(noteFont)) {
					currentField.setFont(noteFont);
					currentField.setForeground(fieldNoteColor);
					currentField.setText("");
					// check all possible insertions
					for (int k = 1; k <= length; k++) {
						if (SudokuSolver.isAllowed(sudoku.getCurrentState(), k, i, j)) {
							currentField.setText(currentField.getText() + k + " ");
						}
					}
					boardGraphic[i][j].setText(boardGraphic[i][j].getText().trim());
				}

			}
		}
	}

	/**
	 * Removes all possibility entries on this board, which are not excluded by
	 * being painted in a different color than white and not darkened.
	 */
	private void removePossibilities() {
		// iterate all fields
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {

				SudokuField currentField = boardGraphic[i][j];
				Color cB = currentField.getBackground();
				Font currentFont = currentField.getFont();
				boolean deletableColor = cB.equals(Color.WHITE) || cB.equals(Color.WHITE.darker());

				if (deletableColor && currentFont.equals(noteFont)) {
					currentField.setText("");
					currentField.setFont(standardFont);
					currentField.setForeground(fieldStandardColor);
				}

			}
		}
	}

	/**
	 * Highlights all possibility pairs, which occur at least two times in a
	 * respective color (Padi pls!). Possibility pair means, that the font of
	 * the field is noteFont and the amount of inserted possibilities is exact
	 * 2. The possibilities of a pair do not need to be in the same order.
	 */
	private void highlightPossibilityPairs() {
		clearPossibilityPairHighlighting();
		/**
		 * Map the possibility pairs to the coordinates of their occurrences.
		 */
		HashMap<String, LinkedList<Coordinate>> map = new HashMap<String, LinkedList<Coordinate>>();

		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {

				SudokuField currentField = boardGraphic[i][j];
				if (currentField.getFont().equals(noteFont)) {
					// cut off any unnecessary whitespaces
					String currentFieldContent = currentField.getText().trim().replaceAll("\\s+", " ");
					String[] possibilities = currentFieldContent.split(" ");
					int possibilityAmount = possibilities.length;

					if (possibilityAmount == 2) { // pair detected
						// sort the possibilities
						int[] parsedPossibilities = new int[possibilityAmount];
						for (int l = 0; l < possibilityAmount; l++) {
							parsedPossibilities[l] = Integer.parseInt(possibilities[l]);
						}
						// sort the entries to make them independent of the
						// inserted order
						Arrays.parallelSort(parsedPossibilities);
						String finalString = "";
						for (int l = 0; l < possibilityAmount; l++) {
							finalString += parsedPossibilities[l] + " ";
						}

						LinkedList<Coordinate> coordinates = map.get(finalString);

						// pair did not occur yet
						if (coordinates == null) {
							coordinates = new LinkedList<Coordinate>();
							coordinates.add(new Coordinate(i, j));
							map.put(finalString, coordinates);
						}
						// pair already occurred
						else {
							coordinates.add(new Coordinate(i, j));
						}

					}
				}

			}
		}

		// color all the same possibility pairs with the same color:
		int pairNumber = 0;
		for (LinkedList<Coordinate> coordinates : map.values()) {
			// only consider possibility pairs who occur at least two times
			// if (coordinates.size() >= 2) {
			Color color = nextColor(pairNumber);

			// iterate all coordinates of this possibility pair and color them
			while (!coordinates.isEmpty()) {
				Coordinate coord = coordinates.removeFirst();
				int i = coord.getICoord();
				int j = coord.getJCoord();
				boardGraphic[i][j].setBackground(color);

				possibilityPairsCoordinates.add(coord);
			}
			pairNumber++;
			// }
		}

	}

	/**
	 * Returns different colors, depending on which number is inserted. If the
	 * inserted number is out of the implemented bounds (0-8), a random color is
	 * returned, whose distance is big enough to previous returned colors.
	 * 
	 * @param colorNumber
	 *            Use different ones for different colors
	 * @return The correspondent color
	 */
	private Color nextColor(int colorNumber) {

		switch (colorNumber) {
		case 0:
			return new Color(102, 102, 255);
		case 1:
			return new Color(204, 204, 0);
		case 2:
			return new Color(153, 76, 0);
		case 3:
			return new Color(153, 255, 255);
		case 4:
			return new Color(204, 0, 204);
		case 5:
			return new Color(160, 160, 160);
		case 6:
			return new Color(0, 153, 76);
		case 7:
			return new Color(204, 153, 255);
		case 8:
			return new Color(102, 255, 102);
		default:
			boolean accepted = false;
			Color newColor = Color.RED; // the outcome should never be red
			while (!accepted) {
				// create new color parameters
				int r = (int) (Math.random() * 256);
				int g = (int) (Math.random() * 256);
				int b = (int) (Math.random() * 256);

				// check for similarity to already used colors
				for (Color savedColor : usedBackgroundColors) {
					int rDistance = savedColor.getRed() - r;
					int gDistance = savedColor.getGreen() - g;
					int bDistance = savedColor.getBlue() - b;
					double totalDistance = Math
							.sqrt(rDistance * rDistance + gDistance * gDistance + bDistance * bDistance);

					if (totalDistance < 200) {
						accepted = false;
					} else {
						accepted = true;
						newColor = new Color(r, g, b);
					}
				}

			}
			usedBackgroundColors.add(newColor);
			return newColor;
		}
	}

	private void clearPossibilityPairHighlighting() {
		for (Coordinate coord : possibilityPairsCoordinates) {
			int i = coord.getICoord();
			int j = coord.getJCoord();
			SudokuField currentField = boardGraphic[i][j];
			if (currentField.isDarkened()) {
				currentField.setBackground(Color.WHITE.darker());
			} else {
				boardGraphic[i][j].setBackground(Color.WHITE);
			}
		}
		possibilityPairsCoordinates.clear();

		usedBackgroundColors.clear();
		usedBackgroundColors.add(Color.RED);
		usedBackgroundColors.add(Color.WHITE);
		usedBackgroundColors.add(Color.WHITE.darker());
		usedBackgroundColors.add(fieldStandardColor);
		usedBackgroundColors.add(fieldNoteColor);
	}

	// ----------------------------------------------------------------------------------------------
	// Getter Methods:

	/**
	 * Returns the SudokuField at the specified coordinates of the board graphic
	 * of this window.
	 */
	public SudokuField getBoardGraphic(int iCoord, int jCoord) {
		return boardGraphic[iCoord][jCoord];
	}

	/** Returns the boardGraphic of this window. */
	public SudokuField[][] getBoardGraphic() {
		return this.boardGraphic;
	}

	public Color getFieldStandardColor() {
		return this.fieldStandardColor;
	}

	public Color getFieldNoteColor() {
		return this.fieldNoteColor;
	}

	public int getCurrentOutline() {
		return this.currentOutline;
	}

	public Keyboard getKeyboard() {
		return this.keyboard;
	}

	public Sudoku getSudoku() {
		return this.sudoku;
	}

	public Font getStandardFont() {
		return this.standardFont;
	}

	public Font getNoteFont() {
		return this.noteFont;
	}

	public Border getOutlineBorder() {
		return this.outlineBorder;
	}

	public String getEgg() {
		return this.egg;
	}

	public int getForegroundIndex() {
		return this.foregroundBox.getSelectedIndex();
	}

	public int getBackgroundIndex() {
		return this.backgroundBox.getSelectedIndex();
	}

	public int getFieldStandardColorIndex() {
		return this.fieldStandardColorBox.getSelectedIndex();
	}

	public int getFieldNoteColorIndex() {
		return this.fieldNoteColorBox.getSelectedIndex();
	}

	public boolean getCountDownwards() {
		return this.countDownwardsButton.isSelected();
	}

	public int getDarkenIndex() {
		return this.darkenBox.getSelectedIndex();
	}

	public long getStartTime() {
		return this.startTime;
	}
	
	public boolean getPossibilityMode(){
		return showPossibilitiesButton.isSelected();
	}

	// ----------------------------------------------------------------------------------------------

	// Setter Methods:

	/**
	 * Set the text of the field at the position k of the boardGraphic.
	 * 
	 * @param k
	 *            The coordinate of the field
	 * @param fieldText
	 *            The text which should be set
	 * @throws IllegalArgumentException
	 *             If the field at this coordinate does not exist
	 */
	void setBoardGraphic(String fieldText, int k) throws IllegalArgumentException {
		if (k >= 0 && k < length * length) {
			boardGraphic[k / length][k % length].setText(fieldText);
		} else {
			throw new IllegalArgumentException("k must be an Integer from 0 to length^2");
		}
	}

	/**
	 * Set the font of the field of the boardGraphic at the position k.
	 * 
	 * @param fontType
	 *            The new font type of the field. 1 means noteFont
	 * @param k
	 *            The Coordinate of the field
	 * @throws IllegalArgumentException
	 *             If the field at this Coordinate does not exist
	 */
	void setFont(int fontType, int k) throws IllegalArgumentException {
		if (k >= 0 && k < length * length) {
			if (fontType == 1) {
				boardGraphic[k / length][k % length].setFont(noteFont);
			}
		} else {
			throw new IllegalArgumentException("k must be an Integer from 0 to length^2");
		}
	}

	/**
	 * Set the background color of the field of the boardGraphic at the position
	 * k.
	 * 
	 * @param colorRepresentation
	 *            The RGS Representation of the color
	 * @param k
	 *            The coordinate at which to set the background color
	 * @throws IllegalArgumentException
	 *             if the field at this coordinate does not exist
	 */
	void setBackground(int colorRepresentation, int k) throws IllegalArgumentException {
		Color color = new Color(colorRepresentation);
		if (k >= 0 && k < length * length) {
			boardGraphic[k / length][k % length].setBackground(color);
		} else {
			throw new IllegalArgumentException("k must be an Integer from 0 to length^2");
		}
	}

	/**
	 * Sets the status of the darken checkbox and darkenMode of this window.
	 * 
	 * @param index
	 *            The new index of the darken checkbox
	 */
	void setDarkenMode(int index) {
		darkenBox.setSelectedIndex(index);
		darkenMode = DarkenStatus.values()[index];
	}

	/**
	 * Sets the foreground color for all components in the paintable list and
	 * updates them.
	 * 
	 * @param index
	 *            The index in the respective combo box
	 */
	void setForeground(int index) {
		foregroundBox.setSelectedIndex(index);
		foregroundColor = indexToColor.get(index);
		updateForeground();
	}

	/**
	 * Sets the background color for all components in the paintable list and
	 * updates them.
	 * 
	 * @param index
	 *            The index in the respective combo box
	 */
	void setBackground(int index) {
		backgroundBox.setSelectedIndex(index);
		backgroundColor = indexToColor.get(index);
		updateBackground();
	}

	/**
	 * Sets the standard foreground color for all sudoku fields of the
	 * boardGraphic and updates them.
	 * 
	 * @param index
	 *            The index in the respective combo box
	 */
	void setFieldStandardColor(int index) {
		fieldStandardColorBox.setSelectedIndex(index);
		fieldStandardColor = indexToColor.get(index);
		updateFieldStandard();
	}

	/**
	 * Sets the note foreground color for all sudoku fields of the boardGraphic
	 * and updates them.
	 * 
	 * @param index
	 *            The index in the respective combo box
	 */
	void setFieldNoteColor(int index) {
		fieldNoteColorBox.setSelectedIndex(index);
		fieldNoteColor = indexToColor.get(index);
		updateFieldNote();
	}

	void setCurrentOutline(int newOutline) {
		currentOutline = newOutline;
	}

	void setKeyboard(Keyboard keyboard) {
		this.keyboard = keyboard;
	}

	void setEgg(String egg) {
		this.egg = egg;
	}

	void setCountDownwards(boolean arg) {
		this.countDownwardsButton.setSelected(arg);
	}

	void setStartTime(long newStartTime) {
		this.startTime = newStartTime;
	}
}