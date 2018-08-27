package javaFXGUI.view;

import javaFXGUI.controller.GameViewController;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;

public class GameView {

    private static final int DEFAULT_WIDTH = 700;
    private static final int DEFAULT_HEIGHT = 600;
    private Scene scene;

    public GameView() {
        BorderPane root = new BorderPane();

        MenuBar menuBar = new MenuBar();

        Menu menuFile = new Menu("Datei");
        menuBar.getMenus().add(menuFile);

        MenuItem menuItemSave = new MenuItem("Speichern");
        menuItemSave.setAccelerator(KeyCombination.valueOf("Ctrl+S"));

        MenuItem menuItemLoad = new MenuItem("Laden");
        menuItemLoad.setAccelerator(KeyCombination.valueOf("Ctrl+L"));

        MenuItem menuItemPrint = new MenuItem("Drucken");
        menuItemPrint.setAccelerator(KeyCombination.valueOf("Ctrl+P"));

        SeparatorMenuItem separator = new SeparatorMenuItem();

        MenuItem menuItemExit = new MenuItem("Beenden");
        menuItemExit.setAccelerator(KeyCombination.valueOf("Ctrl+Q"));
        menuFile.getItems().addAll(menuItemLoad, menuItemPrint, menuItemSave, separator,
                menuItemExit);


        Menu menuView = new Menu("Hilfe");
        menuBar.getMenus().add(menuView);

        MenuItem menuItemCheckErrors = new MenuItem("Fehlerprüfung");
        menuItemCheckErrors.setAccelerator(KeyCombination.valueOf("Ctrl+F"));

        menuView.getItems().addAll(menuItemCheckErrors);


        root.setTop(menuBar);
        scene = new Scene(root, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public void connectHandlers(GameViewController controller) {

    }

    public Scene getScene() {
        return this.scene;
    }
}
