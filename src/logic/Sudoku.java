package logic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import utilities.Coordinate;
import utilities.MathUtilities;
import utilities.GameStatus;

/**
 * Instances of this class calculate a solved Sudoku field (boardSolved) and a
 * sudoku board where some fields are erased, so a player can try solving it
 * (board). It is possible to create those instances with different lengths and
 * difficulties. To actually fill the Sudoku stumps, use the fill() method.
 * 
 * @author Dominik
 *
 */
public class Sudoku {
	/** length of the Sudoku (standard 9) */
	private int length;
	/** the length of a block (standard 3) */
	private int blockLength;
	/** the difficulty of the Sudoku */
	private int difficulty;

	/** the current state of the sudoku played by the user */
	private int[][] currentState;
	/** the Sudoku starting state when beginning to play */
	private int[][] startBoard;
	/** the solved Sudoku field */
	private int[][] solvedBoard;
	/** the solver used to fill the boards */
	private SudokuSolver solver;

	// -------------------------------------------------------------------------

	// Constructors
	/** Standard Constructor for a Sudoku. Uses length 9 and difficulty 5. */
	Sudoku() {
		this(9, 5);
	}

	/**
	 * Constructs a Sudoku with the standard length 9 and the inserted
	 * difficulty.
	 * 
	 * @param difficulty
	 *            How difficult the Sudoku is going to be
	 */
	Sudoku(int difficulty) {
		this(9, difficulty);
	}

	/**
	 * Creates a new Sudoku Object with the given length and difficulty. Empty
	 * boards are initialized and the attributes blockLength and stepWidth
	 * determined. Note that sudoku.fill() is required to actually fill the
	 * boards.
	 * 
	 * @param length
	 *            The length of the Sudoku (default is 9).
	 * @param difficulty
	 *            How difficult the Sudoku is going to be. Ranges from 1 to 10
	 *            (default is 5).
	 * @throws IllegalArgumentException
	 *             if the inserted length is no square number or the inserted
	 *             difficulty is out of bounds (1-10).
	 */
	public Sudoku(int length, int difficulty) throws IllegalArgumentException {
		if (Math.sqrt(length) % 1 != 0) {
			throw new IllegalArgumentException("length is no square number: " + length);
		}
		if (difficulty < 1 || difficulty > 10) {
			throw new IllegalArgumentException("difficulty out of bounds: " + difficulty + ". Allowed range is 1-10.");
		}
		this.length = length;
		this.difficulty = difficulty;
		this.currentState = new int[length][length];
		this.blockLength = (int) Math.sqrt(length);
		this.solver = new SudokuSolver(this);
	}

	// -------------------------------------------------------------------------

	/**
	 * Main generating method to fill an empty sudoku making it definitely
	 * solvable. The startBoard and solvedBoard are afterwards ready to go. It
	 * also prints the solution and the Sudoku to the system.out, as well as
	 * some additional info.
	 */
	public void fill() {
		solver.fill();
	}

	/** Counts the amount of filled fields (not containing 0). */
	public int count() {
		int count = 0;
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				if (startBoard[i][j] != 0) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * Returns a String representation of the normal Sudoku with empty fields
	 * displayed as 0.
	 */
	@Override
	public String toString() {
		return toString(true, false);
	}

	/**
	 * Returns a String representation of the Sudoku.
	 * 
	 * @param zeros
	 *            Determines if zeros are displayed or shown as spaces
	 * @param solution
	 *            Determines if the solved or the normal field is chosen
	 */
	public String toString(boolean zeros, boolean solution) {
		String represantation = "\n"; // an empty line to separate at the top
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				if (j % blockLength == 0 && j != 0) {
					// separate the blocks vertically with "|"s
					represantation += " |";
				}
				for (int k = MathUtilities.digits(length) - MathUtilities.digits(startBoard[i][j]) + 1; k > 0; k--) {
					represantation += " "; // add the right amount of spaces
				}
				if (solution) { // the solution field is demanded
					if (solvedBoard[i][j] == 0 && !zeros) {
						represantation += " ";
					} else {
						represantation += solvedBoard[i][j];
					}
				} else { // the normal field is demanded
					if (startBoard[i][j] == 0 && !zeros) {
						represantation += " ";
					} else {
						represantation += startBoard[i][j];
					}
				}
			}
			if (i % blockLength == blockLength - 1 && i != length - 1) {
				represantation += "\n ";
				for (int k = (MathUtilities.digits(length) + 1) * length + (blockLength - 1) * 2 - 1; k > 0; k--) {
					// separate the blocks horizontally with "-"s
					represantation += "-";
				}
			}
			represantation += "\n"; // new line
		}
		return represantation;
	}

	/**
	 * Prints the current Sudoku and the matching solution to the File
	 * "Sudoku.txt".
	 */
	public void printToTextFile(String path) {
		printToTextFile(path, "Sudoku.txt", "Solution.txt");
	}

	// currently not used
	/**
	 * Prints the current sudoku and the matching solution to the inserted file
	 * name.
	 * 
	 * @param path
	 *            The path leading to the folder of the generated text files
	 * @param fileName
	 *            The name of the destination file
	 * @param solutionFileName
	 *            The name of the solution file
	 */
	public void printToTextFile(String path, String fileName, String solutionFileName) {
		PrintWriter out = null;
		path = path.replace('\\', '/');
		try {
			out = new PrintWriter(new FileWriter(new File(path + fileName + ".txt")));
			out.println("Difficulty: " + difficulty);
			out.println(count() + " fields are filled.");
			out.print(toString(false, false)); // print the normal field
			out.close();
			out = new PrintWriter(new FileWriter(new File(path + solutionFileName + ".txt")));
			out.println("Solution:");
			out.print(toString(true, true)); // print the solution
		} catch (IOException e) {
			System.err.println("Caught IOException: " + e.getMessage());
		} finally {
			if (out != null) {
				out.close();
			} else {
				System.out.println("PrintWriter not open");
			}
		}
	}

	/**
	 * Returns all coordinates blocked by the inserted one.
	 * 
	 * @param initial
	 *            the initial coordinate
	 * @return a collection of all blocked coordinates
	 */
	public Collection<Coordinate> getBlocked(Coordinate initial) {

		Set<Coordinate> blocked = new HashSet<>();

		// iterate the column
		for (int i = 0; i < length; i++) {
			blocked.add(new Coordinate(i, initial.getJCoord()));
		}
		// iterate the row
		for (int j = 0; j < length; j++) {
			blocked.add(new Coordinate(initial.getICoord(), j));
		}
		// iterate the block
		int iStartValue = blockLength * (initial.getICoord() / blockLength);
		int jStartValue = blockLength * (initial.getJCoord() / blockLength);
		for (int i = iStartValue; i < iStartValue + blockLength; i++) {
			for (int j = jStartValue; j < jStartValue + blockLength; j++) {
				blocked.add(new Coordinate(i, j));
			}
		}

		return blocked;
	}

	public Collection<Coordinate> getFilled() {
		Set<Coordinate> filled = new HashSet<>();

		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				if (currentState[i][j] != 0) {
					filled.add(new Coordinate(i, j));
				}
			}
		}
		return filled;
	}

	public Collection<Coordinate> getMistakes() {
		LinkedList<Coordinate> mistakes = new LinkedList<Coordinate>();

		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				if (currentState[i][j] != solvedBoard[i][j] && currentState[i][j] != 0) {
					mistakes.add(new Coordinate(i, j));
				}
			}
		}
		return mistakes;
	}

	/**
	 * Checks the current status of the Sudoku: - Status.INCOMPLETE if not all
	 * fields of the sudoku are filled yet - Status.CORRECT if all fields of the
	 * sudoku are filled and they are correct - Status.INCORRECT if all fields
	 * of the sudoku are filled and at least one of them is incorrect
	 * 
	 * @return The current status of the sudoku
	 */
	public GameStatus getStatus() {
		if (!isFilledCompletely()) {
			return GameStatus.INCOMPLETE;
		} else if (isFilledCorrectly()) {
			return GameStatus.CORRECT;
		} else {
			return GameStatus.INCORRECT;
		}
	}

	/**
	 * Checks if the Sudoku is completely filled, meaning no ""s are found and
	 * returns the answer.
	 * 
	 * @return Whether the field is completely filled
	 */
	private boolean isFilledCompletely() {
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				if (currentState[i][j] == 0) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Checks if the Sudoku is completely filled correctly and returns the
	 * answer. The currentState is checked to be more precise.
	 * 
	 * @return Whether the Sudoku is finished correctly by the player
	 */
	private boolean isFilledCorrectly() {
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				if (currentState[i][j] != solvedBoard[i][j]) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Sets the currentState to the state of the startBoard.
	 */
	public void resetCurrentState() {
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				currentState[i][j] = startBoard[i][j];
			}
		}
	}

	// Methods necessary for communication with other classes
	// Getter-Methods:
	/**
	 * A getter method to get the value of the specified field of the Sudoku
	 * 
	 * @param iCoord
	 *            The i Coordinate of the demanded field
	 * @param jCoord
	 *            The j Coordinate of the demanded field
	 * @return The value of the demanded field
	 */
	public int getBoard(int iCoord, int jCoord) {
		if (iCoord < length && iCoord >= 0 && jCoord < length && jCoord >= 0) {
			return this.startBoard[iCoord][jCoord];
		} else {
			throw new IllegalArgumentException("iCoord " + iCoord + ", jCoord " + jCoord);
		}
	}

	/**
	 * A getter method to get the value of the specified field of the Sudoku
	 * 
	 * @param coord
	 *            The coordinate of the field whose value is demanded
	 * @return The demanded value
	 */
	public int getBoard(int coord) throws IllegalArgumentException {
		if (coord >= 0 && coord < length * length) {
			return this.startBoard[coord / length][coord % length];
		} else {
			throw new IllegalArgumentException("coord " + coord);
		}
	}


	public int[][] getValues() {
		return startBoard;
	}

	public int[][] getBoardSolved() {
		return solvedBoard;
	}

	public int getBoardSolved(int coord) {
		return solvedBoard[coord / length][coord % length];
	}

	public int getBoardSolved(int iCoord, int jCoord) {
		return solvedBoard[iCoord][jCoord];
	}

	public int getLength() {
		return length;
	}

	public int getBlockLength() {
		return blockLength;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public int[][] getBoard() {
		return startBoard;
	}

	public int[][] getCurrentState() {
		return this.currentState;
	}

	// Setter-Methods
	/**
	 * Inserts an Integer at the specified Coordinate in values[][]
	 * 
	 * @param value
	 *            The inserted Value
	 * @param coord
	 *            Coordinate
	 */
	public void setBoard(int value, int coord) throws IllegalArgumentException {
		if (coord < length * length && coord >= 0 && value <= length && value >= 0) {
			startBoard[coord / length][coord % length] = value;
		} else {
			throw new IllegalArgumentException("value " + value + " at coord " + coord);
		}
	}

	/**
	 * Inserts an Integer at the specified Coordinate in boardSolved[][]
	 * 
	 * @param value
	 *            The inserted Value
	 * @param coord
	 *            Coordinate
	 */
	public void setBoardSolved(int value, int coord) throws IllegalArgumentException {
		if (coord < length * length && coord >= 0 && value <= length && value >= 0) {
			solvedBoard[coord / length][coord % length] = value;
		} else {
			throw new IllegalArgumentException("value " + value + " at coord " + coord);
		}
	}

	/**
	 * Sets a new value in the currentState of this Sudoku
	 * 
	 * @param value
	 *            The new value to be inserted
	 * @param iCoord
	 *            The i coordinate on which to insert
	 * @param jCoord
	 *            The j coordinate on which to insert
	 */
	public void setCurrentState(int value, int iCoord, int jCoord) {
		currentState[iCoord][jCoord] = value;
	}

}