package net.lldp.checksims.ui.download.canvas;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.lldp.checksims.ui.ChecksimsInitializer;

public class SubmissionBrowserAssignment extends JPanel {
	ChecksimsInitializer app;
	Assignment assignment;
	
	public SubmissionBrowserAssignment(ChecksimsInitializer app, Assignment assignment) {
		this.app = app;
		this.assignment = assignment;
		
		add(new JLabel(assignment.getName()));
	}
}
