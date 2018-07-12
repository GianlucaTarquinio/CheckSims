package net.lldp.checksims.ui.download.canvas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Group;
import javax.swing.border.EmptyBorder;

import net.lldp.checksims.ui.ChecksimsColors;
import net.lldp.checksims.ui.ChecksimsInitializer;
import net.lldp.checksims.ui.buttons.FancyButtonAction;
import net.lldp.checksims.ui.buttons.FancyButtonColorTheme;
import net.lldp.checksims.ui.buttons.FancyButtonMouseListener;
import net.lldp.checksims.ui.download.ChooseAccountView;
import net.lldp.checksims.ui.download.CreateAccountView;

public class CanvasSubmissionBrowser extends JPanel {
	ChecksimsInitializer app;
	CanvasService canvasService;
	Course[] courses;
	JPanel body = null;
	
	public CanvasSubmissionBrowser(ChecksimsInitializer app, CanvasService canvasService, String username) {
		this.app = app;
		this.canvasService = canvasService;
		
		JPanel topBar = new JPanel();
		topBar.setBackground(ChecksimsColors.WPI_GREY);
		
		BorderLayout borderLayout = new BorderLayout();
		topBar.setLayout(borderLayout);
		
		JPanel topBarLeft = new JPanel();
		topBarLeft.setBackground(new Color(0,0,0,0));
		
		JLabel title = new JLabel("Choose Files");
		title.setFont(new Font(title.getFont().getFontName(), Font.PLAIN, 40));
		title.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		int backHeight = Math.max(title.getMinimumSize().height - 10, 1);
		ImageIcon backImage = new ImageIcon(new ImageIcon(getClass().getResource("/net/lldp/checksims/ui/back_icon.png")).getImage().getScaledInstance(backHeight, backHeight, Image.SCALE_SMOOTH), "Go back.");		
		JLabel goBack = new JLabel(backImage);
		goBack.setBorder(new EmptyBorder(10, 10, 10, 10));
		CanvasSubmissionBrowser self = this;
		goBack.addMouseListener(new FancyButtonMouseListener(goBack, new FancyButtonAction() {
	    		@Override
	    		public void performAction() {
	    			try {
	    				self.app.setPanel(new ChooseAccountView(self.app));
	    			} catch(Exception e) {
	    				self.app.UhOhException(e);
	    			}
	    		}
	    }, FancyButtonColorTheme.HEADER));

		topBarLeft.add(goBack);
		topBarLeft.add(title);
		
		topBar.add(topBarLeft, BorderLayout.WEST);
		
		JPanel topBarRight = new JPanel();
		topBarRight.setBackground(new Color(0,0,0,0));
		topBarRight.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		int iconHeight = Math.max(title.getMinimumSize().height - 10, 1);
		ImageIcon homeImage = new ImageIcon(new ImageIcon(getClass().getResource("/net/lldp/checksims/ui/home_icon.png")).getImage().getScaledInstance((int) Math.floor(iconHeight * 1.7778), iconHeight, Image.SCALE_SMOOTH), "Go home.");		
		JLabel goHome = new JLabel(homeImage);
		goHome.setBorder(new EmptyBorder(10, 10, 10, 10));
		goHome.addMouseListener(new FancyButtonMouseListener(goHome, new FancyButtonAction() {
	    		@Override
	    		public void performAction() {
	    			self.app.goToMain();
	    		}
	    }, FancyButtonColorTheme.HEADER));
		topBarRight.add(goHome);
		
		topBar.add(topBarRight, BorderLayout.EAST);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		setBackground(ChecksimsColors.PRETTY_GREY);
		GridBagConstraints c = new GridBagConstraints();
		
		body = new JPanel();
		JScrollPane scroll = new JScrollPane(body);
		scroll.setBorder(new EmptyBorder(0, 0, 0, 0));
		body.setBackground(ChecksimsColors.PRETTY_GREY);
		
		
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;	
		add(topBar, c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 1000;
		c.fill = GridBagConstraints.BOTH;
		add(scroll, c);
	}
	
	public void load() {
		if(body == null) {
			return;
		}
		courses = canvasService.getCanvasData();
		
		GroupLayout bodyLayout = new GroupLayout(body);
		body.setLayout(bodyLayout);
		
		Group horizontalGroup = bodyLayout.createParallelGroup();
		Group verticalGroup = bodyLayout.createSequentialGroup();
		
		SubmissionBrowserCourse sbc;
		for(Course c : courses) {
			for(int i = 0; i < 3; i++) { //TEMPORARY FOR TESTING
				sbc = new SubmissionBrowserCourse(app, c);
				sbc.minimizePreferredSize();			
				horizontalGroup.addComponent(sbc);
				verticalGroup.addComponent(sbc, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE);
			}
		}
		
		bodyLayout.setHorizontalGroup(horizontalGroup);
		bodyLayout.setVerticalGroup(verticalGroup);
		
		body.revalidate();
		body.repaint();
	}
	
	private String buildDataString(Course[] courses) {
		String data = "";
		for(Course c : courses) {
			data += c.toString() + '\n';
			for(Assignment a : c.getAssignments()) {
				data += '\t' + a.toString() + '\n';
				for(Submission s : a.getSubmissions()) {
					data += "\t\t" + s.toString() + '\n';
				}
			}
		}
		return data.substring(0, data.length() - 1);
	}
}
