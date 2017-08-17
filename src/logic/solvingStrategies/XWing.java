package logic.solvingStrategies;

import logic.Sudoku;
import logic.exceptions.PIVException;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Fish strategy
 * Checks if there are two base rows/columns which contain the possibility for a number in exactly two column/row
 * indices. If that is the case, the two columns/rows covering the two possibilities, get all of their occurrences of
 * the current number except for the base row/column indices removed.
 */
public class XWing implements SolvingStrategy {

    private Sudoku sudoku;
    private int length;

    public XWing(Sudoku sudoku) {
        this.sudoku = sudoku;
        this.length = sudoku.getLength();
    }

    @Override
    public boolean apply() throws PIVException {
        if (!sudoku.isPossibilityInteger()) {
            throw new PIVException("Possibility integrity required for X-Wing strategy");
        }

        boolean didSomething = false;
        boolean foundIn1;
        boolean foundIn2;
        HashSet<Integer> occurrences = new HashSet<>(length);

        //--------------------------------------------------------------------------------------------------------------
        // rows as base and columns as cover:
        for (int baseRow1 = 0; baseRow1 < length; baseRow1++) {
            for (int baseRow2 = baseRow1 + 1; baseRow2 < length; baseRow2++) {
                // iterate all possible numbers
                for (int k = 1; k <= length; k++) {

                    foundIn1 = false;
                    foundIn2 = false;
                    occurrences.clear();
                    // save the column indices of the occurrences of this number in the base rows
                    for (int j = 0; j < length; j++) {
                        if (sudoku.getPossibilities(baseRow1, j).contains(k)) {
                            occurrences.add(j);
                            foundIn1 = true;
                        }
                        if (sudoku.getPossibilities(baseRow2, j).contains(k)) {
                            occurrences.add(j);
                            foundIn2 = true;
                        }
                    }
                    // Two columns do exist, which cover the occurrences of the current number in the current base
                    // rows and contain at least one candidate for the current number each
                    if (foundIn1 && foundIn2 && occurrences.size() == 2) {
                        Iterator<Integer> iterator = occurrences.iterator();
                        int coverColumn1 = iterator.next();
                        int coverColumn2 = iterator.next();

                        // delete all occurrences of the current number in the cover columns, which are not contained
                        // in the base rows too
                        for (int i = 0; i < length; i++) {
                            if (i != baseRow1 && i != baseRow2) {
                                if (sudoku.getPossibilities(i, coverColumn1).remove(k)) {
                                    didSomething = true;
                                }
                                if (sudoku.getPossibilities(i, coverColumn2).remove(k)) {
                                    didSomething = true;
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
                // iterate all possible numbers
                for (int k = 1; k <= length; k++) {

                    foundIn1 = false;
                    foundIn2 = false;
                    occurrences.clear();
                    // save the row indices of the occurrences of this number in the base columns
                    for (int i = 0; i < length; i++) {
                        if (sudoku.getPossibilities(i, baseColumn1).contains(k)) {
                            occurrences.add(i);
                            foundIn1 = true;
                        }
                        if (sudoku.getPossibilities(i, baseColumn2).contains(k)) {
                            occurrences.add(i);
                            foundIn2 = true;
                        }
                    }
                    // Two rows do exist, which cover the occurrences of the current number in the current base columns
                    if (foundIn1 && foundIn2 && occurrences.size() == 2) {
                        Iterator<Integer> iterator = occurrences.iterator();
                        int coverRow1 = iterator.next();
                        int coverRow2 = iterator.next();

                        // delete all occurrences of the current number in the cover rows, which are not contained
                        // in the base columns too
                        for (int j = 0; j < length; j++) {
                            if (j != baseColumn1 && j != baseColumn2) {
                                if (sudoku.getPossibilities(coverRow1, j).remove(k)) {
                                    didSomething = true;
                                }
                                if (sudoku.getPossibilities(coverRow2, j).remove(k)) {
                                    didSomething = true;
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
        return 7;
    }
}