package model2.solving.strategies;

import model2.solving.Solveable;

/**
 * Interface to define solving strategies used to try to solve a sudoku.
 */
public interface SolvingStrategy {

	boolean apply(Solveable board);
	int getDifficulty();
	boolean isRestrictive();
}
