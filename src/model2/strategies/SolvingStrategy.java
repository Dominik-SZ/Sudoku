package model2.strategies;

import model2.Solveable;

/**
 * Interface to define solving strategies used to try to solve a sudoku.
 */
public interface SolvingStrategy {

	boolean apply(Solveable board);
	int getDifficulty();
	boolean isRestrictive();
}
