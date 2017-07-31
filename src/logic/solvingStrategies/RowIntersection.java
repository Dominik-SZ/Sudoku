package logic.solvingStrategies;


import logic.Sudoku;
import logic.exceptions.PossibilityIntegrityViolatedException;
import util.Coordinate;

import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Checks if all possibilities of a row for a specific number are in the
 * same block. If they are, this number is removed from the possibilities
 * of the rest of the fields in this block.
 * This is a restrictive method.
 */
public class RowIntersection implements SolvingStrategy{
	private Sudoku sudoku;
	private int length;
	private int blockLength;

	public RowIntersection(Sudoku sudoku) {
		this.sudoku = sudoku;
		this.length = sudoku.getLength();
		this.blockLength = sudoku.getBlockLength();
	}


	@Override
	public boolean apply() throws PossibilityIntegrityViolatedException {

		if(!sudoku.isPossibilityInteger()) {
            throw new PossibilityIntegrityViolatedException();
		}

		boolean changed = false;
		LinkedList<Coordinate> occurrences = new LinkedList<>();

		// iterate the rows
		for(int iRow = 0; iRow < length; iRow++) {

			// iterate all possible numbers
			for(int k = 1; k <= length; k++) {

				// save all occurrences of this number in this row
				occurrences.clear();
				for(int j = 0; j < length; j++) {
					if(sudoku.getCurrentValue(iRow, j) == 0 && sudoku.getPossibilities(iRow, j).contains(k)){
						occurrences.add(new Coordinate(iRow, j));
					}
				}

				try {
					// check if all occurrences are in the same block
					int iOccurrence = occurrences.getFirst().i;
					int iBlockNumber = occurrences.getFirst().i / blockLength;
					int jBlockNumber = occurrences.getFirst().j / blockLength;
					boolean sameBlock = true;

					for(Coordinate coord: occurrences) {
						if(coord.i / blockLength != iBlockNumber || coord.j / blockLength != jBlockNumber) {
							sameBlock = false;
							break;
						}
					}

					if(sameBlock) {
						int iStart = iBlockNumber + blockLength;
						int jStart  =jBlockNumber * blockLength;

						// remove the possibilities in the rows on top of the occurred row in the same block
						for(int i = iStart; i < iOccurrence; i++) {
							for(int j = jStart; j < jStart + blockLength; j++) {
								if(sudoku.getPossibilities(i, j).contains(k)){
									changed = true;
									sudoku.getPossibilities(i, j).remove(k);
								}
							}
						}
						// remove the possibilities in the rows below the occurred row in the same block
						for(int i = iOccurrence + 1; i < iStart + blockLength; i++) {
							for(int j = jStart; j < jStart + blockLength; j++) {
								if(sudoku.getPossibilities(i, j).contains(k)){
									changed = true;
									sudoku.getPossibilities(i, j).remove(k);
								}
							}
						}

					}

				} catch (NoSuchElementException ex) {
					// do nothing. Skip this number
				}

			}
		}
		return changed;
	}

	@Override
	public int getDifficulty() {
		return 6;
	}
}
