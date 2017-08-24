package logic.solving;

import logic.Sudoku;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class SolvingStrategyFactory {

    public static final int STRATEGY_AMOUNT = 11;

    /**
     * Initializes the strategies in the solvingStrategies list. Restrictive Methods are called first, Interpreting
     * methods second. Inside of this partitioning the methods with better performance are used earlier then the
     * complicated ones.
     **/
    static List<SolvingStrategy> createSolvingStrategyList(Sudoku sudoku, Deque<BackupPoint> backups) {
        int difficulty = sudoku.getDifficulty();
        List<SolvingStrategy> strategies = new LinkedList<>();

        // restrictive methods:
        //--------------------------------------------------------------------------------------------------------------
        // intersection strategies
        strategies.add(new IntersectionRowToBlock(sudoku));
        strategies.add(new IntersectionColumnToBlock(sudoku));
        strategies.add(new IntersectionBlockToRowAndColumn(sudoku));

        // fish strategies
        strategies.add(new XWing(sudoku));
        strategies.add(new Swordfish(sudoku));
        strategies.add(new Jellyfish(sudoku));

        // interpreting methods:
        //--------------------------------------------------------------------------------------------------------------
        // OnlyOnePossibility standard method
        strategies.add(new OnlyOnePossibility(sudoku, backups));

        // hidden single strategies
        strategies.add(new HiddenSingleRow(sudoku, backups));
        strategies.add(new HiddenSingleColumn(sudoku, backups));
        strategies.add(new HiddenSingleBlock(sudoku, backups));

        //--------------------------------------------------------------------------------------------------------------
        // only keep the strategies, which are simple enough
        List<SolvingStrategy> toKeep = new LinkedList<>();
        for(SolvingStrategy strategy: strategies) {
            if(strategy.getDifficulty() <= difficulty) {
                toKeep.add(strategy);
            }
        }

        return toKeep;
    }

}