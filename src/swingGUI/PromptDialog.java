package swingGUI;

import java.awt.Dialog;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class PromptDialog extends JDialog {
    @SuppressWarnings("unused")
    private int[] returnArray;
    private JPanel mainPanel;
    private JTextField lengthInput;
    private JTextField difficultyInput;

    /**
     * Builds a prompt dialog, which includes two labeled text fields in which a user can enter his desired length
     * and difficulty. The user can push one of three buttons: start, load or cancel. When start is clicked, the
     * inserted values for length (returnArray[0]) and difficulty (returnArray[1]), as well as the error code 0
     * (returnArray[2]) are written into the returnArray, which functions kind of as a return object.
     * If load is clicked, a file browser opens in which the user can choose a file, which gets loaded. If the
     * loading process was successful, -2 is written into the returnArray[2] indicating that the dialog took care of
     * the further process and the dialog is closed. The third option is to insert an own Sudoku by clicking the
     * ownSudokuButton. This returns the error code 1 in returnArray[2]. The fourth option is to click the cancel
     * button. When doing that, -1s are written into the returnArray[2] indicating that the method returned an error
     * value. The program should stop after detecting this.
     *
     * @param suggestLength     The suggested length to pre enter into the lengthField
     * @param suggestDifficulty The suggested difficulty to pre enter into the difficultyField
     * @param returnArray       The array in which the return values are written
     */
    public PromptDialog(String suggestLength, String suggestDifficulty, int[] returnArray) {

        // initialise the startListener early so it can be attached for enter presses in the text fields
        ActionListener startListener = arg0 -> {
            String length = checkLength(lengthInput.getText());
            String difficulty = checkDifficulty(difficultyInput.getText());

            if (length != null && difficulty != null) {
                returnArray[0] = Integer.parseInt(lengthInput.getText());
                returnArray[1] = Integer.parseInt(difficultyInput.getText());
                returnArray[2] = 0; // no error code
                dispose();
            }

        };

        this.returnArray = returnArray;
        setBounds(0, 0, 600, 250);
        setTitle("Starte ein Sudoku");
        setLocationRelativeTo(null); // center the frame
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        mainPanel = new JPanel(); // the mainPanel
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        add(mainPanel);

        // generate the noteRows
        JLabel noteRow1 = new JLabel("Bitte geben Sie eine Quadratzahl als Länge ein, welche nicht größer " + "als 16 ist");
        JLabel noteRow2 = new JLabel("und einen Schwierigkeitsgrad von 1 bis 10.");
        JLabel noteRow3 = new JLabel(" "); // third row to have a separating
        // empty line
        mainPanel.add(noteRow1);
        mainPanel.add(noteRow2);
        mainPanel.add(noteRow3);

        // generate the lengthRow
        JPanel lengthRow = new JPanel();
        mainPanel.add(lengthRow);

        JLabel lengthLabel = new JLabel("Länge: ");
        lengthRow.add(lengthLabel);
        lengthLabel.setSize(10, lengthLabel.getHeight());

        lengthInput = new JTextField(10);
        lengthRow.add(lengthInput);
        String lengthFieldContent = checkLength(suggestLength);
        lengthInput.setText(lengthFieldContent);
        lengthInput.setSize(30, lengthInput.getHeight());
        lengthInput.addActionListener(startListener);

        // generate the difficultyRow
        JPanel difficultyRow = new JPanel();
        mainPanel.add(difficultyRow);

        JLabel difficultyLabel = new JLabel("Schwierigkeitsgrad: ");
        difficultyRow.add(difficultyLabel);

        difficultyInput = new JTextField(10);
        String difficultyFieldContent = checkDifficulty(suggestDifficulty);
        difficultyInput.setText(difficultyFieldContent);
        difficultyInput.addActionListener(startListener);
        difficultyRow.add(difficultyInput);

        // generate confirmRow
        JPanel confirmRow = new JPanel();
        mainPanel.add(confirmRow);

        // start button
        JButton startButton = new JButton("Start");
        confirmRow.add(startButton);
        startButton.setToolTipText("Startet ein neues Sudoku Spiel mit den momentan eingegebenen Parametern Länge und" +
                                           " Schwierigkeit");
        startButton.addActionListener(startListener);

        // load button
        JButton loadButton = new JButton("Lade");
        confirmRow.add(loadButton);
        loadButton.setToolTipText("Lädt einen vorher gespeicherten Sudoku Spielstand");
        loadButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int loadFeedback = chooser.showDialog(mainPanel, "Wähle Speicherdatei");

            String path;
            if (loadFeedback == JFileChooser.APPROVE_OPTION) {
                path = chooser.getSelectedFile().getAbsolutePath();

                if (FileIO.load(path)) {
                    returnArray[2] = -2;
                    dispose();
                }

            }
        });

        // ownSudoku button
        JButton ownSudokuButton = new JButton("Eigenes Sudoku");
        confirmRow.add(ownSudokuButton);
        ownSudokuButton.setToolTipText("Ermöglicht das Eintragen eines eigenen Sudokus beispielsweise aus einem " + "Rätselheft");
        ownSudokuButton.addActionListener(e -> {
            String length = checkLength(lengthInput.getText());
            String difficulty = checkDifficulty(difficultyInput.getText());

            if (length != null && difficulty != null) {
                returnArray[0] = Integer.parseInt(lengthInput.getText());
                returnArray[1] = Integer.parseInt(difficultyInput.getText());
                returnArray[2] = 1;
                dispose();
            }
        });

        // cancel button
        JButton cancelButton = new JButton("Abbrechen");
        confirmRow.add(cancelButton);
        cancelButton.setToolTipText("Beendet das Programm ohne etwas zu tun");
        cancelButton.addActionListener(e -> {
            returnArray[2] = -1;
            dispose();
        });

        mainPanel.add(confirmRow);

    }

    /**
     * Checks if the suggested length is a number and if it is a square number
     * greater 1. If its not, null is returned. If the suggested length is the
     * empty String, the standard value of 9 is returned.
     *
     * @param suggestLength The length which should be corrected
     * @return The corrected length as String. 9 if empty, null if illicit and the suggested length otherwise.
     */
    public static String checkLength(String suggestLength) {
        String returnString = suggestLength;
        int number;

        if (suggestLength.equals("")) {
            // if the field is empty, use the standard value
            returnString = "9";
        } else {
            try {
                // check if the suggestLength is a number
                number = Integer.parseInt(suggestLength);

                if (Math.sqrt(number) % 1 != 0 || number > 16) {
                    JOptionPane.showMessageDialog(null, "Unerlaubte Länge eingegeben. Sie ist keine " + "Quadratzahl und/oder zu groß:   " + number);
                    returnString = null; // input illicit: no square number or
                    // too big
                }

                if (number == 1) {
                    JOptionPane.showMessageDialog(null, "Bitte keine 1 als Länge eingeben.");
                    returnString = null; // input illicit: 1
                }
            } catch (NumberFormatException ex) {
                // input illicit: no number
                JOptionPane.showMessageDialog(null, "Unerlaubte Länge eingegeben. Länge ist keine Zahl:   " + suggestLength);
                returnString = null;
            }
        }
        return returnString;
    }

    /**
     * Checks if the suggested difficulty is a number and if it is inside the
     * allowed bounds (1-10). If its not, null is returned. If the suggested
     * difficulty is the empty String, the standard value of 5 is returned.
     *
     * @param suggestDifficulty The difficulty which should be corrected
     * @return The corrected difficulty as String. 5 if empty, null if illicit and the suggested difficulty otherwise.
     */
    public static String checkDifficulty(String suggestDifficulty) {
        String returnString = suggestDifficulty; // standard value
        int number;

        if (suggestDifficulty.equals("")) {
            // if the field is empty, use the standard value
            returnString = "5";
        } else {
            // check if the suggestDifficulty is a number
            try {
                number = Integer.parseInt(suggestDifficulty);

                if (number > 10 || number < 1) {
                    JOptionPane.showMessageDialog(null, "Unerlaubte Schwierigkeit eingegeben. Schwierigkeit ist nicht " + "innerhalb des erlaubten Bereichs (1-10):   " + number + ".");
                    returnString = null; // input illicit: out of bounds
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Unerlaubte Schwierigkeit eingegeben. Schwierigkeit ist keine Zahl:   " + suggestDifficulty);
                returnString = null; // input illicit: no number
            }
        }
        return returnString;
    }

}