package model_new.solving.strategies;

import model_new.solving.Solveable;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Fish strategy.<br> Checks if there are four base rows/columns which contain the possibility for a number in exactly
 * four column/row indices. If that is the case, the four columns/rows covering the four possibilities, get all of their
 * occurrences of the current number except for the base row/column indices removed.
 *
 * @see <a href="http://hodoku.sourceforge.net/de/tech_fishb.php">http://hodoku.sourceforge.net/de/tech_fishb.php</a>
 */
public class Jellyfish implements SolvingStrategy {

    public Jellyfish() {
    }

    @Override
    public boolean apply(Solveable board) {
        int length = board.getLength();
        boolean didSomething = false;
        boolean foundIn1;
        boolean foundIn2;
        boolean foundIn3;
        boolean foundIn4;
        HashSet<Integer> occurrences = new HashSet<>(length);

        //--------------------------------------------------------------------------------------------------------------
        // rows as base and columns as cover:
        for (int baseRow1 = 0; baseRow1 < length; baseRow1++) {
            for (int baseRow2 = baseRow1 + 1; baseRow2 < length; baseRow2++) {
                for (int baseRow3 = baseRow2 + 1; baseRow3 < length; baseRow3++) {
                    for (int baseRow4 = baseRow3 + 1; baseRow4 < length; baseRow4++) {
                        // iterate all possible numbers
                        for (int p = 1; p <= length; p++) {

                            foundIn1 = false;
                            foundIn2 = false;
                            foundIn3 = false;
                            foundIn4 = false;
                            occurrences.clear();
                            // save the column indices of the occurrences of this number in the base rows
                            for (int j = 0; j < length; j++) {
                                if (board.isPossible(p, baseRow1, j)) {
                                    occurrences.add(j);
                                    foundIn1 = true;
                                }
                                if (board.isPossible(p, baseRow2, j)) {
                                    occurrences.add(j);
                                    foundIn2 = true;
                                }
                                if (board.isPossible(p, baseRow3, j)) {
                                    occurrences.add(j);
                                    foundIn3 = true;
                                }
                                if (board.isPossible(p, baseRow4, j)) {
                                    occurrences.add(j);
                                    foundIn4 = true;
                                }
                            }
                            // Four columns do exist, which cover the occurrences of the current number in the current
                            // base rows and contain at least one candidate for the current number each
                            if (foundIn1 && foundIn2 && foundIn3 && foundIn4 && occurrences.size() == 4) {
                                Iterator<Integer> iterator = occurrences.iterator();
                                int coverColumn1 = iterator.next();
                                int coverColumn2 = iterator.next();
                                int coverColumn3 = iterator.next();
                                int coverColumn4 = iterator.next();

                                // delete all occurrences of the current number in the cover columns, which are not
                                // contained in the base rows too
                                for (int i = 0; i < length; i++) {
                                    if (i != baseRow1 && i != baseRow2 && i != baseRow3 && i != baseRow4) {
                                        if (board.removePossibility(p, i, coverColumn1)) {
                                            didSomething = true;
                                        }
                                        if (board.removePossibility(p, i, coverColumn2)) {
                                            didSomething = true;
                                        }
                                        if (board.removePossibility(p, i, coverColumn3)) {
                                            didSomething = true;
                                        }
                                        if (board.removePossibility(p, i, coverColumn4)) {
                                            didSomething = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //--------------------------------------------------------------------------------------------------------------
        // columns as base and rows as cover:
        for (int baseColumn1 = 0; baseColumn1 < length; baseColumn1++) {
            for (int baseColumn2 = baseColumn1 + 1; baseColumn2 < length; baseColumn2++) {
                for (int baseColumn3 = baseColumn2 + 1; baseColumn3 < length; baseColumn3++) {
                    for (int baseColumn4 = baseColumn3 + 1; baseColumn4 < length; baseColumn4++) {
                        // iterate all possible numbers
                        for (int p = 1; p <= length; p++) {

                            foundIn1 = false;
                            foundIn2 = false;
                            foundIn3 = false;
                            foundIn4 = false;
                            occurrences.clear();
                            // save the row indices of the occurrences of this number in the base columns
                            for (int i = 0; i < length; i++) {
                                if (board.isPossible(p, i, baseColumn1)) {
                                    occurrences.add(i);
                                    foundIn1 = true;
                                }
                                if (board.isPossible(p, i, baseColumn2)) {
                                    occurrences.add(i);
                                    foundIn2 = true;
                                }
                                if (board.isPossible(p, i, baseColumn3)) {
                                    occurrences.add(i);
                                    foundIn3 = true;
                                }
                                if (board.isPossible(p, i, baseColumn4)) {
                                    occurrences.add(i);
                                    foundIn4 = true;
                                }
                            }
                            // Three rows do exist, which cover the occurrences of the current number in the current
                            // base columns
                            if (foundIn1 && foundIn2 && foundIn3 && foundIn4 && occurrences.size() == 4) {
                                Iterator<Integer> iterator = occurrences.iterator();
                                int coverRow1 = iterator.next();
                                int coverRow2 = iterator.next();
                                int coverRow3 = iterator.next();
                                int coverRow4 = iterator.next();

                                // delete all occurrences of the current number in the cover rows, which are not
                                // contained in the base columns too
                                for (int j = 0; j < length; j++) {
                                    if (j != baseColumn1 && j != baseColumn2 && j != baseColumn3 && j != baseColumn4) {
                                        if (board.removePossibility(p, coverRow1, j)) {
                                            didSomething = true;
                                        }
                                        if (board.removePossibility(p, coverRow2, j)) {
                                            didSomething = true;
                                        }
                                        if (board.removePossibility(p, coverRow3, j)) {
                                            didSomething = true;
                                        }
                                        if (board.removePossibility(p, coverRow4, j)) {
                                            didSomething = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return didSomething;
    }

    @Override
    public int getDifficulty() {
        return 10;
    }

    @Override
    public boolean isRestrictive() {
        return true;
    }

}