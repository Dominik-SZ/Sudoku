package model_new.solving.strategies;

import model_new.solving.Solveable;

abstract class FillStrategy {

    void applyRuleConstraints(Solveable board, int value, int iCoord, int jCoord) {
        int length = board.getLength();
        int blockLength = board.getBlockLength();

        if (value < 0 || length < value) {
            throw new IllegalArgumentException("Inserted current value out of bounds: " + value);
        }
        if (iCoord < 0 || iCoord >= length) {
            throw new IllegalArgumentException("Inserted i coordinate out of bounds: " + iCoord);
        }
        if (jCoord < 0 || jCoord >= length) {
            throw new IllegalArgumentException("Inserted j coordinate out of bounds: " + jCoord);
        }


        // remove all possibilities from this field
        board.clearPossibilities(iCoord, jCoord);

        // remove the possibilities from the row
        for (int j = 0; j < length; j++) {
            board.removePossibility(value, iCoord, j);
        }

        // remove the possibilities from the column
        for (int i = 0; i < length; i++) {
            board.removePossibility(value, i, jCoord);
        }

        // remove the possibilities from the block
        int iStartValue = blockLength * (iCoord / blockLength);
        int jStartValue = blockLength * (jCoord / blockLength);
        for (int i = iStartValue; i < iStartValue + blockLength; i++) {
            for (int j = jStartValue; j < jStartValue + blockLength; j++) {
                board.removePossibility(value, i, j);
            }
        }
    }
}
