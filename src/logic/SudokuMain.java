package logic;


import gui.PromptDialog;
import gui.SudokuGUI;

public class SudokuMain {

	/**
	 * Starts a prompt to the user which length and difficulty is desired. The
	 * standard values in those checkboxes can be committed by putting them in
	 * the args parameter like this: Main.main(new String[]{lengthString,
	 * difficultyString});. Otherwise the standard values are 9 and 5.
	 * Afterwards a Sudoku Object is generated with the parameters inserted by
	 * the user and immediately filled. Now a SudokuGUI Object is generated and
	 * displayed to allow for the user to play it. In this User Interface are a
	 * lot more features available for the user like printing the Sudoku or
	 * saving it.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		int[] optionsArray = new int[3];
		PromptDialog dialog;
		if (args.length == 2) {
			String length = PromptDialog.checkLength(args[0]);
			String difficulty = PromptDialog.checkDifficulty(args[1]);
			
			// no errors were returned by the checks
			if(length != null && difficulty != null){
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
		
		// the user did not cancel or load
		if(optionsArray[2] != -1 && optionsArray[2] != -2){
			run(optionsArray[0], optionsArray[1]);
		}
	}

	public static void run(int length, int difficulty) {

		System.out.println("generate new Sudoku");
		Sudoku Sudoku = new Sudoku(length, difficulty);
		Sudoku.fill();
		
		SudokuGUI window = new SudokuGUI(Sudoku);
		window.setVisible(true);
	}
}