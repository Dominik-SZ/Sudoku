package customNodes;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

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

    SudokuField(int i, int j, int length, boolean allowNotes) {
        this.i = i;
        this.j = j;
        this.length = length;
        this.allowNotes = allowNotes;
        this.child = new ValueField(length);
        this.getChildren().add(child);
        this.setMinSize(MIN_SIZE, MIN_SIZE);
        this.setPrefSize(PREF_SIZE, PREF_SIZE);

        this.widthProperty().addListener((observable, oldValue, newValue) -> adjustPrefSizeOfChild());
        this.heightProperty().addListener((observable, oldValue, newValue) -> adjustPrefSizeOfChild());

        this.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (this.allowNotes && event.getButton().equals(MouseButton.SECONDARY)) {    // right click
                toggleFieldType();
            }
        });
    }


    private void toggleFieldType() {
        if (child.getClass().equals(ValueField.class)) {
            child = new NoteField(length);
            adjustPrefSizeOfChild();
            getChildren().clear();
            getChildren().add(child);
        } else {
            child = new ValueField(length);
            adjustPrefSizeOfChild();
            getChildren().clear();
            getChildren().add(child);
        }
    }

    public void adjustPrefSizeOfChild() {
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

    // -----------------------------------------------------------------------------------------------------------------
    // Setter:
    public void setToggleable(boolean toggleable) {
        this.allowNotes = toggleable;
    }

}
