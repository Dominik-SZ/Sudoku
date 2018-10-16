package customNodes;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import util.InputChecker;

class NoteField extends Region {


    private int length;
    private int blockLength;

    private GridPane root;
    private TextField[] subFields;
    private boolean[] possibilites;

    NoteField(int length) {
        InputChecker.checkIfValidLength(length);
        this.length = length;
        this.blockLength = (int) Math.sqrt(length);
        this.subFields = new TextField[length];
        this.possibilites = new boolean[length];


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
            System.out.println("New font size: " + field.getFont().getSize());
        }
    }

    /**
     * The small SubFields that are used to contain one possibility each and are length times assigned to one
     * SudokuField.
     */
    private class SubNoteField extends TextField {

        private final int MIN_SIZE = 18;

        private int number;
        private boolean displayed;


        private SubNoteField(int number) {
            this.number = number;
            this.displayed = false;
            this.setMinSize(MIN_SIZE, MIN_SIZE);
            this.setAlignment(Pos.CENTER);
            this.setEditable(false);
            this.setPromptText(number + "");

            addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                if (event.getButton().equals(MouseButton.PRIMARY)) {  // left click
                    toggleDisplay();
                }
            });
        }

        private void toggleDisplay() {
            if (displayed) {
                setText("");
                setPromptText(number + "");
                displayed = false;
            } else {
                this.setText(number + "");
                displayed = true;
            }
        }

    }

}
