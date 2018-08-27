package model2.solving;

import model2.Sudoku;

public class SudokuSolver {

    public static final int PERMUTATION_AMOUNT = 50;

    private int[][] board;
    private int length;
    private int blockLength;

    private SudokuShuffler shuffler;

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
            board = new SudokuReader().readReferenceSudoku();
            shuffler = new SudokuShuffler();
        }

        shuffler.shuffle(board, PERMUTATION_AMOUNT);


        Sudoku sudoku = new Sudoku(board, false);
        return sudoku;
    }


    public int[][] getBoard() {
        return this.board;
    }
}
