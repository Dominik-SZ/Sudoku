package javaFXGUI.view;

import com.sun.javafx.collections.ImmutableObservableList;
import javaFXGUI.controller.OwnSudokuController;
import javaFXGUI.customNodes.SudokuBoard;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class OwnSudokuView {

    private static final int DEFAULT_WIDTH = 700;
    private static final int DEFAULT_HEIGHT = 600;


    private Scene scene;
    private BorderPane root;
    private SudokuBoard sudokuBoard;

    private ChoiceBox<Integer> lengthSelectionBox;

    public OwnSudokuView() {
        root = new BorderPane();

        buildLeft();
        sudokuBoard = new SudokuBoard(9, false);
        root.setCenter(sudokuBoard);

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
        sudokuBoard.changeLength(length);
    }
}
