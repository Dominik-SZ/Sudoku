package model2;

import util.InputChecker;

public class SudokuValidator {

    public boolean isValid(int[][] board) {
        int length = board.length;
        int blockLength = (int) Math.sqrt(length);
        InputChecker.isSquareNumber(length);

        // check rows
        for (int i = 0; i < length; i++) {
            boolean[] occurred = new boolean[length];
            for (int j = 0; j < length; j++) {
                occurred[board[i][j] - 1] = true;
            }
            for (boolean occ : occurred) {
                if (!occ) {
                    return false;
                }
            }
        }

        // check columns
        for (int j = 0; j < length; j++) {
            boolean[] occurred = new boolean[length];
            for (int i = 0; i < length; i++) {
                occurred[board[i][j] - 1] = true;
            }
            for (boolean occ : occurred) {
                if (!occ) {
                    return false;
                }
            }
        }

        // check blocks
        for (int iBlock = 0; iBlock < length; iBlock += blockLength) {
            for (int jBlock = 0; jBlock < length; jBlock += blockLength) {
                boolean[] occurred = new boolean[length];
                for (int i = iBlock; i < iBlock + blockLength; i++) {
                    for (int j = jBlock; j < jBlock + blockLength; j++) {
                        occurred[board[i][j] - 1] = true;
                    }
                }
                for (boolean occ : occurred) {
                    if (!occ) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
