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

    private Layout layout;
    private int length;
    private final ArrayList<BooleanProperty> isValueFields;
    private final ArrayList<IntegerProperty> values;
    private final ArrayList<ArrayList<BooleanProperty>> notes;

    SkyscraperSideBar(Layout layout, int length, boolean allowNotes) {
        this.layout = layout;
        this.length = length;

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

        Pane bar;
        if (layout == Layout.HORIZONTAL) {
            bar = new HBox();
            setMinHeight(SudokuField.MIN_SIZE);
            setMinWidth(length * SudokuField.MIN_SIZE);
        } else {
            bar = new VBox();
            setMinWidth(SudokuField.MIN_SIZE);
            setMinHeight(length * SudokuField.MIN_SIZE);
        }

        for (int i = 0; i < length; i++) {
            SudokuField field = new SudokuField(length, allowNotes);
            field.isValueFieldProperty().bindBidirectional(isValueFields.get(i));
            field.valueProperty().bindBidirectional(values.get(i));
            ArrayList<BooleanProperty> fieldsNotes = field.notesProperty();
            for (int k = 0; k < length; k++) {
                fieldsNotes.get(k).bindBidirectional(notes.get(i).get(k));
            }
            //field.setPadding();
            bar.getChildren().add(field);
        }

        getChildren().add(bar);
    }

    enum Layout {
        VERTICAL, HORIZONTAL
    }
}
