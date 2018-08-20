package javaFXGUI.controller;

import javaFXGUI.view.MainMenuView;
import javafx.stage.Stage;

public class MainMenuController {

    private MainMenuView view;
    private Stage stage;

    public MainMenuController(MainMenuView view, Stage stage) {
        this.view = view;
        this.stage = stage;
    }
}
