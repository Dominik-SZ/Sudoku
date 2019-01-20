package javaFXGUI.customNodes;

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
    static final int PREF_SIZE = 40;
    private final BooleanProperty isValueField;
    private final IntegerProperty value;
    private final ArrayList<BooleanProperty> notes;
    private int length;
    private boolean allowNotes;
    private Region child;

    SudokuField(int length, boolean allowNotes) {
        this.length = length;
        this.allowNotes = allowNotes;
        this.child = new ValueField(length);
        this.getChildren().add(child);

        this.isValueField = new SimpleBooleanProperty(true);
        this.value = new SimpleIntegerProperty();
        value.bindBidirectional(((ValueField) child).valueProperty());
        this.notes = new ArrayList<>(length);
        for (int k = 0; k < length; k++) {
            notes.add(new SimpleBooleanProperty());
        }

        setMinSize(MIN_SIZE, MIN_SIZE);
        setPrefSize(PREF_SIZE, PREF_SIZE);

        widthProperty().addListener((observable, oldValue, newValue) -> adjustPrefSizeOfChild());
        heightProperty().addListener((observable, oldValue, newValue) -> adjustPrefSizeOfChild());

        addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (this.allowNotes && event.getButton().equals(MouseButton.SECONDARY)) {    // right click
                toggleFieldType();
                System.out.println("field pref width: " + getPrefWidth());
                System.out.println("field actual width: " + getWidth());
            }
        });
    }


    private void toggleFieldType() {
        // value -> notes
        if (isValueField.get()) {
            // unbind the old child:
            value.unbindBidirectional(((ValueField) child).valueProperty());

            NoteField newChild = new NoteField(length);
            // bind the new child:
            for (int i = 0; i < length; i++) {
                notes.get(i).bindBidirectional(newChild.noteProperty(i));
            }
            child = newChild;
            adjustPrefSizeOfChild();
            getChildren().clear();
            getChildren().add(child);
            isValueField.set(false);
        }
        // notes -> value
        else {
            // unbind the old child:
            for (int i = 0; i < length; i++) {
                notes.get(i).unbindBidirectional(((NoteField) child).noteProperty(i));
            }

            ValueField newChild = new ValueField(length);
            // bind the new child:
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

    BooleanProperty isValueFieldProperty() {
        return isValueField;
    }

    IntegerProperty valueProperty() {
        return value;
    }

    /**
     * Gets the properties representing the notes of this field indexed from 0 to (length-1)
     *
     * @return
     */
    ArrayList<BooleanProperty> notesProperty() {
        return notes;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setter:
    public void setToggleable(boolean toggleable) {
        this.allowNotes = toggleable;
    }

}
