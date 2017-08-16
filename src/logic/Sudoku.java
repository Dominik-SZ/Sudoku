package logic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import logic.exceptions.PIVException;
import util.Coordinate;
import util.MathUtilities;
import util.GameStatus;

/**
 * Instances of this class calculate a solved Sudoku field (boardSolved) and a Sudoku board where some fields are
 * erased, so a player can try solving it (board). It is possible to create those instances with different lengths and
 * difficulties. To actually fill the Sudoku stumps, use the fill() method.
 *
 * @author Dominik
 */
public class Sudoku {
    /**
     * length of the Sudoku (standard 9)
     */
    private int length;
    /**
     * the length of a block (standard 3)
     */
    private int blockLength;
    /**
     * the difficulty of the Sudoku
     */
    private int difficulty;

    /**
     * the board of this Sudoku
     */
    private SudokuField[][] board;
    /**
     * if the possibilities are guaranteed to be correct at the moment
     */
    private boolean possibilityIntegrity;

    /**
     * if this sudoku has been filled meaning it is ready to be played
     */
    private boolean filled;
    // -------------------------------------------------------------------------

    // Constructors

    /**
     * Standard Constructor for a Sudoku. Uses length 9 and difficulty 5.
     */
    Sudoku() {
        this(9, 5);
    }

    /**
     * Creates a new Sudoku Object with the given length and difficulty. An empty board is initialized and the
     * attributes blockLength and stepWidth determined. Note that sudoku.fill() is required to actually fill the board.
     *
     * @param length     The length of the Sudoku (default is 9).
     * @param difficulty How difficult the Sudoku is going to be. Ranges from 1 to 10 (default is 5).
     * @throws IllegalArgumentException if the inserted length is no square number or the inserted difficulty is out of
     *                                  bounds (1-10).
     */
    public Sudoku(int length, int difficulty) throws IllegalArgumentException {
        if (Math.sqrt(length) % 1 != 0) {
            throw new IllegalArgumentException("Length is no square number: " + length);
        }
        if (difficulty < 1 || difficulty > 10) {
            throw new IllegalArgumentException("Difficulty out of bounds: " + difficulty + ". Allowed range is 1-10.");
        }
        this.length = length;
        this.difficulty = difficulty;
        this.blockLength = (int) Math.sqrt(length);
        this.board = new SudokuField[length][length];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                board[i][j] = new SudokuField(length);
            }
        }
        possibilityIntegrity = true;
        filled = false;
    }

    /**
     * Create a new Sudoku with the inserted values as start values and current values. To try to generate
     * solutionValues use solve(). This can be used to insert Sudokus out of riddle magazines or similar sources.
     *
     * @param startBoard The inserted startValues
     */
    public Sudoku(int[][] startBoard) {
        if (startBoard.length != startBoard[0].length) {
            throw new IllegalArgumentException("The dimensions of the inserted startBoard must be equal" + startBoard.length + " != " + startBoard[0].length);
        }
        if (Math.sqrt(startBoard.length) % 1 != 0) {
            throw new IllegalArgumentException("Length is no square number: " + startBoard.length);
        }
        this.length = startBoard.length;
        this.blockLength = (int) Math.sqrt(length);
        this.difficulty = 10;   // if it becomes solved, all values used to solve it are erased

        board = new SudokuField[length][length];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                board[i][j] = new SudokuField(length);
                board[i][j].setStartValue(startBoard[i][j]);
                board[i][j].setCurrentValue(startBoard[i][j]);
            }
        }
        calculatePossibilities();
    }

    /**
     * Returns a String representation of the normal Sudoku with empty fields displayed as 0.
     */
    @Override
    public String toString() {
        return toString(true, false);
    }

    /**
     * Returns a String representation of the Sudoku.
     *
     * @param zeros    Determines if zeros are displayed or shown as spaces
     * @param solution Determines if the solved or the normal field is chosen
     */
    String toString(boolean zeros, boolean solution) {
        String lineSeparator = System.getProperty("line.separator");
        StringBuilder builder = new StringBuilder(3 * length * length + 40);

        builder.append(lineSeparator);  // an empty line to separate at the top
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (j % blockLength == 0 && j != 0) {
                    // separate the blocks vertically with "|"s
                    builder.append(" |");
                }
                for (int k = MathUtilities.digits(length) - MathUtilities.digits(board[i][j].getCurrentValue()) + 1; k > 0; k--) {
                    builder.append(" ");    // add the right amount of spaces
                }
                if (solution) { // the solution board is demanded
                    if (board[i][j].getSolutionValue() == 0 && !zeros) {
                        builder.append(" ");
                    } else {
                        builder.append(board[i][j].getSolutionValue());
                    }
                } else { // the normal board is demanded
                    if (board[i][j].getCurrentValue() == 0 && !zeros) {
                        builder.append(" ");
                    } else {
                        builder.append(board[i][j].getCurrentValue());
                    }
                }
            }
            if (i % blockLength == blockLength - 1 && i != length - 1) {
                builder.append(lineSeparator);
                builder.append(" ");
                for (int k = (MathUtilities.digits(length) + 1) * length + (blockLength - 1) * 2 - 1; k > 0; k--) {
                    // separate the blocks horizontally with "-"s
                    builder.append("-");
                }
            }
            builder.append(lineSeparator);  // new line
        }
        return builder.toString();
    }

    /**
     * Prints the current Sudoku and the matching solution to the File "Sudoku.txt".
     */
    public void printToTextFile(String path) {
        printToTextFile(path, "Sudoku.txt", "Solution.txt");
    }

    // currently not used

    /**
     * Prints the current sudoku and the matching solution to the inserted file name.
     *
     * @param path             The path leading to the folder of the generated text files
     * @param fileName         The name of the destination file
     * @param solutionFileName The name of the solution file
     */
    private void printToTextFile(String path, String fileName, String solutionFileName) {
        PrintWriter out = null;
        path = path.replace('\\', '/');
        try {
            out = new PrintWriter(new FileWriter(new File(path + fileName + ".txt")));
            out.println("Difficulty: " + difficulty);
            out.println(count() + " fields are filled.");
            out.print(toString(false, false)); // print the normal field
            out.close();
            out = new PrintWriter(new FileWriter(new File(path + solutionFileName + ".txt")));
            out.println("Solution:");
            out.print(toString(true, true)); // print the solution
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            } else {
                System.out.println("PrintWriter not open");
            }
        }
    }

    // -------------------------------------------------------------------------

    /**
     * Actually fill the Sudoku. This sets startValues and solutionValues in the board. The current values of the board
     * are initialized to the start values. This method maintains possibility integrity.
     */
    void fill() {
        new SudokuSolver(this).fill();
        filled = true;
    }

    /**
     * Tries to solve the Sudoku like a player using the solving strategies available and returns if it was successful
     * in doing so.
     *
     * @return If the solving process was successful
     */
    public boolean solve() {
        try {
            return new SudokuSolver(this).solve();
        } catch (PIVException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Counts the amount of filled fields (current value != 0).
     */
    public int count() {
        int count = 0;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (board[i][j].getStartValue() != 0) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Checks if the inserted value at the specified coordinates is allowed to be placed. It is, if the possibilities of
     * this field contain the inserted value. Therefore possibility integrity must be assured when using this function.
     * In return it is way faster, than the other isAllowed() method.
     *
     * @param value  The value to check
     * @param iCoord The i coordinate of the position to check
     * @param jCoord The j coordinate of the position to check
     * @return If the value is allowed to be inserted there
     */
    boolean isAllowedQuick(int value, int iCoord, int jCoord) throws PIVException {
        if (!possibilityIntegrity) {
            throw new PIVException();
        }
        return board[iCoord][jCoord].getPossibilities().contains(value);
    }

    /**
     * Checks if the inserted value at the specified coordinates is allowed to be placed. It is, if the value is not
     * already inserted as currentValue in the same row, column or block. This method does not require possibility
     * integrity.
     *
     * @param value  The value to check
     * @param iCoord The i coordinate of the position to check
     * @param jCoord The j coordinate of the position to check
     * @return If the value is allowed to be inserted there
     */
    public boolean isAllowed(int value, int iCoord, int jCoord) {

        // check row
        for (int j = 0; j < length; j++) {
            if (board[iCoord][j].getCurrentValue() == value) {
                return false;
            }
        }

        // check column
        for (int i = 0; i < length; i++) {
            if (board[i][jCoord].getCurrentValue() == value) {
                return false;
            }
        }

        // check block
        int iStartValue = blockLength * (iCoord / blockLength);
        int jStartValue = blockLength * (jCoord / blockLength);
        for (int i = iStartValue; i < iStartValue + blockLength; i++) {
            for (int j = jStartValue; j < jStartValue + blockLength; j++) {
                if (board[i][j].getCurrentValue() == value) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Clears the board of the Sudoku. All currentValues are hereby set to 0 and the possibilities reset. Afterwards
     * possibility integrity is assured. If you want to reset the sudoku completely, consider using clearCompletely()
     * instead.
     */
    void clear() {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                board[i][j].setCurrentValue(0);
                board[i][j].resetPossibilities();
            }
        }
        possibilityIntegrity = true;
    }

    /**
     * Clears the board of the Sudoku completely. All current-, start- and solution values are hereby set to 0 and the
     * possibilities are reset. Afterwards it is no longer considered as "filled". If you only need to reset the current
     * values and possibilities, consider using clear() instead.
     */
    void clearCompletely() {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                board[i][j].setCurrentValue(0);
                board[i][j].setStartValue(0);
                board[i][j].setSolutionValue(0);
                board[i][j].resetPossibilities();
            }
        }
        possibilityIntegrity = true;
        filled = false;
    }

    /**
     * Returns all coordinates blocked by the inserted one. Those are the coordinates of the same row, column and
     * block.
     *
     * @param initial the initial coordinate
     * @return a collection of all blocked coordinates
     */
    public Collection<Coordinate> getBlocked(Coordinate initial) {

        Set<Coordinate> blocked = new HashSet<>();

        // iterate the column
        for (int i = 0; i < length; i++) {
            blocked.add(new Coordinate(i, initial.j));
        }
        // iterate the row
        for (int j = 0; j < length; j++) {
            blocked.add(new Coordinate(initial.i, j));
        }
        // iterate the block
        int iStartValue = blockLength * (initial.i / blockLength);
        int jStartValue = blockLength * (initial.j / blockLength);
        for (int i = iStartValue; i < iStartValue + blockLength; i++) {
            for (int j = jStartValue; j < jStartValue + blockLength; j++) {
                blocked.add(new Coordinate(i, j));
            }
        }

        return blocked;
    }

    /**
     * Returns the coordinates of all fields, whose currentValue is not 0.
     *
     * @return The coordinates of all fields, whose currentValue is not 0
     */
    public Collection<Coordinate> getFilled() {
        Set<Coordinate> filled = new HashSet<>();

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (board[i][j].getCurrentValue() != 0) {
                    filled.add(new Coordinate(i, j));
                }
            }
        }
        return filled;
    }

    /**
     * Checks all fields of this sudoku if their current value is not 0 and does not match the respective solution
     * value. All fields fulfilling this condition are returned.
     *
     * @return The coordinates of all non empty fields diverging from their solution value
     */
    public Collection<Coordinate> getMistakes() {
        LinkedList<Coordinate> mistakes = new LinkedList<>();

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (board[i][j].getCurrentValue() != 0 && board[i][j].getCurrentValue() != board[i][j].getSolutionValue()) {
                    mistakes.add(new Coordinate(i, j));
                }
            }
        }
        return mistakes;
    }

    /**
     * Checks the current status of the Sudoku: - Status.INCOMPLETE if not all fields of the sudoku are filled yet -
     * Status.CORRECT if all fields of the sudoku are filled and they are correct - Status.INCORRECT if all fields of
     * the sudoku are filled and at least one of them is incorrect
     *
     * @return The current status of the sudoku
     */
    public GameStatus getStatus() {
        if (!isFilledCompletely()) {
            return GameStatus.INCOMPLETE;
        } else if (isFilledCorrectly()) {
            return GameStatus.CORRECT;
        } else if (isFilledAlternatively()) {
            return GameStatus.ALTERNATIVELY_FILLED;
        } else {
            return GameStatus.INCORRECT;
        }
    }

    /**
     * Checks if the Sudoku is completely filled, meaning no 0s are found as current values and returns the answer.
     *
     * @return Whether the field is completely filled
     */
    private boolean isFilledCompletely() {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (board[i][j].getCurrentValue() == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if the Sudoku is completely filled correctly and returns the answer. The current values are checked by
     * doing so.
     *
     * @return Whether the Sudoku is finished correctly by the player
     */
    private boolean isFilledCorrectly() {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (board[i][j].getCurrentValue() != board[i][j].getSolutionValue()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Can be used to check if the current values of the board contain no conflicts (same number in same row, column or
     * block). This returns true even if the inserted solution differs from the original expected one.
     *
     * @return If the current values of the board contain no conflicts and zeros
     */
    private boolean isFilledAlternatively() {

        HashSet<Integer> occurrences = new HashSet<>();
        // check the rows
        for (int i = 0; i < length; i++) {
            occurrences.clear();
            for (int j = 0; j < length; j++) {
                if (occurrences.contains(board[i][j].getCurrentValue()) || board[i][j].getCurrentValue() == 0) {
                    return false;
                }
                occurrences.add(board[i][j].getCurrentValue());
            }
        }

        // check the columns
        for (int j = 0; j < length; j++) {
            occurrences.clear();
            for (int i = 0; i < length; i++) {
                if (occurrences.contains(board[i][j].getCurrentValue()) || board[i][j].getCurrentValue() == 0) {
                    return false;
                }
                occurrences.add(board[i][j].getCurrentValue());
            }
        }

        // check the blocks
        for (int iBlock = 0; iBlock < blockLength; iBlock++) {
            for (int jBlock = 0; jBlock < blockLength; jBlock++) {

                occurrences.clear();
                // iterate the current block
                int iStartValue = iBlock * blockLength;
                int jStartValue = jBlock * blockLength;
                for (int i = iStartValue; i < iStartValue + blockLength; i++) {
                    for (int j = jStartValue; j < jStartValue + blockLength; j++) {
                        if (occurrences.contains(board[i][j].getCurrentValue()) || board[i][j].getCurrentValue() == 0) {
                            return false;
                        }
                        occurrences.add(board[i][j].getCurrentValue());
                    }
                }

            }
        }

        return true;
    }

    /**
     * Sets the current values of all fields to their respective start values and updates the possibilities afterwards.
     */
    public void resetCurrentValues() {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                board[i][j].setCurrentValue(board[i][j].getStartValue());
            }
        }
        calculatePossibilities();
    }

    /**
     * Calculates the current possibility for all empty fields of the board based of the currently inserted current
     * values of the board. Previously inserted possibilities are overwritten. Afterwards possibility integrity is
     * assured. Note that this method does not use any solving strategy to restrict the possibilities. Progress made in
     * restricting the possibilities using restrictive solving strategies is lost afterwards.
     */
    public void calculatePossibilities() {
        // iterate the whole board
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {

                SudokuField currentField = board[i][j];
                currentField.getPossibilities().clear();
                if (currentField.getCurrentValue() == 0) {    // empty field
                    for (int k = 1; k <= length; k++) {
                        if (isAllowed(k, i, j)) {
                            currentField.getPossibilities().add(k);
                        }
                    }
                }

            }
        }
        possibilityIntegrity = true;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Setter-Methods

    /**
     * Sets the specified value as the current value of the field at the specified coordinates. This is done in five
     * different ways optimizing performance depending on the needs of the input: The following operations are listed in
     * descending order regarding performance:
     *
     * 1. insert a value, that is already set at this position.
     * 2. insert a value without caring about possibility integrity (maintainPI = false). Note that possibility
     * integrity is broken by doing this.
     * 3. setting a value on an empty field (current value == 0) while maintaining possibility integrity
     * 4. setting 0 as value onto a filled (current value != 0) field while maintaining possibility integrity
     * 5. replacing an existing current value with a new one (both != 0) while maintaining possibility integrity
     *
     * @param value The new current value to set
     * @param iCoord    The i coordinate at which to set the new current value
     * @param jCoord    The j coordinate at which to set the new current value
     * @param maintainPI    If possibility integrity should be maintained (affects performance)
     */
    public void setCurrentValue(int value, int iCoord, int jCoord, boolean maintainPI) {
        if (value < 0 || length < value) {
            throw new IllegalArgumentException("Inserted start value out of bounds: " + value);
        }
        if (iCoord < 0 || iCoord >= length) {
            throw new IllegalArgumentException("Inserted i coordinate out of bounds: " + iCoord);
        }
        if (jCoord < 0 || jCoord >= length) {
            throw new IllegalArgumentException("Inserted j coordinate out of bounds: " + jCoord);
        }

        // check if the value is already inserted at this position => nothing to do
        if (board[iCoord][jCoord].getCurrentValue() == value) {
            return;
        }

        // maintaining possibility integrity is not necessary
        if (!maintainPI) {
            board[iCoord][jCoord].setCurrentValue(value);
            board[iCoord][jCoord].getPossibilities().clear();
            possibilityIntegrity = false;
        }

        // maintaining possibility integrity is desired
        else {
            // just set a value
            if (board[iCoord][jCoord].getCurrentValue() == 0) {
                board[iCoord][jCoord].setCurrentValue(value);
                disallowPossibilities(value, iCoord, jCoord);
            }
            // just remove a value
            else if (value == 0) {
                reallowPossibilities(board[iCoord][jCoord].getCurrentValue(), iCoord, jCoord);
                board[iCoord][jCoord].setCurrentValue(0);
            }
            // replace an existing value
            else {
                reallowPossibilities(board[iCoord][jCoord].getCurrentValue(), iCoord, jCoord);
                board[iCoord][jCoord].setCurrentValue(value);
                disallowPossibilities(value, iCoord, jCoord);
            }
        }
    }

    //    /**
    //     * Sets the inserted value at the specified coordinates as currentValue in the board. Note that this method is very
    //     * quick but breaks possibility integrity. Consider using insertValue(), removeValue() or replaceValue() instead.
    //     *
    //     * @param value The value to be set
    //     * @param i     The i coordinate at which to set the specified value
    //     * @param j     The j coordinate at which to set the specified value
    //     */
    //    void setCurrentValue(int value, int i, int j) throws IllegalArgumentException {
    //        if (value < 0 || length < value) {
    //            throw new IllegalArgumentException("Inserted start value out of bounds: " + value);
    //        }
    //        if (i < 0 || i >= length) {
    //            throw new IllegalArgumentException("Inserted i coordinate out of bounds: " + i);
    //        }
    //        if (j < 0 || j >= length) {
    //            throw new IllegalArgumentException("Inserted j coordinate out of bounds: " + j);
    //        }
    //
    //        board[i][j].setCurrentValue(value);
    //        board[i][j].getPossibilities().clear();
    //        possibilityIntegrity = false;
    //    }
    //
    //    /**
    //     * Sets a new value as a current value of this Sudoku at the specified coordinates, removes all possibilities from
    //     * it and removes the affected possibilities. Note that this method only keeps possibility integrity, if the
    //     * previous entry was 0 or already the same as the new one.
    //     *
    //     * @param value  The new value to be inserted
    //     * @param iCoord The i coordinate on which to insert
    //     * @param jCoord The j coordinate on which to insert
    //     */
    //    public void insertCurrentValue(int value, int iCoord, int jCoord) throws IllegalArgumentException {
    //        if (value < 0 || length < value) {
    //            throw new IllegalArgumentException("Inserted start value out of bounds: " + value);
    //        }
    //        if (iCoord < 0 || length <= iCoord) {
    //            throw new IllegalArgumentException("Inserted i coordinate out of bounds: " + iCoord);
    //        }
    //        if (jCoord < 0 || length <= jCoord) {
    //            throw new IllegalArgumentException("Inserted j coordinate out of bounds: " + jCoord);
    //        }
    //
    //        // check if the value is already inserted at this position
    //        if (board[iCoord][jCoord].getCurrentValue() == value) {
    //            return;
    //        }
    //
    //        // check if the value at this position was 0 up to now
    //        if (board[iCoord][jCoord].getCurrentValue() != 0) {
    //            possibilityIntegrity = false;
    //        }
    //
    //        // actually set the value
    //        board[iCoord][jCoord].setCurrentValue(value);
    //        disallowPossibilities(value, iCoord, jCoord);
    //    }
    //
    //    /**
    //     * Removes the current value on the inserted coordinate (sets it to 0) and updates the possibilities on all affected
    //     * fields. This method maintains possibility integrity.
    //     *
    //     * @param iCoord The i coordinate of the value to be removed
    //     * @param jCoord The j coordinate of the value to be removed
    //     */
    //    public void removeCurrentValue(int iCoord, int jCoord) throws IllegalArgumentException {
    //        if (iCoord < 0 || length <= iCoord) {
    //            throw new IllegalArgumentException("Inserted i coordinate out of bounds: " + iCoord);
    //        }
    //        if (jCoord < 0 || length <= jCoord) {
    //            throw new IllegalArgumentException("Inserted j coordinate out of bounds: " + jCoord);
    //        }
    //
    //        // case of the value already being 0
    //        if (board[iCoord][jCoord].getCurrentValue() == 0) {
    //            return;
    //        }
    //
    //        int oldValue = board[iCoord][jCoord].getCurrentValue();
    //
    //        // remove the old value
    //        board[iCoord][jCoord].setCurrentValue(0);
    //        reallowPossibilities(oldValue, iCoord, jCoord);
    //    }
    //
    //    /**
    //     * Replaces the current value on the field, whose coordinates are inserted to the new value, which is inserted while
    //     * maintaining possibility integrity. This method does not have greatest performance, so consider using -
    //     * removeCurrentValue() if you only need to remove (set to 0) a current value - insertCurrentValue() if you know the
    //     * field is empty (current value == 0) and want to set a new current value - setCurrentValue() if you want to set a
    //     * new current value very fast without maintaining possibility integrity This method maintains possibility
    //     * integrity.
    //     *
    //     * @param newValue The new value to replace its predecessor
    //     * @param iCoord   The i coordinate at which to replace the current value
    //     * @param jCoord   The j coordinate at which to replace the current value
    //     */
    //    void replaceCurrentValue(int newValue, int iCoord, int jCoord) {
    //        if (newValue < 0 || length < newValue) {
    //            throw new IllegalArgumentException("Inserted start value out of bounds: " + newValue);
    //        }
    //        if (iCoord < 0 || length <= iCoord) {
    //            throw new IllegalArgumentException("Inserted i coordinate out of bounds: " + iCoord);
    //        }
    //        if (jCoord < 0 || length <= jCoord) {
    //            throw new IllegalArgumentException("Inserted j coordinate out of bounds: " + jCoord);
    //        }
    //
    //        reallowPossibilities(board[iCoord][jCoord].getCurrentValue(), iCoord, jCoord);
    //        board[iCoord][jCoord].setCurrentValue(newValue);
    //        disallowPossibilities(newValue, iCoord, jCoord);
    //    }

    /**
     * Allows all possibilities, which are no longer prohibited by the specified value at the specified coordinates. The
     * own possibilities become completely recalculated, while all fields in the same row, column and block check if the
     * deleted value is a legit possibility for themselves. This method regains possibility integrity after deleting
     * (setting to 0) the specified value at the specified coordinates.
     *
     * @param deletedValue The deleted value
     * @param iCoord       The i coordinate of the deleted value
     * @param jCoord       The j coordinate of the deleted value
     */
    private void reallowPossibilities(int deletedValue, int iCoord, int jCoord) {

        // recalculate the own possibilities
        for (int k = 1; k <= length; k++) {
            if (isAllowed(k, iCoord, jCoord)) {
                board[iCoord][jCoord].getPossibilities().add(k);
            }
        }

        // expand the possibilities of the row
        for (int j = 0; j < length; j++) {
            if (isAllowed(deletedValue, iCoord, j)) {
                board[iCoord][j].getPossibilities().add(deletedValue);
            }
        }

        // expand the possibilities of the column
        for (int i = 0; i < length; i++) {
            if (isAllowed(deletedValue, i, jCoord)) {
                board[i][jCoord].getPossibilities().add(deletedValue);
            }
        }

        // expand the possibilities of the block
        int iStartValue = blockLength * (iCoord / blockLength);
        int jStartValue = blockLength * (jCoord / blockLength);
        for (int i = iStartValue; i < iStartValue + blockLength; i++) {
            for (int j = jStartValue; j < jStartValue + blockLength; j++) {
                if (isAllowed(deletedValue, i, j)) {
                    board[i][j].getPossibilities().add(deletedValue);
                }
            }
        }

    }

    /**
     * Disallow all possibilities, which are prohibited by inserting the specified value into the field at the specified
     * coordinates (setting its current value). Affected Possibilities are the ones of this field (they become cleared),
     * the ones of the same row, the same column and the same block (These loose the possibility of the inserted value,
     * if they had it).
     *
     * @param value  The inserted value
     * @param iCoord The i coordinate where the value got placed
     * @param jCoord The j coordinate where the value got placed
     */
    private void disallowPossibilities(int value, int iCoord, int jCoord) {

        // remove all possibilities from this field
        board[iCoord][jCoord].getPossibilities().clear();

        // remove the possibilities from the row
        for (int j = 0; j < length; j++) {
            board[iCoord][j].getPossibilities().remove(value);
        }

        // remove the possibilities from the column
        for (int i = 0; i < length; i++) {
            board[i][jCoord].getPossibilities().remove(value);
        }

        // remove the possibilities from the block
        int iStartValue = blockLength * (iCoord / blockLength);
        int jStartValue = blockLength * (jCoord / blockLength);
        for (int i = iStartValue; i < iStartValue + blockLength; i++) {
            for (int j = jStartValue; j < jStartValue + blockLength; j++) {
                board[i][j].getPossibilities().remove(value);
            }
        }
    }

    /**
     * Sets the start value at the inserted 1D coordinate to the inserted value. The 2D coordinates are computed as
     * follows by doing so: i = coord / length j = coord % length
     *
     * @param value The new start value
     * @param coord The coordinate at which to set the new start value
     */
    public void setStartValue(int value, int coord) throws IllegalArgumentException {
        if (value < 0 || length < value) {
            throw new IllegalArgumentException("Inserted start value out of bounds: " + value);
        }
        if (coord < 0 || length * length <= coord) {
            throw new IllegalArgumentException("Inserted coordinate out of bounds: " + coord);
        }

        board[coord / length][coord % length].setStartValue(value);
    }

    /**
     * Sets a new start value at the specified coordinates of the Sudoku board
     *
     * @param value  The value to be set
     * @param iCoord The i coordinate at which to insert
     * @param jCoord The j coordinate at which to insert
     */
    void setStartValue(int value, int iCoord, int jCoord) throws IllegalArgumentException {
        if (value < 0 || length < value) {
            throw new IllegalArgumentException("Inserted start value out of bounds: " + value);
        }
        if (iCoord < 0 || length <= iCoord) {
            throw new IllegalArgumentException("Inserted i coordinate out of bounds: " + iCoord);
        }
        if (jCoord < 0 || length <= jCoord) {
            throw new IllegalArgumentException("Inserted j coordinate out of bounds: " + jCoord);
        }

        board[iCoord][jCoord].setStartValue(value);
    }

    /**
     * Sets the solution value at the inserted 1D coordinate to the inserted value. The 2D coordinates are computed as
     * follows by doing so: i = coord / length j = coord % length
     *
     * @param value The new solution value
     * @param coord The coordinate at which to set the new solution value
     */
    public void setSolutionValue(int value, int coord) throws IllegalArgumentException {
        if (value < 1 || length < value) {
            throw new IllegalArgumentException("Inserted start value out of bounds: " + value);
        }
        if (coord < 0 || length * length <= coord) {
            throw new IllegalArgumentException("Inserted coordinate out of bounds: " + coord);
        }

        board[coord / length][coord % length].setSolutionValue(value);
    }

    /**
     * Sets a new solution value at the specified coordinates of the Sudoku board
     *
     * @param value  The value to be set
     * @param iCoord The i coordinate at which to insert
     * @param jCoord The j coordinate at which to insert
     */
    void setSolutionValue(int value, int iCoord, int jCoord) throws IllegalArgumentException {
        if (value < 1 || length < value) {
            throw new IllegalArgumentException("Inserted start value out of bounds: " + value);
        }
        if (iCoord < 0 || length <= iCoord) {
            throw new IllegalArgumentException("Inserted i coordinate out of bounds: " + iCoord);
        }
        if (jCoord < 0 || length <= jCoord) {
            throw new IllegalArgumentException("Inserted j coordinate out of bounds: " + jCoord);
        }

        board[iCoord][jCoord].setSolutionValue(value);
    }

    //------------------------------------------------------------------------------------------------------------------
    // Getter-Methods:

    /**
     * Returns the current value of the specified field of this Sudoku.
     *
     * @param iCoord The i coordinate of the field whose current value is demanded
     * @param jCoord The j coordinate of the field whose current value is demanded
     * @return The demanded current value
     */
    public int getCurrentValue(int iCoord, int jCoord) throws IllegalArgumentException {
        if (iCoord < 0 || length <= iCoord) {
            throw new IllegalArgumentException("Inserted i coordinate out of bounds: " + iCoord);
        }
        if (jCoord < 0 || length <= jCoord) {
            throw new IllegalArgumentException("Inserted j coordinate out of bounds: " + jCoord);
        }

        return this.board[iCoord][jCoord].getCurrentValue();
    }

    /**
     * Returns the value of the specified field of the Sudoku.
     *
     * @param iCoord The i Coordinate of the demanded field
     * @param jCoord The j Coordinate of the demanded field
     * @return The value of the demanded field
     */
    public int getStartValue(int iCoord, int jCoord) throws IllegalArgumentException {
        if (iCoord < 0 || length <= iCoord) {
            throw new IllegalArgumentException("Inserted i coordinate out of bounds: " + iCoord);
        }
        if (jCoord < 0 || length <= jCoord) {
            throw new IllegalArgumentException("Inserted j coordinate out of bounds: " + jCoord);
        }

        return this.board[iCoord][jCoord].getStartValue();
    }

    /**
     * Returns the start value of the specified field of this Sudoku.
     *
     * @param coord The coordinate of the field whose start value is demanded
     * @return The demanded start value
     */
    public int getStartValue(int coord) throws IllegalArgumentException {
        if (coord < 0 || length * length <= coord) {
            throw new IllegalArgumentException("Inserted coordinate out of bounds: " + coord);
        }

        return this.board[coord / length][coord % length].getStartValue();
    }

    /**
     * Returns the solution value of the specified field of this Sudoku.
     *
     * @param coord The coordinate of the field whose solution value is demanded
     * @return The demanded solution value
     */
    public int getSolutionValue(int coord) throws IllegalArgumentException {
        if (coord < 0 || length * length <= coord) {
            throw new IllegalArgumentException("Inserted coordinate out of bounds: " + coord);
        }

        return board[coord / length][coord % length].getSolutionValue();
    }

    /**
     * Returns the solution value of the specified field of this Sudoku.
     *
     * @param iCoord The i coordinate of the field whose solution value is demanded
     * @param jCoord The j coordinate of the field whose solution value is demanded
     * @return The demanded solution value
     */
    public int getSolutionValue(int iCoord, int jCoord) throws IllegalArgumentException {
        if (iCoord < 0 || length <= iCoord) {
            throw new IllegalArgumentException("Inserted i coordinate out of bounds: " + iCoord);
        }
        if (jCoord < 0 || length <= jCoord) {
            throw new IllegalArgumentException("Inserted j coordinate out of bounds: " + jCoord);
        }

        return board[iCoord][jCoord].getSolutionValue();
    }

    /**
     * Returns the possibilities of the sudoku field at the chosen coordinates. These are the numbers which are not yet
     * inserted in the same row, column or block like the chosen coordinate.
     *
     * @param iCoord The i coordinate of the chosen field
     * @param jCoord The j coordinate of the chosen field
     * @return The possibilities remaining on this field
     */
    public HashSet<Integer> getPossibilities(int iCoord, int jCoord) {
        if (iCoord < 0 || length <= iCoord) {
            throw new IllegalArgumentException("Inserted i coordinate out of bounds: " + iCoord);
        }
        if (jCoord < 0 || length <= jCoord) {
            throw new IllegalArgumentException("Inserted j coordinate out of bounds: " + jCoord);
        }

        return board[iCoord][jCoord].getPossibilities();
    }

    /**
     * Returns the length of this sudoku (standard 9).
     *
     * @return The length of this sudoku
     */
    public int getLength() {
        return length;
    }

    /**
     * Returns the block length of this sudoku, which is the square root if its length (standard 3).
     *
     * @return The block length of this sudoku
     */
    public int getBlockLength() {
        return blockLength;
    }

    /**
     * Returns the difficulty of this sudoku.
     *
     * @return The difficulty of this sudoku
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Returns the board containing the sudoku fields of this sudoku.
     *
     * @return The board of this sudoku
     */
    public SudokuField[][] getBoard() {
        return this.board;
    }

    /**
     * Returns if the possibilities saved in this board are assured to be correct.
     *
     * @return If the possibilities saved in this sudoku are guaranteed to be correct
     */
    public boolean isPossibilityInteger() {
        return possibilityIntegrity;
    }

    boolean isFilled() {
        return filled;
    }

}
