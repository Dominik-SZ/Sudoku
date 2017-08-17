package logic.solvingStrategies;

import logic.exceptions.PIVException;

/**
 * Interface to define solving strategies used to try to solve a sudoku.
 */
public interface SolvingStrategy {

	boolean apply() throws PIVException;
	int getDifficulty();
	boolean isRestrictive();

}
