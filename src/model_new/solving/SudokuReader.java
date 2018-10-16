package model_new.solving;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This class can be used to read special saved Sudokus.
 */
public class SudokuReader {

    /**
     * Reads a correct, previously saved Sudoku and returns it. This can be used to generate a new Sudoku by
     * permuting it and erasing fields in a controlled way.
     *
     * @return The Sudoku board read or null if an IOError occurred
     */
    public int[][] readReferenceSudoku() {
        try {
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(
                            "ReferenceSudoku.txt")));
            // BufferedReader reader = new BufferedReader(new FileReader(new File("ReferenceSudoku.txt")));
            int length = Integer.parseInt(reader.readLine());
            int[][] board = new int[length][length];

            for (int i = 0; i < length; i++) {
                String[] line = reader.readLine().split(",");
                for (int j = 0; j < line.length; j++) {
                    board[i][j] = Integer.parseInt(line[j]);
                }
            }
            return board;
        } catch (FileNotFoundException e) {
            System.err.println("SudokuSolver: Couldn't find the reference Sudoku");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("SudokuSolver: Error reading from the reference Sudoku");
            e.printStackTrace();
        }
        return null;
    }
}
