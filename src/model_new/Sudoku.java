package model_new;

import java.util.Deque;

public class Sudoku {

    int length;
    int blockLength;
    int difficulty;
    /**
     * the states of the states, the user went through with this Sudoku
     */
    Deque<SudokuField> states;
    /**
     * the states that have been undone and can be forwarded again
     */
    Deque<SudokuState> putAside;
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
