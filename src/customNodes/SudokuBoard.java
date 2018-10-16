package customNodes;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Screen;

public class SudokuBoard extends Region {

    private static final int SUB_BLOCK_GAP = 12;
    private static final int FIELD_GAP = 5;
    private static final int ROOT_PADDING = 7;


    private int length;
    private int blockLength;
    private boolean allowNotes;

    private GridPane root;


    /**
     * board for convenient access via standard i and j coordinates onto the fields
     */
    private SudokuField[][] board;
    private ChangeListener resizeHandler;

    public SudokuBoard(int length, boolean allowNotes) {
        this.length = length;
        this.blockLength = (int) Math.sqrt(length);
        this.allowNotes = allowNotes;

        //mouseHandler = new FieldChangeListener();
        resizeHandler = new ResizeHandler();
        widthProperty().addListener(resizeHandler);
        heightProperty().addListener(resizeHandler);

        double placeForGaps = 2 * ROOT_PADDING + (length - blockLength) * FIELD_GAP + (blockLength - 1) * SUB_BLOCK_GAP;
        this.setMinWidth(length * SudokuField.MIN_SIZE + placeForGaps);
        this.setMinHeight(length * SudokuField.MIN_SIZE + placeForGaps);

        buildUp();
    }

    private void buildUp() {
        board = new SudokuField[length][length];
        this.getChildren().clear();

        root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(ROOT_PADDING));

        for (int iBlock = 0; iBlock < blockLength; iBlock++) {
            for (int jBlock = 0; jBlock < blockLength; jBlock++) {
                GridPane subBlock = new GridPane();
                subBlock.setHgap(FIELD_GAP);
                subBlock.setVgap(FIELD_GAP);
                subBlock.setPadding(new Insets(SUB_BLOCK_GAP / 2.0));

                for (int i = 0; i < blockLength; i++) {
                    for (int j = 0; j < blockLength; j++) {
                        int globalI = iBlock * blockLength + i;
                        int globalJ = jBlock * blockLength + j;
                        SudokuField field = new SudokuField(globalI, globalJ, length, allowNotes);

                        board[globalI][globalJ] = field;
                        subBlock.add(field, i, j);
                    }
                }
                root.add(subBlock, iBlock, jBlock);
            }
        }
        this.getChildren().add(root);
    }


    public void setLength(int length) {
        this.length = length;
        this.blockLength = (int) Math.sqrt(length);
        buildUp();
    }


    /**
     * Used to listen to changes of the width and height property of the SudokuBoard.
     */
    private class ResizeHandler implements ChangeListener {

        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            double availableSpace = Math.min(getWidth(), getHeight());
            int spaceForGaps = (length - blockLength) * FIELD_GAP + (blockLength - 1) * SUB_BLOCK_GAP + 2 * ROOT_PADDING;
            int whyDoINeedThis = 20;

            // take the screen height into account
            availableSpace = Math.min(availableSpace, Screen.getPrimary().getVisualBounds().getHeight());
            for (SudokuField[] rows : board) {  // maybe they are columns instead of rows. It's unimportant here.
                for (SudokuField field : rows) {
                    double newFieldSize = (availableSpace - spaceForGaps - whyDoINeedThis) / length;
                    field.setPrefSize(newFieldSize, newFieldSize);
                }
            }
        }
    }


}


