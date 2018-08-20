package javaFXGUI;

import javaFXGUI.controller.MainMenuController;
import javaFXGUI.view.MainMenuView;
import javafx.application.Application;
import javafx.stage.Stage;

public class SudokuJavaFXMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        MainMenuView view = new MainMenuView();
        MainMenuController controller = new MainMenuController(view, primaryStage);
        view.connectHandlers(controller);

        primaryStage.setTitle("Hauptmenü");
        primaryStage.setScene(view.getScene());
        primaryStage.show();
    }
}
