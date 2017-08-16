package logic.solvingStrategies;

import logic.Sudoku;
import logic.exceptions.PIVException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;


public class IntersectionRowToBlockTest {

    private static Sudoku sudoku;
    private static IntersectionRowToBlock strategy;

    @BeforeClass
    public static void setUp() throws Exception {
        int[][] values = {
                {3, 1, 8, 0, 0, 5, 4, 0, 6},
                {0, 0, 0, 6, 0, 3, 8, 1, 0},
                {0, 0, 6, 0, 8, 0, 5, 0, 3},
                {8, 6, 4, 9, 5, 2, 1, 3, 7},
                {1, 2, 3, 4, 7, 6, 9, 5, 8},
                {7, 9, 5, 3, 1, 8, 2, 6, 4},
                {0, 3, 0, 5, 0, 0, 7, 8, 0},
                {0, 0, 0, 0, 0, 7, 3, 0, 5},
                {0, 0, 0, 0, 3, 9, 6, 4, 1}
        };
        sudoku = new Sudoku(values);
        strategy = new IntersectionRowToBlock(sudoku);
    }

    @Test
    public void beforeStrategy() throws PIVException {
        // field 21 (affected by the strategy)
        HashSet<Integer> expectedOld21 = new HashSet<>(2);
        expectedOld21.add(4);
        expectedOld21.add(7);
        HashSet<Integer> foundOld21 = sudoku.getPossibilities(2,1);
        assertEquals(expectedOld21, foundOld21);

        // field 11 (not affected by the strategy)
        HashSet<Integer> expectedOld11 = new HashSet<>(3);
        expectedOld11.add(4);
        expectedOld11.add(5);
        expectedOld11.add(7);
        HashSet<Integer> foundOld11 = sudoku.getPossibilities(1,1);
        assertEquals(expectedOld11, foundOld11);

        // field 20 (not affected by the strategy)
        HashSet<Integer> expectedOld20 = new HashSet<>(3);
        expectedOld20.add(2);
        expectedOld20.add(4);
        expectedOld20.add(9);
        HashSet<Integer> foundOld20 = sudoku.getPossibilities(2,0);
        assertEquals(expectedOld20, foundOld20);

        // field 23 (outside of the block)
        HashSet<Integer> expectedOld23 = new HashSet<>(3);
        expectedOld23.add(1);
        expectedOld23.add(2);
        expectedOld23.add(7);
        HashSet<Integer> foundOld23 = sudoku.getPossibilities(2,3);
        assertEquals(expectedOld23, foundOld23);

        strategy.apply();

        // field 21 (affected by the strategy)
        HashSet<Integer> expectedNew21 = new HashSet<>(1);
        expectedNew21.add(4);
        HashSet<Integer> foundNew21 = sudoku.getPossibilities(2,1);
        assertEquals(expectedNew21, foundNew21);

        // field 11 (not affected by the strategy)
        HashSet<Integer> expectedNew11 = new HashSet<>(3);
        expectedNew11.add(4);
        expectedNew11.add(5);
        expectedNew11.add(7);
        HashSet<Integer> foundNew11 = sudoku.getPossibilities(1,1);
        assertEquals(expectedNew11, foundNew11);

        // field 20 (not affected by the strategy)
        HashSet<Integer> expectedNew20 = new HashSet<>(3);
        expectedNew20.add(2);
        expectedNew20.add(4);
        expectedNew20.add(9);
        HashSet<Integer> foundNew20 = sudoku.getPossibilities(2,0);
        assertEquals(expectedNew20, foundNew20);

        // field 23 (outside of the block)
        HashSet<Integer> expectedNew23 = new HashSet<>(3);
        expectedNew23.add(1);
        expectedNew23.add(2);
        expectedNew23.add(7);
        HashSet<Integer> foundNew23 = sudoku.getPossibilities(2,3);
        assertEquals(expectedNew23, foundNew23);
    }

}