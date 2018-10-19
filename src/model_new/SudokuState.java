package model_new;

public class SudokuState {

    SudokuField[][] state;

    public SudokuState(int[][] values, boolean[][][] notes) {
        int length = values.length;
        state = new SudokuField[length][length];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                state[i][j] = new SudokuField(length, values[i][j], notes[i][j]);
            }
        }
    }

}
