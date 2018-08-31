package javaFXGUI.controller;

import javaFXGUI.view.GameView;
import javaFXGUI.view.MainMenuView;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class MainMenuController {

    private MainMenuView view;
    private Stage stage;

    public MainMenuController(MainMenuView view, Stage stage) {
        this.view = view;
        this.stage = stage;
    }

    public void handleNewGame(ActionEvent event) {
        GameView newView = new GameView();
        GameViewController newController = new GameViewController(stage, newView);
        newView.connectHandlers(newController);

        stage.setScene(newView.getScene());
        stage.setTitle("Sudoku");
    }

    public void handleLoad(ActionEvent event) {

    }

    public void handleOwnSudoku(ActionEvent event) {

    }

    public void handleClose(ActionEvent event) {
        stage.close();
        System.exit(0);
    }
}
