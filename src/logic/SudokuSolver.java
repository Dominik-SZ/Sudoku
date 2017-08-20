package logic;

import java.util.*;

import logic.exceptions.NoBackupsException;
import logic.exceptions.PIVException;
import logic.solvingStrategies.*;
import util.MathUtilities;


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
    private Stack<BackupPoint> backups;
    /**
     * the distance in fields between two assumptions
     */
    private int stepWidth;
    /**
     * the strategies used to solve this Sudoku
     */
    private ArrayList<SolvingStrategy> strategies;

    // ------------------------------------------------------------------------

    SudokuSolver(Sudoku sudoku) {
        this.sudoku = sudoku;
        this.length = sudoku.getLength();
        // generate a stepWidth, which has no common divisor with the Sudoku length greater than 1
        stepWidth = (int) (length * 1.3);
        while (MathUtilities.greatestCommonDivisor(stepWidth, length) != 1) {
            stepWidth++;
        }

        backups = new Stack<>();
        backups.push(new BackupPoint(-1, new LinkedList<>()));

        initializeStrategies();
    }

    /**
     * Initializes the strategies in the solvingStrategies list. Restrictive Methods are called first, Interpreting
     * methods second.
     * Inside of this partitioning the methods with better performance are used earlier then the complicated ones.
     **/
    private void initializeStrategies() {
        int difficulty = sudoku.getDifficulty();

        // TODO: look for a possibility to check for the difficulty of the strategy before instantiating it
        strategies = new ArrayList<>(14);

        // restrictive methods:
        //--------------------------------------------------------------------------------------------------------------
        // intersection strategies
        SolvingStrategy strategy = new IntersectionRowToBlock(sudoku);
        if(strategy.getDifficulty() <= difficulty) {
            strategies.add(strategy);
        }
        strategy = new IntersectionColumnToBlock(sudoku);
        if(strategy.getDifficulty() <= difficulty) {
            strategies.add(strategy);
        }
        strategy = new IntersectionBlockToRowAndColumn(sudoku);
        if(strategy.getDifficulty() <= difficulty) {
            strategies.add(strategy);
        }

        // fish strategies
        strategy = new XWing(sudoku);
        if(strategy.getDifficulty() <= difficulty) {
            strategies.add(strategy);
        }
        strategy = new Swordfish(sudoku);
        if(strategy.getDifficulty() <= difficulty) {
            strategies.add(strategy);
        }
        strategy = new Jellyfish(sudoku);
        if(strategy.getDifficulty() <= difficulty) {
            strategies.add(strategy);
        }

        // interpreting methods:
        //--------------------------------------------------------------------------------------------------------------
        // OnlyOnePossibilityOnField standard method
        strategy = new OnlyOnePossibilityOnField(sudoku, this);
        strategies.add(strategy);   // is always used

        // hidden single strategies
        strategy = new HiddenSingleRow(sudoku, this);
        if(strategy.getDifficulty() <= difficulty) {
            strategies.add(strategy);
        }
        strategy = new HiddenSingleColumn(sudoku, this);
        if(strategy.getDifficulty() <= difficulty) {
            strategies.add(strategy);
        }
        strategy = new HiddenSingleBlock(sudoku, this);
        if(strategy.getDifficulty() <= difficulty) {
            strategies.add(strategy);
        }
    }

    // ------------------------------------------------------------------------
    // Main methods used to fill a Sudoku

    /**
     * Main generating method to fill an empty sudoku, making it definitely
     * solvable. It also prints the solution and the Sudoku to the system.out,
     * as well as some additional info.
     */
    ArrayList<SolvingStrategy> fill(int randomFills) {
        System.out.println("start filling");
        int fillTrials = 1;
        // try to fill the Sudoku completely until it succeeds
        while (!fillTrial(randomFills)) {
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

        return strategies;
    }

    /**
     * Tries to solve the Sudoku like a player and returns if it was successful doing so.
     *
     * @return If solving the Sudoku succeeded
     */
    boolean solve() throws PIVException {
        boolean successful = true;
        while (successful && !isFinished()) {
            successful = trySolving();
        }

        if (successful) {
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
        erase();

        return successful;
    }

    //------------------------------------------------------------------------------------------------------------------

    /**
     * Helping method which tries to fill the Sudoku completely, but gives up if it exceeds the allowed amount of
     * trials (length² / 2). It returns whether it was successful.
     *
     * @return If the the method was successful in filling the sudoku (startValues, solutionValues and currentValues)
     */
    private boolean fillTrial(int randomFills) {

        System.out.println("start fill trial");
        do {
            sudoku.clear();
            // fill randomFills fields randomly without direct conflicts
            randomFill(randomFills);
        } while (isLocked());
        System.out.println("randomFill successfully performed");
        // System.out.println(sudoku.toString());

        try {
            while (trySolving()) {
                // debug:
                // System.out.println("trySolving iteration");
            }
        } catch (PIVException e) {
            e.printStackTrace();
        }

        int trySolvingAssumeCycles = 0;
        int cap = length * length / 2;
        while (!isFinished()) {

            // restart the fill method if it seems like there is no solution to find
            if (trySolvingAssumeCycles > cap) {
                System.out.println("NEW TRY");
                backups.clear(); // clear the backups for the next trial
                backups.push(new BackupPoint(-1, new LinkedList<>())); // Backup for the first tsFills
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
     * sure that every number appears at most once in every row, column and block).
     * This method maintains possibility integrity.
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
     * changed (indicating that it should be continued trying to solve the Sudoku). The fields filled by this method
     * may become deleted afterwards.
     * This method maintains possibility integrity.
     *
     * @return If some progress could been made to solve the sudoku
     */
    private boolean trySolving() throws PIVException {
        boolean changedSomething = false;
        for(SolvingStrategy strategy: strategies) {
            changedSomething = strategy.apply();
        }
        return changedSomething;
    }

    /**
     * If there is no value to be found by trying to solve the Sudoku like a player, there is an assumption to be
     * made. It is very well possible that this assumption is wrong, which is why a backup is made to be returned
     * to, if that is the case. This method attempts an assumption after the last one by iterating systematically in
     * steps of the size stepWidth through the Sudoku. If the Sudoku is found to be locked, the stepBack method is
     * called.
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
        Stack<Integer> tSFills = latestBackup.getTSFills();

        // Remove all values which have been made based on the assumption
        while (!tSFills.isEmpty()) {
            int coord = tSFills.pop();
            sudoku.setCurrentValue(0, coord / length, coord % length, true);
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
            sudoku.setCurrentValue(latestBackup.popRandomPossibility(), i, j, true);
            // System.out.println("DEBUG: stepBack(): keep node Coord:
            // "+latestBackup.getChangedCoord() + " now with value:
            // "+getCurrentValue(latestBackup.getChangedCoord()));
            backups.push(latestBackup);
        }
    }

    /**
     * Erases the permitted fields of the Sudoku solverBoard, which are those, filled in by the trySolving method.
     * Not permitted are the fields of the randomFill method and the assumed ones. The higher the difficulty, the
     * more fields are erased. Afterwards the possibilities are updated.
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
            sudoku.setCurrentValue(0, actualCoord / length, actualCoord % length, false);
            entriesDeleted++;
        }
        sudoku.calculatePossibilities();
        System.out.println(entriesDeleted + " out of " + deletable + " deletable entries deleted");
        System.out.println("with difficulty " + sudoku.getDifficulty());
    }

    // -------------------------------------------------------------------------
    // Secondary Methods to fill the Sudoku

    /**
     * checks if there is no allowed possibility remaining to fill one of the empty fields.
     */
    private boolean isLocked() {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (sudoku.getCurrentValue(i, j) == 0 && sudoku.getPossibilities(i, j).size() == 0) {
                    System.out.println("isLocked: no remaining possibilities found at i=" + i + " and j=" + j);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if the currentValues of the Sudoku contain no "0". If it
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
