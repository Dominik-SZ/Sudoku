package model2.solving.strategies;

import model2.solving.Solveable;
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

    public IntersectionBlockToRowAndColumn() {
    }


    @Override
    public boolean apply(Solveable board) {
        int length = board.getLength();
        int blockLength = board.getBlockLength();
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
                            if (board.getValue(i, j) == 0 && board.isPossible(k, i, j)) {
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
                                board.removePossibility(k, firstI, j);
                            }
                            // the part of the row on the right of the block
                            for (int j = jBlock + blockLength; j < length; j++) {
                                board.removePossibility(k, firstI, j);
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
                                if (board.removePossibility(k, i, firstJ)) {
                                    didSomething = true;
                                }
                            }
                            // the part of the column below the block
                            for (int i = iBlock + blockLength; i < length; i++) {
                                if (board.removePossibility(k, i, firstJ)) {
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

}
