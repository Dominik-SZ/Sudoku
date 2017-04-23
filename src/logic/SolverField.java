package logic;

import java.util.HashSet;

class SolverField {
    private int length;
    HashSet<Integer> possibilities;
    int value;

    /**
     * Creates a new SolverField with the inserted value and a new HashSet containing all possibilities from 1 to length.
     *
     * @param value The value to be placed on this field
     * @param length    The length of the wrapping array
     */
    SolverField(int value, int length) {
        this.length = length;
        this.value = value;
        this.possibilities = new HashSet<>();
        resetPossibilities();
    }

    void resetPossibilities() {
        for (int k = 1; k <= length; k++) {
            possibilities.add(k);
        }
    }
}
