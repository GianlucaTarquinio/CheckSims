package net.lldp.checksims.ui.download.canvas;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

public class DownloadProgress extends JDialog {
	private int max;
	private JLabel progressLabel = null;
	private JProgressBar progressBar = null;
	private Assignment assignment;
	
	public DownloadProgress(JFrame parent, int max, Assignment assignment) {
		super(parent, "Downloading files.");
		setModal(true);
		setResizable(false);
		setMinimumSize(new Dimension(600, 300));
		setLocationRelativeTo(null);
		this.max = max;
		this.assignment = assignment;
		
		JPanel progressPanel = new JPanel();
		progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));
		
		progressLabel = new JLabel("Starting download");
		progressLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
		progressLabel.setFont(new Font(progressLabel.getFont().getFontName(), Font.PLAIN, 17));
		progressLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		progressBar = new JProgressBar(0, this.max);
		progressBar.setBorder(new EmptyBorder(10, 10, 10, 10));
		progressBar.setAlignmentX(Component.LEFT_ALIGNMENT);
		progressBar.setValue(0);
		
		progressPanel.add(progressLabel);
		progressPanel.add(progressBar);
		
		getContentPane().add(progressPanel);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	public void set(int val) {
		if(progressLabel != null) {
			progressLabel.setText("Downloading submission " + (val + 1) + "/" + max);
		}
		if(progressBar != null) {
			progressBar.setValue(val);
		}
		revalidate();
		System.out.println(val + "/" + max);
		if(val == max) {
			assignment.setDownloaded();
			dispose();
		}
	}
}
