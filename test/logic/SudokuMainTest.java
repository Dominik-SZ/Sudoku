package logic;

import org.junit.Test;

import static org.junit.Assert.*;

public class SudokuMainTest {
    @Test
    public void buildSudoku() throws Exception {
        Sudoku sudoku = SudokuMain.buildSudoku(9,1);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();
        sudoku = SudokuMain.buildSudoku(9,2);
        assertTrue(sudoku.isFilled());
        sudoku = SudokuMain.buildSudoku(9,3);
        assertTrue(sudoku.isFilled());
        sudoku = SudokuMain.buildSudoku(9,4);
        assertTrue(sudoku.isFilled());
        sudoku = SudokuMain.buildSudoku(9,5);
        assertTrue(sudoku.isFilled());
        sudoku = SudokuMain.buildSudoku(9,6);
        assertTrue(sudoku.isFilled());
        sudoku = SudokuMain.buildSudoku(9,7);
        assertTrue(sudoku.isFilled());
        sudoku = SudokuMain.buildSudoku(9,8);
        assertTrue(sudoku.isFilled());
        sudoku = SudokuMain.buildSudoku(9,9);
        assertTrue(sudoku.isFilled());
        sudoku = SudokuMain.buildSudoku(9,10);
        assertTrue(sudoku.isFilled());
    }

}