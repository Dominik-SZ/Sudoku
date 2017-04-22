package gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.colorchooser.AbstractColorChooserPanel;

public class ColorChooserPanel extends  AbstractColorChooserPanel {
	/** just to inhibit warnings */
	private static final long serialVersionUID = 5106935617338696751L;
	SudokuField parentField;
	JPanel topPanel;
	JPanel bottomPanel;
	Color selectedColor;
	JToggleButton currentlySelected;
	LinkedList<JToggleButton> buttons;
	
	public ColorChooserPanel(SudokuField field) {
		parentField = field;
	}

	@Override
	protected void buildChooser() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		selectedColor = parentField.getBackground();
		buttons = new LinkedList<JToggleButton>();
		
		// generate the two main panels
		topPanel = new JPanel();
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(2,5));
		add(topPanel);
		add(bottomPanel);
		
		// generate the content for the top panel
		JButton detailButton = new JButton("Mehr Farben");
		detailButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Color color = JColorChooser.showDialog(
	                     parentField,
	                     "Wähle deine gewünschte Farbe für dieses Feld",
	                     selectedColor);
				if(color != null){
					for(JToggleButton currentButton: buttons){
						currentButton.setSelected(false);
					}
					selectedColor = color;
					currentlySelected.setIcon(new ColorIcon(selectedColor));
					getColorSelectionModel().setSelectedColor(color);
					
				}
			}
			
			
		});
		
		currentlySelected = new JToggleButton();
		currentlySelected.setEnabled(false);
		Icon icon = new ColorIcon(selectedColor);
		currentlySelected.setIcon(icon);
		buttons.add(currentlySelected);
		topPanel.add(detailButton);
		topPanel.add(currentlySelected);
		
		// generate the content for the bottom panel
		addColorOption(Color.BLACK);
		addColorOption(Color.DARK_GRAY);
		addColorOption(Color.MAGENTA);
		addColorOption(Color.PINK);
		addColorOption(Color.ORANGE);
		addColorOption(Color.WHITE);
		addColorOption(Color.CYAN);
		addColorOption(Color.BLUE);
		addColorOption(Color.GREEN);
		addColorOption(Color.YELLOW);
		
	}

	@Override
	public String getDisplayName() {
		return "Colors";
	}

	@Override
	public Icon getLargeDisplayIcon() {
		// currently unused
		return null;
	}

	@Override
	public Icon getSmallDisplayIcon() {
		// currently unused
		return null;
	}

	@Override
	public void updateChooser() {
//		Color color = getColorFromModel();
//	    if (Color.red.equals(color)) {
//	        tbRed.setSelected(true);
//	    } else if (Color.yellow.equals(color)) {
//	        tbYellow.setSelected(true);
//	    } else if (Color.green.equals(color)) {
//	        tbGreen.setSelected(true);
//	    } else if (Color.blue.equals(color)) {
//	        tbBlue.setSelected(true);
//	    }
	}
	
	private void addColorOption(Color color){
		Icon icon = new ColorIcon(color);
		JToggleButton tb = new JToggleButton();
		tb.setIcon(icon);
		tb.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent event) {
				getColorSelectionModel().setSelectedColor(color);
				for(JToggleButton currentButton: buttons){
					currentButton.setSelected(false);
				}
				tb.setSelected(true);
				
				if(color != null){
					selectedColor = color;
					currentlySelected.setIcon(new ColorIcon(selectedColor));
				}
			}
			
		});
		buttons.add(tb);
		bottomPanel.add(tb);
	}

}
