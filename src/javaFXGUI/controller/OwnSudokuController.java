package javaFXGUI.controller;

import javaFXGUI.view.OwnSudokuView;
import javafx.event.ActionEvent;
import javafx.stage.Stage;


public class OwnSudokuController {

    private Stage stage;
    private OwnSudokuView view;

    public OwnSudokuController(Stage stage, OwnSudokuView view) {
        this.stage = stage;
        this.view = view;
    }

    public void handleLengthSelection(ActionEvent event) {
        view.setLength(view.getLengthSelected());
    }
}
