package logic;


public class SudokuField extends SolverField {
    private int startValue;
    private int solutionValue;

    SudokuField() {
        this.startValue = 0;
        this.solutionValue = 0;
    }

    /**
     * Creates a new SudokuField with the specified length attribute in its
     * parent class as well as the startValue 0 and the solutionValue 0
     *
     * @param length    The length of the based Sudoku
     */
    SudokuField(int length) {
        super(length);
        this.startValue = 0;
        this.solutionValue = 0;
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
