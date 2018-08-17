package controller;


import swingGUI.PromptDialog;
import swingGUI.SudokuGUI;
import model.Sudoku;

public class SudokuMain {

    /**
     * Starts a prompt to the user which length and difficulty is desired. The standard values in those checkboxes
     * can be committed by putting them in the args parameter like this: Main.main(new String[]{lengthString,
     * difficultyString});. Otherwise the standard values are 9 and 5. Afterwards a Sudoku Object is generated with
     * the parameters inserted by the user and immediately filled. Now a SudokuGUI Object is generated and
     * displayed to allow for the user to play it. In this User Interface are a lot more features available for the
     * user like printing the Sudoku or saving it.
     *
     * @param args  Desired length and difficulty
     */
    public static void main(String[] args) {
        int[] optionsArray = new int[3];
        optionsArray[2] = -1;

        PromptDialog dialog;
        if (args.length == 2) {
            String length = PromptDialog.checkLength(args[0]);
            String difficulty = PromptDialog.checkDifficulty(args[1]);

            // no errors were returned by the checks
            if (length != null && difficulty != null) {
                dialog = new PromptDialog(length, difficulty, optionsArray);
                dialog.setVisible(true);
            } else {
                System.err.println("illicit parameters inserted: " + args[0] + "  " + args[1]);
                System.exit(-1);
            }

        } else {
            // open standard dialog
            dialog = new PromptDialog("", "", optionsArray);
            dialog.setVisible(true);
        }

        // the user starts a new Sudoku
        if (optionsArray[2] == 0) {
            Sudoku sudoku = buildSudoku(optionsArray[0], optionsArray[1]);
            buildSwingGUI(sudoku);
        }
        // the user inserts an own Sudoku
        if(optionsArray[2] == 1) {
            SudokuGUI gui = new SudokuGUI(optionsArray[0]);
            gui.setVisible(true);
        }
    }

    /**
     * Creates a new sudoku object, which becomes filled.
     *
     * @param length    The length of the new sudoku
     * @param difficulty    The difficulty of the new sudoku
     * @return The sudoku built
     */
    public static Sudoku buildSudoku(int length, int difficulty) {
        System.out.println("generate new Sudoku with length: " + length + " and difficulty: " + difficulty);
        Sudoku sudoku = new Sudoku(length, difficulty);
        sudoku.fill();
        return sudoku;
    }

    /**
     * Builds a SwingGUI to the inserted filled sudoku object.
     *
     * @param sudoku    The sudoku to display in the new GUI
     */
    private static void buildSwingGUI(Sudoku sudoku) {
        SudokuGUI window = new SudokuGUI(sudoku);
        window.setVisible(true);
    }
}