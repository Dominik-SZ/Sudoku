package model2.solving;

import model2.SudokuValidator;
import org.junit.Assert;
import org.junit.Test;

public class SudokuShufflerTest {

    @Test
    public void shuffleTest() {
        int[][] board = new SudokuReader().readReferenceSudoku();
        new SudokuShuffler().shuffle(board, SudokuSolver.PERMUTATION_AMOUNT);
        Assert.assertTrue(new SudokuValidator().isValid(board));
    }
}
