package logic.solvingStrategies;

import logic.Sudoku;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;


public class RowIntersectionTest {

    private Sudoku sudoku;
    private RowIntersection subject;

    @Before
    public void setUp() throws Exception {
        int[][] values = {
                {9, 8, 4, 0, 0, 0, 0, 0, 0},
                {0, 0, 2, 5, 0, 0, 0, 4, 0},
                {0, 0, 1, 9, 0, 4, 0, 0, 2},
                {0, 0, 6, 0, 9, 7, 3, 2, 0},
                {0, 0, 3, 6, 0, 2, 0, 0, 0},
                {2, 0, 9, 0, 3, 5, 6, 1, 0},
                {1, 9, 5, 7, 6, 8, 4, 2, 3},
                {4, 2, 7, 3, 5, 1, 8, 9, 6},
                {6, 3, 8, 0, 0, 9, 7, 5, 1}
        };
        Sudoku sudoku = new Sudoku(values);
        sudoku.calculatePossibilities();
        subject = new RowIntersection(sudoku);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void applyTest() throws Exception {
    }

    @Test
    public void nochEinTest() {

    }

}