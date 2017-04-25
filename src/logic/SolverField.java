package logic;

import java.util.Collection;
import java.util.HashSet;

class SolverField {
    private int length;
    private int currentValue;
    private HashSet<Integer> possibilities;

    /**
     * Creates an empty SolverField with the standard length 9 and the currentValue 0 as well as an empty HashSet.
     */
    SolverField() {
        this.length = 9;
        this.currentValue = 0;
        this.possibilities = new HashSet<>();
    }

    /**
     * Creates a new SolverField with the inserted currentValue and a new HashSet containing all possibilities from 1 to length.
     *
     * @param currentValue The currentValue to be placed on this field
     * @param length       The length of the wrapping array
     */
    SolverField(int currentValue, int length) {
        this.length = length;
        this.currentValue = currentValue;
        this.possibilities = new HashSet<>();
        resetPossibilities();
    }

    void resetPossibilities() {
        for (int k = 1; k <= length; k++) {
            possibilities.add(k);
        }
    }

    Collection<Integer> getPossibilities() {
        return this.possibilities;
    }

    public int getCurrentValue() {
        return currentValue;
    }

    void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }
}
