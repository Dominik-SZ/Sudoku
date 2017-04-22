package logic;

import java.util.LinkedList;
import java.util.Stack;

public class BackupPoint {
	/**
	 * The changed coordinate of this backup. The i and j coordinate can be
	 * gained by i = changedCoord / length and j = changedCoord % length .
	 */
	private int changedCoord;
	/** remaining possibilities which could be assumed on this field */
	private LinkedList<Integer> possibilities;
	/**
	 * trySolving fill coordinates which have been made based on this assumption
	 */
	private Stack<Integer> tSFills;

	/**
	 * Creates a new BackupPoint at the coordinate changedCoord and remaining
	 * possibilities to try on this field.
	 * 
	 * @param changedCoord
	 *            The 1D coordinate on which a new assumption is been made
	 * @param possibilities
	 *            The list of remaining possibilities on this field.
	 */
	BackupPoint(int changedCoord, LinkedList<Integer> possibilities) {
		this.changedCoord = changedCoord;
		this.possibilities = possibilities;
		this.tSFills = new Stack<Integer>();
	}

	public int getChangedCoord() {
		return changedCoord;
	}

	public LinkedList<Integer> getPossibilities() {
		return possibilities;
	}

	public Stack<Integer> getTSFills() {
		return tSFills;
	}

	public void setChangedCoord(int changedCoord) {
		this.changedCoord = changedCoord;
	}

	public void setPossibilities(LinkedList<Integer> possibilities) {
		this.possibilities = possibilities;
	}

	public void setTSFills(Stack<Integer> tSFills) {
		this.tSFills = tSFills;
	}

	// additional useful "getter and setter methods"
	/**
	 * Pushes the inserted coordinate to the tSFills stack.
	 * 
	 * @param coord
	 *            the one dimensional coordinate, which is supposed to be saved
	 */
	public void pushTSFills(int coord) {
		this.tSFills.push(coord);
	}

	/**
	 * Returns a random entry of the possibility list and removes it from the
	 * list
	 */
	public int popRandomPossibility() {
		return possibilities.get((int) (Math.random() * possibilities.size()));
	}
}
