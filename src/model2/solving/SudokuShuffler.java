package model2.solving;

import util.InputChecker;
import util.IntPair;

import java.util.ArrayList;

public class SudokuShuffler {

    private int[][] board;
    private int length;
    private int blockLength;
    private ArrayList<IntPair> possiblePairs;

    /**
     * Shuffles the inserted board while keeping it valid regarding the Sudoku ruleset to make it a random starting
     * point from which on to erase fields.
     *
     * @param board The board to shuffle
     * @param times The amount of permutations that shall be performed on the board
     */
    public void shuffle(int[][] board, int times) {
        InputChecker.isSquareNumber(board.length);

        if (length != board.length) {   // new length => new possible index pairs
            this.length = board.length;
            this.blockLength = (int) Math.sqrt(length);
            createPossibleIndexPairs();
        }
        this.board = board;

        for (int i = 0; i < times; i++) {
            IntPair indices = getRandomIndexPair();
            if (Math.random() < 0.5) {
                switchRows(indices.i, indices.j);
            } else {
                switchColumns(indices.i, indices.j);
            }
        }
    }


    private void createPossibleIndexPairs() {
        possiblePairs = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            for (int j = i + 1; j < (i / blockLength) * blockLength + blockLength; j++) {
                possiblePairs.add(new IntPair(i, j));
            }
        }
    }

    /**
     * Gets an index pair whose corresponding rows and columns are safely allowed to be permuted without breaking
     * Sudoku constraints. These indices are two out of {0,1,2}, or {3,4,5}, or {6,7,8} at standard length.
     *
     * @return An index pair whose corresponding rows or columns may be permuted
     */
    private IntPair getRandomIndexPair() {
        return possiblePairs.get((int) (Math.random() * possiblePairs.size()));
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
