package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

public class SolutionWindow {
	
	Font fieldFont= new Font("SansSerif", Font.BOLD, 30);
	JFrame mainFrame;
	JPanel centerPanel;
	int[][] boardSolved;
	
	// Constructor
	SolutionWindow(int[][] boardSolved, int[][] board, Color fieldStandardColor){
		int gap = 5;
		this.boardSolved = boardSolved;
		int blockLength = (int) Math.sqrt(boardSolved.length);
		mainFrame = new JFrame("Lösung");
		
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);		// only close the solution frame
		mainFrame.setSize(new Dimension(45*boardSolved.length, 45*boardSolved.length+80));
		mainFrame.setLayout(new BorderLayout());
		
		centerPanel = new JPanel(new GridLayout(blockLength, blockLength, gap, gap));
		centerPanel.setBackground(Color.BLACK);
		
		for(int blockNumber= 0; blockNumber < boardSolved.length; blockNumber++){		// generating the blocks
			JPanel block= new JPanel();
			block.setLayout(new GridLayout(blockLength, blockLength));
			
			int iStartValue = blockLength * (blockNumber / blockLength);
			int jStartValue = blockLength * (blockNumber % blockLength);
			for(int i = iStartValue; i < iStartValue + blockLength; i++){
				for(int j = jStartValue; j < jStartValue + blockLength; j++){
					SudokuField field= new SudokuField(i, j);		// giving each block its fields
					field.setText(boardSolved[i][j]+"");
					field.setHorizontalAlignment(JTextField.CENTER);
					field.setFont(fieldFont);
					field.setEditable(false);
					field.setBorder(new EtchedBorder(10));
					field.setBackground(Color.WHITE);
					block.add(field);
					if(board[i][j] == 0)	field.setForeground(fieldStandardColor);
					else					field.setForeground(Color.BLACK);
				}
			}
			centerPanel.add(block);							// adding the blocks to the center panel
		}
		mainFrame.add(BorderLayout.CENTER, centerPanel);
	    mainFrame.setLocationRelativeTo(null);				// center the frame
	}
	
	public void setVisible(boolean arg){
		mainFrame.setVisible(arg);
	}
}