package javaFXGUI.customNodes;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import util.InputChecker;

import java.util.ArrayList;

class NoteField extends Region {


    private final ArrayList<BooleanProperty> notes;
    private int length;
    private int blockLength;
    private GridPane root;
    private TextField[] subFields;

    NoteField(int length) {
        InputChecker.checkIfValidLength(length);
        this.length = length;
        this.blockLength = (int) Math.sqrt(length);
        this.subFields = new TextField[length];
        this.notes = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            notes.add(new SimpleBooleanProperty());
        }

        root = new GridPane();
        for (int i = 0; i < length; i++) {
            TextField field = new SubNoteField(i + 1);
            root.add(field, i % blockLength, blockLength - (i / blockLength));
            subFields[i] = field;
        }

        this.widthProperty().addListener((observable, oldValue, newValue) -> adjustChildren());
        this.heightProperty().addListener((observable, oldValue, newValue) -> adjustChildren());

        this.getChildren().add(root);
        adjustChildren();
    }

    /**
     * Adjusts size and font size of the subFields.
     */
    private void adjustChildren() {
        for (TextField field : subFields) {
            field.setPrefSize(getPrefWidth() / blockLength, getPrefHeight() / blockLength);
            field.setFont(new Font(field.getPrefHeight() * 0.6));
        }
    }

    /**
     * Returns the BooleanProperty of the inserted index, meaning the inserted index is noted if this property contains
     * true and the index is not noted if the property contains false.
     *
     * @param index The number whose note property should be returned (0-8 corresponding to numbers 1-9 standard)
     * @return The note property of the inserted index
     */
    BooleanProperty noteProperty(int index) {
        if (index < 0 || length <= index) {
            throw new IllegalArgumentException("Note Property demanded for illegal index: " + index + ". Must be " +
                    "0 to (length-1).");
        }
        return notes.get(index);
    }

    /**
     * The small SubFields that are used to contain one possibility each and are length times assigned to one
     * SudokuField.
     */
    private class SubNoteField extends TextField {

        private final int MIN_SIZE = 18;

        private int number;
        private BooleanProperty selected;


        private SubNoteField(int number) {
            this.number = number;
            this.selected = new SimpleBooleanProperty();
            selected.bindBidirectional(notes.get(number - 1));
            this.setMinSize(MIN_SIZE, MIN_SIZE);
            this.setAlignment(Pos.CENTER);
            this.setEditable(false);
            this.setPromptText(number + "");

            addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                if (event.getButton().equals(MouseButton.PRIMARY)) {  // left click
                    toggleMode();
                }
            });
        }

        private void toggleMode() {
            if (selected.get()) {    // selected -> not selected
                setText("");
                setFocused(false);
                setPromptText(number + "");
                selected.set(false);
            } else {
                setText(number + "");
                setFocused(false);
                selected.set(true);
            }
        }

    }

    // -----------------------------------------------------------------------------------------------------------------
    // Getter:

}
