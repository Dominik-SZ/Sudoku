package logic.solvingStrategies;

import com.sun.rowset.internal.Row;
import logic.Sudoku;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;


public class RowIntersectionTest {

    private Sudoku sudoku1;
    private RowIntersection subject1;

    private Sudoku sudoku2;
    private RowIntersection subject2;

    @Before
    public void setUp() throws Exception {
        int[][] values1 = {
                {9, 8, 4, 0, 0, 0, 0, 0, 0},
                {0, 0, 2, 5, 0, 0, 0, 4, 0},
                {0, 0, 1, 9, 0, 4, 0, 0, 2},
                {0, 0, 6, 0, 9, 7, 2, 3, 0},
                {0, 0, 3, 6, 0, 2, 0, 0, 0},
                {2, 0, 9, 0, 3, 5, 6, 1, 0},
                {1, 9, 5, 7, 6, 8, 4, 2, 3},
                {4, 2, 7, 3, 5, 1, 8, 9, 6},
                {6, 3, 8, 0, 0, 9, 7, 5, 1}
        };
        sudoku1 = new Sudoku(values1);
        sudoku1.calculatePossibilities();
        subject1 = new RowIntersection(sudoku1);

        int[][] values2 = {
                {3, 4, 0, 0, 0, 6, 0, 7, 0},
                {0, 8, 0, 0, 0, 0, 9, 3, 0},
                {0, 0, 2, 0, 3, 0, 0, 6, 0},
                {0, 0, 0, 0, 1, 0, 0, 0, 0},
                {0, 9, 7, 3, 6, 4, 8, 5, 0},
                {0, 0, 0, 0, 0, 2, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 6, 0, 8, 0, 9, 0},
                {0, 0, 0, 9, 2, 3, 7, 8, 5}
        };
        sudoku2 = new Sudoku(values2);
        sudoku2.calculatePossibilities();
        subject2 = new RowIntersection(sudoku2);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test1() throws Exception {
        assertTrue(sudoku1.getPossibilities(2, 6).contains(3));
        assertTrue(sudoku1.getPossibilities(2, 6).contains(5));
        subject1.apply();
        assertTrue(sudoku1.getPossibilities(2, 6).contains(3));
        assertFalse(sudoku1.getPossibilities(2, 6).contains(5));
    }

    @Test
    public void test2() {
        int[] poss1 = {1, 2, 4, 5, 6, 7, 8, 9};
        Collection<Integer> la = sudoku2.getPossibilities(0, 6);
        int [] aposs2 = la.toArray(new int[la.size()]);  //TODO: HELP PLOX
        assertArrayEquals(poss1, sudoku2.getPossibilities(0, 6).toArray(new int[])));
    }

}