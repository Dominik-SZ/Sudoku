package model_new;

import java.util.Deque;

public class Sudoku {

    int length;
    int blockLength;
    int difficulty;
    Deque states;
    int[][] startBoard;
    int[][] solutionBoard;


    public Sudoku() {

    }

    /**
     * Creates a new Sudoku object with the inserted board either as start board (for instance when inserted out of a
     * riddle magazine) or as solution board (for instance when created by a SudokuSolver).
     *
     * @param board The board to use
     * @param start If it is the start board or the solution board
     */
    public Sudoku(int[][] board, boolean start) {
        if (start) {
            this.startBoard = board;
        } else {
            this.solutionBoard = board;
        }
    }

}
