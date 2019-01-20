package javaFXGUI.customNodes;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class SudokuBoardTest extends Application {

    private int length = 9;
    private SudokuBoardModel viewModel;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Debug mode");
        SudokuBoard board = new SudokuBoard(length, true);
        viewModel = board.getViewModel();

        // wrap the board into additional functionality
        BorderPane root = new BorderPane();
        root.setCenter(board);
        VBox buttonBox = new VBox();
        root.setLeft(buttonBox);
        Button printIsValueFieldsButton = new Button("Print isValueField data");
        printIsValueFieldsButton.setOnAction(this::printIsValueFields);
        Button printValuesButton = new Button("Print values");
        printValuesButton.setOnAction(this::printValues);
        Button printNotesButton = new Button("Print notes");
        printNotesButton.setOnAction(this::printNotes);
        buttonBox.getChildren().addAll(printIsValueFieldsButton, printValuesButton, printNotesButton);

        Scene scene = new Scene(root, 1000, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void printIsValueFields(ActionEvent event) {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                System.out.print(viewModel.isValueFieldProperty(i, j).get() + "  ");
            }
            System.out.println();
        }
        System.out.print("\r\n\r\n");
    }

    private void printValues(ActionEvent event) {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                System.out.print(viewModel.valueProperty(i, j).get() + "  ");
            }
            System.out.println();
        }
        System.out.print("\r\n\r\n");
    }

    private void printNotes(ActionEvent event) {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                ArrayList<BooleanProperty> fieldsNotes = viewModel.notesProperty(i, j);
                for (int k = 0; k < length; k++) {
                    if (fieldsNotes.get(k).get()) {
                        System.out.print((k + 1) + " ");
                    }
                }
                System.out.print("| ");
            }
            System.out.print("\r\n");
        }
        System.out.print("\r\n\r\n");
    }
}