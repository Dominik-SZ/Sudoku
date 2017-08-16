package logic.solvingStrategies;

import logic.Sudoku;
import logic.SudokuSolver;
import logic.exceptions.PIVException;

/**
 * Iterates all fields and their possibilities searches for fields with only one possibility remaining. If such a
 * field is found, the only remaining possibility is inserted as current value.
 * This is an interpreting method meaning that it does not manipulate the possibilities of the fields but finds new
 * values to set.
 */
public class OnlyOnePossibilityOnField implements SolvingStrategy {
	private Sudoku sudoku;
	private SudokuSolver solver;
	private int length;

	public OnlyOnePossibilityOnField (Sudoku sudoku, SudokuSolver solver) {
		this.sudoku = sudoku;
		this.solver = solver;
		this.length = sudoku.getLength();
	}

	@Override
	public boolean apply() throws PIVException {

		if(!sudoku.isPossibilityInteger()) {
			throw new PIVException();
		}
		boolean answer = false;
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				if (sudoku.getCurrentValue(i, j) == 0 && sudoku.getPossibilities(i, j).size() == 1) {
					int onlyPossibility = (int) sudoku.getPossibilities(i, j).toArray()[0];
					solver.pushTrySolvingBackup(i, j);
					sudoku.insertCurrentValue(onlyPossibility, i, j);
					answer = true;
				}
			}
		}
		return answer;
	}

	@Override
	public int getDifficulty() {
		return(1);
	}

}
