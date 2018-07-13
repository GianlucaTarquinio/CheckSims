package net.lldp.checksims.ui.download.canvas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.lldp.checksims.ui.ChecksimsInitializer;
import net.lldp.checksims.ui.buttons.FancyButtonAction;
import net.lldp.checksims.ui.buttons.FancyButtonColorTheme;
import net.lldp.checksims.ui.buttons.FancyButtonMouseListener;

public class SubmissionBrowserAssignment extends JPanel {
	ChecksimsInitializer app;
	Assignment assignment;
	
	public SubmissionBrowserAssignment(ChecksimsInitializer app, Assignment assignment) {
		this.app = app;
		this.assignment = assignment;
		
		setLayout(new BorderLayout());
		setBackground(Color.decode("#F5F5F5"));
		setBorder(BorderFactory.createLineBorder(Color.black));
		
		JLabel nameLabel = new JLabel(assignment.getName());
		nameLabel.setFont(new Font(nameLabel.getFont().getFontName(), Font.PLAIN, 17));
		nameLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
		add(nameLabel, BorderLayout.WEST);
		
		int height = nameLabel.getMinimumSize().height;
		ImageIcon downloadImage = new ImageIcon(new ImageIcon(getClass().getResource("/net/lldp/checksims/ui/download_icon.png")).getImage().getScaledInstance(height, height, Image.SCALE_SMOOTH), "Download");		
		JLabel downloadButton = new JLabel(downloadImage);
		downloadButton.setBorder(new EmptyBorder(10, 10, 10, 10));
		downloadButton.addMouseListener(new FancyButtonMouseListener(downloadButton, new FancyButtonAction() {
	    		@Override
	    		public void performAction() {
	    			System.out.println("Download submissions for " + assignment.getName());
	    		}
	    }, FancyButtonColorTheme.BROWSE));
		add(downloadButton, BorderLayout.EAST);
	}
	
	public void minimizePreferredSize() {
		setPreferredSize(getMinimumSize());
	}
}
