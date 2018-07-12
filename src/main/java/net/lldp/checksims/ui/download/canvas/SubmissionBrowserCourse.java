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

public class SubmissionBrowserCourse extends JPanel {
	ChecksimsInitializer app;
	Course course;
	JLabel expandButton, collapseButton;
	
	public SubmissionBrowserCourse(ChecksimsInitializer app, Course course) {
		this.app = app;
		this.course = course;
		
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createLineBorder(Color.black));
		
		JLabel nameLabel = new JLabel(course.getName());
		nameLabel.setFont(new Font(nameLabel.getFont().getFontName(), Font.PLAIN, 25));
		nameLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
		add(nameLabel, BorderLayout.WEST);
		
		initButtons(nameLabel.getMinimumSize().height);
	}
	
	public void minimizePreferredSize() {
		setPreferredSize(getMinimumSize());
	}
	
	private void initButtons(int height) {
		ImageIcon expandImage = new ImageIcon(new ImageIcon(getClass().getResource("/net/lldp/checksims/ui/expand_icon.png")).getImage().getScaledInstance(height, height, Image.SCALE_SMOOTH), "Expand");		
		expandButton = new JLabel(expandImage);
		expandButton.setBorder(new EmptyBorder(10, 10, 10, 10));
		expandButton.addMouseListener(new FancyButtonMouseListener(expandButton, new FancyButtonAction() {
	    		@Override
	    		public void performAction() {
	    			expand();
	    		}
	    }, FancyButtonColorTheme.BROWSE));
		
		ImageIcon collapseImage = new ImageIcon(new ImageIcon(getClass().getResource("/net/lldp/checksims/ui/collapse_icon.png")).getImage().getScaledInstance(height, height, Image.SCALE_SMOOTH), "Expand");		
		collapseButton = new JLabel(collapseImage);
		collapseButton.setBorder(new EmptyBorder(10, 10, 10, 10));
		collapseButton.addMouseListener(new FancyButtonMouseListener(collapseButton, new FancyButtonAction() {
	    		@Override
	    		public void performAction() {
	    			collapse();
	    		}
	    }, FancyButtonColorTheme.BROWSE));
		
		add(expandButton, BorderLayout.EAST);
	}
	
	private void expand() {
		remove(expandButton);
		add(collapseButton, BorderLayout.EAST);
		revalidate();
		repaint();
	}
	
	private void collapse() {
		remove(collapseButton);
		add(expandButton, BorderLayout.EAST);
		revalidate();
		repaint();
	}
}
