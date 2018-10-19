package model_new;


import java.util.ArrayList;
import java.util.List;

public class SudokuField {

    /**
     * the maximum value of this field, which is equal to the length of the containing Sudoku
     */
    private final int length;
    private final int value;
    /**
     * The notes of the user on this field. Indices range from 0-9 (standard). Thereby used are 1-9 (standard)
     */
    private final boolean[] notes;


    /**
     * Creates a new SudokuField with initial value 0 and no notes inserted yet.
     *
     * @param length    The length of the based board (standard 9)
     */
    SudokuField(int length) {
		this.length = length;
        this.value = 0;
        this.notes = new boolean[length + 1];
    }

    SudokuField(int length, int value, boolean[] notes) {
        this.length = length;
        this.value = value;
        this.notes = notes;
    }


	// -----------------------------------------------------------------------------------------------------------------
    // Getter:

    public int getValue() {
        return value;
    }

    List<Integer> getNotes() {
        List<Integer> pos = new ArrayList<>(length);
        for (int i = 1; i < notes.length; i++) {
            if (notes[i]) {
                pos.add(i);
            }
        }
        return pos;
    }

}
