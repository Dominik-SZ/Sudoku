package logic;
//package generator;
//
//import java.util.LinkedList;
//import java.util.Stack;
//
//import utilities.Coordinate;
//
//import java.util.HashSet;
//
///**
// * This class provides the strategies used in the trySolving method in the class
// * Sudoku.
// * 
// * @author Dominik
// *
// */
//public class SolvingStrategies {
//	/**
//	 * This Method searches in the board of a sudoku for a field filled with 0
//	 * and only one allowed possibility to fill it. If such a field is found, it
//	 * is filled with this sole possibility and true is returned. If none of the
//	 * fields meet the conditions, false is returned. This method includes the
//	 * so called strategies "Full House/Last Digit" and "Naked Single".
//	 * 
//	 * @param board
//	 *            The board on which another entry is searched
//	 * @param backups
//	 *            The stack of backups in which the found solution should be
//	 *            noted
//	 */
//	static boolean onePossibilityOnField(int[][] board, Stack<BackupPoint> backups) {
//		int length = board.length;
//		LinkedList<Integer> possibilities = new LinkedList<Integer>();
//		for (int i = 0; i < length; i++) { // iterating the rows
//			for (int j = 0; j < length; j++) { // iterating the columns
//				// testing if the field is not filled yet
//				if (board[i][j] == 0) {
//					// testing every possible number
//					for (int k = 1; k <= length; k++) {
//						if (Sudoku.isAllowed(board, k, i, j)) {
//							// saving the allowed ones in a list
//							possibilities.add(k);
//						}
//					}
//					// there has been only one value found fitting
//					if (possibilities.size() == 1) {
//						board[i][j] = possibilities.getFirst();
//						// add the filled coordinate to the trySolvingFills
//						// of the latest backupPoint
//						backups.peek().pushTSFills(i * length + j);
//						return true;
//					}
//					possibilities.clear();
//				}
//			}
//		}
//		return false; // No fitting value has been found
//	}
//
//	/**
//	 * Checks if a possibility for a row is impossible to be placed on all but
//	 * one field. If such a value and field is found, the value gets placed on
//	 * this field and true is returned. If no field and value are found, false
//	 * is returned.
//	 * 
//	 * @param board
//	 *            The board on which another entry is searched
//	 * @param backups
//	 *            The stack of backups in which the found solution should be
//	 *            noted
//	 */
//	static boolean hiddenSingleRow(int[][] board, Stack<BackupPoint> backups) {
//		int length = board.length;
//		for (int i = 0; i < length; i++) { // iterating the rows
//			HashSet<Integer> possibilities = new HashSet<Integer>();
//			for (int k = 1; k <= length; k++) {
//				// fill the set with every number from 1 to length
//				possibilities.add(k);
//			}
//			for (int j = 0; j < length; j++) {
//				// remove already inserted values in this row from the set
//				possibilities.remove(board[i][j]);
//			}
//			// now it contains only possibilities for this row
//
//			// iterating the remaining possibilities of the current row
//			for (Integer k : possibilities) {
//				LinkedList<Coordinate> allowedFieldsForThisNumber = new LinkedList<Coordinate>();
//				for (int j = 0; j < length; j++) { // iterating the row
//					// only search in the empty fields
//					if (board[i][j] == 0 && Sudoku.isAllowed(board, k, i, j)) {
//						// add all possible coordinates of this row for this
//						// number
//						allowedFieldsForThisNumber.add(new Coordinate(i, j));
//					}
//				}
//				if (allowedFieldsForThisNumber.size() == 1) {
//					// set the found value
//					Coordinate coord = allowedFieldsForThisNumber.getFirst();
//					board[coord.getICoord()][coord.getJCoord()] = k;
//					backups.peek().pushTSFills(coord.getICoord() * length + coord.getJCoord());
//					// add the filled coordinate to the try solving fills of
//					// the latest backupPoint
//					return true;
//				}
//			}
//		}
//		return false; // no value has been found
//	}
//
//	/**
//	 * Checks if a possibility for a column is impossible to be placed on all
//	 * but one field. If such a value and field is found, the value gets placed
//	 * on this field and true is returned. If no field and value are found,
//	 * false is returned.
//	 * 
//	 * @param board
//	 *            The board on which another entry is searched
//	 * @param backups
//	 *            The stack of backups in which the found solution should be
//	 *            noted
//	 */
//	static boolean hiddenSingleColumn(int[][] board, Stack<BackupPoint> backups) {
//		int length = board.length;
//		for (int j = 0; j < length; j++) { // iterating the columns
//			HashSet<Integer> possibilities = new HashSet<Integer>();
//			for (int k = 1; k <= length; k++) {
//				possibilities.add(k); // filling the set with every number from
//										// 1-length
//			}
//			for (int i = 0; i < length; i++) {
//				// remove already inserted values in this column from the set
//				possibilities.remove(board[i][j]);
//			} // now it contains only possibilities for this column
//
//			// iterating the remaining possibilities of the current column
//			for (Integer k : possibilities) {
//				LinkedList<Coordinate> allowedFieldsForThisNumber = new LinkedList<Coordinate>();
//				for (int i = 0; i < length; i++) { // iterating the column
//					// only search in the empty fields
//					if (board[i][j] == 0 && Sudoku.isAllowed(board, k, i, j)) {
//						// add all possible coordinates of this column for this
//						// number
//						allowedFieldsForThisNumber.add(new Coordinate(i, j));
//					}
//				}
//				if (allowedFieldsForThisNumber.size() == 1) {
//					// set the found value
//					Coordinate coord = allowedFieldsForThisNumber.getFirst();
//					board[coord.getICoord()][coord.getJCoord()] = k;
//					backups.peek().pushTSFills(coord.getICoord() * length + coord.getJCoord());
//					// add the filled coordinate to the try solving fills of
//					// the latest backupPoint
//					return true;
//				}
//			}
//		}
//		return false; // no value has been found
//	}
//
//	/**
//	 * Checks if a possibility for a block is impossible to be placed on all but
//	 * one field. If such a value and field is found, the value gets placed on
//	 * this field and true is returned. If no field and value are found, false
//	 * is returned.
//	 * 
//	 * @param board
//	 *            The board on which another entry is searched
//	 * @param backups
//	 *            The stack of backups in which the found solution should be
//	 *            noted
//	 */
//	static boolean hiddenSingleBlock(int[][] board, Stack<BackupPoint> backups) {
//		int length = board.length;
//		int blockLength = (int) Math.sqrt(length);
//		// iterating the rows blockwise
//		for (int iBlock = 0; iBlock < length; iBlock = iBlock + blockLength) {
//			// iterating the columns blockwise
//			for (int jBlock = 0; jBlock < length; jBlock = jBlock + blockLength) {
//				HashSet<Integer> possibilities = new HashSet<Integer>();
//				for (int k = 1; k <= length; k++) {
//					// filling the set with every number from 1-length
//					possibilities.add(k);
//				}
//				for (int i = iBlock; i < iBlock + blockLength; i++) {
//					for (int j = 0; j < length; j++) {
//						// remove already inserted values in this block from the
//						// set
//						possibilities.remove(board[i][j]);
//					} // now it contains only possibilities for this block
//				}
//
//				// iterating the remaining possibilities of the current block
//				for (Integer k : possibilities) {
//					LinkedList<Coordinate> allowedFieldsForThisNumber = new LinkedList<Coordinate>();
//					// iterating the rows of the current block
//					for (int i = iBlock; i < iBlock + blockLength; i++) {
//						// iterating the columns of the current block
//						for (int j = jBlock; j < jBlock + blockLength; j++) {
//							// only search in the empty fields
//							if (board[i][j] == 0 && Sudoku.isAllowed(board, k, i, j)) {
//								// add all possible coordinates of this block
//								// for this number
//								allowedFieldsForThisNumber.add(new Coordinate(i, j));
//							}
//						}
//					}
//					if (allowedFieldsForThisNumber.size() == 1) {
//						Coordinate coord = allowedFieldsForThisNumber.getFirst();
//						fillValue(k, coord, board, adsf);
//						board[coord.getICoord()][coord.getJCoord()] = k;
//						backups.peek().pushTSFills(coord.getICoord() * length + coord.getJCoord());
//						// add the filled coordinate to the try solving fills
//						// of the latest backupPoint
//						return true;
//					}
//				}
//			}
//		}
//		return false; // no value has been found
//	}
//
//	
//}
