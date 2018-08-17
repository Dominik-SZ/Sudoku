package model.solving.strategies;

import model.Sudoku;
import model.solving.solver_util.BackupPoint;
import model.solving.solver_util.PerformedOperation;
import swingGUI.util.Coordinate;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Checks if a possibility for a row is impossible to be placed on all but one field. If such a value and field is
 * found, the value gets placed on this field and true is returned. If no field and value are found, false is returned.<br>
 * This is an interpreting method meaning that it actually looks for new values to insert and does so.
 *
 * @see <a href="http://hodoku.sourceforge.net/de/tech_singles.php">http://hodoku.sourceforge.net/de/tech_singles.php</a>
 */
public class HiddenSingleRow implements SolvingStrategy {

	private Sudoku sudoku;
	private Deque<BackupPoint> backups;
	private int length;
	private LinkedList<PerformedOperation> performedOperations;

	public HiddenSingleRow(Sudoku sudoku, Deque<BackupPoint> backups) {
		this.sudoku = sudoku;
		this.backups = backups;
		this.length = sudoku.getLength();
		this.performedOperations = new LinkedList<>();
	}

	@Override
	public boolean apply() {
		HashSet<Integer> possi = new HashSet<>();
		boolean didSomething = false;
		for (int i = 0; i < length; i++) { // iterating the rows
			possi.clear();
			for (int k = 1; k <= length; k++) {
				// fill the set with every number from 1 to length
				possi.add(k);
			}
			for (int j = 0; j < length; j++) {
				// remove already inserted values in this row from the set
				possi.remove(sudoku.getCurrentValue(i, j));
			}
			// now it contains only possibilities for this row

			// iterating the remaining possibilities of the current row
			for (Integer k : possi) {
				LinkedList<Coordinate> allowedFieldsForThisNumber = new LinkedList<>();
				for (int j = 0; j < length; j++) { // iterating the row
					// only search in the empty fields
					if (sudoku.getCurrentValue(i, j) == 0 && sudoku.isAllowed(k, i, j)) {
						// add all possible coordinates of this row for this number
						allowedFieldsForThisNumber.add(new Coordinate(i, j));
					}
				}
				if (allowedFieldsForThisNumber.size() == 1) {
					// set the found value
					Coordinate coord = allowedFieldsForThisNumber.getFirst();
					backups.peek().addTSFill(coord);
					sudoku.setCurrentValue(k, coord.i, coord.j, true);
					performedOperations.add(new PerformedOperation(k, coord.i, coord.j));
					didSomething = true;
				}
			}
		}
		return didSomething;
	}

	@Override
	public int getDifficulty() {
		return 3;
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