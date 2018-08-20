package model2.strategies;

import model2.Solveable;

import java.util.List;

/**
 * Iterates all fields and their possibilities searches for fields with only one possibility remaining. If such a
 * field is found, the only remaining possibility is inserted as current value.<br>
 * This is an interpreting method meaning that it actually looks for new values to insert and does so.
 */
public class OnlyOnePossibility extends FillStrategy implements SolvingStrategy {

    public OnlyOnePossibility() {
    }

    @Override
    public boolean apply(Solveable board) {
        int length = board.getLength();
        boolean didSomething = false;

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                List<Integer> possibilities = board.getPossibilities(i, j);
                if (board.getValue(i, j) == 0 && possibilities.size() == 1) {
                    int onlyPossibility = possibilities.get(0);

                    board.setValue(onlyPossibility, i, j);
                    applyRuleConstraints(board, onlyPossibility, i, j);
                    didSomething = true;
                }
            }
        }
        return didSomething;
    }

    @Override
    public int getDifficulty() {
        return (1);
    }

    @Override
    public boolean isRestrictive() {
        return false;
    }

}
