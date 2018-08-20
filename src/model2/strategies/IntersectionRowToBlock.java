package model2.strategies;


import model2.Solveable;
import swingGUI.util.Coordinate;

import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Checks if all possibilities of a row for a specific number are in the same block. If they are, this number is removed
 * from the possibilities of the rest of this block's fields.
 * This is a restrictive method meaning that it only removes possibilities and does not insert new values.
 *
 * @see <a href="http://hodoku.sourceforge.net/de/tech_intersections.php">http://hodoku.sourceforge.net/de/tech_intersections.php</a>
 */
public class IntersectionRowToBlock implements SolvingStrategy {

    public IntersectionRowToBlock() {
    }


    @Override
    public boolean apply(Solveable board) {
        int length = board.getLength();
        int blockLength = board.getBlockLength();
        boolean didSomething = false;
        LinkedList<Coordinate> occurrences = new LinkedList<>();

        // iterate the rows
        for (int iRow = 0; iRow < length; iRow++) {

            // iterate all possible numbers
            for (int p = 1; p <= length; p++) {

                // save all occurrences of this number in this row
                occurrences.clear();
                for (int j = 0; j < length; j++) {
                    if (board.getValue(iRow, j) == 0 && board.isPossible(p, iRow, j)) {
                        occurrences.add(new Coordinate(iRow, j));
                    }
                }

                try {
                    // check if all occurrences are in the same block
                    int iOccurrence = occurrences.getFirst().i;
                    int iBlockNumber = occurrences.getFirst().i / blockLength;
                    int jBlockNumber = occurrences.getFirst().j / blockLength;
                    boolean sameBlock = true;

                    for (Coordinate coord : occurrences) {
                        if (coord.i / blockLength != iBlockNumber || coord.j / blockLength != jBlockNumber) {
                            sameBlock = false;
                            break;
                        }
                    }

                    if (sameBlock) {
                        int iStart = iBlockNumber * blockLength;
                        int jStart = jBlockNumber * blockLength;

                        // remove the possibilities in the rows on top of the occurred row in the same block
                        for (int i = iStart; i < iOccurrence; i++) {
                            for (int j = jStart; j < jStart + blockLength; j++) {
                                if (board.removePossibility(p, i, j)) {
                                    didSomething = true;
                                }
                            }
                        }
                        // remove the possibilities in the rows below the occurred row in the same block
                        for (int i = iOccurrence + 1; i < iStart + blockLength; i++) {
                            for (int j = jStart; j < jStart + blockLength; j++) {
                                if (board.removePossibility(p, i, j)) {
                                    didSomething = true;
                                }
                            }
                        }

                    }

                } catch (NoSuchElementException ignored) {
                    // do nothing. Skip this number
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
