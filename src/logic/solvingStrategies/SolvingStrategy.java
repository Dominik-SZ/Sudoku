package logic.solvingStrategies;

import logic.exceptions.PIVException;

public interface SolvingStrategy {

	boolean apply() throws PIVException;
	int getDifficulty();

}
