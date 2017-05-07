package logic.solvingStrategies;

import logic.exceptions.PossibilityIntegrityViolatedException;

public interface SolvingStrategy {

	boolean apply() throws PossibilityIntegrityViolatedException;
	int getDifficulty();

}
