package swingGUI;

import javax.swing.JOptionPane;

import model.Sudoku;

import java.io.*;

public class FileIO {

    /**
     * Saves the important data of the inserted sudokuGUI and its sudoku to the specified path.
     *
     * @param sudoku The associated sudoku object whose data should be saved
     * @param path   The path to which to write the save file
     */
    public static void save(SudokuGUI gui, Sudoku sudoku, String path) {

        String ls = "\r\n"; // line separator
        PrintWriter writer = null;
        try { // checks if the last 7 letters of the path are ".sudoku". If they
            // are not, ".sudoku" is added to the path.
            if (!(path.substring(path.length() - 4).equals(".sud"))) {
                path = path + ".sud";
            }
            writer = new PrintWriter(new FileWriter(new File(path)));
            int fieldAmount = sudoku.getLength() * sudoku.getLength();

            // (line 1) header
            writer.write("Sudoku Savegame" + ls);

            // (line 2) save the length
            writer.write("Length," + sudoku.getLength() + ls);

            // (line 3) save the difficulty
            writer.write("Difficulty," + sudoku.getDifficulty() + ls);

            // (line 4) save the start board of the sudoku
            writer.write("StartBoard,");
            writer.write(String.valueOf(sudoku.getStartValue(0)));
            for (int k = 1; k < fieldAmount; k++) {
                writer.write("," + sudoku.getStartValue(k));
            }
            writer.write(ls);

            // (line 5) save the boardSolved of the sudoku
            writer.write("Solution,");
            writer.write(String.valueOf(sudoku.getSolutionValue(0)));
            for (int k = 1; k < fieldAmount; k++) {
                writer.write("," + sudoku.getSolutionValue(k));
            }
            writer.write(ls);

            // (line 6) save the board filled in by the user (boardGraphic)
            writer.write("UserBoard,");
            for (int i = 0; i < sudoku.getLength(); i++) {
                for (int j = 0; j < sudoku.getLength(); j++) {
                    writer.write(gui.getBoardGraphic(i, j).getText().trim());
                    if (i != sudoku.getLength() && j != sudoku.getLength()) {   // not the last entry
                        writer.write(",");
                    }
                }
            }
            writer.write(ls);

            // (line 7) save the time the user has already spent solving this Sudoku
            writer.write("Time played,");
            writer.print(System.nanoTime() - gui.getStartTime());

            writer.flush();

        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        } finally {
            if (writer != null) {
                writer.close();
            } else {
                System.err.println("BufferedWriter not open");
            }
        }
    }


    /**
     * Loads the saved data from a previous SudokuGUI instance. This data is
     * used to build the previous sduokuGUI again, which is also set visible.
     *
     * @param path The absolute path from where to load
     * @return if the loading process was successful
     */
    static boolean load(String path) {
        BufferedReader reader;
        String[] thisLineArray;
        try {
            if (!(path.substring(path.length() - 4).equals(".sud"))) {
                path = path + ".sud";
            }
            reader = new BufferedReader(new FileReader(new File(path)));

            // (line 1) check header
            if (!reader.readLine().equals("Sudoku Savegame")) {
                System.err.println("Incorrect version. Problems may occur.");
            }

            // (line 2) read length
            int length = Integer.parseInt(reader.readLine().split(",")[1]);

            // (line 3) read difficulty
            int difficulty = Integer.parseInt(reader.readLine().split(",")[1]);

            // build a new Sudoku object based on the length and difficulty read
            Sudoku sudoku = new Sudoku(length, difficulty);

            // (line 4) read the board of the Sudoku
            thisLineArray = reader.readLine().split(",");
            for (int k = 1; k < thisLineArray.length; k++) {
                sudoku.setStartValue(Integer.parseInt(thisLineArray[k]), k - 1);
            }
            // (line 5) read the solution of the Sudoku
            thisLineArray = reader.readLine().split(",");
            for (int k = 1; k < thisLineArray.length; k++) {
                sudoku.setSolutionValue(Integer.parseInt(thisLineArray[k]), k - 1);
            }

            // build the new window with the new Sudoku
            SudokuGUI window = new SudokuGUI(sudoku);

            // (line 6) read the board filled by the user
            thisLineArray = reader.readLine().split(",");
            for (int k = 1; k < thisLineArray.length; k++) {
                window.setBoardGraphic(thisLineArray[k], k - 1);
            }
            // update the current state
            for (int i = 0; i < sudoku.getLength(); i++) {
                for (int j = 0; j < sudoku.getLength(); j++) {
                    if (window.getBoardGraphic(i, j).getFont().equals(window.getStandardFont())) {
                        int currentValue = 0;
                        try {
                            currentValue = Integer.parseInt(window.getBoardGraphic(i, j).getText());
                        } catch (NumberFormatException ignored) {
                        }
                        sudoku.setCurrentValue(currentValue, i, j, true);
                    }
                }
            }
            sudoku.calculatePossibilities();

            // (line 7) load the time already spent trying to solve the sudoku
            thisLineArray = reader.readLine().split(",");
            long additionalTime = Long.valueOf(thisLineArray[1]);

            reader.close();
            window.setVisible(true);
            window.setStartTime(window.getStartTime() - additionalTime);
        } catch (FileNotFoundException e) {
            System.err.println("load: File not found.");
            JOptionPane.showMessageDialog(null, "Datei nutzt das falsche Format! (erlaubt ist .sud)");
            return false;
        } catch (IOException e) {
            System.err.println("load: IOException");
            e.printStackTrace();
            return false;
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException ex) {
            ex.printStackTrace();
            System.err.println("load: File is not written correctly.");
            JOptionPane.showMessageDialog(null, "Datei ist nicht korrekt abgespeichert.");
            return false;
        }
        return true;
    }

}
