package model.solving.solver;

import model.Sudoku;
import model.solving.solver_util.BackupPoint;
import model.solving.strategies.*;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class SolvingStrategyFactory {

    public static final int STRATEGY_AMOUNT = 11;

    /**
     * Initializes the model.solving.strategies in the solvingStrategies list. Restrictive Methods are called first, Interpreting
     * methods second. Inside of this partitioning the methods with better performance are used earlier then the
     * complicated ones.
     **/
    static List<SolvingStrategy> createSolvingStrategyList(Sudoku sudoku, Deque<BackupPoint> backups) {
        int difficulty = sudoku.getDifficulty();
        List<SolvingStrategy> strategies = new LinkedList<>();

        // restrictive methods:
        //--------------------------------------------------------------------------------------------------------------
        // intersection model.solving.strategies
        strategies.add(new IntersectionRowToBlock(sudoku));
        strategies.add(new IntersectionColumnToBlock(sudoku));
        strategies.add(new IntersectionBlockToRowAndColumn(sudoku));

        // fish model.solving.strategies
        strategies.add(new XWing(sudoku));
        strategies.add(new Swordfish(sudoku));
        strategies.add(new Jellyfish(sudoku));

        // interpreting methods:
        //--------------------------------------------------------------------------------------------------------------
        // OnlyOnePossibility standard method
        strategies.add(new OnlyOnePossibility(sudoku, backups));

        // hidden single model.solving.strategies
        strategies.add(new HiddenSingleRow(sudoku, backups));
        strategies.add(new HiddenSingleColumn(sudoku, backups));
        strategies.add(new HiddenSingleBlock(sudoku, backups));

        //--------------------------------------------------------------------------------------------------------------
        // only keep the model.solving.strategies, which are simple enough
        List<SolvingStrategy> toKeep = new LinkedList<>();
        for(SolvingStrategy strategy: strategies) {
            if(strategy.getDifficulty() <= difficulty) {
                toKeep.add(strategy);
            }
        }

        return toKeep;
    }

}