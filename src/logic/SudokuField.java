package logic;


import java.util.Collection;
import java.util.HashSet;

public class SudokuField {

    /** the maximum value of this field */
    private final int maxValue;
    private int currentValue;
    private HashSet<Integer> possibilities;

    private int startValue;
    private int solutionValue;

	/**
	 * Creates a new SudokuField with 0s as value for the currentValue, startValue and solutionValue as well as a
	 * possibility HashSet, which is filled with all values from 1-length.
	 *
	 * @param maxValue	The length of the based board
	 */
	SudokuField(int maxValue) {
		this.maxValue = maxValue;
		this.currentValue = 0;
		this.startValue = 0;
		this.solutionValue = 0;
		this.possibilities = new HashSet<>();
		resetPossibilities();
	}

    //-------------------------------------------------------------------------

	/**
	 * Fills the possibilities HashMap of the field with all numbers from
	 * 1 to maxValue.
	 */
	void resetPossibilities() {
		for (int k = 1; k <= maxValue; k++) {
			possibilities.add(k);
		}
	}

    public int getCurrentValue() {
        return currentValue;
    }

    void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }

    Collection<Integer> getPossibilities() {
        return this.possibilities;
    }

    int getStartValue() {
        return startValue;
    }

    void setStartValue(int value) {
        this.startValue = value;
    }

    public int getSolutionValue() {
        return solutionValue;
    }

    void setSolutionValue(int value) {
        this.solutionValue = value;
    }



}
