import model.Sudoku;
import model.solving.strategies.Swordfish;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.*;

public class SwordfishTest {
    private static Sudoku sudoku;
    private static Swordfish strategy;

    @BeforeClass
    public static void setUp() throws Exception {
        int[][] values = {
                {1, 6, 0, 5, 4, 3, 0, 7, 0},
                {0, 7, 8, 6, 0, 1, 4, 3, 5},
                {4, 3, 5, 8, 0, 7, 6, 0, 1},
                {7, 2, 0, 4, 5, 8, 0, 6, 9},
                {6, 0, 0, 9, 1, 2, 0, 5, 7},
                {0, 0, 0, 3, 7, 6, 0, 0, 4},
                {0, 1, 6, 0, 3, 0, 0, 4, 0},
                {3, 0, 0, 0, 8, 0, 0, 1, 6},
                {0, 0, 7, 1, 6, 4, 5, 0, 3}
        };
        sudoku = new Sudoku(values);
        strategy = new Swordfish(sudoku);
    }

    @Test
    public void apply() throws Exception {
        // field 10 (used but not affected by the strategy)
        HashSet<Integer> expected10 = new HashSet<>(2);
        expected10.add(2);
        expected10.add(9);
        HashSet<Integer> found10 = sudoku.getPossibilities(1, 0);
        assertEquals(expected10, found10);

        // field 14 (used but not affected by the strategy)
        HashSet<Integer> expected14 = new HashSet<>(2);
        expected14.add(2);
        expected14.add(9);
        HashSet<Integer> found14 = sudoku.getPossibilities(1, 4);
        assertEquals(expected14, found14);

        // field 27 (used but not affected by the strategy)
        HashSet<Integer> expected27 = new HashSet<>(2);
        expected27.add(2);
        expected27.add(9);
        HashSet<Integer> found27 = sudoku.getPossibilities(2, 7);
        assertEquals(expected27, found27);

        // field 80 (used but not affected by the strategy)
        HashSet<Integer> expected80 = new HashSet<>(3);
        expected80.add(2);
        expected80.add(8);
        expected80.add(9);
        HashSet<Integer> found80 = sudoku.getPossibilities(8, 0);
        assertEquals(expected80, found80);

        // field 87 (used but not affected by the strategy)
        HashSet<Integer> expected87 = new HashSet<>(3);
        expected87.add(2);
        expected87.add(8);
        expected87.add(9);
        HashSet<Integer> found87 = sudoku.getPossibilities(8, 7);
        assertEquals(expected87, found87);

        // field 57 (affected by the strategy)
        HashSet<Integer> expected57 = new HashSet<>(2);
        expected57.add(2);
        expected57.add(8);
        HashSet<Integer> found57 = sudoku.getPossibilities(5, 7);
        assertEquals(expected57, found57);

        // field 60 (affected by the strategy)
        HashSet<Integer> expected60 = new HashSet<>(4);
        expected60.add(2);
        expected60.add(5);
        expected60.add(8);
        expected60.add(9);
        HashSet<Integer> found60 = sudoku.getPossibilities(6, 0);
        assertEquals(expected60, found60);

        // field 50 (affected by the strategy)
        HashSet<Integer> expected50 = new HashSet<>(3);
        expected50.add(5);
        expected50.add(8);
        expected50.add(9);
        HashSet<Integer> found50 = sudoku.getPossibilities(5, 0);
        assertEquals(expected50, found50);

        //--------------------------------------------------------------------------------------------------------------
        strategy.apply();

        // field 10 (used but not affected by the strategy)
        HashSet<Integer> expectedNew10 = new HashSet<>(2);
        expectedNew10.add(2);
        expectedNew10.add(9);
        HashSet<Integer> foundNew10 = sudoku.getPossibilities(1, 0);
        assertEquals(expectedNew10, foundNew10);

        // field 14 (used but not affected by the strategy)
        HashSet<Integer> expectedNew14 = new HashSet<>(2);
        expectedNew14.add(2);
        expectedNew14.add(9);
        HashSet<Integer> foundNew14 = sudoku.getPossibilities(1, 4);
        assertEquals(expectedNew14, foundNew14);

        // field 27 (used but not affected by the strategy)
        HashSet<Integer> expectedNew27 = new HashSet<>(2);
        expectedNew27.add(2);
        expectedNew27.add(9);
        HashSet<Integer> foundNew27 = sudoku.getPossibilities(2, 7);
        assertEquals(expectedNew27, foundNew27);

        // field 80 (used but not affected by the strategy)
        HashSet<Integer> expectedNew80 = new HashSet<>(3);
        expectedNew80.add(2);
        expectedNew80.add(8);
        expectedNew80.add(9);
        HashSet<Integer> foundNew80 = sudoku.getPossibilities(8, 0);
        assertEquals(expectedNew80, foundNew80);

        // field 87 (used but not affected by the strategy)
        HashSet<Integer> expectedNew87 = new HashSet<>(3);
        expectedNew87.add(2);
        expectedNew87.add(8);
        expectedNew87.add(9);
        HashSet<Integer> foundNew87 = sudoku.getPossibilities(8, 7);
        assertEquals(expectedNew87, foundNew87);

        // field 57 (affected by the strategy)
        HashSet<Integer> expectedNew57 = new HashSet<>(1);
        // expectedNew57.add(2); removed by the strategy
        expectedNew57.add(8);
        HashSet<Integer> foundNew57 = sudoku.getPossibilities(5, 7);
        assertEquals(expectedNew57, foundNew57);

        // field 60 (affected by the strategy)
        HashSet<Integer> expectedNew60 = new HashSet<>(3);
        // expectedNew60.add(2); removed by the strategy
        expectedNew60.add(5);
        expectedNew60.add(8);
        expectedNew60.add(9);
        HashSet<Integer> foundNew60 = sudoku.getPossibilities(6, 0);
        assertEquals(expectedNew60, foundNew60);

        // field 50 (not affected by the strategy)
        HashSet<Integer> expectedNew50 = new HashSet<>(3);
        expectedNew50.add(5);
        expectedNew50.add(8);
        expectedNew50.add(9);
        HashSet<Integer> foundNew50 = sudoku.getPossibilities(5, 0);
        assertEquals(expectedNew50, foundNew50);
    }

}