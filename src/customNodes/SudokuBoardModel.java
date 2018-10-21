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

    private final ArrayList<ArrayList<BooleanProperty>> isValueField;
    private final ArrayList<ArrayList<IntegerProperty>> values;
    private final ArrayList<ArrayList<ArrayList<BooleanProperty>>> notes;

    /**
     * Creates a new SudokuBoardModel with unbound simple properties.
     *
     * @param length The length of the represented SudokuBoard
     */
    SudokuBoardModel(int length) {
        // create isValueField properties
        this.isValueField = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            ArrayList<BooleanProperty> row = new ArrayList<>(length);
            for (int j = 0; j < length; j++) {
                row.add(new SimpleBooleanProperty());
            }
            isValueField.add(row);
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
                ArrayList<BooleanProperty> notes = new ArrayList<>(length + 1);
                for (int k = 0; k < length + 1; k++) {
                    notes.add(new SimpleBooleanProperty());
                }
                row.add(notes);
            }
            notes.add(row);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Binding methods:

    void bindIsValueField(int i, int j, BooleanProperty isValueField) {
        this.isValueField.get(i).get(j).bindBidirectional(isValueField);
    }

    void bindValue(int i, int j, IntegerProperty value) {
        this.values.get(i).get(j).bindBidirectional(value);
    }

    void bindNotes(int i, int j, ArrayList<BooleanProperty> notes) {
        ArrayList<BooleanProperty> fieldsNotes = this.notes.get(i).get(j);
        for (int k = 0; k < notes.size(); k++) {
            fieldsNotes.get(k).bindBidirectional(notes.get(k));
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Getter:
    public BooleanProperty isValueFieldProperty(int i, int j) {
        return isValueField.get(i).get(j);
    }

    public IntegerProperty valueProperty(int i, int j) {
        return values.get(i).get(j);
    }

    public ArrayList<BooleanProperty> notesProperty(int i, int j) {
        return notes.get(i).get(j);
    }

    /**
     * Get a two dimensional array of all isValue information of this board addressable by i and j coordinates. These
     * booleans decide if their respective field is a value field or a note field. Only the respective information
     * (value or notes) is relevant.
     *
     * @return The isValueField information of this board
     */
    public ArrayList<ArrayList<BooleanProperty>> getIsValueField() {
        return isValueField;
    }

    /**
     * Get a two dimensional array of all value information of this board addressable by i and j coordinates. Note
     * that the value of a field is most likely irrelevant if the isValueField bit of this field is set to false.
     *
     * @return The isValueField information of this board
     */
    public ArrayList<ArrayList<IntegerProperty>> getValues() {
        return values;
    }

    /**
     * Get a two dimensional array of all notes information of this board addressable by i and j coordinates. Note
     * that the notes of a field are most likely irrelevant if the isValueField bit of this field is set to true.
     *
     * @return The notes information of this board
     */
    public ArrayList<ArrayList<ArrayList<BooleanProperty>>> getNotes() {
        return notes;
    }
}
