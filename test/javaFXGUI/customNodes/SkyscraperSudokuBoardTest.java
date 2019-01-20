package javaFXGUI.customNodes;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SkyscraperSudokuBoardTest extends Application {

    private int length = 9;
    private boolean allowNotes = true;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("SkyscraperSudokuBoard Test");

        SkyscraperSudokuBoard skyscraperBoard = new SkyscraperSudokuBoard(new SudokuBoard(length, allowNotes));
        Scene scene = new Scene(skyscraperBoard, 800, 800);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
