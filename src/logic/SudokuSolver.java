package logic;

import java.util.*;

import logic.solvingStrategies.*;
import utilities.MathUtilities;


public class SudokuSolver {
    /**
     * the Sudoku which should be solved
     */
    private Sudoku sudoku;
    /**
     * the length additionally saved to save access
     */
    private int length;

    /**
     * the backups to stepBack() to, if an assumption proofs to be wrong
     */
    private Stack<BackupPoint> backups = new Stack<>();
    /**
     * the distance in fields between two assumptions
     */
    private int stepWidth;

    // ------------------------------------------------------------------------

    SudokuSolver(Sudoku sudoku) {
        this.sudoku = sudoku;
        this.length = sudoku.getLength();
        // generate a stepWidth, which has no common divisor with the Sudoku
        // length greater than 1
        stepWidth = (int) (length * 1.3);
        while (MathUtilities.greatestCommonDivisor(stepWidth, length) != 1) {
            stepWidth++;
        }

        backups = new Stack<>();
        backups.push(new BackupPoint(-1, new LinkedList<>()));
    }

    // ------------------------------------------------------------------------
    // Main methods used to fill a Sudoku

    /**
     * Main generating method to fill an empty sudoku, making it definitely
     * solvable. It also prints the solution and the Sudoku to the system.out,
     * as well as some additional info.
     */
    void fill() {
        System.out.println("start filling");
        int fillTrials = 1;
        // try to fill the Sudoku completely until it succeeds
        while (!fillTrial()) {
            fillTrials++; // count how many trials were necessary
        }

        // saving the Solution array before erasing
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                int currentValue = sudoku.getCurrentValue(i, j);
                sudoku.setSolutionValue(currentValue, i, j);
            }
        }
        System.out.println("Solution: ");
        System.out.println(sudoku.toString(true, true));

        // erase the permitted fields considering the difficulty
        erase();
        System.out.println("successfully erased");

        // saving the newly generated state as startBoard in the Sudoku
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                sudoku.setStartValue(sudoku.getCurrentValue(i, j), i, j);
            }
        }

        if (fillTrials == 1) {
            System.out.println("needed " + fillTrials + " fill trial to succeed");
        } else {
            System.out.println("needed " + fillTrials + " fill trials to succeed");
        }
        System.out.println(sudoku.count() + " of " + length * length + " fields are filled.");
        System.out.println(sudoku.toString(true, false));
    }

    /**
     * Tries to solve the Sudoku like a player and returns if it was successful doing so
     *
     * @return  If solving the Sudoku succeeded
     */
    boolean solve() {
        boolean successful = true;
        while(successful && !isFinished()) {
            successful = trySolving();
        }

        if(successful) {
            // saving the Solution array before erasing
            for (int i = 0; i < length; i++) {
                for (int j = 0; j < length; j++) {
                    int currentValue = sudoku.getCurrentValue(i, j);
                    sudoku.setSolutionValue(currentValue, i, j);
                }
            }
            System.out.println("Solution: ");
            System.out.println(sudoku.toString(true, true));
        }
        sudoku.calculatePossibilities();

        return successful;
    }

    //------------------------------------------------------------------------------------------------------------------

    /**
     * Helping method which tries to fill the Sudoku completely, but gives up if
     * it exceeds the allowed amount of trials. It returns whether it was
     * successful.
     *
     * @return If the the method was successful in filling the sudoku (startValues and solutionValues and currentValues)
     */
    private boolean fillTrial() {

        System.out.println("start fill trial");
        do {
            clear();
            System.out.println("start random fill");
            // fill about a quarter of the fields without direct conflicts
            randomFill(length * length / 4);
        } while (isLocked());
        System.out.println("randomFill successfully performed");
        System.out.println(sudoku.toString());

        while (trySolving()) {
            System.out.println("trySolving iteration");
        }

        int trySolvingAssumeCycles = 0;
        int cap = length * length / 3;
        while (!isFinished()) {

            // restart the fill method if it seems like there is no solution to find
            if (trySolvingAssumeCycles > cap) {
                System.out.println("NEW TRY");
                backups.clear(); // clear the backups for the next trial
                backups.push(new BackupPoint(-1, new LinkedList<>()));
                sudoku.resetAllPossibilities();
                return false;
            }

            // make an assumption
            try {
                System.out.println("assume");
                assume(backups.peek().getChangedCoord());
            } catch (NoBackupsException ex) {
                System.out.println("no backups exception");
                return false;
            }

            // if (isLocked()) {
            // System.out.println("DEBUG: is locked!");
            // stepBack();
            // }

            // trySolving loop
            // use the trySolving method as long as it succeeds
            boolean a = trySolving();
            System.out.println("first TrySolving: " + a);
            while (a) {
                System.out.println("trySolving: " + a);
                a = trySolving();
            }

            trySolvingAssumeCycles++;
            System.out.println("amount of trySolving, assume cycles: " + trySolvingAssumeCycles);
        }
        return true; // Sudoku is finished
    }

    /**
     * Inserts "amount" values randomly on fields of the solverBoard filled with 0
     * considering the gamerules (making sure that every number appears at most
     * once in every row, column and block).
     */
    private void randomFill(int amount) {
        for (int k = 0; k < amount; k++) {
            int iCoord = MathUtilities.randomNumber(length) - 1;
            int jCoord = MathUtilities.randomNumber(length) - 1;
            int value = MathUtilities.randomNumber(length);

            if (sudoku.getCurrentValue(iCoord, jCoord) == 0 && sudoku.isAllowed(value, iCoord, jCoord)) {
                sudoku.insertCurrentValue(value, iCoord, jCoord);
            } else {
                k--;
            }
        }
    }

    /**
     * Tries to solve the given Sudoku like a player and returns true if at
     * least one value got changed (indicating that it should be continued
     * trying to solve the Sudoku). The fields filled by this method may be
     * deleted afterwards.
     */
    private boolean trySolving() {
        Collection<SolvingStrategy> strategies = Arrays.asList(
                new RowIntersection(sudoku),
                new ColumnIntersection(sudoku),
                new BlockIntersection(sudoku),
                new HiddenSingleRow(sudoku, this),
                new HiddenSingleColumn(sudoku, this),
                new HiddenSingleBlock(sudoku, this)
        );

        return strategies.stream().filter(strategy -> strategy.getDifficulty() <= sudoku.getDifficulty())
                                            .map(SolvingStrategy::apply)
                                            .reduce(Boolean::logicalOr).orElse(false);
    }

    /**
     * If there is no value to be found by trying to solve the Sudoku like a
     * player, there is an assumption to be made. It is very well possible that
     * this assumption is wrong, which is why a backup is made to be returned
     * to, if that is the case. This method attempts an assumption after the
     * last one by iterating systematically in steps of the size stepWidth
     * through the Sudoku. If the Sudoku is found to be locked, the stepBack
     * method is called.
     *
     * @param coord The 1D coordinate of the last assumption
     */
    private void assume(int coord) throws NoBackupsException {
        coord = coord + stepWidth; // iterate to the next field
        for (int k = 0; k < length * length; coord = coord + stepWidth, k++) {
            // when overflowing return to the beginning of the solverBoard
            coord = coord % (length * length);
            int i = coord / length;
            int j = coord % length;

            // testing if the field is not filled yet
            if (sudoku.getCurrentValue(i, j) == 0) {

                LinkedList<Integer> possi = new LinkedList<>();

                // testing every possible number
                for (int l = 1; l <= length; l++) {
                    if (sudoku.isAllowed(l, i, j)) {
                        // saving the allowed ones in a stack
                        possi.add(l);
                    }
                }
                if (possi.isEmpty()) { // Sudoku is locked
                    // System.out.println("DEBUG: assume: step back");
                    stepBack();
                    return;
                } else {
                    // choose a random possible assumption on this field
                    int index = (int) (Math.random() * possi.size());
                    sudoku.insertCurrentValue(possi.get(index), i, j);
                    possi.remove(index);
                    // System.out.println(" DEBUG: new assumption at:
                    // i-Coord: "+i +
                    // " j-Coord: "+j + " with value: "+values[i][j]);
                    backups.push(new BackupPoint(coord, possi));
                    return; // assumption successfully made
                }
            }
        }
    }

    /**
     * Undoes the last assumption and chooses the next one. If there are still
     * possibilities on this field remaining, one of those is chosen, while if
     * there are none, the assume method is called at this point.
     */
    private void stepBack() throws NoBackupsException {
        if (backups.size() == 1) {
            throw new NoBackupsException();
        }
        BackupPoint latestBackup = backups.pop(); // undo the latest backup
        Stack<Integer> tSFills = latestBackup.getTSFills();

        // Remove all values which have been made based on the assumption
        while (!tSFills.isEmpty()) {
            int coord = tSFills.pop();
            sudoku.removeCurrentValue(coord / length, coord % length);
        }

        int coord = latestBackup.getChangedCoord();
        int i = coord / length;
        int j = coord % length;
        // no possible assumptions for this field remaining => change field
        if (latestBackup.getPossibilities().isEmpty()) {
            //            System.out.println("DEBUG: stepBack(): change field from i = " + i + " j = " + j);
            sudoku.removeCurrentValue(i, j);
            assume(coord);
        }

        // test another possible assumption on this field
        else {
            sudoku.insertCurrentValue(latestBackup.popRandomPossibility(), i, j);
            // System.out.println("DEBUG: stepBack(): keep node Coord:
            // "+latestBackup.getChangedCoord() + " now with value:
            // "+getCurrentValue(latestBackup.getChangedCoord()));
            backups.push(latestBackup);
        }
    }

    /**
     * Erases the permitted fields of the Sudoku solverBoard, which are those, filled
     * in by the trySolving method. Not permitted are the fields of the
     * randomFill method and the assumed ones. The higher the difficulty, the
     * more fields are erased.
     * Afterwards the possibilities are updated.
     */
    private void erase() {
        // delete all trySolving entries of the backups and push them into one
        // stack
        Stack<Integer> totalTSFills = new Stack<>();
        while (!backups.isEmpty()) {
            Stack<Integer> currentTSFills = backups.pop().getTSFills();
            while (!currentTSFills.isEmpty()) {
                totalTSFills.push(currentTSFills.pop());
            }
        }

        int deletable = totalTSFills.size();
        int entriesDeleted = 0; // difficulty 1 -> delete 50% of the entries;
        // difficulty 10 -> delete 100 % of the entries
        while (totalTSFills.size() > (int) ((0.5 - 0.0555555555555555 * (sudoku.getDifficulty() - 1)) * deletable)) {
            // get a random value of the stack
            int actualCoord = totalTSFills.remove((int) (Math.random() * totalTSFills.size()));
            sudoku.setCurrentValue(0, actualCoord / length, actualCoord % length);
            entriesDeleted++;
        }
        sudoku.calculatePossibilities();
        System.out.println(entriesDeleted + " out of " + deletable + " deletable entries deleted");
        System.out.println("with difficulty " + sudoku.getDifficulty());
    }

    // -------------------------------------------------------------------------
    // Secondary Methods to fill the Sudoku

    /**
     * checks if there is no allowed possibility remaining to fill one of the
     * empty fields.
     */
    private boolean isLocked() {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (sudoku.getPossibilities(i, j).size() == 0) {
                    System.out.println("isLocked: no remaining possibilities found at i=" + i + " and j=" + j);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if the entries of the Sudoku solverBoard contain no "0". If it
     * does so, it is considered as "finished".
     */
    private boolean isFinished() {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (sudoku.getCurrentValue(i, j) == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * sets all fields to 0.
     */
    private void clear() {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                sudoku.removeCurrentValue(i, j);
            }
        }
    }

    //    /**
    //     * Inserts the inserted value in the startBoard at the specified iCoord and
    //     * jCoord. Also all possibilities, which are directly blocked as a result of
    //     * this new entry, are removed and the new entry is added to the trySolving
    //     * stack of the current backup point if it is deletable.
    //     *
    //     * @param value  The value, which should be inserted
    //     * @param iCoord The i coordinate at which to insert the value
    //     * @param jCoord The j coordinate at which to insert the value
    //     * @param deletable If the inserted value is generated by the trySolving
    //     *                  method and thus may be erased later on
    //     */
    //    public void insertValue(int value, int iCoord, int jCoord, boolean deletable) {
    //
    //        sudoku.insertCurrentValue(value, iCoord, jCoord);
    //
    //        if (deletable) {
    //            backups.peek().pushTSFills(iCoord * length + jCoord);
    //        }
    //
    //    }
    //
    //    /**
    //     * Removes the value at the specified coordinate of the solverBoard
    //     * (setting it to 0) and updates the possibilities of all affected
    //     * coordinates
    //     *
    //     * @param iCoord    The i coordinate at which to remove
    //     * @param jCoord    The j coordinate at which to remove
    //     */
    //    private void removeValue(int iCoord, int jCoord) {
    //
    //        sudoku.removeCurrentValue(iCoord, jCoord);
    //    }
    //-----------------------------------------------------------------------------
    // Getter and Setter

    /**
     * Saves a trySolving entry in the current backup of this solver.
     *
     * @param iCoord The i coordinate of the inserted value
     * @param jCoord The j coordinate of the inserted value
     */
    public void pushTrySolvingBackup(int iCoord, int jCoord) {
        backups.peek().pushTSFills(iCoord * length + jCoord);
    }
}
