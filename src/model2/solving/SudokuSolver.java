package model2.solving;

import javafx.util.Pair;
import model2.Sudoku;
import util.IntPair;

import java.io.*;
import java.util.Random;

public class SudokuSolver {

    private static final int PERMUTATION_AMOUNT = 50;

    private int[][] board;
    private int length;
    private int blockLength;

    public SudokuSolver() {
    }

    public Sudoku generateSudoku(int length, int difficulty) {
        if (length < 1) {
            throw new IllegalArgumentException("length must be at least 1.");
        }
        this.length = length;
        double bl = Math.sqrt(length);
        if (bl % 1 != 0) {
            throw new IllegalArgumentException("length must be a square number.");
        }
        this.blockLength = (int) bl;

        // first solving call
        if (board == null) {
            readReferenceSudoku();
        }

        shuffle();


        Sudoku sudoku = new Sudoku(board);
        return sudoku;
    }

    private void readReferenceSudoku() {
        board = new int[length][length];
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("ReferenceSudoku.txt")));
            for (int i = 0; i < length; i++) {
                String[] line = reader.readLine().split(",");
                for (int j = 0; j < length; j++) {
                    board[i][j] = Integer.parseInt(line[j]);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("SudokuSolver: Couldn't find the reference Sudoku");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("SudokuSolver: Error reading from the reference Sudoku");
            e.printStackTrace();
        }
    }

    /**
     * Shuffles the board to make it a random starting point from which on to erase fields.
     */
    private void shuffle() {
        for (int i = 0; i < PERMUTATION_AMOUNT; i++) {
            IntPair indices = createIndexPair();
            if (Math.random() < 0.5) {
                switchRows(indices.i, indices.j);
            } else {
                switchColumns(indices.i, indices.j);
            }
        }
    }

    /**
     * Creates an index pair whose corresponding rows and columns are safely allowed to be permuted without breaking
     * Sudoku constraints. These indices are two out of {0,1,2}, or {3,4,5}, or {6,7,8} at standard length.
     *
     * @return And index pair whose corresponding rows or columns may be permuted
     */
    private IntPair createIndexPair() {
        int firstIndex = (int) (Math.random() * blockLength);
        int offset = (int) (Math.random() * (blockLength - 1));
        int secondIndex = (firstIndex + offset) % blockLength;
        return new IntPair(firstIndex, secondIndex);
    }

    private void switchRows(int firstIndex, int secondIndex) {
        int[] buffer = new int[length];
        System.arraycopy(board[firstIndex], 0, buffer, 0, length);
        System.arraycopy(board[secondIndex], 0, board[firstIndex], 0, length);
        System.arraycopy(buffer, 0, board[secondIndex], 0, length);
    }

    private void switchColumns(int firstIndex, int secondIndex) {
        int buffer;
        for (int i = 0; i < length; i++) {
            buffer = board[i][firstIndex];
            board[i][firstIndex] = board[i][secondIndex];
            board[i][secondIndex] = buffer;
        }
    }
}
