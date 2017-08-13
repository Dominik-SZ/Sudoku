package logic.solvingStrategies;


import logic.Sudoku;
import logic.SudokuSolver;
import util.Coordinate;

import java.util.HashSet;
import java.util.LinkedList;

/**
 * Checks if a possibility for a column is impossible to be placed on all but one field. If such a value and field is
 * found, the value gets placed on this field and true is returned. If no field and value are found, false is returned.
 * This is an interpreting method meaning that it does not manipulate the possibilities but interprets them to find
 * new values.
 */
public class HiddenSingleColumn implements SolvingStrategy{
	private Sudoku sudoku;
	private SudokuSolver solver;
	private int length;

	public HiddenSingleColumn(Sudoku sudoku, SudokuSolver solver) {
		this.sudoku = sudoku;
		this.solver = solver;
		this.length = sudoku.getLength();
	}

	@Override
	public boolean apply() {
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
				possi.remove(sudoku.getCurrentValue(i, j));
			} // now it contains only possibilities for this column

			// iterating the remaining possibilities of the current column
			for (Integer k : possi) {
				LinkedList<Coordinate> allowedFieldsForThisNumber = new LinkedList<>();
				for (int i = 0; i < length; i++) { // iterating the column
					// only search in the empty fields
					if (sudoku.getCurrentValue(i, j) == 0 && sudoku.isAllowed(k, i, j)) {
						// add all possible coordinates of this column for this
						// number
						allowedFieldsForThisNumber.add(new Coordinate(i, j));
					}
				}
				if (allowedFieldsForThisNumber.size() == 1) {
					// set the found value
					Coordinate coord = allowedFieldsForThisNumber.getFirst();
					solver.pushTrySolvingBackup(coord.i, coord.j);
					sudoku.insertCurrentValue(k, coord.i, coord.j);
					answer = true;
				}
			}
		}
		return answer; // false if no value has been found
	}

	@Override
	public int getDifficulty() {
		return 3;
	}
}
