package logic;


public class SudokuField extends SolverField{
    private int startValue;
    private int solutionValue;

    SudokuField () {
        this.startValue = 0;
        this.solutionValue = 0;
    }

    int getStartValue() {
        return startValue;
    }

    void setStartValue(int value) {
        this.startValue = value;
    }

    int getSolutionValue() {
        return solutionValue;
    }

    void setSolutionValue(int value) {
        this.solutionValue = value;
    }
}
