package customNodes;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class SkyscraperSideBar extends Region {

    private int length;
    private Pane bar;
    private SudokuField[] fields;

    private final ArrayList<BooleanProperty> isValueFields;
    private final ArrayList<IntegerProperty> values;
    private final ArrayList<ArrayList<BooleanProperty>> notes;

    SkyscraperSideBar(Layout layout, int length, boolean allowNotes) {
        this.length = length;
        this.fields = new SudokuField[length];

        // initialize the properties
        this.isValueFields = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            isValueFields.add(new SimpleBooleanProperty());
        }
        this.values = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            values.add(new SimpleIntegerProperty());
        }
        this.notes = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            ArrayList<BooleanProperty> fieldsNotes = new ArrayList<>(length);
            for (int j = 0; j < length; j++) {
                fieldsNotes.add(new SimpleBooleanProperty());
            }
            notes.add(fieldsNotes);
        }

        if (layout == Layout.HORIZONTAL) {
            bar = new HBox();
            setMinHeight(SudokuField.MIN_SIZE);
            setMinWidth(length * SudokuField.MIN_SIZE);
        } else {
            bar = new VBox();
            setMinWidth(SudokuField.MIN_SIZE);
            setMinHeight(length * SudokuField.MIN_SIZE);
        }
        getChildren().add(bar);

        for (int i = 0; i < length; i++) {
            SudokuField field = new SudokuField(length, allowNotes);
            fields[i] = field;
            bar.getChildren().add(field);

            isValueFields.get(i).bindBidirectional(field.isValueFieldProperty());
            values.get(i).bindBidirectional(field.valueProperty());
            ArrayList<BooleanProperty> fieldsNotes = field.notesProperty();
            for (int k = 0; k < length; k++) {
                notes.get(i).get(k).bindBidirectional(fieldsNotes.get(k));
            }


        }

    }

    enum Layout {
        VERTICAL, HORIZONTAL
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Getter:
    SudokuField getField(int i) {
        return fields[i];
    }

}
