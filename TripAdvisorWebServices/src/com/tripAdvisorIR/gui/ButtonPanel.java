package com.tripAdvisorIR.gui;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.FlowLayout;

public class ButtonPanel extends JPanel {
	
	JButton btnExit;
	JButton btnSearch;
	
	public ButtonPanel() {
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		// create buttons
		btnExit = new JButton("Exit");
		btnSearch = new JButton("Search");
		
		// add buttons to panel
		add(btnExit);
		add(btnSearch);

	}

}
