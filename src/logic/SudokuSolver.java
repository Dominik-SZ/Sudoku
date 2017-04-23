package logic;

import java.util.EmptyStackException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;

import utilities.Coordinate;
import utilities.MathUtilities;

public class SudokuSolver {
	/** the Sudoku which should be solved */
	private Sudoku sudoku;
	/** the length additionally saved to save access */
	private int length;
	/** the block length additionally saved to save access */
	private int blockLength;

	/** the board on which to the solution is generated */
	private SolverField[][] board;
	/** the backups to stepBack() to, if an assumption proofs to be wrong */
	private Stack<BackupPoint> backups = new Stack<>();
	/** the distance in fields between two assumptions */
	private int stepWidth;

	// ------------------------------------------------------------------------

	SudokuSolver(Sudoku sudoku) {
		this.sudoku = sudoku;
		this.length = sudoku.getLength();
		this.blockLength = sudoku.getBlockLength();
		// generate a stepWidth, which has no common divisor with the Sudoku
		// length greater than 1
		stepWidth = (int) (length * 1.3);
		while (MathUtilities.greatestCommonDivisor(stepWidth, length) != 1) {
			stepWidth++;
		}
		// initialize the board
		board = new SolverField[length][length];
		for(int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				board[i][j] = new SolverField(0, length);
			}
		}

		backups = new Stack<>();
		backups.push(new BackupPoint(-1, new LinkedList<>()));
	}

	// ------------------------------------------------------------------------
	// Main methods used to fill a Sudoku
	/**
	 * Main generating method to fill an empty sudoku, making it definitely
	 * solvable. It also prints the solution and the Sudoku to the system.out,
	 * as well as some additional info.
	 */
	void fill() {
		int fillTrials = 1;
		// try to fill the Sudoku completely until it succeeds
		while (!fillTrial()) {
			fillTrials++; // count how many trials were necessary
		}

		// saving the Solution array before erasing
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				sudoku.getSolvedBoard()[i][j] = board[i][j].value;
			}
		}
		System.out.println("Solution: ");
		System.out.println(sudoku.toString(true, true));

		// erase the permitted fields considering the difficulty
		erase();

		if (fillTrials == 1) {
			System.out.println("needed " + fillTrials + " fill trial to succeed");
		} else {
			System.out.println("needed " + fillTrials + " fill trials to succeed");
		}
		System.out.println(sudoku.count() + " of " + length * length + " fields are filled.");
		System.out.println(sudoku.toString(true, false));
	}

	/**
	 * Helping method which tries to fill the Sudoku completely, but gives up if
	 * it exceeds the allowed amount of trials. It returns whether it was
	 * successful.
	 * 
	 * @return If the the method was successful in filling the sudoku
	 *         (startBoard and boardSolved)
	 */
	private boolean fillTrial() {

		do {
			clear();
			// fill about a quarter of the fields without direct conflicts
			randomFill(length * length / 4);
		} while (isLocked());

		while (trySolving()) {
		}

		int trySolvingAssumeCycles = 0;
		while (!isFinished()) {
			// check if there is a need to restart
			if (trySolvingAssumeCycles > length * length / 3) {
				// System.out.println("NEW TRY"); // restart the fill method if
				// it seems like there is no solution to find
				backups.clear(); // clear the backups for the next trial
				backups.push(new BackupPoint(-1, new LinkedList<>()));
				for (int i = 0; i < length; i++) {
					for (int j = 0; j < length; j++) {
						board[i][j].resetPossibilities();
					}
				}
				// firstTry.clear();
				return false;
			}

			// make an assumption
			assume(backups.peek().getChangedCoord());

			// if (isLocked()) {
			// System.out.println("DEBUG: is locked!");
			// stepBack();
			// }

			// trySolving loop
			while (trySolving()) {
			} // use the trySolving method as long as it succeeds

			trySolvingAssumeCycles++;
			// System.out.println("amount of trySolving, assume cycles: "+
			// trySolvingAssumeCycles);
		}
		return true; // Sudoku is finished
	}

	/**
	 * Inserts "amount" values randomly on fields of the board filled with 0
	 * considering the gamerules (making sure that every number appears at most
	 * once in every row, column and block).
	 */
	private void randomFill(int amount) {
		for (int k = 0; k < amount; k++) {
			int iCoord = MathUtilities.randomNumber(length) - 1;
			int jCoord = MathUtilities.randomNumber(length) - 1;
			int value = MathUtilities.randomNumber(length);

			if (board[iCoord][jCoord].value == 0 && isAllowed(board, value, iCoord, jCoord)) {
				insertNumber(value, iCoord, jCoord);
			} else {
				k--;
			}
		}
	}

	/**
	 * Tries to solve the given Sudoku like a player and returns true if at
	 * least one value got changed (indicating that it should be continued
	 * trying to solve the Sudoku). The fields filled by this method may be
	 * deleted afterwards.
	 */
	private boolean trySolving() {
		boolean answer;
		answer = onlyOnePossibilityOnField();

		// if the length is at least 16, those strategies are necessary to fill
		// the Sudoku
		if (sudoku.getDifficulty() >= 4 || length >= 16) {
			answer = answer || hiddenSingleRow();
			answer = answer || hiddenSingleColumn();
			answer = answer || hiddenSingleBlock();
		}
		return answer;
	}

	/**
	 * If there is no value to be found by trying to solve the Sudoku like a
	 * player, there is an assumption to be made. It is very well possible that
	 * this assumption is wrong, which is why a backup is made to be returned
	 * to, if that is the case. This method attempts an assumption after the
	 * last one by iterating systematically in steps of the size stepWidth
	 * through the Sudoku. If the Sudoku is found to be locked, the stepBack
	 * method is called.
	 * 
	 * @param coord
	 *            The 1D coordinate of the last assumption
	 */
	private void assume(int coord) {
		coord = coord + stepWidth; // iterate to the next field
		for (int k = 0; k < length * length; coord = coord + stepWidth, k++) {
			// when overflowing return to the beginning of the board
			coord = coord % (length * length);
			int i = coord / length;
			int j = coord % length;

			// testing if the field is not filled yet
			if (board[i][j].value == 0) {

				LinkedList<Integer> possibilities = new LinkedList<>();

				// testing every possible number
				for (int l = 1; l <= length; l++) {
					if (isAllowed(board, l, i, j)) {
						// saving the allowed ones in a stack
						possibilities.add(l);
					}
				}
				if (possibilities.isEmpty()) { // Sudoku is locked
					// System.out.println("DEBUG: assume: step back");
					stepBack();
					return;
				} else {
					// choose a random possible assumption on this field
					int index = (int) (Math.random() * possibilities.size());
					board[i][j].value = possibilities.get(index);
					possibilities.remove(index);
					// System.out.println(" DEBUG: new assumption at:
					// i-Coord: "+i +
					// " j-Coord: "+j + " with value: "+values[i][j]);
					backups.push(new BackupPoint(coord, possibilities));
					return; // assumption successfully made
				}
			}
		}
	}

	/**
	 * Undoes the last assumption and chooses the next one. If there are still
	 * possibilities on this field remaining, one of those is chosen, while if
	 * there are none, the assume method is called at this point.
	 */
	private void stepBack() {
		if (backups.isEmpty()) {
			throw new EmptyStackException();
		}
		BackupPoint latestBackup = backups.pop(); // undo the latest backup
		Stack<Integer> tSFills = latestBackup.getTSFills();

		// Remove all values which have been made based on the assumption
		while (!tSFills.isEmpty()) {
			int coord = tSFills.pop();
			board[coord / length][coord % length].value = 0;
		}

		int coord = latestBackup.getChangedCoord();
		int i = coord / length;
		int j = coord % length;
		// no possible assumptions for this field remaining => change field
		if (latestBackup.getPossibilities().isEmpty()) {
			// System.out.println("DEBUG: stepBack(): change field");
			board[i][j].value = 0;
			assume(coord);
		}

		// test another possible assumption on this field
		else {
			board[i][j].value = latestBackup.popRandomPossibility();
			// System.out.println("DEBUG: stepBack(): keep node Coord:
			// "+latestBackup.getChangedCoord() + " now with value:
			// "+getValue(latestBackup.getChangedCoord()));
			backups.push(latestBackup);
		}
	}

	/**
	 * Erases the permitted fields of the Sudoku board, which are those filled
	 * in by the trySolving method. Not permitted are the fields of the
	 * randomFill method and the assumed ones. The higher the difficulty, the
	 * more fields are erased.
	 */
	private void erase() {
		// delete all trySolving entries of the backups and push them into one
		// stack
		Stack<Integer> totalTSFills = new Stack<>();
		while (!backups.isEmpty()) {
			Stack<Integer> currentTSFills = backups.pop().getTSFills();
			while (!currentTSFills.isEmpty()) {
				totalTSFills.push(currentTSFills.pop());
			}
		}

		int deleteable = totalTSFills.size();
		int entriesDeleted = 0; // difficulty 1 -> delete 50% of the entries;
								// difficulty 10 -> delete 100 % of the entries
		while (totalTSFills.size() > (int) ((0.5 - 0.0555555555555555 * (sudoku.getDifficulty() - 1)) * deleteable)) {
			// get a random value of the stack
			int actualCoord = totalTSFills.remove((int) (Math.random() * totalTSFills.size()));
			board[actualCoord / length][actualCoord % length].value = 0;
			entriesDeleted++;
		}
		System.out.println(entriesDeleted + " out of " + deleteable + " deletable entries deleted");
		System.out.println("with difficulty " + sudoku.getDifficulty());
	}

	// -------------------------------------------------------------------------
	// Secondary Methods to fill the Sudoku

	/**
	 * Checks if the inserted value at the specified coordinates on the inserted
	 * board is allowed or not. It is, if the value is not already inserted in
	 * the same row, column or block.
	 * 
	 * @param board
	 *            The board on which to check
	 * @param value
	 *            The value to check
	 * @param iCoord
	 *            The i coordinate of the position to check
	 * @param jCoord
	 *            The j coordinate of the position to check
	 * @return If the value is allowed to be inserted there
	 */
	public static boolean isAllowed(SolverField[][] board, int value, int iCoord, int jCoord) {
		return (testBlock(board, value, iCoord, jCoord) && testColumn(board, value, jCoord)
				&& testRow(board, value, iCoord));
	}

	/**
	 * Checks if "value" is allowed to be placed in this block (there is not the
	 * same number inserted yet).
	 * 
	 * @param board
	 *            The board on which to check
	 * @param value
	 *            The value which shall be checked
	 * @param iCoord
	 *            The i coordinate of the value
	 * @param jCoord
	 *            The j coordinate of the value
	 * 
	 * @return If the value may be inserted on this field regarding the block
	 */
	private static boolean testBlock(SolverField[][] board, int value, int iCoord, int jCoord) {
		boolean answer = true;
		int blockLength = (int) Math.sqrt(board.length);
		int iStartValue = blockLength * (iCoord / blockLength);
		int jStartValue = blockLength * (jCoord / blockLength);
		for (int i = iStartValue; i < iStartValue + blockLength; i++) {
			for (int j = jStartValue; j < jStartValue + blockLength; j++) {
				if (board[i][j].value == value) {
					answer = false;
				}
			}
		}
		return answer;
	}

	/**
	 * checks if "value" is allowed to be placed in this column (there is not
	 * the same number inserted yet).
	 * 
	 * @param board
	 *            The board on which to check
	 * @param value
	 *            The value to check
	 * @param jCoord
	 *            The column number in which to check
	 */
	private static boolean testColumn(SolverField[][] board, int value, int jCoord) {
		boolean answer = true;
		for (int i = 0; i < board.length; i++) {
			if (board[i][jCoord].value == value) {
				answer = false;
			}
		}
		return answer;
	}

	/**
	 * checks if "value" is allowed to be placed in this row (there is not the
	 * same number inserted yet)
	 * 
	 * @param board
	 *            The board on which to check
	 * @param value
	 *            The value to check
	 * @param iCoord
	 *            The row number in which to check
	 */
	private static boolean testRow(SolverField[][] board, int value, int iCoord) {
		boolean answer = true;
		for (int j = 0; j < board.length; j++) {
			if (board[iCoord][j].value == value) {
				answer = false;
			}
		}
		return answer;
	}

	/**
	 * checks if there is no allowed possibility remaining to fill one of the
	 * empty fields.
	 */
	private boolean isLocked() {
		boolean answer = true;
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				if (board[i][j].value == 0) {
					for (int k = 1; k < length; k++) {
						answer = answer && !isAllowed(board, k, i, j);
					}
					if (!answer) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Returns true if the entries of the Sudoku board contain no "0". If it
	 * does so, it is considered as "finished".
	 */
	private boolean isFinished() {
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				if (board[i][j].value == 0) {
					return false;
				}
			}
		}
		return true;
	}

	/** sets all fields to 0. */
	private void clear() {
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				board[i][j].value = 0;
			}
		}
	}

	/**
	 * Inserts the inserted value in the startBoard at the specified iCoord and
	 * jCoord. Also all possibilities, which are directly blocked as a result of
	 * this new entry, are removed and the new entry is added to the trySolving
	 * stack of the current backup point.
	 * 
	 * @param value
	 *            The value, which should be inserted
	 * @param iCoord
	 *            The i coordinate at which to insert the value
	 * @param jCoord
	 *            The j coordinate at which to insert the value
	 */
	private void insertNumber(int value, int iCoord, int jCoord) {
		// remove the possibilities from the row
		for (int j = 0; j < length; j++) {
			board[iCoord][j].possibilities.remove(value);
		}

		// remove the possibilities from the column
		for (int i = 0; i < length; i++) {
			board[i][jCoord].possibilities.remove(value);
		}

		// remove the possibilities from the block
		int iStartValue = blockLength * (iCoord / blockLength);
		int jStartValue = blockLength * (jCoord / blockLength);
		for (int i = iStartValue; i < iStartValue + blockLength; i++) {
			for (int j = jStartValue; j < jStartValue + blockLength; j++) {
				board[i][j].possibilities.remove(value);
			}
		}

		backups.peek().pushTSFills(iCoord * length + jCoord);

		board[iCoord][jCoord].value = value;
	}

	// -------------------------------------------------------------------------
	// solving strategy stuff (methods used by fillTrial())

	/**
	 * Iterates the whole possibility array and searches for fields with only
	 * one possibility remaining. If such a field is found, the only remaining
	 * value is inserted via insertNumber().
	 * 
	 * @return If only one possibility has been found
	 */
	private boolean onlyOnePossibilityOnField() {
		boolean answer = false;
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				if (board[i][j].possibilities.size() == 1) {
					// TODO: check this
					int onlyPossibility = (int) board[i][j].possibilities.toArray()[0];
					insertNumber(onlyPossibility, i, j);
					System.out.println("nur eine Möglichkeit verbleibt: " + onlyPossibility);
					answer = true;
				}
			}
		}
		return answer;
	}

	/**
	 * Checks if a possibility for a row is impossible to be placed on all but
	 * one field. If such a value and field is found, the value gets placed on
	 * this field and true is returned. If no field and value are found, false
	 * is returned.
	 */
	private boolean hiddenSingleRow() {
		HashSet<Integer> possi = new HashSet<>();
		boolean answer = false;
		for (int i = 0; i < length; i++) { // iterating the rows
			possi.clear();
			for (int k = 1; k <= length; k++) {
				// fill the set with every number from 1 to length
				possi.add(k);
			}
			for (int j = 0; j < length; j++) {
				// remove already inserted values in this row from the set
				possi.remove(board[i][j].value);
			}
			// now it contains only possibilities for this row

			// iterating the remaining possibilities of the current row
			for (Integer k : possi) {
				LinkedList<Coordinate> allowedFieldsForThisNumber = new LinkedList<>();
				for (int j = 0; j < length; j++) { // iterating the row
					// only search in the empty fields
					if (board[i][j].value == 0 && SudokuSolver.isAllowed(board, k, i, j)) {
						// add all possible coordinates of this row for this
						// number
						allowedFieldsForThisNumber.add(new Coordinate(i, j));
					}
				}
				if (allowedFieldsForThisNumber.size() == 1) {
					// set the found value
					Coordinate coord = allowedFieldsForThisNumber.getFirst();
					insertNumber(k, coord.i, coord.j);
					answer =  true;
				}
			}
		}
		return answer; // false if no value has been found
	}
	
	/**
	 * Checks if a possibility for a column is impossible to be placed on all
	 * but one field. If such a value and field is found, the value gets placed
	 * on this field and true is returned. If no field and value are found,
	 * false is returned.
	 */
	private boolean hiddenSingleColumn() {
		HashSet<Integer> possi = new HashSet<>();
		boolean answer = false;
		for (int j = 0; j < length; j++) { // iterating the columns
			possi.clear();
			for (int k = 1; k <= length; k++) {
				possi.add(k); // filling the set with every number from
										// 1-length
			}
			for (int i = 0; i < length; i++) {
				// remove already inserted values in this column from the set
				possi.remove(board[i][j].value);
			} // now it contains only possibilities for this column

			// iterating the remaining possibilities of the current column
			for (Integer k : possi) {
				LinkedList<Coordinate> allowedFieldsForThisNumber = new LinkedList<>();
				for (int i = 0; i < length; i++) { // iterating the column
					// only search in the empty fields
					if (board[i][j].value == 0 && SudokuSolver.isAllowed(board, k, i, j)) {
						// add all possible coordinates of this column for this
						// number
						allowedFieldsForThisNumber.add(new Coordinate(i, j));
					}
				}
				if (allowedFieldsForThisNumber.size() == 1) {
					// set the found value
					Coordinate coord = allowedFieldsForThisNumber.getFirst();
					insertNumber(k, coord.i, coord.j);
					answer = true;
				}
			}
		}
		return answer; // false if no value has been found
	}
	
	/**
	 * Checks if a possibility for a block is impossible to be placed on all but
	 * one field. If such a value and field is found, the value gets placed on
	 * this field and true is returned. If no field and value are found, false
	 * is returned.
	 */
	private boolean hiddenSingleBlock() {
		HashSet<Integer> possi = new HashSet<>();
		boolean answer = false;
		// iterating the rows blockwise
		for (int iBlock = 0; iBlock < length; iBlock = iBlock + blockLength) {
			// iterating the columns blockwise
			for (int jBlock = 0; jBlock < length; jBlock = jBlock + blockLength) {
				possi.clear();
				for (int k = 1; k <= length; k++) {
					// filling the set with every number from 1-length
					possi.add(k);
				}
				for (int i = iBlock; i < iBlock + blockLength; i++) {
					for (int j = 0; j < length; j++) {
						// remove already inserted values in this block from the
						// set
						possi.remove(board[i][j].value);
					} // now it contains only possibilities for this block
				}

				// iterating the remaining possibilities of the current block
				for (Integer k : possi) {
					LinkedList<Coordinate> allowedFieldsForThisNumber = new LinkedList<>();
					// iterating the rows of the current block
					for (int i = iBlock; i < iBlock + blockLength; i++) {
						// iterating the columns of the current block
						for (int j = jBlock; j < jBlock + blockLength; j++) {
							// only search in the empty fields
							if (board[i][j].value == 0 && SudokuSolver.isAllowed(board, k, i, j)) {
								// add all possible coordinates of this block
								// for this number
								allowedFieldsForThisNumber.add(new Coordinate(i, j));
							}
						}
					}
					if (allowedFieldsForThisNumber.size() == 1) {
						Coordinate coord = allowedFieldsForThisNumber.getFirst();
						insertNumber(k, coord.i, coord.j);
						answer = true;
					}
				}
			}
		}
		return answer; // false if no value has been found
	}
	
}
