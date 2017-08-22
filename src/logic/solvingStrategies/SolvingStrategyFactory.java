package logic.solvingStrategies;

import logic.Sudoku;
import logic.SudokuSolver;

import java.util.ArrayList;

public class SolvingStrategyFactory {

    /**
     * Initializes the strategies in the solvingStrategies list. Restrictive Methods are called first, Interpreting
     * methods second. Inside of this partitioning the methods with better performance are used earlier then the
     * complicated ones.
     **/
    public static ArrayList<SolvingStrategy> createSolvingStrategyList(Sudoku sudoku, SudokuSolver solver) {
        int difficulty = sudoku.getDifficulty();
        ArrayList<SolvingStrategy> strategies = new ArrayList<>(11);

        // TODO: look for a possibility to check for the difficulty of the strategy before instantiating it

        // restrictive methods:
        //--------------------------------------------------------------------------------------------------------------
        // intersection strategies
        SolvingStrategy strategy = new IntersectionRowToBlock(sudoku);
        if (strategy.getDifficulty() <= difficulty) {
            strategies.add(strategy);
        }
        strategy = new IntersectionColumnToBlock(sudoku);
        if (strategy.getDifficulty() <= difficulty) {
            strategies.add(strategy);
        }
        strategy = new IntersectionBlockToRowAndColumn(sudoku);
        if (strategy.getDifficulty() <= difficulty) {
            strategies.add(strategy);
        }

        // fish strategies
        strategy = new XWing(sudoku);
        if (strategy.getDifficulty() <= difficulty) {
            strategies.add(strategy);
        }
        strategy = new Swordfish(sudoku);
        if (strategy.getDifficulty() <= difficulty) {
            strategies.add(strategy);
        }
        strategy = new Jellyfish(sudoku);
        if (strategy.getDifficulty() <= difficulty) {
            strategies.add(strategy);
        }

        // interpreting methods:
        //--------------------------------------------------------------------------------------------------------------
        // OnlyOnePossibilityOnField standard method
        strategy = new OnlyOnePossibilityOnField(sudoku, solver);
        strategies.add(strategy);   // is always used

        // hidden single strategies
        strategy = new HiddenSingleRow(sudoku, solver);
        if (strategy.getDifficulty() <= difficulty) {
            strategies.add(strategy);
        }
        strategy = new HiddenSingleColumn(sudoku, solver);
        if (strategy.getDifficulty() <= difficulty) {
            strategies.add(strategy);
        }
        strategy = new HiddenSingleBlock(sudoku, solver);
        if (strategy.getDifficulty() <= difficulty) {
            strategies.add(strategy);
        }

        return strategies;
    }

}
