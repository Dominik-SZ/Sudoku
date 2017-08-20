package logic.solvingStrategies;

/**
 * This class can be used to save a performed action: Removing the value or possibility "value_possibility" from the
 * coordinates "i" and "j".
 */
public class PerformedOperation {

    private int value_possibility;
    private int i;
    private int j;

    PerformedOperation(int value_possibility, int i, int j) {
        this.value_possibility = value_possibility;
        this.i = i;
        this.j = j;
    }

    public int getValue_possibility() {
        return value_possibility;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }
}
