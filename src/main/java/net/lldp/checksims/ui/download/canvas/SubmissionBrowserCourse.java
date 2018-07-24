package net.lldp.checksims.ui.download.canvas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.GroupLayout.Group;
import javax.swing.border.EmptyBorder;

import net.lldp.checksims.ui.ChecksimsInitializer;
import net.lldp.checksims.ui.buttons.FancyButtonAction;
import net.lldp.checksims.ui.buttons.FancyButtonColorTheme;
import net.lldp.checksims.ui.buttons.FancyButtonMouseListener;

public class SubmissionBrowserCourse extends JPanel {
	ChecksimsInitializer app;
	Course course;
	JLabel expandButton, collapseButton;
	JPanel coursePanel;
	JPanel assignmentPanel;
	CanvasSubmissionBrowser csb;
	
	public SubmissionBrowserCourse(ChecksimsInitializer app, CanvasSubmissionBrowser csb, Course course) {
		this.app = app;
		this.course = course;
		this.csb = csb;
		
		coursePanel = new JPanel();
		coursePanel.setLayout(new BorderLayout());
		coursePanel.setBackground(Color.decode("#F5F5F5"));
		coursePanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		JLabel nameLabel = new JLabel(this.course.getName());
		nameLabel.setFont(new Font(nameLabel.getFont().getFontName(), Font.PLAIN, 25));
		nameLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
		coursePanel.add(nameLabel, BorderLayout.WEST);
		
		buildAssignmentPanel();
		
		initButtons(nameLabel.getMinimumSize().height);
		
		setBackground(new Color(0, 0, 0, 0));
		
		GroupLayout groupLayout = new GroupLayout(this);
		setLayout(groupLayout);
		
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup()
					.addComponent(coursePanel)
					.addGroup(
							groupLayout.createSequentialGroup()
								.addPreferredGap(ComponentPlacement.RELATED, 35, 35)
								.addComponent(assignmentPanel)));
		groupLayout.setVerticalGroup(
				groupLayout.createSequentialGroup()
					.addComponent(coursePanel)
					.addComponent(assignmentPanel));
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
		
		ImageIcon collapseImage = new ImageIcon(new ImageIcon(getClass().getResource("/net/lldp/checksims/ui/collapse_icon.png")).getImage().getScaledInstance(height, height, Image.SCALE_SMOOTH), "Collapse");		
		collapseButton = new JLabel(collapseImage);
		collapseButton.setBorder(new EmptyBorder(10, 10, 10, 10));
		collapseButton.addMouseListener(new FancyButtonMouseListener(collapseButton, new FancyButtonAction() {
	    		@Override
	    		public void performAction() {
	    			collapse();
	    		}
	    }, FancyButtonColorTheme.BROWSE));
		
		coursePanel.add(expandButton, BorderLayout.EAST);
	}
	
	private void buildAssignmentPanel() {
		assignmentPanel = new JPanel();
		assignmentPanel.setVisible(false);
		
		GroupLayout groupLayout = new GroupLayout(assignmentPanel);
		assignmentPanel.setLayout(groupLayout);
		
		Group horizontalGroup = groupLayout.createParallelGroup();
		Group verticalGroup = groupLayout.createSequentialGroup();
		
		SubmissionBrowserAssignment sba;
		for(Assignment a : course.getAssignments()) {
			sba = new SubmissionBrowserAssignment(app, csb, a);
			sba.minimizePreferredSize();			
			horizontalGroup.addComponent(sba);
			verticalGroup.addComponent(sba, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE);
		}
		
		groupLayout.setHorizontalGroup(horizontalGroup);
		groupLayout.setVerticalGroup(verticalGroup);
	}
	
	private void expand() {
		coursePanel.remove(expandButton);
		coursePanel.add(collapseButton, BorderLayout.EAST);
		assignmentPanel.setVisible(true);
		revalidate();
		minimizePreferredSize();
		getParent().revalidate();
	}
	
	private void collapse() {
		coursePanel.remove(collapseButton);
		coursePanel.add(expandButton, BorderLayout.EAST);
		assignmentPanel.setVisible(false);
		revalidate();
		minimizePreferredSize();
		getParent().revalidate();
	}
}
