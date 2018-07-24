package net.lldp.checksims.ui.download.canvas;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import net.lldp.checksims.ui.ChecksimsInitializer;
import net.lldp.checksims.ui.download.TurninConverter;

public class Assignment {
	private int id;
	private String name;
	private Submission[] submissions;
	private File from, to;
	private String suffixes;
	private ProgressMonitor progressMonitor;
	
	public class DownloadTask extends SwingWorker<Void, Void> {		
		@Override
		protected Void doInBackground() throws Exception {
			int i = 0;
			setProgress(1);
			while(i < submissions.length && !progressMonitor.isCanceled()) {
				try {
					submissions[i].download(from, to, suffixes);
				} catch(Exception e) {
					e.printStackTrace();
					return null;
				}
				setProgress(++i + 1);
			}
			return null;
		}
	}
	
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
		String folderName = dcp.getName();
		if(folderName == null || folderName.length() == 0) {
			JOptionPane.showMessageDialog(null, "Must provide a name.", "No Name", JOptionPane.ERROR_MESSAGE);
			return;
		}
		String path = dcp.getPath();
		if(path == null || path.length() == 0) {
			JOptionPane.showMessageDialog(null, "Must provide a path.", "No Path", JOptionPane.ERROR_MESSAGE);
			return;
		}
		suffixes = dcp.getSuffixes();
		
		from = ChecksimsInitializer.TEMPORARY_DOWNLOAD_PATH;
		if(from.exists()) {
			if(!from.isDirectory()) {
				JOptionPane.showMessageDialog(null, "'" + from.getAbsolutePath() + "' already exists, and is not a directory.", "File Already Exists", JOptionPane.ERROR_MESSAGE);
				return;
			}
			TurninConverter.delete(from);
		}
		
		if(!from.mkdirs()) {
			JOptionPane.showMessageDialog(null, "'" + from.getAbsolutePath() + "' could not be created.", "File Not Created", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		File toParent = new File(path);
		toParent.mkdirs();
		try {
			to = new File(TurninConverter.getUnusedName(toParent, folderName));
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Could not create destination directory.", "File Not Created", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(to.exists()) {
			if(!to.isDirectory()) {
				JOptionPane.showMessageDialog(null, "'" + to.getAbsolutePath() + "' already exists, and is not a directory.", "File Already Exists", JOptionPane.ERROR_MESSAGE);
				return;
			}
		} else {
			to.mkdirs();
		}

		progressMonitor = new ProgressMonitor(csb, "Downloading Submissions", "Starting download", 0, submissions.length + 1);
		progressMonitor.setMillisToPopup(0);
		progressMonitor.setMillisToDecideToPopup(0);
				
		DownloadTask download = new DownloadTask();
		download.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if(evt.getPropertyName().equals("progress")) {
					int progress = (Integer) evt.getNewValue();
					progressMonitor.setProgress(progress);
					progressMonitor.setNote("Downloaded " + (progress - 1) + "/" + submissions.length);
					if(progressMonitor.isCanceled() || download.isDone()) {
						if(progressMonitor.isCanceled()) {
							download.cancel(true);
							TurninConverter.delete(from);
							TurninConverter.delete(to);
							JOptionPane.showMessageDialog(null, "Your download has been cancelled.", "Download Cancelled", JOptionPane.ERROR_MESSAGE);
						} else {
							TurninConverter.delete(from);
							app.getMenu().setCurrentFIOPath(to.getAbsolutePath());
							app.goToMain();
						}
					}
				}
			}		
		});
		download.execute();
	}
	
	@Override
	public String toString() {
		return "Assignment [id=" + id + ", name=" + name + "]";
	}
}
