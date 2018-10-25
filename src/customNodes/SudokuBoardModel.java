package customNodes;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;

/**
 * This class can hold all necessary information of a SudokuBoard, which could be interesting for external use. This
 * information consists of:
 * <ul>
 * <li>All information if the fields are in "value" or in "note" mode</li>
 * <li>All values currently inserted in the fields</li>
 * <li>All notes currently inserted in the fields</li>
 * </ul>
 */
class SudokuBoardModel {

    private final ArrayList<ArrayList<BooleanProperty>> isValueFields;
    private final ArrayList<ArrayList<IntegerProperty>> values;
    private final ArrayList<ArrayList<ArrayList<BooleanProperty>>> notes;

    /**
     * Creates a new SudokuBoardModel with unbound simple properties.
     *
     * @param length The length of the represented SudokuBoard
     */
    SudokuBoardModel(int length) {
        // create isValueFields properties
        this.isValueFields = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            ArrayList<BooleanProperty> row = new ArrayList<>(length);
            for (int j = 0; j < length; j++) {
                row.add(new SimpleBooleanProperty());
            }
            isValueFields.add(row);
        }

        // create value properties
        this.values = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            ArrayList<IntegerProperty> row = new ArrayList<>(length);
            for (int j = 0; j < length; j++) {
                row.add(new SimpleIntegerProperty());
            }
            values.add(row);
        }

        // create notes properties
        this.notes = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            ArrayList<ArrayList<BooleanProperty>> row = new ArrayList<>(length);
            for (int j = 0; j < length; j++) {
                ArrayList<BooleanProperty> notes = new ArrayList<>(length);
                for (int k = 0; k < length; k++) {
                    notes.add(new SimpleBooleanProperty());
                }
                row.add(notes);
            }
            notes.add(row);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Getter:

    /**
     * Get the isValue information of this board specified by i and j coordinates. This boolean represents if the
     * respective field is a value field or a note field. Only the respective information (value or notes) is relevant.
     *
     * @return The isValueField bit of this field
     */
    public BooleanProperty isValueFieldProperty(int i, int j) {
        return isValueFields.get(i).get(j);
    }

    /**
     * Get the value information of this field specified by i and j coordinates. Note that the value of this field is
     * most likely irrelevant if the isValueField bit of this field is set to false.
     *
     * @return The value of this field
     */
    public IntegerProperty valueProperty(int i, int j) {
        return values.get(i).get(j);
    }

    /**
     * Get the notes information of this field specified by the inserted i and j coordinates. Note that the notes of
     * this field are most likely irrelevant if the isValueField bit of this field is set to true.
     *
     * @return The notes information of this field
     */
    public ArrayList<BooleanProperty> notesProperty(int i, int j) {
        return notes.get(i).get(j);
    }

}
