package util;

public class InputChecker {

    /**
     * Checks if the inserted number is a square number. If it is not, an IllegalArgumentException is thrown.
     *
     * @param length The number to check
     */
    static public void checkIfValidLength(int length) {
        double root = Math.sqrt(length);
        if (!(root * root == length)) {
            throw new IllegalArgumentException("Input is no square number: " + length);
        }
        if (length < 1) {
            throw new IllegalArgumentException("length must be at least 1");
        }
    }

    /**
     * Checks if the inserted value is an allowed entry of it's Sudoku (1 to length) (element of {1,2,3,4,5,6,7,8,9}
     * standard). If it is not, an IllegalArgumentException is thrown.
     *
     * @param input  The entry to check
     * @param length The length of the Sudoku
     */
    static public void checkIfValidEntry(int input, int length, EntryType type) {
        if (type == EntryType.ENTRY && (input < 0 || length < input)) {
            throw new IllegalArgumentException("Inserted entry out of bounds: " + input);
        }
        if (type == EntryType.INDEX && (input < 0 || length <= input)) {
            throw new IllegalArgumentException("Inserted index out of bounds: " + input);
        }
    }

    public enum EntryType {
        ENTRY, INDEX
    }
}
