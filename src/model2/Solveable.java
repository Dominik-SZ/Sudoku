package model2;

import java.util.List;

public interface Solveable {

    int getLength();
    int getBlockLength();
    void setValue(int v, int i, int j);
    int getValue(int i, int j);
    List<Integer> getPossibilities(int i, int j);
    boolean isPossible(int p, int i, int j);
    boolean removePossibility(int poss, int i, int j);
    void clearPossibilities(int i, int j);
}
