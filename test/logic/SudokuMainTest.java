package logic;

import org.junit.Test;

import static org.junit.Assert.*;

public class SudokuMainTest {

    @Test
    public void build4Sudokus() throws Exception {
        Sudoku sudoku = SudokuMain.buildSudoku(4,1);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();
        sudoku = SudokuMain.buildSudoku(4,2);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();
        sudoku = SudokuMain.buildSudoku(4,3);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();
        sudoku = SudokuMain.buildSudoku(4,4);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();
        sudoku = SudokuMain.buildSudoku(4,5);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();
        sudoku = SudokuMain.buildSudoku(4,6);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();
        sudoku = SudokuMain.buildSudoku(4,7);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();
        sudoku = SudokuMain.buildSudoku(4,8);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();
        sudoku = SudokuMain.buildSudoku(4,9);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();
        sudoku = SudokuMain.buildSudoku(4,10);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();
    }

    @Test
    public void build9Sudokus() throws Exception {

        Sudoku sudoku = SudokuMain.buildSudoku(9,1);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();

        sudoku = SudokuMain.buildSudoku(9,2);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();

        sudoku = SudokuMain.buildSudoku(9,3);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();
        sudoku = SudokuMain.buildSudoku(9,4);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();
        sudoku = SudokuMain.buildSudoku(9,5);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();
        sudoku = SudokuMain.buildSudoku(9,6);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();

        sudoku = SudokuMain.buildSudoku(9,7);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();

        sudoku = SudokuMain.buildSudoku(9,8);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();

        sudoku = SudokuMain.buildSudoku(9,9);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();
        sudoku = SudokuMain.buildSudoku(9,10);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();

    }

    @Test
    public void fillAmount() {
        final int TEST_AMOUNT = 20;
        double totalFills = 0;

        for(int i = 1; i <= TEST_AMOUNT; i++) {
            Sudoku sudoku = SudokuMain.buildSudoku(9, 10);
            totalFills += sudoku.count();
        }
        double average = totalFills /TEST_AMOUNT;
        System.out.println(average + " fields filled on average by " + TEST_AMOUNT + " tests with difficulty 10 and " +
                                   "length 9");
    }

}