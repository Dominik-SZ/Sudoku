import controller.SudokuMain;
import model.Sudoku;
import model.solving.strategies.SolvingStrategy;
import model.solving.solver.SolvingStrategyFactory;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
    @Ignore
    public void fillAnalysis() {
        // WARNING: This test takes a long time to perform! About 3 minutes on my (Dominik) computer. You can adjust
        // the time by reducing the TEST_AMOUNT or the RANDOM_FILL_AMOUNT. Needed time is O(TEST_AMOUNT *
        // RANDOM_FILL_AMOUNT).

        // how many Sudokus are generated and used to determine the average per random fill amount
        final int TEST_AMOUNT = 20;
        final int RANDOM_FILL_AMOUNT = 25;

        double[] averageFieldsFilled = new double[RANDOM_FILL_AMOUNT];
        long[] averageTimeNeeded = new long[RANDOM_FILL_AMOUNT];
        ArrayList[] averageOperationsAmount = new ArrayList[RANDOM_FILL_AMOUNT];
        Sudoku sudoku = new Sudoku(9, 10);
        List<SolvingStrategy> usedStrategies = new LinkedList<>();

        // iterate the random fill amounts
        for (int rf = 0; rf < RANDOM_FILL_AMOUNT; rf++) {
            double totalFills = 0;
            long startTime = System.nanoTime();
            // only save the amount information of the performed operations
            ArrayList<Double> operationsAmount = new ArrayList<>(SolvingStrategyFactory.STRATEGY_AMOUNT);
            for (int i = 0; i < SolvingStrategyFactory.STRATEGY_AMOUNT; i++) {
                operationsAmount.add(0.0);
            }

            // tests with fixed random fills
            for (int t = 1; t <= TEST_AMOUNT; t++) {
                usedStrategies = sudoku.fill(rf);
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

        //--------------------------------------------------------------------------------------------------------------
        // print the outcome
        String lineSeparator = System.getProperty("line.separator");
        StringBuilder builder = new StringBuilder(500);

        // first line
        String[] firstLine = new String[usedStrategies.size() + 3];
        firstLine[0] = String.format("%17s", "");
        firstLine[1] = "| average fields filled  ";
        firstLine[2] = "| average time needed  ";
        for(int i = 0; i < usedStrategies.size(); i++) {
            System.out.println(usedStrategies.get(i).getClass().toString().substring(20));
            firstLine[i + 3] = "| " + usedStrategies.get(i).getClass().toString().substring(20) + "  ";
        }
        for(String column: firstLine) {
            builder.append(column);
        }
        int lineWidth = builder.toString().length();
        builder.append(lineSeparator);

        // second line
        StringBuilder secondLine = new StringBuilder(lineWidth);
        for(int i = 0; i < lineWidth; i++) {
            secondLine.append("-");
        }
        secondLine.append(lineSeparator);
        builder.append(secondLine);

        // further lines
        int halfColumnLength;
        String leftFormat;
        String rightFormat;

        for (int rf = 0; rf < RANDOM_FILL_AMOUNT; rf++) {
            // column 0
            builder.append("RandomFills: ");
            builder.append(rf);
            if(rf < 10) {
                builder.append("   ");
            } else {
                builder.append("  ");
            }

            // column 1
            halfColumnLength = firstLine[1].length() / 2;
            leftFormat = "|%"+ (halfColumnLength + firstLine[1].length() % 2) +"s";
            rightFormat = "%" + (halfColumnLength-1) + "s";
            builder.append(String.format(leftFormat, averageFieldsFilled[rf]));
            builder.append(String.format(rightFormat, ""));

            // column 2
            halfColumnLength = firstLine[2].length() / 2;
            leftFormat = "|%"+ (halfColumnLength + firstLine[2].length() % 2) +"s";
            rightFormat = "%" + (halfColumnLength-1) + "s";
            builder.append(String.format(leftFormat, averageTimeNeeded[rf] + " ms"));
            builder.append(String.format(rightFormat, ""));

            // rest columns
            ArrayList averageOperations = averageOperationsAmount[rf];
            for(int i = 3; i < firstLine.length; i++) {
                halfColumnLength = firstLine[i].length() / 2;
                leftFormat = "|%"+ (halfColumnLength + firstLine[i].length() % 2) +"s";
                rightFormat = "%" + (halfColumnLength-1) + "s";
                builder.append(String.format(leftFormat, averageOperations.get(i-3)));
                builder.append(String.format(rightFormat, ""));
            }
            builder.append(lineSeparator);

        }
        System.out.println(builder);

    }

}