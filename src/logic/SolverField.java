package logic;

import java.util.HashSet;

/**
 * Created by Dominik on 23.04.2017.
 */
class SolverField {
    private HashSet<Integer> possibilities;
    private int value;
    private int i;
    private int j;

    SolverField (int value, int i, int j){
        this.value = value;
        this.i = i;
        this.j = j;
    }
}
