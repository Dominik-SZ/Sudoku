package javaFXGUI.customNodes;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Screen;

import java.util.ArrayList;

public class SudokuBoard extends Region {

    public static final int SUB_BLOCK_GAP = 12;
    public static final int FIELD_GAP = 5;
    public static final int ROOT_PADDING = 7;


    private int length;
    private int blockLength;
    private boolean allowNotes;

    private GridPane root;
    /**
     * board for convenient access via standard i and j coordinates onto the fields
     */
    private SudokuField[][] board;

    private SudokuBoardModel viewModel;


    public SudokuBoard(int length, boolean allowNotes) {
        this.length = length;
        this.blockLength = (int) Math.sqrt(length);
        this.allowNotes = allowNotes;
        this.viewModel = new SudokuBoardModel(length);

        ChangeListener resizeHandler = new ResizeListener();
        widthProperty().addListener(resizeHandler);
        heightProperty().addListener(resizeHandler);

        double placeForGaps = 2 * ROOT_PADDING + (length - blockLength) * FIELD_GAP + (blockLength - 1) * SUB_BLOCK_GAP;
        this.setMinWidth(length * SudokuField.MIN_SIZE + placeForGaps);
        this.setMinHeight(length * SudokuField.MIN_SIZE + placeForGaps);

        buildUp();
    }

    /**
     * Change the length of this displayed SudokuBoard. Note that the interior fields are newly generated by doing so.
     *
     * @param length The new length for this SudokuBoard
     */
    public void changeLength(int length) {
        this.length = length;
        this.blockLength = (int) Math.sqrt(length);
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
                        SudokuField field = new SudokuField(length, allowNotes);

                        // connect this field to the ViewModel
                        viewModel.isValueFieldProperty(globalI, globalJ).bindBidirectional(field.isValueFieldProperty());
                        viewModel.valueProperty(globalI, globalJ).bindBidirectional(field.valueProperty());
                        ArrayList<BooleanProperty> modelNotes = viewModel.notesProperty(globalI, globalJ);
                        ArrayList<BooleanProperty> fieldNotes = field.notesProperty();
                        for (int k = 0; k < length; k++) {
                            modelNotes.get(k).bindBidirectional(fieldNotes.get(k));
                        }

                        board[globalI][globalJ] = field;
                        subBlock.add(field, j, i);
                    }
                }
                root.add(subBlock, jBlock, iBlock);
            }
        }
        this.getChildren().add(root);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Getter:
    public SudokuBoardModel getViewModel() {
        return viewModel;
    }

    public int getLength() {
        return this.length;
    }

    public boolean notesAllowed() {
        return this.allowNotes;
    }

    /**
     * Used to listen to changes of the width and height property of the SudokuBoard.
     */
    private class ResizeListener implements ChangeListener<Number> {

        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
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


