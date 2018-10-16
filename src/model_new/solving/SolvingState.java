package model_new.solving;

import java.util.List;

public class SolvingState implements Solveable {

    @Override
    public int getLength() {
        return 0;
    }

    @Override
    public int getBlockLength() {
        return 0;
    }

    @Override
    public int getDifficulty() {
        return 0;
    }

    @Override
    public void setValue(int v, int i, int j) {

    }

    @Override
    public int getValue(int i, int j) {
        return 0;
    }

    @Override
    public List<Integer> getPossibilities(int i, int j) {
        return null;
    }

    @Override
    public boolean isPossible(int p, int i, int j) {
        return false;
    }

    @Override
    public boolean removePossibility(int poss, int i, int j) {
        return false;
    }

    @Override
    public void clearPossibilities(int i, int j) {

    }

    @Override
    public Solveable copy() {
        return null;
    }
}
