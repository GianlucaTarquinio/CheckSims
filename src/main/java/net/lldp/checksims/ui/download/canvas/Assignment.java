package net.lldp.checksims.ui.download.canvas;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
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
		//build the dialog
		JPanel configPanel = new JPanel();
		configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.Y_AXIS));
		
		JLabel pathLabel = new JLabel("Download path");
		
		JFileChooser path = new JFileChooser();
		path.setCurrentDirectory(ChecksimsInitializer.DEFAULT_DOWNLOAD_PATH);
		path.setDialogTitle("Choose a download path");
		path.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		path.setAcceptAllFileFilterUsed(false);
		
		JTextField pathField = new JTextField(80);
		pathField.setEditable(false);
		pathField.setText(path.getCurrentDirectory().getAbsolutePath());
		pathField.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		JButton pathButton = new JButton("Browse");
		Assignment self = this;
		pathButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(path.showOpenDialog(csb) == JFileChooser.APPROVE_OPTION) {
					System.out.println(path.getSelectedFile());
				}
			}
			
		});
		
		JPanel pathPanel = new JPanel();
		pathPanel.setLayout(new BorderLayout());
		
		pathPanel.add(pathLabel, BorderLayout.WEST);
		pathPanel.add(pathButton, BorderLayout.EAST);
		pathPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		configPanel.add(pathPanel);
		//bring up the dialog
		String[] options = new String[]{ "OK", "Cancel" };
		int option = JOptionPane.showOptionDialog(null, pathPanel, "Configure Download", JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if(option != 0) {
			System.out.println("Download to " + path.getSelectedFile());
		} 
		//check the dialog
		//download the submissions
	}
	
	@Override
	public String toString() {
		return "Assignment [id=" + id + ", name=" + name + "]";
	}
}
