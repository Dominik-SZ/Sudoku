package logic;


import java.util.Collection;
import java.util.HashSet;

public class SudokuField {

	private int length;
	private int currentValue;
	private HashSet<Integer> possibilities;

	private int startValue;
	private int solutionValue;

	/**
	 * Creates a new SudokuField with 0s as value for the currentValue, startValue and solutionValue as well as a
	 * possibility HashSet, which is filled with all values from 1-length.
	 *
	 * @param length	The length of the based board
	 */
	SudokuField(int length) {
		this.length = length;
		this.currentValue = 0;
		this.startValue = 0;
		this.solutionValue = 0;
		this.possibilities = new HashSet<>();
		resetPossibilities();
	}

	//-------------------------------------------------------------------------

	/**
	 * Fills the possibilities HashMap of the field with all numbers from
	 * 1 to length.
	 */
	void resetPossibilities() {
		for (int k = 1; k <= length; k++) {
			possibilities.add(k);
		}
	}

	public void setLength(int length) {
		this.length = length;
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
