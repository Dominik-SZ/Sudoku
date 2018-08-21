package model2.solving.strategies;


import model2.solving.Solveable;
import swingGUI.util.Coordinate;

import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Checks if all possibilities of a column for a specific number are in the same block. If they are, this number is
 * removed from the possibilities of the rest of the fields in this block. This is a restrictive method meaning it only
 * removes possibilities instead of finding new values.
 *
 * @see <a href="http://hodoku.sourceforge.net/de/tech_intersections.php">http://hodoku.sourceforge.net/de/tech_intersections.php</a>
 */
public class IntersectionColumnToBlock implements SolvingStrategy {

    public IntersectionColumnToBlock() {
    }

    @Override
    public boolean apply(Solveable board) {
        int length = board.getLength();
        int blockLength = board.getBlockLength();
        boolean didSomething = false;
        LinkedList<Coordinate> occurrences = new LinkedList<>();

        // iterate all columns
        for (int jColumn = 0; jColumn < length; jColumn++) {

            // iterate all possible numbers
            for (int k = 1; k <= length; k++) {

                // save all occurrences of this number in this column
                occurrences.clear();
                for (int i = 0; i < length; i++) {
                    if (board.getValue(i, jColumn) == 0 && board.isPossible(k, i, jColumn)) {
                        occurrences.add(new Coordinate(i, jColumn));
                    }
                }

                try {
                    // check if all occurrences are in the same block
                    int jOccurrence = occurrences.getFirst().j;
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

                        // remove the possibilities in the columns left of the occurred column in the same block
                        for (int i = iStart; i < iStart + blockLength; i++) {
                            for (int j = jStart; j < jOccurrence; j++) {
                                if (board.removePossibility(k, i, j)) {
                                    didSomething = true;
                                }
                            }
                        }
                        // remove the possibilities in the columns right of the occurred column in the same block
                        for (int i = iStart + 1; i < iStart + blockLength; i++) {
                            for (int j = jOccurrence + 1; j < jStart + blockLength; j++) {
                                if (board.removePossibility(k, i, j)) {
                                    didSomething = true;
                                }
                            }
                        }

                    }

                } catch (NoSuchElementException ex) {
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
