package logic.solvingStrategies;

import logic.Sudoku;
import logic.SudokuSolver;
import logic.exceptions.PIVException;

import java.util.Iterator;
import java.util.LinkedList;

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
	private LinkedList<PerformedOperation> performedOperations;

	public OnlyOnePossibilityOnField (Sudoku sudoku, SudokuSolver solver) {
		this.sudoku = sudoku;
		this.solver = solver;
		this.length = sudoku.getLength();
		this.performedOperations = new LinkedList<>();
	}

	@Override
	public boolean apply() throws PIVException {

		if(!sudoku.isPossibilityInteger()) {
			throw new PIVException();
		}
		boolean didSomething = false;
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				if (sudoku.getCurrentValue(i, j) == 0 && sudoku.getPossibilities(i, j).size() == 1) {
                    Iterator<Integer> iterator = sudoku.getPossibilities(i, j).iterator();
					int onlyPossibility = iterator.next();

					solver.pushTrySolvingBackup(i, j);
					sudoku.setCurrentValue(onlyPossibility, i, j, true);
                    performedOperations.add(new PerformedOperation(onlyPossibility, i, j));
					didSomething = true;
				}
			}
		}
		return didSomething;
	}

	@Override
	public int getDifficulty() {
		return(1);
	}

	@Override
	public boolean isRestrictive() {
		return false;
	}

    @Override
    public LinkedList<PerformedOperation> getPerformedOperations() {
        return performedOperations;
    }
}
