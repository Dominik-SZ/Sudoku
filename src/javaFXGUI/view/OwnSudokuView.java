package javaFXGUI.view;

import com.sun.javafx.collections.ImmutableObservableList;
import javaFXGUI.controller.OwnSudokuController;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class OwnSudokuView {

    private static final int DEFAULT_WIDTH = 700;
    private static final int DEFAULT_HEIGHT = 600;
    private static final int SUB_BLOCK_GAP = 5;
    private static final int FIELD_GAP = 5;
    private static final int CENTER_PADDING = 7;

    private Scene scene;
    private BorderPane root;

    private int length = 9;
    private int blockLength = 3;
    private GridPane centerRoot;
    private ChoiceBox<Integer> lengthSelectionBox;

    public OwnSudokuView() {
        root = new BorderPane();
        buildLeft();
        buildCenter(null);

        scene = new Scene(root, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    private void buildLeft() {
        GridPane leftRoot = new GridPane();
        leftRoot.setHgap(20);
        leftRoot.setPadding(new Insets(10));

        Text lengthsLabel = new Text("Sudokulänge");
        leftRoot.add(lengthsLabel, 0, 0);

        lengthSelectionBox = new ChoiceBox<>();
        lengthSelectionBox.setItems(new ImmutableObservableList<>(4, 9, 16));
        lengthSelectionBox.setValue(9);
        lengthSelectionBox.setPrefSize(40, 30);
        leftRoot.add(lengthSelectionBox, 1, 0);

        root.setLeft(leftRoot);
    }

    public void buildCenter(ActionEvent event) {
        centerRoot = new GridPane();
        centerRoot.setAlignment(Pos.CENTER);
        centerRoot.setPadding(new Insets(CENTER_PADDING));

        for (int iBlock = 0; iBlock < blockLength; iBlock++) {
            for (int jBlock = 0; jBlock < blockLength; jBlock++) {
                GridPane subBlock = new GridPane();
                subBlock.setHgap(FIELD_GAP);
                subBlock.setVgap(FIELD_GAP);
                subBlock.setPadding(new Insets(SUB_BLOCK_GAP));

                for (int i = 0; i < blockLength; i++) {
                    for (int j = 0; j < blockLength; j++) {
                        TextField field = new TextField();
                        field.setPrefSize(40, 40);
                        subBlock.add(field, i, j);
                    }
                }
                centerRoot.add(subBlock, iBlock, jBlock);
            }
        }

        root.setCenter(centerRoot);
    }

    public void connectHandlers(OwnSudokuController controller) {
        lengthSelectionBox.setOnAction(controller::handleLengthSelection);
    }

    public Scene getScene() {
        return this.scene;
    }

    public int getLengthSelected() {
        return this.lengthSelectionBox.getValue();
    }

    public void setLength(int length) {
        this.length = length;
        this.blockLength = (int) Math.sqrt(length);
    }

}
