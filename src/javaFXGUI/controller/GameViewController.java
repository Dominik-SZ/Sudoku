package javaFXGUI.controller;

import javaFXGUI.view.GameView;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class GameViewController {

    private Stage stage;
    private GameView view;

    public GameViewController(Stage stage, GameView view) {
        this.stage = stage;
        this.view = view;
    }

    public void handleExit(ActionEvent event) {
        stage.close();
        System.exit(0);
    }

}
