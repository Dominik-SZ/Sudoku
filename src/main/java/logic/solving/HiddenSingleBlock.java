package logic.solving;


import logic.Sudoku;
import util.Coordinate;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Checks if a possibility for a block is impossible to be placed on all but one field. If such a value and field is
 * found, the value gets placed on this field and true is returned. If no field and value are found, false is
 * returned.<br>
 * This is an interpreting method meaning that it actually looks for new values to insert and does so.
 *
 * @see <a href="http://hodoku.sourceforge.net/de/tech_singles.php">http://hodoku.sourceforge.net/de/tech_singles.php</a>
 */
class HiddenSingleBlock implements SolvingStrategy {
    private Sudoku sudoku;
    private Deque<BackupPoint> backups;
    private int length;
    private int blockLength;
    private LinkedList<PerformedOperation> performedOperations;

    HiddenSingleBlock(Sudoku sudoku, Deque<BackupPoint> backups) {
        this.sudoku = sudoku;
        this.backups = backups;
        this.length = sudoku.getLength();
        this.blockLength = sudoku.getBlockLength();
        this.performedOperations = new LinkedList<>();
    }

    @Override
    public boolean apply() {
        HashSet<Integer> possi = new HashSet<>();
        boolean didSomething = false;

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
                    for (int j = jBlock; j < jBlock + blockLength; j++) {
                        // remove already inserted values in this block from the set
                        possi.remove(sudoku.getCurrentValue(i, j));
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
                            if (sudoku.getCurrentValue(i, j) == 0 && sudoku.isAllowed(k, i, j)) {
                                // add all possible coordinates of this block
                                // for this number
                                allowedFieldsForThisNumber.add(new Coordinate(i, j));
                            }
                        }
                    }
                    if (allowedFieldsForThisNumber.size() == 1) {
                        Coordinate coord = allowedFieldsForThisNumber.getFirst();
                        backups.peek().addTSFill(coord);
                        sudoku.setCurrentValue(k, coord.i, coord.j, true);
                        performedOperations.add(new PerformedOperation(k, coord.i, coord.j));
                        didSomething = true;
                    }
                }
            }
        }
        return didSomething; // false if no value has been found
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
