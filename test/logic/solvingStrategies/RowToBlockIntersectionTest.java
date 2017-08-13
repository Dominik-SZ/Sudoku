package logic.solvingStrategies;

import logic.Sudoku;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.*;


public class RowToBlockIntersectionTest {

    private Sudoku sudoku;
    private RowToBlockIntersection strategy;

    @Before
    public void setUp() throws Exception {
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
        sudoku.calculatePossibilities();
        strategy = new RowToBlockIntersection(sudoku);
    }

    @Test
    public void test() throws Exception {
        HashSet<Integer> old21 = sudoku.getPossibilities(2,1);
        strategy.apply();
        HashSet<Integer> new21 = sudoku.getPossibilities(2,1);

        assertEquals(old21, new21);
    }


}