package model.solving.strategies;

import model.Sudoku;
import model.exceptions.PIVException;
import model.solving.solver_util.PerformedOperation;
import swingGUI.util.Coordinate;

import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Checks all blocks if a possibility in one of them is only possible in the same row or column. If that is the case,
 * this possibility is removed in all other fields in this row or column outside the block. <br>This is a restrictive
 * method.
 *
 * @see <a href="http://hodoku.sourceforge.net/de/tech_intersections.php">http://hodoku.sourceforge.net/de/tech_intersections.php</a>
 */
public class IntersectionBlockToRowAndColumn implements SolvingStrategy {
    private Sudoku sudoku;
    private int length;
    private int blockLength;
    private LinkedList<PerformedOperation> performedOperations;

    public IntersectionBlockToRowAndColumn(Sudoku sudoku) {
        this.sudoku = sudoku;
        this.length = sudoku.getLength();
        this.blockLength = sudoku.getBlockLength();
        this.performedOperations = new LinkedList<>();
    }


    @Override
    public boolean apply() throws PIVException {
        if (!sudoku.isPossibilityInteger()) {
            throw new PIVException();
        }

        boolean didSomething = false;
        LinkedList<Coordinate> occurrences = new LinkedList<>();

        // iterating the rows blockwise
        for (int iBlock = 0; iBlock < length; iBlock = iBlock + blockLength) {
            // iterating the columns blockwise
            for (int jBlock = 0; jBlock < length; jBlock = jBlock + blockLength) {

                // iterate all possible numbers
                for (int k = 1; k <= length; k++) {
                    occurrences.clear();
                    // save all occurrences of this number in this block
                    for (int i = iBlock; i < iBlock + blockLength; i++) {
                        for (int j = jBlock; j < jBlock + blockLength; j++) {
                            if (sudoku.getCurrentValue(i, j) == 0 && sudoku.getPossibilities(i, j).contains(k)) {
                                // save the occurrences of the current number
                                occurrences.add(new Coordinate(i, j));
                            }

                        }
                    }

                    try {
                        // check if all occurrences are in the same row
                        int firstI = occurrences.getFirst().i;
                        boolean sameRow = true;
                        for (Coordinate coord : occurrences) {
                            if (coord.i != firstI) {
                                sameRow = false;
                                break;
                            }
                        }
                        // remove the number from the rest of the row
                        if (sameRow) {
                            // the part of the row on the left of the block
                            for (int j = 0; j < jBlock; j++) {
                                sudoku.getPossibilities(firstI, j).remove(k);
                            }
                            // the part of the row on the right of the block
                            for (int j = jBlock + blockLength; j < length; j++) {
                                sudoku.getPossibilities(firstI, j).remove(k);
                            }
                        }


                        // check if all occurrences are in the same column
                        int firstJ = occurrences.getFirst().j;
                        boolean sameColumn = true;
                        for (Coordinate coord : occurrences) {
                            if (coord.j != firstJ) {
                                sameColumn = false;
                                break;
                            }
                        }
                        if (sameColumn) {
                            // the part of the column on top of the block
                            for (int i = 0; i < iBlock; i++) {
                                if (sudoku.getPossibilities(i, firstJ).remove(k)) {
                                    performedOperations.add(new PerformedOperation(k, i, firstJ));
                                    didSomething = true;
                                }
                            }
                            // the part of the column below the block
                            for (int i = iBlock + blockLength; i < length; i++) {
                                if (sudoku.getPossibilities(i, firstJ).remove(k)) {
                                    performedOperations.add(new PerformedOperation(k, i, firstJ));
                                    didSomething = true;
                                }
                            }
                        }
                    } catch (NoSuchElementException ex) {
                        // do nothing. Skip this number
                    }
                }

            }
        }
        return didSomething;

    }

    @Override
    public int getDifficulty() {
        return 6;
    }

    @Override
    public boolean isRestrictive() {
        return true;
    }

    @Override
    public LinkedList<PerformedOperation> getPerformedOperations() {
        return performedOperations;
    }
}
