package logic;


import java.util.Collection;
import java.util.HashSet;

public class SudokuField {

    private int length;         // the length of the based
    private int currentValue;
    private HashSet<Integer> possibilities;

    private int startValue;
    private int solutionValue;

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
    public void resetPossibilities() {
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

    public void setCurrentValue(int currentValue) {
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
