package model.solving.solver;

import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

import model.exceptions.NoBackupsException;
import model.exceptions.PIVException;
import model.Sudoku;
import model.solving.solver_util.BackupPoint;
import model.solving.strategies.SolvingStrategy;
import swingGUI.util.Coordinate;
import model.MathUtilities;


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
    private Deque<BackupPoint> backups;
    /**
     * the distance in fields between two assumptions
     */
    private int stepWidth;
    /**
     * the strategies used to solve this Sudoku
     */
    private List<SolvingStrategy> strategies;

    /**
     * The maximum amount of fill trials until the solver gives up the solving operation
     */
    private final int FILL_TRIAL_CAP = 50;

    //------------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new SudokuSolver for the inserted Sudoku. It can be used to fill() or solve() the Sudoku.
     *
     * @param sudoku    The Sudoku which shall be manipulated
     */
    public SudokuSolver(Sudoku sudoku) {
        this.sudoku = sudoku;
        this.length = sudoku.getLength();
        // generate a stepWidth, which has no common divisor with the Sudoku length greater than 1
        stepWidth = (int) (length * 1.3);
        while (MathUtilities.greatestCommonDivisor(stepWidth, length) != 1) {
            stepWidth++;
        }

        backups = new LinkedBlockingDeque<>();
        backups.push(new BackupPoint(-1, new LinkedList<>()));

        strategies = SolvingStrategyFactory.createSolvingStrategyList(sudoku, backups);
    }

    //------------------------------------------------------------------------------------------------------------------
    // Main methods used to fill a Sudoku

    /**
     * Main generating method to fill an empty sudoku, making it definitely solvable. It also prints the solution and
     * the Sudoku to the system.out, as well as some additional info.
     *
     * @return The list of solving strategies used to fill the sudoku or null if the allowed amount of fill trials has
     * been exceeded
     */
    public List<SolvingStrategy> fill(int randomFills) {
        System.out.println("start filling");
        int fillTrials = 1;
        // try to fill the Sudoku completely until it succeeds
        while (!fillTrial(randomFills)) {
            if (fillTrials > FILL_TRIAL_CAP) {
                System.out.println("COULD NOT FILL THE SUDOKU. Fill trial cap exceeded: " + fillTrials);
                return null;
            }
            fillTrials++; // count how many trials were necessary
        }

        // saving the solution values before erasing
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
        // System.out.println("successfully erased");

        // saving the newly generated state as start values in the Sudoku
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

        return strategies;
    }

    /**
     * Tries to solve the Sudoku like a player and returns 0 if it was successful doing so, 1, if it was unsuccessful
     * maybe because its solving algorithm is not strong enough and -1 if the Sudoku is not solvable, probably because
     * there was an error inserting it.
     *
     * @return If solving the Sudoku succeeded
     */
    public int solve() throws PIVException {
        boolean successful = true;
        while (successful && !isFinished()) {
            successful = trySolving();
        }

        if (successful) {
            // saving the Solution array before erasing
            for (int i = 0; i < length; i++) {
                for (int j = 0; j < length; j++) {
                    sudoku.setSolutionValue(sudoku.getCurrentValue(i, j), i, j);
                }
            }
            erase();
            // save the newly generated state as start values
            for (int i = 0; i < length; i++) {
                for (int j = 0; j < length; j++) {
                    sudoku.setStartValue(sudoku.getCurrentValue(i, j), i, j);
                }
            }
            System.out.println("Solution: ");
            System.out.println(sudoku.toString(true, true));
            return 0;
        } else {
            System.out.println("Failed solving the Sudoku. Furthest Progress:");
            System.out.println(sudoku.toString());
            boolean locked = sudoku.isLocked();
            sudoku.clear();
            if (locked) {
                return -1;
            } else {
                return 1;
            }
        }

    }

    //------------------------------------------------------------------------------------------------------------------

    /**
     * Helping method which tries to fill the Sudoku completely, but gives up if it exceeds the allowed amount of trials
     * (length² / 2). It returns whether it was successful.
     *
     * @return If the the method was successful in filling the sudoku (startValues, solutionValues and currentValues)
     */
    private boolean fillTrial(int randomFills) {

        // System.out.println("start fill trial");
        do {
            sudoku.clear();
            // fill randomFills fields randomly without direct conflicts
            randomFill(randomFills);
        } while (sudoku.isLocked());
        // System.out.println("randomFill successfully performed");
        // System.out.println(sudoku.toString());

        try {
            while (trySolving()) {
                // debug:
                // System.out.println("trySolving iteration");
            }
        } catch (PIVException e) {
            e.printStackTrace();
        }

        //--------------------------------------------------------------------------------------------------------------
        int trySolvingAssumeCycles = 0;
        int cap = length * length / 2;
        while (!isFinished()) {

            // restart the fill method if it seems like there is no solution to find
            if (trySolvingAssumeCycles > cap) {
                // System.out.println("NEW TRY");
                backups.clear(); // clear the backups for the next trial
                backups.push(new BackupPoint(-1, new LinkedList<>())); // Backup for the first tsFills
                // clear the performed operations data of the solving strategies
                for (SolvingStrategy strategy : strategies) {
                    strategy.getPerformedOperations().clear();
                }
                sudoku.clear();
                return false;
            }

            // make an assumption
            try {
                // debug:
                // System.out.println("assume");
                assume(backups.peek().getChangedCoord());
            } catch (NoBackupsException ex) {
                System.out.println("no backups exception");
                backups.clear(); // clear the backups for the next trial
                backups.push(new BackupPoint(-1, new LinkedList<>())); // Backup for the first tsFills
                sudoku.clear();
                return false;
            } catch (PIVException e) {
                e.printStackTrace();
            }

            // trySolving loop
            // use the trySolving method as long as it succeeds
            try {
                while (trySolving()) {
                    // debug:
                    // System.out.println("trySolving iteration");
                }
            } catch (PIVException e) {
                e.printStackTrace();
            }

            trySolvingAssumeCycles++;
            // debug:
            // System.out.println("amount of trySolving, assume cycles: " + trySolvingAssumeCycles);
        }
        return true; // Sudoku is finished
    }

    /**
     * Inserts "amount" values randomly on fields of the solverBoard filled with 0 considering the game rules (making
     * sure that every number appears at most once in every row, column and block). This method maintains possibility
     * integrity.
     */
    private void randomFill(int amount) {
        for (int k = 0; k < amount; k++) {
            int iCoord = MathUtilities.randomNumber(length) - 1;
            int jCoord = MathUtilities.randomNumber(length) - 1;
            int value = MathUtilities.randomNumber(length);

            try {
                if (sudoku.getCurrentValue(iCoord, jCoord) == 0 && sudoku.isAllowedQuick(value, iCoord, jCoord)) {
                    sudoku.setCurrentValue(value, iCoord, jCoord, true);
                } else {
                    k--;
                }
            } catch (PIVException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Tries to solve the given Sudoku like a player and returns true if at least one current value or possibility got
     * changed (indicating that it should be continued trying to solve the Sudoku). The fields filled by this method may
     * become deleted afterwards. This method maintains possibility integrity.
     *
     * @return If some progress could been made to solve the sudoku
     */
    private boolean trySolving() throws PIVException {
        boolean changedSomething = false;
        for (SolvingStrategy strategy : strategies) {
            changedSomething = changedSomething || strategy.apply();
        }
        return changedSomething;
    }

    /**
     * If there is no value to be found by trying to solve the Sudoku like a player, there is an assumption to be made.
     * It is very well possible that this assumption is wrong, which is why a backup is made to be returned to, if that
     * is the case. This method attempts an assumption after the last one by iterating systematically in steps of the
     * size stepWidth through the Sudoku. If the Sudoku is found to be locked, the stepBack method is called.
     *
     * @param coord The 1D coordinate of the last assumption (i = coord / length, j = coord % length)
     * @throws PIVException If possibility integrity is not assured
     */
    private void assume(int coord) throws NoBackupsException, PIVException {
        if (!sudoku.isPossibilityInteger()) {
            throw new PIVException();
        }

        coord = coord + stepWidth; // iterate to the next field
        for (int k = 0; k < length * length; coord = coord + stepWidth, k++) {
            // when overflowing return to the beginning of the solverBoard
            coord = coord % (length * length);
            int i = coord / length;
            int j = coord % length;

            // testing if the field is not filled yet
            if (sudoku.getCurrentValue(i, j) == 0) {

                LinkedList<Integer> possi = new LinkedList<>();

                possi.addAll(sudoku.getPossibilities(i, j));

                if (possi.isEmpty()) { // Sudoku is locked
                    // System.out.println("DEBUG: assume: step back");
                    stepBack();
                    return;
                } else {
                    // choose a random possible assumption on this field
                    int index = (int) (Math.random() * possi.size());
                    sudoku.setCurrentValue(possi.get(index), i, j, true);
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
     * Undoes the last assumption and chooses the next one. If there are still possibilities on this field remaining,
     * one of these is chosen, while if there are none, the assume method is called at this point.
     */
    private void stepBack() throws NoBackupsException {
        if (backups.size() == 1) {
            throw new NoBackupsException();
        }
        BackupPoint latestBackup = backups.pop(); // undo the latest backup
        List<Coordinate> tSFills = latestBackup.getTSFills();

        // Remove all values which have been made based on the assumption
        while (!tSFills.isEmpty()) {
            Coordinate coord = tSFills.remove(0);
            sudoku.setCurrentValue(0, coord.i, coord.j, true);
        }

        int coord = latestBackup.getChangedCoord();
        int i = coord / length;
        int j = coord % length;
        // no possible assumptions for this field remaining => change field
        if (latestBackup.getPossibilities().isEmpty()) {
            //            System.out.println("DEBUG: stepBack(): change field from i = " + i + " j = " + j);
            sudoku.setCurrentValue(0, i, j, true);
            try {
                assume(coord);
            } catch (PIVException ex) {
                ex.printStackTrace();
            }
        }

        // test another possible assumption on this field
        else {
            sudoku.setCurrentValue(latestBackup.getRandomPossibility(), i, j, true);
            // System.out.println("DEBUG: stepBack(): keep node Coord:
            // "+latestBackup.getChangedCoord() + " now with value:
            // "+getCurrentValue(latestBackup.getChangedCoord()));
            backups.push(latestBackup);
        }
    }

    /**
     * Erases the permitted fields of the Sudoku solverBoard, which are those, filled in by the trySolving method. Not
     * permitted are the fields of the randomFill method and the assumed ones. The higher the difficulty, the more
     * fields are erased. Afterwards the possibilities are updated.
     */
    private void erase() {
        // delete all trySolving entries of the backups and push them into one
        // stack
        Stack<Coordinate> totalTSFills = new Stack<>();
        while (!backups.isEmpty()) {
            List<Coordinate> currentTSFills = backups.pop().getTSFills();
            while (!currentTSFills.isEmpty()) {
                totalTSFills.push(currentTSFills.remove(0));
            }
        }

        int deletable = totalTSFills.size();
        int entriesDeleted = 0; // difficulty 1 -> delete 50% of the entries;
        // difficulty 10 -> delete 100 % of the entries
        while (totalTSFills.size() > (int) ((0.5 - 0.0555555555555555 * (sudoku.getDifficulty() - 1)) * deletable)) {
            // get a random value of the stack
            Coordinate currentCoord = totalTSFills.remove((int) (Math.random() * totalTSFills.size()));
            sudoku.setCurrentValue(0, currentCoord.i, currentCoord.j, false);
            entriesDeleted++;
        }
        sudoku.calculatePossibilities();
        System.out.println(entriesDeleted + " out of " + deletable + " deletable entries deleted");
        System.out.println("with difficulty " + sudoku.getDifficulty());
    }

    /**
     * Returns true if the currentValues of the Sudoku contain no "0". If it does so, it is considered as "finished".
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

    //------------------------------------------------------------------------------------------------------------------
    // Getter and Setter (currently nothing)

}