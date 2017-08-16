package gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

import logic.Sudoku;

public class FileIO {

	/**
	 * Saves the important data of the inserted sudokuGUI and its sudoku to the
	 * standard path: Next to the started jar file as SavaGame.sudoku.
	 * 
	 * @param sudokuGUI
	 *            The sudokuGUI object whose data should be saved
	 * @param sudoku
	 *            The associated sudoku object whose data should be saved
	 */
	public static void save(SudokuGUI sudokuGUI, Sudoku sudoku) {
		save(sudokuGUI, sudoku, ".SaveGame.sudoku");
	}

	/**
	 * Saves the important data of the inserted sudokuGUI and its sudoku to the
	 * specified path.
	 * 
	 * @param sudokuGUI
	 *            The sudokuGUI object whose data should be saved
	 * @param sudoku
	 *            The associated sudoku object whose data should be saved
	 * @param path
	 *            The path to which to write the save file
	 */
	public static void save(SudokuGUI sudokuGUI, Sudoku sudoku, String path) {

		PrintWriter writer = null;
		try { // checks if the last 7 letters of the path are ".sudoku". If they
				// are not, ".sudoku" is added to the path.
			if (!(path.substring(path.length() - 7).equals(".sudoku"))) {
				path = path + ".sudoku";
			}
			writer = new PrintWriter(new FileWriter(new File(path)));
			int fieldAmount = sudoku.getLength() * sudoku.getLength();

			// (line 1) save length and difficulty
			writer.print("SudokuSavegame;" + sudoku.getLength() + ";" + sudoku.getDifficulty());

			// (line 2) save the board of the sudoku
			writer.print("\r\nStartBoard;");
			for (int k = 0; k < fieldAmount; k++) {
				writer.print(sudoku.getStartValue(k) + ";");
			}

			// (line 3) save the boardSolved of the sudoku
			writer.print("\r\nSolution;");
			for (int k = 0; k < fieldAmount; k++) {
				writer.print(sudoku.getSolutionValue(k) + ";");
			}

			// (line 4) save the colors of the gui overlay list
			writer.print("\r\nInterface color indices;");
			writer.print(sudokuGUI.getForegroundIndex() + ";" + sudokuGUI.getBackgroundIndex());

			// (line 5) save the fonts of the boardGraphic
			writer.print("\r\nFonts;");
			for (int i = 0; i < sudoku.getLength(); i++) {
				for (int j = 0; j < sudoku.getLength(); j++) {
					if (sudokuGUI.getBoardGraphic()[i][j].getFont().equals(sudokuGUI.getNoteFont())) {
						writer.print("1;"); // save the noteFont as "1"
					} else {
						writer.print("0;"); // save the fieldFont as "0"
					}
				}
			}

			// (line 6) save the colors of the sudoku fields
			writer.print("\r\nSudoku fields color indices;");
			writer.print(sudokuGUI.getFieldStandardColorIndex() + ";" + sudokuGUI.getFieldNoteColorIndex());

			// (line 7) save the status of countDownwards checkbox
			writer.print("\r\nCountDownwards;");
			writer.print(sudokuGUI.getCountDownwards());

			// (line 8) save the status of the darken box
			writer.print("\r\nDarken;");
			writer.print(sudokuGUI.getDarkenIndex());

			// (line 9) save the board filled in by the user (boardGraphic)
			writer.print("\r\nUserBoard;");
			for (int i = 0; i < sudoku.getLength(); i++) {
				for (int j = 0; j < sudoku.getLength(); j++) {
					writer.print(sudokuGUI.getBoardGraphic(i, j).getText().trim() + ";");
				}
			}

			// (line 10) save the current outline
			writer.print("\r\nOutline;");
			writer.print(sudokuGUI.getCurrentOutline());

			// (line 11) save the background colors of the boardGraphic
			writer.print("\r\nField colors;");
			for (int i = 0; i < sudoku.getLength(); i++) {
				for (int j = 0; j < sudoku.getLength(); j++) {
					int RGBCode = sudokuGUI.getBoardGraphic(i, j).getBackground().getRGB();
					writer.print(RGBCode + ";");
				}
			}

			// (line 12) save the time the user has already spent solving this
			// Sudoku
			writer.print("\r\nTime;");
			writer.print(System.nanoTime() - sudokuGUI.getStartTime());

		} catch (IOException e) {
			System.err.println("Caught IOException: " + e.getMessage());
		} finally {
			if (writer != null) {
				writer.close();
			} else {
				System.err.println("PrintWriter not open");
			}
		}
	}

	/**
	 * Loads the saved data from a previous SudokuGUI instance. This data is
	 * used to build the previous sduokuGUI again, which is also set visible.
	 * 
	 * @param path
	 *            The absolute path from where to load
	 * @return if the loading process was successful
	 */
	static boolean load(String path) {
		BufferedReader reader;
		String[] thisLineArray;
		try {
			if (!(path.substring(path.length() - 7).equals(".sudoku"))) {
				path = path + ".sudoku";
			}
			reader = new BufferedReader(new FileReader(new File(path)));

			// (line 1) read length and difficulty
			thisLineArray = reader.readLine().split(";");

			// build a new Sudoku object based on the length and difficulty read
			Sudoku sudoku = new Sudoku(Integer.parseInt(thisLineArray[1]), Integer.parseInt(thisLineArray[2]));

			// (line 2) read the board of the Sudoku
			thisLineArray = reader.readLine().split(";");
			for (int k = 1; k < thisLineArray.length; k++) {
				if (!thisLineArray[k].equals("")) {
					sudoku.setStartValue(Integer.parseInt(thisLineArray[k]), k - 1);
				}
			}
			// (line 3) read the boardSolved of the Sudoku
			thisLineArray = reader.readLine().split(";");
			for (int k = 1; k < thisLineArray.length; k++) {
				sudoku.setSolutionValue(Integer.parseInt(thisLineArray[k]), k - 1);
			}

			// build the new window with the new Sudoku
			SudokuGUI window = new SudokuGUI(sudoku);

			// (line 4) load the colors of the gui overlay list
			thisLineArray = reader.readLine().split(";");
			window.setForeground(Integer.parseInt(thisLineArray[1]));
			window.setBackground(Integer.parseInt(thisLineArray[2]));

			// (line 5) load the fonts of the boardGraphic
			thisLineArray = reader.readLine().split(";");
			for (int k = 1; k < thisLineArray.length; k++) {
				window.setFont(Integer.parseInt(thisLineArray[k]), k - 1);
			}

			// (line 6) load the colors of the sudoku fields
			thisLineArray = reader.readLine().split(";");
			window.setFieldStandardColor(Integer.parseInt(thisLineArray[1]));
			window.setFieldNoteColor(Integer.parseInt(thisLineArray[2]));

			// (line 7) load the countDownwards checkbox status
			thisLineArray = reader.readLine().split(";");
			boolean countDownwards = Boolean.valueOf(thisLineArray[1]);
			window.setCountDownwards(countDownwards);
			window.updateRightPanel();

			// (line 8) load the status of the darken box
			thisLineArray = reader.readLine().split(";");
			window.setDarkenMode(Integer.valueOf(thisLineArray[1]));

			// (line 9) read the board filled by the user
			thisLineArray = reader.readLine().split(";");
			for (int k = 1; k < thisLineArray.length; k++) {
				window.setBoardGraphic(thisLineArray[k], k - 1);
			}
			// update the current state
			for (int i = 0; i < sudoku.getLength(); i++) {
				for (int j = 0; j < sudoku.getLength(); j++) {
					if (window.getBoardGraphic(i, j).getFont().equals(window.getStandardFont())) {
						int currentValue = 0;
						try {
							currentValue = Integer.parseInt(window.getBoardGraphic(i, j).getText());
						} catch (NumberFormatException ignored) {
						}
						sudoku.insertCurrentValue(currentValue, i, j);
					}
				}
			}
			sudoku.calculatePossibilities();

			// (line 10) load the current outline
			thisLineArray = reader.readLine().split(";");
			window.outline(Integer.parseInt(thisLineArray[1]));

			// (line 11) load the background colors of the boardGraphic
			thisLineArray = reader.readLine().split(";");
			for (int k = 1; k < thisLineArray.length; k++) {
				window.setBackground(Integer.parseInt(thisLineArray[k]), k - 1);
			}

			// (line 12) load the time already spent trying to solve the sudoku
			thisLineArray = reader.readLine().split(";");
			long additionalTime = Long.valueOf(thisLineArray[1]);

			reader.close();
			window.setVisible(true);
			window.setStartTime(window.getStartTime() - additionalTime);
		} catch (FileNotFoundException e) {
			System.err.println("load: File not found.");
			JOptionPane.showMessageDialog(null, "Datei nutzt das falsche Format! (erlaubt ist .sudoku)");
			return false;
		} catch (IOException e) {
			System.err.println("load: IOException");
			e.printStackTrace();
			return false;
		} catch (ArrayIndexOutOfBoundsException | NumberFormatException ex) {
			ex.printStackTrace();
			System.err.println("load: File is not written correctly.");
			JOptionPane.showMessageDialog(null, "Datei ist nicht korrekt abgespeichert.");
			return false;
		}
		return true;
	}

}
