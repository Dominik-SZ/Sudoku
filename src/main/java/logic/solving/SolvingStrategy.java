package logic.solving;

import logic.exceptions.PIVException;

import java.util.LinkedList;

/**
 * Interface to define solving strategies used to try to solve a sudoku.
 */
public interface SolvingStrategy {

	boolean apply() throws PIVException;
	int getDifficulty();
	boolean isRestrictive();
	LinkedList<PerformedOperation> getPerformedOperations();
}
