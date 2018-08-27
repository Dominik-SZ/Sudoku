package javaFXGUI;

import javaFXGUI.controller.MainMenuController;
import javaFXGUI.view.MainMenuView;
import javafx.application.Application;
import javafx.scene.image.Image;
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
        primaryStage.getIcons().add(new Image(SudokuJavaFXMain.class.getClassLoader().getResourceAsStream("icons/sudokuIcon128x128.png")));
        primaryStage.setScene(view.getScene());
        primaryStage.show();
    }
}
