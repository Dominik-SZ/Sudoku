package model_new.solving;

import model_new.solving.strategies.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SolvingStrategyFactory {

    public static final int FILL_STRATEGY_AMOUNT = 4;
    public static final int RESTRICTIVE_STRATEGY_AMOUNT = 6;

    static List<SolvingStrategy> getFillStrategies(int difficulty) {
        List<SolvingStrategy> strategies = new ArrayList<>(FILL_STRATEGY_AMOUNT);

        // OnlyOnePossibility standard method
        strategies.add(new OnlyOnePossibility());

        // hidden single model.solving.strategies
        strategies.add(new HiddenSingleRow());
        strategies.add(new HiddenSingleColumn());
        strategies.add(new HiddenSingleBlock());

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

    static List<SolvingStrategy> getRestrictiveStrategies(int difficulty) {
        List<SolvingStrategy> strategies = new ArrayList<>(RESTRICTIVE_STRATEGY_AMOUNT);

        // intersection model.solving.strategies
        strategies.add(new IntersectionRowToBlock());
        strategies.add(new IntersectionColumnToBlock());
        strategies.add(new IntersectionBlockToRowAndColumn());

        // fish model.solving.strategies
        strategies.add(new XWing());
        strategies.add(new Swordfish());
        strategies.add(new Jellyfish());

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