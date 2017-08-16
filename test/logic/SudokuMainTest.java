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
        // how many sudokus are generated and used to determine the average per random fill amount
        final int TEST_AMOUNT = 25;
        final int RANDOM_FILL_CAP = 25;
        double[] averageFieldsFilled = new double[RANDOM_FILL_CAP + 1];
        long[] averageTimeNeeded = new long[RANDOM_FILL_CAP + 1];

        for (int rf = 0; rf <= RANDOM_FILL_CAP; rf++) {
            double totalFills = 0;
            long startTime = System.nanoTime();

            for(int t = 1; t <= TEST_AMOUNT; t++) {
                Sudoku sudoku = new Sudoku(9, 10);
                sudoku.fill(rf);
                totalFills += sudoku.count();
            }

            averageTimeNeeded[rf] = (System.nanoTime() - startTime) / TEST_AMOUNT / 1000000;
            averageFieldsFilled[rf] = totalFills /TEST_AMOUNT;
        }

        String lineSeparator = System.getProperty("line.separator");
        StringBuilder builder = new StringBuilder(500);
        builder.append("\t\t\t\taverage fields filled\t\taverage time needed");
        builder.append(lineSeparator);
        for(int i = 0; i < averageFieldsFilled.length; i++) {
            builder.append("RandomFills: ");
            builder.append(i);
            builder.append("\t\t\t");
            builder.append(averageFieldsFilled[i]);
            builder.append("\t\t\t\t\t\t");
            builder.append(averageTimeNeeded[i]);
            builder.append(" ms");
            builder.append(lineSeparator);
        }
        System.out.println(builder);
    }

}