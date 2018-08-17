package model;


import java.util.HashSet;

public class SudokuField {

    /** the maximum value of this field */
    private final int maxValue;
    private int startValue;
    private int solutionValue;
    private int currentValue;
    private HashSet<Integer> possibilities;



	/**
	 * Creates a new SudokuField with 0s as value for the currentValue, startValue and solutionValue as well as a
	 * possibility HashSet, which is filled with all values from 1-length.
	 *
	 * @param maxValue	The length of the based board
	 */
	SudokuField(int maxValue) {
		this.maxValue = maxValue;
		this.startValue = 0;
		this.solutionValue = 0;
		this.currentValue = 0;
        this.possibilities = new HashSet<>(maxValue);
		resetPossibilities();
	}

    //------------------------------------------------------------------------------------------------------------------

	/**
	 * Fills the possibilities HashMap of the field with all numbers from
	 * 1 to maxValue.
	 */
	void resetPossibilities() {
		for (int k = 1; k <= maxValue; k++) {
			possibilities.add(k);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------
    // Getter:

    public int getCurrentValue() {
        return currentValue;
    }

    HashSet<Integer> getPossibilities() {
        return this.possibilities;
    }

    public int getSolutionValue() {
        return solutionValue;
    }

    int getStartValue() {
        return startValue;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setter:
    void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }

    void setStartValue(int value) {
        this.startValue = value;
    }

    void setSolutionValue(int value) {
        this.solutionValue = value;
    }

}
