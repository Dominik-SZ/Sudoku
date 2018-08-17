import model.Sudoku;
import model.solving.strategies.Jellyfish;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;

public class JellyfishTest {
    private static Sudoku sudoku;
    private static Jellyfish strategy;

    @BeforeClass
    public static void setUp() throws Exception {
        int[][] values = {
                {2, 0, 0, 0, 0, 0, 0, 0, 3},
                {0, 8, 0, 0, 3, 0, 0, 5, 0},
                {0, 0, 3, 4, 0, 2, 1, 0, 0},
                {0, 0, 1, 2, 0, 5, 4, 0, 0},
                {0, 0, 0, 0, 9, 0, 0, 0, 0},
                {0, 0, 9, 3, 0, 8, 6, 0, 0},
                {0, 0, 2, 5, 0, 6, 9, 0, 0},
                {0, 9, 0, 0, 2, 0, 0, 7, 0},
                {4, 0, 0, 0, 0, 0, 0, 0, 1}
        };
        sudoku = new Sudoku(values);
        strategy = new Jellyfish(sudoku);
    }
    @Test
    public void apply() throws Exception {
        strategy.apply();

        // field 01 (affected by the strategy)
        HashSet<Integer> expected01 = new HashSet<>(4);
        expected01.add(1);
        expected01.add(4);
        expected01.add(5);
        expected01.add(6);
        // expected01.add(7); removed by the strategy
        HashSet<Integer> found01 = sudoku.getPossibilities(0, 1);
        assertEquals(expected01, found01);

        // field 04 (affected by the strategy)
        HashSet<Integer> expected04 = new HashSet<>(4);
        expected04.add(1);
        expected04.add(5);
        expected04.add(6);
        // expected04.add(7); removed by the strategy
        expected04.add(8);
        HashSet<Integer> found04 = sudoku.getPossibilities(0, 4);
        assertEquals(expected04, found04);

        // field 10 (affected by the strategy)
        HashSet<Integer> expected10 = new HashSet<>(3);
        expected10.add(1);
        expected10.add(6);
        // expected04.add(7); removed by the strategy
        expected10.add(9);
        HashSet<Integer> found10 = sudoku.getPossibilities(1, 0);
        assertEquals(expected10, found10);

        // field 48 (affected by the strategy)
        HashSet<Integer> expected48 = new HashSet<>(3);
        expected48.add(2);
        expected48.add(5);
        // expected04.add(7); removed by the strategy
        expected48.add(8);
        HashSet<Integer> found48 = sudoku.getPossibilities(4, 8);
        assertEquals(expected48, found48);

        // field 84 (affected by the strategy)
        HashSet<Integer> expected84 = new HashSet<>(1);
        // expected04.add(7); removed by the strategy
        expected84.add(8);
        HashSet<Integer> found84 = sudoku.getPossibilities(8, 4);
        assertEquals(expected84, found84);

        // field 20 (used but not affected by the strategy)
        HashSet<Integer> expected20 = new HashSet<>(4);
        expected20.add(5);
        expected20.add(6);
        expected20.add(7);
        expected20.add(9);
        HashSet<Integer> found20 = sudoku.getPossibilities(2, 0);
        assertEquals(expected20, found20);

        // field 46 (not affected by the strategy)
        HashSet<Integer> expected46 = new HashSet<>(4);
        expected46.add(2);
        expected46.add(3);
        expected46.add(5);
        expected46.add(7);
        expected46.add(8);
        HashSet<Integer> found46 = sudoku.getPossibilities(4, 6);
        assertEquals(expected46, found46);
    }

}