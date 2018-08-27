package javaFXGUI.view;

import javaFXGUI.controller.MainMenuController;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;

public class MainMenuView {

    private static final int DEFAULT_WIDTH = 350;
    private static final int DEFAULT_HEIGHT = 500;

    private Button newGameButton;
    private Button loadGameButton;
    private Button ownSudokuButton;
    private Button closeGameButton;


    private Scene scene;

    public MainMenuView() {
        GridPane grid = new GridPane();
        grid.setVgap(30);
        grid.getStyleClass().add("styles/MainMenuStyle.css");
        grid.setAlignment(Pos.CENTER);

        newGameButton = new Button("Neues Spiel");
        grid.add(newGameButton, 0, 0);
        GridPane.setHalignment(newGameButton, HPos.CENTER);
        newGameButton.setTooltip(new Tooltip("Starte ein neu generiertes Sudoku"));

        loadGameButton = new Button("Spiel Laden");
        grid.add(loadGameButton, 0, 1);
        GridPane.setHalignment(loadGameButton, HPos.CENTER);
        loadGameButton.setTooltip(new Tooltip("Lade einen gespeicherten Sudoku Spielstand"));

        ownSudokuButton = new Button("Eigenes Sudoku");
        grid.add(ownSudokuButton, 0, 2);
        GridPane.setHalignment(ownSudokuButton, HPos.CENTER);
        ownSudokuButton.setTooltip(new Tooltip("Gebe ein eigenes Sudoku ein und bearbeite es hier"));

        closeGameButton = new Button("Beenden");
        grid.add(closeGameButton, 0, 3);
        GridPane.setHalignment(closeGameButton, HPos.CENTER);
        closeGameButton.setTooltip(new Tooltip("Beende das Programm"));

        scene = new Scene(grid, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("styles/MainMenuStyle.css").toExternalForm());
    }

    public void connectHandlers(MainMenuController controller) {
        newGameButton.setOnAction(controller::handleNewGame);
        loadGameButton.setOnAction(controller::handleLoad);
        ownSudokuButton.setOnAction(controller::handleLoad);
        closeGameButton.setOnAction(controller::handleClose);
    }

    public Scene getScene() {
        return this.scene;
    }
}
