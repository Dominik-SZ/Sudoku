package model_new.solving;

import model_new.Sudoku;
import util.InputChecker;

public class SudokuSolver {

    public static final int PERMUTATION_AMOUNT = 50;

    private int[][] board;
    private int length;
    private int blockLength;

    private SudokuShuffler shuffler;

    public SudokuSolver() {
    }

    public Sudoku generateSudoku(int length, int difficulty) {
        InputChecker.checkIfValidLength(length);
        this.length = length;
        this.blockLength = (int) Math.sqrt(length);

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
