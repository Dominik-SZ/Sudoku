package model.solving.strategies;

import model.exceptions.PIVException;
import model.solving.solver_util.PerformedOperation;

import java.util.LinkedList;

/**
 * Interface to define solving model.solving.strategies used to try to solve a sudoku.
 */
public interface SolvingStrategy {

	boolean apply() throws PIVException;
	int getDifficulty();
	boolean isRestrictive();
	LinkedList<PerformedOperation> getPerformedOperations();
}
