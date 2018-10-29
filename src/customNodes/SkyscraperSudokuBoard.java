package customNodes;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

public class SkyscraperSudokuBoard extends Region {

    private int length;
    private boolean allowNotes;

    private SudokuBoard innerBoard;
    private GridPane root;
    /**
     * first index refers to 0 -> top, 1 -> right, 2 -> bot and 3 -> left
     */
    private SudokuField[][] sideFields;

    SkyscraperSudokuBoard(SudokuBoard innerBoard) {
        this.length = innerBoard.getLength();
        this.allowNotes = innerBoard.notesAllowed();

        this.innerBoard = innerBoard;
        this.sideFields = new SudokuField[4][length];

        root = new GridPane();
        getChildren().add(root);
        root.setGridLinesVisible(true);
        root.add(innerBoard, 1, 1, length, length);


        for (int i = 0; i < length; i++) {
            for (int side = 0; side < 4; side++) {
                SudokuField field = new SudokuField(length, allowNotes);
                sideFields[side][i] = field;
                field.setPadding(new Insets(SudokuBoard.FIELD_GAP / 2.0));

                // top:
                if (side == 0) {
                    root.add(field, i + 1, 0);
                }
                // right:
                else if (side == 1) {
                    root.add(field, length + 2, i + 1);
                }
                // bot:
                else if (side == 2) {
                    root.add(field, i + 1, length + 2);
                }
                // left:
                else {
                    root.add(field, 0, i + 1);
                }
            }
        }

        ResizeListener resizer = new ResizeListener();
        widthProperty().addListener(resizer);
        heightProperty().addListener(resizer);

        addEventFilter(MouseEvent.MOUSE_CLICKED, event -> printWidths());
    }

    /**
     * only for debugging purposes
     */
    private void printWidths() {
        System.out.println("this values:");
        System.out.println("pref width: " + this.getPrefWidth());
        System.out.println("actual width: " + this.getWidth());
        System.out.println("min width: " + this.getMinWidth());
        System.out.println("max width: " + this.getMaxWidth());
        System.out.println("root values:");
        System.out.println("pref width: " + root.getPrefWidth());
        System.out.println("actual width: " + root.getWidth());
        System.out.println("min width: " + root.getMinWidth());
        System.out.println("max width: " + root.getMaxWidth());
    }


    private enum Side {
        TOP, RIGHT, BOT, LEFT
    }

    private class ResizeListener implements ChangeListener<Number> {

        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            double availableSpace = Math.min(getWidth(), getHeight());
            root.setPrefSize(availableSpace, availableSpace);

            double fieldSize = (availableSpace - length * SudokuBoard.FIELD_GAP) / (length + 2);
            for (int side = 0; side < 4; side++) {
                for (int i = 0; i < length; i++) {
                    sideFields[side][i].setPrefSize(fieldSize, fieldSize);
                }
            }
        }
    }
}
