package customNodes;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

import java.util.ArrayList;

class SudokuField extends Region {

    /**
     * The minimum width and length of a SudokuField
     */
    static final int MIN_SIZE = 25;
    static final int PREF_SIZE = 50;

    private int i;
    private int j;
    private int length;
    private boolean allowNotes;
    private Region child;

    private final BooleanProperty isValueField;
    private final IntegerProperty value;
    private final ArrayList<BooleanProperty> notes;

    SudokuField(int i, int j, int length, boolean allowNotes) {
        this.i = i;
        this.j = j;
        this.length = length;
        this.allowNotes = allowNotes;
        this.child = new ValueField(length);
        this.getChildren().add(child);

        this.isValueField = new SimpleBooleanProperty();
        this.value = new SimpleIntegerProperty();
        this.notes = new ArrayList<>(length + 1);

        setMinSize(MIN_SIZE, MIN_SIZE);
        setPrefSize(PREF_SIZE, PREF_SIZE);

        widthProperty().addListener((observable, oldValue, newValue) -> adjustPrefSizeOfChild());
        heightProperty().addListener((observable, oldValue, newValue) -> adjustPrefSizeOfChild());

        addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (this.allowNotes && event.getButton().equals(MouseButton.SECONDARY)) {    // right click
                toggleFieldType();
            }
        });
    }


    private void toggleFieldType() {
        if (isValueField.get()) {   // value -> notes
            value.unbindBidirectional(((ValueField) child).valueProperty());
            child = new NoteField(length);
            adjustPrefSizeOfChild();
            getChildren().clear();
            getChildren().add(child);
            isValueField.set(false);
        } else {                    // notes -> value
            ValueField newChild = new ValueField(length);
            value.bindBidirectional(newChild.valueProperty());
            child = newChild;
            adjustPrefSizeOfChild();
            getChildren().clear();
            getChildren().add(child);
            isValueField.set(true);
        }
    }

    private void adjustPrefSizeOfChild() {
        child.setPrefSize(getPrefWidth(), getPrefHeight());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Getter:
    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public BooleanProperty isValueFieldProperty() {
        return isValueField;
    }

    public IntegerProperty valueProperty() {
        return value;
    }

    public ArrayList<BooleanProperty> notesProperty() {
        return notes;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setter:
    public void setToggleable(boolean toggleable) {
        this.allowNotes = toggleable;
    }

}
