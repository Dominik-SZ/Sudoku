package javaFXGUI.controller;

import javaFXGUI.view.GameView;
import javafx.stage.Stage;

public class GameViewController {

    Stage stage;
    GameView view;

    public GameViewController(Stage stage, GameView view) {
        this.stage = stage;
        this.view = view;
    }

}
