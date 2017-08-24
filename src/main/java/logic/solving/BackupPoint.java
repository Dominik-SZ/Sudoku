package logic.solving;

import util.Coordinate;

import java.util.LinkedList;
import java.util.List;

class BackupPoint {
    /**
     * The changed coordinate of this backup. The i and j coordinate can be gained by i = changedCoord / length and j =
     * changedCoord % length .
     */
    private int changedCoord;
    /**
     * remaining possibilities which could be assumed on this field
     */
    private LinkedList<Integer> possibilities;
    /**
     * trySolving fill coordinates which have been made based on this assumption
     */
    private List<Coordinate> tSFills;

    /**
     * Creates a new BackupPoint at the coordinate changedCoord and remaining possibilities to try on this field.
     *
     * @param changedCoord  The 1D coordinate on which a new assumption is been made
     * @param possibilities The list of remaining possibilities on this field.
     */
    BackupPoint(int changedCoord, LinkedList<Integer> possibilities) {
        this.changedCoord = changedCoord;
        this.possibilities = possibilities;
        this.tSFills = new LinkedList<>();
    }

    int getChangedCoord() {
        return changedCoord;
    }

    LinkedList<Integer> getPossibilities() {
        return possibilities;
    }

    List<Coordinate> getTSFills() {
        return tSFills;
    }

    //------------------------------------------------------------------------------------------------------------------
    // getter and setter

    /**
     * Adds the inserted coordinate to the tSFills list.
     *
     * @param coord the coordinate, which is supposed to be saved
     */
    void addTSFill(Coordinate coord) {
        this.tSFills.add(coord);
    }

    /**
     * Returns a random entry of the possibility list and removes it from the list
     */
    int getRandomPossibility() {
        return possibilities.get((int) (Math.random() * possibilities.size()));
    }
}
