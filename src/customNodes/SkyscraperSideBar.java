package customNodes;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class SkyscraperSideBar extends Region {

    private Layout layout;
    private int length;

    SkyscraperSideBar(Layout layout, int length, boolean allowNotes) {
        this.layout = layout;
        this.length = length;

        Pane bar;
        if (layout == Layout.HORIZONTAL) {
            bar = new HBox();
        } else (layout == Layout.VERTICAL) {
            bar = new VBox();
        }

        for (int i = 0; i < length; i++) {
            bar.getChildren().add(new SudokuField(length, allowNotes));
        }
    }

    private enum Layout {
        VERTICAL, HORIZONTAL
    }
}
