package logic.solvingStrategies;


import logic.Sudoku;
import logic.SudokuSolver;
import logic.exceptions.PIVException;
import util.Coordinate;

import java.util.HashSet;
import java.util.LinkedList;

/**
 * Checks if a possibility for a block is impossible to be placed on all but one field. If such a value and field is
 * found, the value gets placed on this field and true is returned. If no field and value are found, false is returned.
 * This is an interpreting method meaning that it actually looks for new values to insert.
 */
public class HiddenSingleBlock implements SolvingStrategy {
    private Sudoku sudoku;
    private SudokuSolver solver;
    private int length;
    private int blockLength;

    public HiddenSingleBlock(Sudoku sudoku, SudokuSolver solver) {
        this.sudoku = sudoku;
        this.solver = solver;
        this.length = sudoku.getLength();
        this.blockLength = sudoku.getBlockLength();
    }

    @Override
    public boolean apply() {
        HashSet<Integer> possi = new HashSet<>();
        boolean answer = false;

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
                        // remove already inserted values in this block from the
                        // set
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
                        solver.pushTrySolvingBackup(coord.i, coord.j);
                        sudoku.insertCurrentValue(k, coord.i, coord.j);
                        answer = true;
                    }
                }
            }
        }
        return answer; // false if no value has been found
    }

    @Override
    public int getDifficulty() {
        return 3;
    }
}
