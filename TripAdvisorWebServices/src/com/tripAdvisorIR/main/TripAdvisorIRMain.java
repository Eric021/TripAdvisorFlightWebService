package com.tripAdvisorIR.main;


import java.awt.EventQueue;

import com.tripAdvisorIR.cli.CommandLineInterface;
import com.tripAdvisorIR.gui.MainFrame;

public class TripAdvisorIRMain {
	
	static long startTime, endTime;
	public static void main(String[] args) {
		
		startTime = System.currentTimeMillis();
		runCLI();
//		runGUI();
		endTime = System.currentTimeMillis();
	}
	
	private static void runGUI(){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private static void runCLI(){
		CommandLineInterface commandLineInterface = new CommandLineInterface();
		commandLineInterface.run();
	}
	
}
