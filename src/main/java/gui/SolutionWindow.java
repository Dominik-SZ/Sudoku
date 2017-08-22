package gui;

import logic.SudokuField;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

class SolutionWindow {

	private JFrame mainFrame;

	// Constructor
	SolutionWindow(SudokuField[][] board, Color fieldStandardColor){
		int gap = 5;
		int length = board.length;
		int blockLength = (int) Math.sqrt(length);
		Font fieldFont= new Font("SansSerif", Font.BOLD, 30);
		mainFrame = new JFrame("Lösung");
		
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);		// only close the solution frame
		mainFrame.setSize(new Dimension(45*length, 45*length+80));
		mainFrame.setLayout(new BorderLayout());
		
		JPanel centerPanel = new JPanel(new GridLayout(blockLength, blockLength, gap, gap));
		centerPanel.setBackground(Color.BLACK);
		
		for(int blockNumber= 0; blockNumber < length; blockNumber++){		// generating the blocks
			JPanel block= new JPanel();
			block.setLayout(new GridLayout(blockLength, blockLength));
			
			int iStartValue = blockLength * (blockNumber / blockLength);
			int jStartValue = blockLength * (blockNumber % blockLength);
			for(int i = iStartValue; i < iStartValue + blockLength; i++){
				for(int j = jStartValue; j < jStartValue + blockLength; j++){

                    // giving each block its fields
					GraphicalSudokuField field= new GraphicalSudokuField(fieldFont, i, j);
					field.setText(board[i][j].getSolutionValue() +"");
					field.setHorizontalAlignment(JTextField.CENTER);
					field.setEditable(false);
					field.setBorder(new EtchedBorder(10));
					field.setBackground(Color.WHITE);
					block.add(field);
					if(board[i][j].getCurrentValue() == 0)	field.setForeground(fieldStandardColor);
					else					field.setForeground(Color.BLACK);
				}
			}
			centerPanel.add(block);							// adding the blocks to the center panel
		}
		mainFrame.add(BorderLayout.CENTER, centerPanel);
	    mainFrame.setLocationRelativeTo(null);				// center the frame
	}
	
	void setVisible(boolean arg){
		mainFrame.setVisible(arg);
	}
}