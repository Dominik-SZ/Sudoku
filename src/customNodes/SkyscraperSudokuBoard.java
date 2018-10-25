package customNodes;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

public class SkyscraperSudokuBoard extends Region {

    private int length;
    private boolean allowNotes;
    private SudokuBoard innerBoard;

    SkyscraperSudokuBoard(SudokuBoard innerBoard) {
        this.innerBoard = innerBoard;
        this.length = innerBoard.getLength();
        this.allowNotes = innerBoard.notesAllowed();

        GridPane root = new GridPane();
        root.setGridLinesVisible(true);
        root.add(innerBoard, 1, 1, length, length);

        SkyscraperSideBar top = new SkyscraperSideBar(SkyscraperSideBar.Layout.HORIZONTAL, length, true);
        root.add(top, 1, 0, length, 1);
        SkyscraperSideBar right = new SkyscraperSideBar(SkyscraperSideBar.Layout.VERTICAL, length, true);
        root.add(right, length + 1, 1, 1, length);
        SkyscraperSideBar bot = new SkyscraperSideBar(SkyscraperSideBar.Layout.HORIZONTAL, length, true);
        root.add(bot, 1, length + 1, length, 1);
        SkyscraperSideBar left = new SkyscraperSideBar(SkyscraperSideBar.Layout.VERTICAL, length, true);
        root.add(left, 0, 1, 1, length);
        getChildren().add(root);
    }
}
