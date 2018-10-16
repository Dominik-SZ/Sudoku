package model_new.solving.strategies;

import model_new.solving.Solveable;

/**
 * Interface to define solving model.solving.strategies used to try to solve a sudoku.
 */
public interface SolvingStrategy {

	boolean apply(Solveable board);
	int getDifficulty();
	boolean isRestrictive();
}
