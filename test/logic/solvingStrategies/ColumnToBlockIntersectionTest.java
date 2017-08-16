package logic.solvingStrategies;

import logic.Sudoku;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;


public class ColumnToBlockIntersectionTest {

    private Sudoku sudoku;
    private ColumnToBlockIntersection strategy;

    @Before
    public void setUp() throws Exception {
        int[][] values = {
                {7, 6, 2, 0, 0, 8, 0, 0, 1},
                {9, 8, 0, 0, 0, 0, 0, 0, 6},
                {1, 5, 0, 0, 0, 0, 0, 8, 7},
                {4, 7, 8, 0, 0, 3, 1, 6, 9},
                {5, 2, 6, 0, 0, 9, 8, 7, 3},
                {3, 1, 9, 8, 0, 0, 4, 2, 5},
                {8, 3, 5, 0, 0, 1, 6, 9, 2},
                {2, 9, 7, 6, 8, 5, 3, 1, 4},
                {6, 4, 1, 9, 3, 2, 7, 5, 8}
        };

        sudoku = new Sudoku(values);
        strategy = new ColumnToBlockIntersection(sudoku);
    }


    @Test
    public void apply() throws Exception {
        // field 03
        HashSet<Integer> foundOld03 = sudoku.getPossibilities(0, 3);
        HashSet<Integer> expectedOld03 = new HashSet<>(3);
        expectedOld03.add(3);
        expectedOld03.add(4);
        expectedOld03.add(5);
        assertEquals(expectedOld03, foundOld03);

        // field 04
        HashSet<Integer> foundOld04 = sudoku.getPossibilities(0, 4);
        HashSet<Integer> expectedOld04 = new HashSet<>(3);
        expectedOld04.add(4);
        expectedOld04.add(5);
        expectedOld04.add(9);
        assertEquals(expectedOld04, foundOld04);

        // field 15
        HashSet<Integer> foundOld15 = sudoku.getPossibilities(1, 5);
        HashSet<Integer> expectedOld15 = new HashSet<>(2);
        expectedOld15.add(4);
        expectedOld15.add(7);
        assertEquals(expectedOld15, foundOld15);

        // field 25
        HashSet<Integer> foundOld25 = sudoku.getPossibilities(2, 5);
        HashSet<Integer> expectedOld25 = new HashSet<>(2);
        expectedOld25.add(4);
        expectedOld25.add(6);
        assertEquals(expectedOld25, foundOld25);

        // apply the strategy
        strategy.apply();

        // field 03
        HashSet<Integer> foundNew03 = sudoku.getPossibilities(0,3);
        HashSet<Integer> expectedNew03 = new HashSet<>(2);
        expectedNew03.add(3);
        expectedNew03.add(5);
        assertEquals(expectedNew03, foundNew03);

        // field 04
        HashSet<Integer> foundNew04 = sudoku.getPossibilities(0,4);
        HashSet<Integer> expectedNew04 = new HashSet<>(2);
        expectedNew04.add(5);
        expectedNew04.add(9);
        assertEquals(expectedNew04, foundNew04);

        // field 15
        HashSet<Integer> foundNew15 = sudoku.getPossibilities(1,5);
        HashSet<Integer> expectedNew15 = new HashSet<>(2);
        expectedNew15.add(4);
        expectedNew15.add(7);
        assertEquals(expectedNew15, foundNew15);

        // field 25
        HashSet<Integer> foundNew25 = sudoku.getPossibilities(2,5);
        HashSet<Integer> expectedNew25 = new HashSet<>(2);
        expectedNew25.add(4);
        expectedNew25.add(6);
        assertEquals(expectedNew25, foundNew25);
    }

}