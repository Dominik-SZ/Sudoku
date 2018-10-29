package customNodes;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;

import java.util.ArrayList;

public class SkyscraperSudokuBoardModel extends SudokuBoardModel {

    private ArrayList<ArrayList<BooleanProperty>> isValueFieldsBars;
    private ArrayList<ArrayList<IntegerProperty>> valuesBars;
    private ArrayList<ArrayList<ArrayList<BooleanProperty>>> notesBars;


    /**
     * Creates a new SkyscraperSudokuBoardModel with unbound simple properties.
     *
     * @param length The length of the represented SudokuBoard
     */
    SkyscraperSudokuBoardModel(int length) {
        super(length);


    }
}
