package customNodes;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

class ValueField extends Region {

    private int length;
    private TextField field;

    ValueField(int length) {
        this(length, "");
    }

    /**
     * @param length         The length of the containing SudokuBoard
     * @param initialContent The initial text content of the field
     */
    ValueField(int length, String initialContent) {
        this.length = length;
        field = new TextField();
        field.setText(initialContent);
        field.setAlignment(Pos.CENTER);
        field.setMinSize(this.getMinWidth(), this.getMinHeight());
        field.setPrefSize(this.getPrefWidth(), this.getPrefHeight());
        field.setMaxSize(this.getMaxWidth(), this.getMaxHeight());

        ResizeHandler resizeHandler = new ResizeHandler();
        this.widthProperty().addListener(resizeHandler);
        this.heightProperty().addListener(resizeHandler);

        addEventFilter(KeyEvent.KEY_TYPED, new KeyHandler());

        this.getChildren().add(field);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Getter:
    public int getValue() {
        return Integer.parseInt(field.getText());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setter:
    public void setText(String text) {
        field.setText(text);
    }

    /**
     * Adjusts the prefSize and the font of the field.
     */
    private class ResizeHandler implements ChangeListener {

        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            field.setPrefSize(getPrefWidth(), getPrefHeight());
            field.setFont(new Font(getPrefHeight() * 0.5 - 5));
        }
    }

    /**
     * Makes sure only legit numbers (1 to length) are inserted into this field.
     */
    private class KeyHandler implements EventHandler<KeyEvent> {

        @Override
        public void handle(KeyEvent event) {
            if (event.getCharacter().matches("\\d+") && !event.getCharacter().equals("0")) {

                String leftOfCaret = field.getText().substring(0, field.getCaretPosition());
                String rightOfCaret = field.getText().substring(field.getCaretPosition());
                String newText = leftOfCaret + event.getCharacter() + rightOfCaret;

                if (!newText.equals("") && Integer.parseInt(newText) > length) {
                    field.clear();
                }

                // System.out.println("number detected");
            } else {
                // System.out.println("non number detected");
                event.consume();
            }
        }

    }
}
