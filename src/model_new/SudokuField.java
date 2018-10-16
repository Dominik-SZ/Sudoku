package model_new;


import java.util.ArrayList;
import java.util.List;

public class SudokuField {

    /** the maximum value of this field */
    private final int maxValue;
    private int value;
    /** indices range from 0-9 (standard). Thereby used are 1-9 (standard) */
    private boolean[] possibilities;
    /**
     * the notes made by the user in this field
     */
    private boolean[] notes;



	/**
	 * Creates a new SudokuField with initial value 0 and no possibilities inserted yet.
	 *
	 * @param maxValue	The length of the based board (standard 9)
	 */
	SudokuField(int maxValue) {
		this.maxValue = maxValue;
		this.value = 0;
        this.possibilities = new boolean[maxValue + 1];
	}


	// -----------------------------------------------------------------------------------------------------------------
    // Getter:

    public int getValue() {
        return value;
    }

    List<Integer> getPossibilities() {
        List<Integer> pos = new ArrayList<>(maxValue);
        for (int i = 1; i < possibilities.length; i++) {
            if (possibilities[i]) {
                pos.add(i);
            }
        }
        return pos;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setter:
    void setValue(int value) {
        this.value = value;
    }

    void setPossibility(int possibility, boolean arg) {
	    possibilities[possibility] = arg;
    }
}
