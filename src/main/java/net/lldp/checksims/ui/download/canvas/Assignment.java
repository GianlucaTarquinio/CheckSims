package net.lldp.checksims.ui.download.canvas;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.lldp.checksims.ui.ChecksimsInitializer;

public class Assignment {
	private int id;
	private String name;
	private Submission[] submissions;
	
	public Assignment(int id, String name) {
		this.id = id;
		this.name = name;
		submissions = null;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Submission[] getSubmissions() {
		return submissions;
	}
	
	public void setSubmissions(Submission[] submissions) {
		this.submissions = submissions;
	}
	
	public void downloadSubmissions(ChecksimsInitializer app, CanvasSubmissionBrowser csb) {
		char[] safeName = name.toCharArray();
		char c;
		for(int i = 0; i < safeName.length; i++) {
			c = safeName[i];
			if(!(Character.isLetter(c) || Character.isDigit(c))) {
				safeName[i] = '_';
			} else if(Character.isUpperCase(c)) {
				safeName[i] = Character.toLowerCase(c);
			}
		}
		DownloadConfigPanel dcp = new DownloadConfigPanel(csb, new String(safeName) + "_submissions");
		String[] options = new String[]{ "Download", "Cancel" };
		int option = JOptionPane.showOptionDialog(null, dcp, "Configure Download", JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if(option != 0) {
			return;
		}
		System.out.println("Download as: " + dcp.getName());
		System.out.println("Download to: " + dcp.getPath());
		System.out.println("Suffixes: " + dcp.getSuffixes());
		//check the dialog
		//download the submissions
	}
	
	@Override
	public String toString() {
		return "Assignment [id=" + id + ", name=" + name + "]";
	}
}
