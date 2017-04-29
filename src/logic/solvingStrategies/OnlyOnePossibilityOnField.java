package logic.solvingStrategies;

import logic.SudokuSolver;
import logic.Sudoku;


public class OnlyOnePossibilityOnField implements SolvingStrategy {
	private Sudoku sudoku;
	private SudokuSolver solver;

	OnlyOnePossibilityOnField (Sudoku sudoku, SudokuSolver solver) {
		this.sudoku = sudoku;
		this.solver = solver;
	}

	@Override
	public boolean apply() {
		boolean answer = false;
		for (int i = 0; i < sudoku.getLength(); i++) {
			for (int j = 0; j < sudoku.getLength(); j++) {
				if (sudoku.getPossibilities(i, j).size() == 1) {
					int onlyPossibility = (int) sudoku.getPossibilities(i, j).toArray()[0];
					solver.insertValue(onlyPossibility, i, j, true);
					answer = true;
				}
			}
		}
		return answer;
	}

	@Override
	public int getDifficulty() {
		return 1;
	}

}
