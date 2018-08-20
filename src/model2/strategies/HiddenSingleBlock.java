package model2.strategies;


import model2.Solveable;
import swingGUI.util.Coordinate;

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
public class HiddenSingleBlock extends FillStrategy implements SolvingStrategy {

    public HiddenSingleBlock() {
    }

    @Override
    public boolean apply(Solveable board) {
        int length = board.getLength();
        int blockLength = board.getBlockLength();
        HashSet<Integer> possi = new HashSet<>();
        boolean didSomething = false;

        // iterating the rows blockwise
        for (int iBlock = 0; iBlock < length; iBlock = iBlock + blockLength) {
            // iterating the columns blockwise
            for (int jBlock = 0; jBlock < length; jBlock = jBlock + blockLength) {

                possi.clear();
                for (int p = 1; p <= length; p++) {
                    // filling the set with every number from 1-length
                    possi.add(p);
                }
                for (int i = iBlock; i < iBlock + blockLength; i++) {
                    for (int j = jBlock; j < jBlock + blockLength; j++) {
                        // remove already inserted values in this block from the set
                        possi.remove(board.getValue(i, j));
                    } // now it contains only possibilities for this block
                }

                // iterating the remaining possibilities of the current block
                for (Integer p : possi) {
                    LinkedList<Coordinate> allowedFieldsForThisNumber = new LinkedList<>();
                    // iterating the rows of the current block
                    for (int i = iBlock; i < iBlock + blockLength; i++) {
                        // iterating the columns of the current block
                        for (int j = jBlock; j < jBlock + blockLength; j++) {
                            // only search in the empty fields
                            if (board.getValue(i, j) == 0 && board.isPossible(p, i, j)) {
                                // collect all possible fields of this block on which k is possible
                                allowedFieldsForThisNumber.add(new Coordinate(i, j));
                            }
                        }
                    }
                    if (allowedFieldsForThisNumber.size() == 1) {
                        Coordinate coord = allowedFieldsForThisNumber.getFirst();
                        board.setValue(p, coord.i, coord.j);
                        applyRuleConstraints(board, p, coord.i, coord.j);
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

}
