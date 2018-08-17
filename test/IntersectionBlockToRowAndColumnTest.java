import model.Sudoku;
import model.solving.strategies.IntersectionBlockToRowAndColumn;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;

public class IntersectionBlockToRowAndColumnTest {

    private static Sudoku sudoku;
    private static IntersectionBlockToRowAndColumn strategy;

    @BeforeClass
    public static void setUp() {
        int [][] values = {
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

        sudoku = new Sudoku(values);
        strategy = new IntersectionBlockToRowAndColumn(sudoku);

    }

    @Test
    public void apply() throws Exception {
        // field 62 (affected by the strategy)
        HashSet<Integer> expectedOld62 = new HashSet<>(7);
        expectedOld62.add(1);
        expectedOld62.add(3);
        expectedOld62.add(4);
        expectedOld62.add(5);
        expectedOld62.add(6);
        expectedOld62.add(8);
        expectedOld62.add(9);
        HashSet<Integer> foundOld62 = sudoku.getPossibilities(6,2);
        assertEquals(expectedOld62, foundOld62);

        // field 66 (affected by the strategy)
        HashSet<Integer> expectedOld66 = new HashSet<>(5);
        expectedOld66.add(1);
        expectedOld66.add(2);
        expectedOld66.add(3);
        expectedOld66.add(4);
        expectedOld66.add(6);
        HashSet<Integer> foundOld66 = sudoku.getPossibilities(6,6);
        assertEquals(expectedOld66, foundOld66);

        // field 63 (not affected by the strategy)
        HashSet<Integer> expectedOld63 = new HashSet<>(4);
        expectedOld63.add(1);
        expectedOld63.add(4);
        expectedOld63.add(5);
        expectedOld63.add(7);
        HashSet<Integer> foundOld63 = sudoku.getPossibilities(6,3);
        assertEquals(expectedOld63, foundOld63);

        // field 65 (not affected by the strategy)
        HashSet<Integer> expectedOld65 = new HashSet<>(3);
        expectedOld65.add(1);
        expectedOld65.add(5);
        expectedOld65.add(7);
        HashSet<Integer> foundOld65 = sudoku.getPossibilities(6,5);
        assertEquals(expectedOld65, foundOld65);

        // field 52 (outside of the row)
        HashSet<Integer> expectedOld52 = new HashSet<>(6);
        expectedOld52.add(1);
        expectedOld52.add(3);
        expectedOld52.add(4);
        expectedOld52.add(5);
        expectedOld52.add(6);
        expectedOld52.add(8);
        HashSet<Integer> foundOld52 = sudoku.getPossibilities(5,2);
        assertEquals(expectedOld52, foundOld52);

        // field 53 (outside of the row)
        HashSet<Integer> expectedOld53 = new HashSet<>(3);
        expectedOld53.add(5);
        expectedOld53.add(7);
        expectedOld53.add(8);
        HashSet<Integer> foundOld53 = sudoku.getPossibilities(5,3);
        assertEquals(expectedOld53, foundOld53);

        strategy.apply();

        // field 62 (affected by the strategy)
        HashSet<Integer> expectedNew62 = new HashSet<>(5);
        // expectedNew62.add(1);    removed by the strategy
        expectedNew62.add(3);
        expectedNew62.add(4);
        expectedNew62.add(5);
        // expectedNew62.add(6);    also removed by the strategy
        expectedNew62.add(8);
        expectedNew62.add(9);
        HashSet<Integer> foundNew62 = sudoku.getPossibilities(6,2);
        assertEquals(expectedNew62, foundNew62);

        // field 66 (affected by the strategy)
        HashSet<Integer> expectedNew66 = new HashSet<>(4);
        // expectedNew66.add(1);    removed by the strategy
        expectedNew66.add(2);
        expectedNew66.add(3);
        expectedNew66.add(4);
        expectedNew66.add(6);
        HashSet<Integer> foundNew66 = sudoku.getPossibilities(6,6);
        assertEquals(expectedNew66, foundNew66);

        // field 63 (not affected by the strategy)
        HashSet<Integer> expectedNew63 = new HashSet<>(4);
        expectedNew63.add(1);
        expectedNew63.add(4);
        expectedNew63.add(5);
        expectedNew63.add(7);
        HashSet<Integer> foundNew63 = sudoku.getPossibilities(6,3);
        assertEquals(expectedNew63, foundNew63);

        // field 65 (not affected by the strategy)
        HashSet<Integer> expectedNew65 = new HashSet<>(3);
        expectedNew65.add(1);
        expectedNew65.add(5);
        expectedNew65.add(7);
        HashSet<Integer> foundNew65 = sudoku.getPossibilities(6,5);
        assertEquals(expectedNew65, foundNew65);

        // field 52 (outside of the row)
        HashSet<Integer> expectedNew52 = new HashSet<>(6);
        expectedNew52.add(1);
        expectedNew52.add(3);
        expectedNew52.add(4);
        expectedNew52.add(5);
        expectedNew52.add(6);
        expectedNew52.add(8);
        HashSet<Integer> foundNew52 = sudoku.getPossibilities(5,2);
        assertEquals(expectedNew52, foundNew52);

        // field 53 (outside of the row)
        HashSet<Integer> expectedNew53 = new HashSet<>(3);
        expectedNew53.add(5);
        expectedNew53.add(7);
        expectedNew53.add(8);
        HashSet<Integer> foundNew53 = sudoku.getPossibilities(5,3);
        assertEquals(expectedNew53, foundNew53);
    }

}