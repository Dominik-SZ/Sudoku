package customNodes;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SudokuBoardTest extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        SudokuBoard board = new SudokuBoard(9, true);
        Scene scene = new Scene(board, 800, 800);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}