package logic;

import logic.solvingStrategies.SolvingStrategy;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Formatter;

import static org.junit.Assert.assertTrue;

public class SudokuMainTest {

    @Test
    public void build4Sudokus() throws Exception {
        Sudoku sudoku = SudokuMain.buildSudoku(4, 1);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();
        sudoku = SudokuMain.buildSudoku(4, 2);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();
        sudoku = SudokuMain.buildSudoku(4, 3);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();
        sudoku = SudokuMain.buildSudoku(4, 4);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();
        sudoku = SudokuMain.buildSudoku(4, 5);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();
        sudoku = SudokuMain.buildSudoku(4, 6);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();
        sudoku = SudokuMain.buildSudoku(4, 7);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();
        sudoku = SudokuMain.buildSudoku(4, 8);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();
        sudoku = SudokuMain.buildSudoku(4, 9);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();
        sudoku = SudokuMain.buildSudoku(4, 10);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();
    }

    @Test
    public void build9Sudokus() throws Exception {
        Sudoku sudoku = SudokuMain.buildSudoku(9, 1);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();
        sudoku = SudokuMain.buildSudoku(9, 2);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();
        sudoku = SudokuMain.buildSudoku(9, 3);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();
        sudoku = SudokuMain.buildSudoku(9, 4);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();
        sudoku = SudokuMain.buildSudoku(9, 5);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();
        sudoku = SudokuMain.buildSudoku(9, 6);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();
        sudoku = SudokuMain.buildSudoku(9, 7);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();
        sudoku = SudokuMain.buildSudoku(9, 8);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();
        sudoku = SudokuMain.buildSudoku(9, 9);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();
        sudoku = SudokuMain.buildSudoku(9, 10);
        assertTrue(sudoku.isFilled());
        sudoku.clearCompletely();
    }

    @Test
    public void fillAnalysis() {
        // how many sudokus are generated and used to determine the average per random fill amount
        final int TEST_AMOUNT = 20;
        final int RANDOM_FILL_AMOUNT = 25;
        final int STRATEGY_AMOUNT = 11;

        double[] averageFieldsFilled = new double[RANDOM_FILL_AMOUNT];
        long[] averageTimeNeeded = new long[RANDOM_FILL_AMOUNT];
        ArrayList[] averageOperationsAmount = new ArrayList[RANDOM_FILL_AMOUNT];
        Sudoku sudoku = new Sudoku(9, 10);

        for (int rf = 0; rf < RANDOM_FILL_AMOUNT; rf++) {
            double totalFills = 0;
            long startTime = System.nanoTime();
            // only save the amount information of the performed operations
            ArrayList<Double> operationsAmount = new ArrayList<>(STRATEGY_AMOUNT);
            for (int i = 0; i < STRATEGY_AMOUNT; i++) {
                operationsAmount.add(0.0);
            }

            // tests with fixed random fills
            for (int t = 1; t <= TEST_AMOUNT; t++) {
                ArrayList<SolvingStrategy> usedStrategies = sudoku.fill(rf);
                totalFills += sudoku.count();
                for (int i = 0; i < usedStrategies.size(); i++) {
                    operationsAmount.set(i, operationsAmount.get(i) + usedStrategies.get(i).getPerformedOperations()
                            .size());
                }
                sudoku.clearCompletely();
            }

            // calculate the averages of one cycle
            for (int i = 0; i < operationsAmount.size(); i++) {
                operationsAmount.set(i, operationsAmount.get(i) / TEST_AMOUNT);
            }
            averageOperationsAmount[rf] = operationsAmount;

            averageTimeNeeded[rf] = (System.nanoTime() - startTime) / TEST_AMOUNT / 1000000;
            averageFieldsFilled[rf] = totalFills / TEST_AMOUNT;

        }

        String lineSeparator = System.getProperty("line.separator");
        StringBuilder builder = new StringBuilder(500);

        builder.append("\t\t\t\taverage fields filled\taverage time needed\tIntersectionRowToBlock\t");
        builder.append("IntersectionColumnToBlock\tIntersectionBlockToRowAndColumn\tX-Wing\tSwordfish\t");
        builder.append("Jellyfish\tOnlyOnePossibilityOnField\tHiddenSingleRow\tHiddenSingleColumn\tHiddenSingleBlock");
        builder.append(lineSeparator);
        for (int rf = 0; rf < RANDOM_FILL_AMOUNT; rf++) {
            builder.append("RandomFills: ");
            builder.append(rf);
            builder.append("\t\t\t");
            builder.append(averageFieldsFilled[rf]);
            builder.append("\t\t\t\t\t\t");
            builder.append(averageTimeNeeded[rf]);
            builder.append(" ms\t\t");
            for(Object oa:averageOperationsAmount[rf]) {
                builder.append(oa);
                builder.append("\t\t\t\t");
            }
            builder.append(lineSeparator);
        }
        System.out.println(builder);

        String centerAlignFormat = "| %-15s | %-4d |%n";

    }

}