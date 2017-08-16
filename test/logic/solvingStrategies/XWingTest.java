package logic.solvingStrategies;

import logic.Sudoku;
import logic.exceptions.PIVException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;

public class XWingTest {
    /**
     * the Sudoku using rows as base and columns as cover
     */
    private static Sudoku sudokuRowBases;
    /**
     * the Sudoku using columns as bases and rows as cover
     */
    private static Sudoku sudokuColumnBases;
    private static XWing strategyRowBases;
    private static XWing strategyColumnBases;

    @BeforeClass
    public static void setUp() throws Exception {
        int[][] valuesRowBases = {
                {0, 4, 1, 7, 2, 9, 0, 3, 0},
                {7, 6, 9, 0, 0, 3, 4, 0, 2},
                {0, 3, 2, 6, 4, 0, 7, 1, 9},
                {4, 0, 3, 9, 0, 0, 1, 7, 0},
                {6, 0, 7, 0, 0, 4, 9, 0, 3},
                {1, 9, 5, 3, 7, 0, 0, 2, 4},
                {2, 1, 4, 5, 6, 7, 3, 9, 8},
                {3, 7, 6, 0, 9, 0, 5, 4, 1},
                {9, 5, 8, 4, 3, 1, 2, 6, 7}
            };
        sudokuRowBases = new Sudoku(valuesRowBases);
        strategyRowBases = new XWing(sudokuRowBases);

        int[][] valuesColumnBases = {
                {9, 8, 0, 0, 6, 2, 7, 5, 3},
                {0, 6, 5, 0, 0, 3, 0, 0, 0},
                {3, 2, 7, 0, 5, 0, 0, 0, 6},
                {7, 9, 0, 0, 3, 0, 5, 0, 0},
                {0, 5, 0, 0, 0, 9, 0, 0, 0},
                {8, 3, 2, 0, 4, 5, 0, 0, 9},
                {6, 7, 3, 5, 9, 1, 4, 2, 8},
                {2, 4, 9, 0, 8, 7, 0, 0, 5},
                {5, 1, 8, 0, 2, 0, 0, 0, 7}
            };
        sudokuColumnBases = new Sudoku(valuesColumnBases);
        strategyColumnBases = new XWing(sudokuColumnBases);
    }

    @Test
    public void rowBases() throws Exception {
        // field 14 (used but not affected by the strategy)
        HashSet<Integer> expected14 = new HashSet<>(3);
        expected14.add(1);
        expected14.add(5);
        expected14.add(8);
        HashSet<Integer> found14 = sudokuRowBases.getPossibilities(1, 4);
        assertEquals(expected14, found14);

        // field 17 (used but not affected by the strategy)
        HashSet<Integer> expected17 = new HashSet<>(2);
        expected17.add(5);
        expected17.add(8);
        HashSet<Integer> found17 = sudokuRowBases.getPossibilities(1, 7);
        assertEquals(expected17, found17);

        // field 44 (used but not affected by the strategy)
        HashSet<Integer> expected44 = new HashSet<>(3);
        expected44.add(1);
        expected44.add(5);
        expected44.add(8);
        HashSet<Integer> found44 = sudokuRowBases.getPossibilities(4, 4);
        assertEquals(expected44, found44);

        // field 47 (used but not affected by the strategy)
        HashSet<Integer> expected47 = new HashSet<>(2);
        expected47.add(5);
        expected47.add(8);
        HashSet<Integer> found47 = sudokuRowBases.getPossibilities(4, 7);
        assertEquals(expected47, found47);

        // field 34 (affected by the strategy)
        HashSet<Integer> expected34 = new HashSet<>(2);
        expected34.add(5);
        expected34.add(8);
        HashSet<Integer> found34 = sudokuRowBases.getPossibilities(3, 4);
        assertEquals(expected34, found34);

        // field 35 (unaffected)
        HashSet<Integer> expected35 = new HashSet<>(4);
        expected35.add(2);
        expected35.add(5);
        expected35.add(6);
        expected35.add(8);
        HashSet<Integer> found35 = sudokuRowBases.getPossibilities(3, 5);
        assertEquals(expected35, found35);

        strategyRowBases.apply();

        // field 14 (used but not affected by the strategy)
        HashSet<Integer> expectedNew14 = new HashSet<>(3);
        expectedNew14.add(1);
        expectedNew14.add(5);
        expectedNew14.add(8);
        HashSet<Integer> foundNew14 = sudokuRowBases.getPossibilities(1, 4);
        assertEquals(expectedNew14, foundNew14);

        // field 17 (used but not affected by the strategy)
        HashSet<Integer> expectedNew17 = new HashSet<>(2);
        expectedNew17.add(5);
        expectedNew17.add(8);
        HashSet<Integer> foundNew17 = sudokuRowBases.getPossibilities(1, 7);
        assertEquals(expectedNew17, foundNew17);

        // field 44 (used but not affected by the strategy)
        HashSet<Integer> expectedNew44 = new HashSet<>(3);
        expectedNew44.add(1);
        expectedNew44.add(5);
        expectedNew44.add(8);
        HashSet<Integer> foundNew44 = sudokuRowBases.getPossibilities(4, 4);
        assertEquals(expectedNew44, foundNew44);

        // field 47 (used but not affected by the strategy)
        HashSet<Integer> expectedNew47 = new HashSet<>(2);
        expectedNew47.add(5);
        expectedNew47.add(8);
        HashSet<Integer> foundNew47 = sudokuRowBases.getPossibilities(4, 7);
        assertEquals(expectedNew47, foundNew47);

        // field 34 (affected by the strategy)
        HashSet<Integer> expectedNew34 = new HashSet<>(2);
        expectedNew34.add(8);
        HashSet<Integer> foundNew34 = sudokuRowBases.getPossibilities(3, 4);
        assertEquals(expectedNew34, foundNew34);

        // field 35 (unaffected)
        HashSet<Integer> expectedNew35 = new HashSet<>(4);
        expectedNew35.add(2);
        expectedNew35.add(5);
        expectedNew35.add(6);
        expectedNew35.add(8);
        HashSet<Integer> foundNew35 = sudokuRowBases.getPossibilities(3, 5);
        assertEquals(expectedNew35, foundNew35);
    }

    @Test
    public void columnBases() throws PIVException {
        // field 10 (used but not affected by the strategy)
        HashSet<Integer> expected10 = new HashSet<>(2);
        expected10.add(1);
        expected10.add(4);
        HashSet<Integer> found10 = sudokuColumnBases.getPossibilities(1, 0);
        assertEquals(expected10, found10);

        // field 14 (used but not affected by the strategy)
        HashSet<Integer> expected14 = new HashSet<>(2);
        expected14.add(1);
        expected14.add(7);
        HashSet<Integer> found14 = sudokuColumnBases.getPossibilities(1, 4);
        assertEquals(expected14, found14);

        // field 40 (used but not affected by the strategy)
        HashSet<Integer> expected40 = new HashSet<>(2);
        expected40.add(1);
        expected40.add(4);
        HashSet<Integer> found40 = sudokuColumnBases.getPossibilities(4, 0);
        assertEquals(expected40, found40);

        // field 44 (used but not affected by the strategy)
        HashSet<Integer> expected44 = new HashSet<>(2);
        expected44.add(1);
        expected44.add(7);
        HashSet<Integer> found44 = sudokuColumnBases.getPossibilities(4, 4);
        assertEquals(expected44, found44);

        // field 13 (affected by the strategy)
        HashSet<Integer> expected13 = new HashSet<>(5);
        expected13.add(1);
        expected13.add(4);
        expected13.add(7);
        expected13.add(8);
        expected13.add(9);
        HashSet<Integer> found13 = sudokuColumnBases.getPossibilities(1, 3);
        assertEquals(expected13, found13);

        // field 17 (affected by the strategy)
        HashSet<Integer> expected17 = new HashSet<>(4);
        expected17.add(1);
        expected17.add(4);
        expected17.add(8);
        expected17.add(9);
        HashSet<Integer> found17 = sudokuColumnBases.getPossibilities(1, 7);
        assertEquals(expected17, found17);

        // field 47 (affected by the strategy)
        HashSet<Integer> expected47 = new HashSet<>(6);
        expected47.add(1);
        expected47.add(3);
        expected47.add(4);
        expected47.add(6);
        expected47.add(7);
        expected47.add(8);
        HashSet<Integer> found47 = sudokuColumnBases.getPossibilities(4, 7);
        assertEquals(expected47, found47);

        // field 42 (affected by the strategy)
        HashSet<Integer> expected42 = new HashSet<>(3);
        expected42.add(1);
        expected42.add(4);
        expected42.add(6);
        HashSet<Integer> found42 = sudokuColumnBases.getPossibilities(4, 2);
        assertEquals(expected42, found42);

        // field 26 (unaffected)
        HashSet<Integer> expected26 = new HashSet<>(3);
        expected26.add(1);
        expected26.add(8);
        expected26.add(9);
        HashSet<Integer> found26 = sudokuColumnBases.getPossibilities(2, 6);
        assertEquals(expected26, found26);

        strategyColumnBases.apply();

        // field 10 (used but not affected by the strategy)
        HashSet<Integer> expectedNew10 = new HashSet<>(2);
        expectedNew10.add(1);
        expectedNew10.add(4);
        HashSet<Integer> foundNew10 = sudokuColumnBases.getPossibilities(1, 0);
        assertEquals(expectedNew10, foundNew10);

        // field 14 (used but not affected by the strategy)
        HashSet<Integer> expectedNew14 = new HashSet<>(2);
        expectedNew14.add(1);
        expectedNew14.add(7);
        HashSet<Integer> foundNew14 = sudokuColumnBases.getPossibilities(1, 4);
        assertEquals(expectedNew14, foundNew14);

        // field 40 (used but not affected by the strategy)
        HashSet<Integer> expectedNew40 = new HashSet<>(2);
        expectedNew40.add(1);
        expectedNew40.add(4);
        HashSet<Integer> foundNew40 = sudokuColumnBases.getPossibilities(4, 0);
        assertEquals(expectedNew40, foundNew40);

        // field 44 (used but not affected by the strategy)
        HashSet<Integer> expectedNew44 = new HashSet<>(2);
        expectedNew44.add(1);
        expectedNew44.add(7);
        HashSet<Integer> foundNew44 = sudokuColumnBases.getPossibilities(4, 4);
        assertEquals(expectedNew44, foundNew44);

        // field 13 (affected by the strategy)
        HashSet<Integer> expectedNew13 = new HashSet<>(4);
        // expected13.add(1); removed by the strategy
        expectedNew13.add(4);
        expectedNew13.add(7);
        expectedNew13.add(8);
        expectedNew13.add(9);
        HashSet<Integer> foundNew13 = sudokuColumnBases.getPossibilities(1, 3);
        assertEquals(expectedNew13, foundNew13);

        // field 17 (affected by the strategy)
        HashSet<Integer> expectedNew17 = new HashSet<>(3);
        // expected17.add(1); removed by the strategy
        expectedNew17.add(4);
        expectedNew17.add(8);
        expectedNew17.add(9);
        HashSet<Integer> foundNew17 = sudokuColumnBases.getPossibilities(1, 7);
        assertEquals(expectedNew17, foundNew17);

        // field 47 (affected by the strategy)
        HashSet<Integer> expectedNew47 = new HashSet<>(5);
        // expected47.add(1); removed by the strategy
        expectedNew47.add(3);
        expectedNew47.add(4);
        expectedNew47.add(6);
        expectedNew47.add(7);
        expectedNew47.add(8);
        HashSet<Integer> foundNew47 = sudokuColumnBases.getPossibilities(4, 7);
        assertEquals(expectedNew47, foundNew47);

        // field 42 (affected by the strategy)
        HashSet<Integer> expectedNew42 = new HashSet<>(2);
        // expected42.add(1); removed by the strategy
        expectedNew42.add(4);
        expectedNew42.add(6);
        HashSet<Integer> foundNew42 = sudokuColumnBases.getPossibilities(4, 2);
        assertEquals(expectedNew42, foundNew42);

        // field 26 (unaffected)
        HashSet<Integer> expectedNew26 = new HashSet<>(3);
        expectedNew26.add(1);
        expectedNew26.add(8);
        expectedNew26.add(9);
        HashSet<Integer> foundNew26 = sudokuColumnBases.getPossibilities(2, 6);
        assertEquals(expectedNew26, foundNew26);
    }

}