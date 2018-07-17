package net.lldp.checksims.ui.download.canvas;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.lldp.checksims.ui.ChecksimsInitializer;

public class DownloadConfigPanel extends JPanel {
	private JTextField nameField = null;
	private JTextField pathField = null;
	private JTextArea listField = null;
	
	public DownloadConfigPanel(CanvasSubmissionBrowser csb, String defaultName) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JLabel nameLabel = new JLabel("Name");
		nameLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
		nameLabel.setFont(new Font(nameLabel.getFont().getFontName(), Font.PLAIN, 17));
		nameLabel.setAlignmentX(LEFT_ALIGNMENT);
		
		nameField = new JTextField(50);
		nameField.setFont(new Font(nameField.getFont().getFontName(), Font.PLAIN, 13));
		nameField.setText(defaultName);
		nameField.setAlignmentX(LEFT_ALIGNMENT);
		
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
		
		namePanel.add(nameLabel);
		namePanel.add(nameField);
		namePanel.setAlignmentX(CENTER_ALIGNMENT);
		
		
		JLabel pathLabel = new JLabel("Download path");
		pathLabel.setBorder(new EmptyBorder(30, 0, 10, 0));
		pathLabel.setFont(new Font(pathLabel.getFont().getFontName(), Font.PLAIN, 17));
		
		JFileChooser path = new JFileChooser();
		path.setCurrentDirectory(null);
		path.setSelectedFile(ChecksimsInitializer.DEFAULT_DOWNLOAD_PATH);
		path.setDialogTitle("Choose a download path");
		path.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		path.setAcceptAllFileFilterUsed(false);
		
		pathField = new JTextField(50);
		pathField.setEditable(false);
		pathField.setText(ChecksimsInitializer.DEFAULT_DOWNLOAD_PATH.getAbsolutePath());
		pathField.setFont(new Font(pathField.getFont().getFontName(), Font.PLAIN, 13));
		
		JButton pathButton = new JButton("Browse");
		pathButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(path.showOpenDialog(csb) == JFileChooser.APPROVE_OPTION) {
					pathField.setText(path.getSelectedFile().getAbsolutePath());
				}
			}
			
		});
		
		JPanel pathPanel = new JPanel();
		pathPanel.setLayout(new BorderLayout());
		
		pathPanel.add(pathLabel, BorderLayout.NORTH);
		pathPanel.add(pathField, BorderLayout.WEST);
		pathPanel.add(pathButton, BorderLayout.EAST);
		pathPanel.setAlignmentX(CENTER_ALIGNMENT);
		
		
		JLabel listLabel = new JLabel("Code File Suffixes");
		listLabel.setFont(new Font(listLabel.getFont().getFontName(), Font.PLAIN, 17));
		listLabel.setBorder(new EmptyBorder(30, 0, 10, 0));
		listLabel.setAlignmentX(LEFT_ALIGNMENT);
		
		JLabel listInfo = new JLabel("<html>Any file whose name ends in one of the strings in this comma-separated list will be considered code.</html>");
		listInfo.setFont(new Font(listInfo.getFont().getFontName(), Font.PLAIN, 13));
		listInfo.setBorder(new EmptyBorder(0, 0, 10, 0));
		listInfo.setAlignmentX(LEFT_ALIGNMENT);
		
		listField = new JTextArea(5, 30);
		listField.setFont(new Font(listLabel.getFont().getFontName(), Font.PLAIN, 13));
		listField.setBorder(new EmptyBorder(5, 5, 5, 5));
		listField.setLineWrap(true);
		listField.setWrapStyleWord(true);
		listField.setText(ChecksimsInitializer.DEFAULT_CODE_SUFFIXES);
		listField.setAlignmentX(LEFT_ALIGNMENT);
		
		JPanel listPanel = new JPanel();
		listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
		
		listPanel.add(listLabel);
		listPanel.add(listInfo);
		listPanel.add(listField);
		listPanel.setAlignmentX(CENTER_ALIGNMENT);
		
		add(namePanel);
		add(pathPanel);
		add(listPanel);
	}
	
	public String getName() {
		if(nameField == null) {
			return null;
		}
		return nameField.getText();
	}
	
	public String getPath() {
		if(pathField == null) {
			return null;
		}
		return pathField.getText();
	}
	
	public String getSuffixes() {
		if(listField == null) {
			return null;
		}
		return listField.getText();
	}
}
