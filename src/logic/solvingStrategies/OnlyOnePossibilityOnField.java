package logic.solvingStrategies;

import logic.Sudoku;
import logic.SudokuSolver;

/**
 * Iterates the whole possibility array and searches for fields with only
 * one possibility remaining. If such a field is found, the only remaining
 * value is inserted via insertValue().
 */
public class OnlyOnePossibilityOnField implements SolvingStrategy {
	private Sudoku sudoku;
	private SudokuSolver solver;
	private int length;

	OnlyOnePossibilityOnField (Sudoku sudoku, SudokuSolver solver) {
		this.sudoku = sudoku;
		this.solver = solver;
		this.length = sudoku.getLength();
	}

	@Override
	public boolean apply() {
		boolean answer = false;
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				if (sudoku.getPossibilities(i, j).size() == 1) {
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
