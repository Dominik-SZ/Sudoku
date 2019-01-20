package javaFXGUI.customNodes;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;


class ValueField extends Region {

    private final IntegerProperty value;
    private int length;
    private TextField field;

    ValueField(int length) {
        this(length, "");
    }

    /**
     * Creates a new ValueField, that only accepts numbers from 1-length as input.
     *
     * @param length         The length of the containing SudokuBoard
     * @param initialContent The initial text content of the field
     */
    ValueField(int length, String initialContent) {
        this.length = length;
        this.value = new SimpleIntegerProperty();
        field = new TextField();
        field.setText(initialContent);
        field.setAlignment(Pos.CENTER);
        field.setMinSize(this.getMinWidth(), this.getMinHeight());
        field.setPrefSize(this.getPrefWidth(), this.getPrefHeight());
        field.setMaxSize(this.getMaxWidth(), this.getMaxHeight());
        //field.setBackground(new Background(new BackgroundFill(Color.SKYBLUE, new CornerRadii(5), new Insets(0))));
        //field.setBorder(new Border(new BorderStroke(Color.SPRINGGREEN, new BorderStrokeStyle())));

        ResizeHandler resizeHandler = new ResizeHandler();
        widthProperty().addListener(resizeHandler);
        heightProperty().addListener(resizeHandler);

        addEventFilter(KeyEvent.KEY_TYPED, new KeyHandler());

        getChildren().add(field);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Getter:
    public int getValue() {
        return Integer.parseInt(field.getText());
    }

    IntegerProperty valueProperty() {
        return value;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Setter:
    public void setText(String text) {
        field.setText(text);
    }

    /**
     * Makes sure only legit numbers (1 to length) are inserted into this field. The value property is also set to
     * the new value.
     */
    private class KeyHandler implements EventHandler<KeyEvent> {

        @Override
        public void handle(KeyEvent event) {
            if (event.getCharacter().matches("\\d+") && !event.getCharacter().equals("0")) {

                String leftOfCaret = field.getText().substring(0, field.getCaretPosition());
                String rightOfCaret = field.getText().substring(field.getCaretPosition());
                String newText = leftOfCaret + event.getCharacter() + rightOfCaret;

                if (!newText.equals("")) {
                    if (Integer.parseInt(newText) > length) {
                        field.clear();
                        value.set(Integer.parseInt(event.getCharacter()));
                    } else {
                        value.set(Integer.parseInt(newText));
                    }
                }


            } else {
                // not a number inserted
                event.consume();
            }
        }

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

}
