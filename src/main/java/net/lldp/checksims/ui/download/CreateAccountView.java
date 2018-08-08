package net.lldp.checksims.ui.download;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Paths;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.lldp.checksims.ui.ChecksimsColors;
import net.lldp.checksims.ui.ChecksimsInitializer;
import net.lldp.checksims.ui.buttons.FancyButtonAction;
import net.lldp.checksims.ui.buttons.FancyButtonColorTheme;
import net.lldp.checksims.ui.buttons.FancyButtonMouseListener;

public class CreateAccountView extends JPanel {
	ChecksimsInitializer app;
	ChooseAccountView chooseAccountView;
	Service service;
	
	public CreateAccountView(ChecksimsInitializer app, ChooseAccountView chooseAccountView, Service service) {
		this.app = app;
		this.chooseAccountView = chooseAccountView;
		this.service = service;
		
		JPanel topBar = new JPanel();
		topBar.setBackground(ChecksimsColors.WPI_GREY);
		
		BorderLayout borderLayout = new BorderLayout();
		topBar.setLayout(borderLayout);
		
		JPanel topBarLeft = new JPanel();
		topBarLeft.setBackground(new Color(0,0,0,0));
		
		JLabel title = new JLabel("Create Account");
		title.setFont(new Font(title.getFont().getFontName(), Font.PLAIN, 40));
		title.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		int backHeight = Math.max(title.getMinimumSize().height - 10, 1);
		ImageIcon backImage = new ImageIcon(new ImageIcon(getClass().getResource("/net/lldp/checksims/ui/back_icon.png")).getImage().getScaledInstance(backHeight, backHeight, Image.SCALE_SMOOTH), "Go back.");		
		JLabel goBack = new JLabel(backImage);
		goBack.setBorder(new EmptyBorder(10, 10, 10, 10));
		CreateAccountView self = this;
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
		
		JPanel body = new JPanel();
		body.setBackground(ChecksimsColors.PRETTY_GREY);
		body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
		
		JLabel note = new JLabel("<html>Note: The credentials entered below are only used to identify and protect your account within CheckSims.  They need not be the same as the credentials of the external " + service.getName() + " account that you are adding.</html>");
		note.setFont(new Font(note.getFont().getFontName(), Font.PLAIN, 14));
		note.setBorder(new EmptyBorder(10, 10, 0, 10));
		note.setAlignmentX(LEFT_ALIGNMENT);
		body.add(note);
		
		JLabel usernameLabel = new JLabel("Username:");
		usernameLabel.setFont(new Font(usernameLabel.getFont().getFontName(), Font.PLAIN, 17));
		usernameLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
		usernameLabel.setAlignmentX(LEFT_ALIGNMENT);
		body.add(usernameLabel);
		
		JTextField usernameField = new JTextField(ChecksimsInitializer.MAX_USERNAME_LEN);
		usernameField.setFont(new Font(usernameField.getFont().getFontName(), Font.PLAIN, 17));
		usernameField.setBorder(new EmptyBorder(2, 10, 2, 10));
		usernameField.setAlignmentX(LEFT_ALIGNMENT);
		body.add(usernameField);
		
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setFont(new Font(passwordLabel.getFont().getFontName(), Font.PLAIN, 17));
		passwordLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
		passwordLabel.setAlignmentX(LEFT_ALIGNMENT);
		body.add(passwordLabel);
		
		JPasswordField passwordField = new JPasswordField(ChecksimsInitializer.MAX_USERNAME_LEN);
		passwordField.setFont(new Font(passwordField.getFont().getFontName(), Font.PLAIN, 17));
		passwordField.setBorder(new EmptyBorder(2, 10, 2, 10));
		passwordField.setAlignmentX(LEFT_ALIGNMENT);
		body.add(passwordField);
		
		JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
		confirmPasswordLabel.setFont(new Font(confirmPasswordLabel.getFont().getFontName(), Font.PLAIN, 17));
		confirmPasswordLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
		confirmPasswordLabel.setAlignmentX(LEFT_ALIGNMENT);
		body.add(confirmPasswordLabel);
		
		JPasswordField confirmPasswordField = new JPasswordField(ChecksimsInitializer.MAX_USERNAME_LEN);
		confirmPasswordField.setFont(new Font(confirmPasswordField.getFont().getFontName(), Font.PLAIN, 17));
		confirmPasswordField.setBorder(new EmptyBorder(2, 10, 2, 10));
		confirmPasswordField.setAlignmentX(LEFT_ALIGNMENT);
		body.add(confirmPasswordField);
		
		body.add(Box.createRigidArea(new Dimension(0, 20)));
		
		JLabel createAccountButton = new JLabel("Create Account");
		createAccountButton.setFont(new Font(createAccountButton.getFont().getFontName(), Font.PLAIN, 20));
		createAccountButton.setBorder(new EmptyBorder(10, 10, 10, 10));
		createAccountButton.setAlignmentX(LEFT_ALIGNMENT);
		createAccountButton.addMouseListener(new FancyButtonMouseListener(createAccountButton, new FancyButtonAction() {
	    		@Override
	    		public void performAction() {
	    			String username = usernameField.getText();
	    			String password = new String(passwordField.getPassword());
	    			String confirmPassword = new String(confirmPasswordField.getPassword());
	    			if(username == null) {
	    				JOptionPane.showMessageDialog(null, "Username must contain at least " + ChecksimsInitializer.MIN_USERNAME_LEN + " " + (ChecksimsInitializer.MIN_USERNAME_LEN == 1 ? "character" : "characters") + ".", "Username Too Short", JOptionPane.ERROR_MESSAGE);
	    				return;
	    			}
	    			int uLen = username.length();
	    			int pLen = password.length();
	    			String invalid = checkInvalidUsername(username);
	    			if(invalid != null) {
	    				JOptionPane.showMessageDialog(null, "Username must contain only alphanumeric characters (contains '" + invalid + "').", "Invalid Username", JOptionPane.ERROR_MESSAGE);
	    			} else if(uLen < ChecksimsInitializer.MIN_USERNAME_LEN) {
	    				JOptionPane.showMessageDialog(null, "Username must contain at least " + ChecksimsInitializer.MIN_USERNAME_LEN + " " + (ChecksimsInitializer.MIN_USERNAME_LEN == 1 ? "character" : "characters") + ".", "Username Too Short", JOptionPane.ERROR_MESSAGE);
	    			} else if(uLen > ChecksimsInitializer.MAX_USERNAME_LEN) {
	    				JOptionPane.showMessageDialog(null, "Username must contain at most " + ChecksimsInitializer.MAX_USERNAME_LEN + " " + (ChecksimsInitializer.MAX_USERNAME_LEN == 1 ? "character" : "characters") + ".", "Username Too Long", JOptionPane.ERROR_MESSAGE);
	    			} else if(pLen < ChecksimsInitializer.MIN_PASSWORD_LEN) {
	    				JOptionPane.showMessageDialog(null, "Password must contain at least " + ChecksimsInitializer.MIN_PASSWORD_LEN + " " + (ChecksimsInitializer.MIN_PASSWORD_LEN == 1 ? "character" : "characters") + ".", "Password Too Short", JOptionPane.ERROR_MESSAGE);
	    			} else if(pLen > ChecksimsInitializer.MAX_PASSWORD_LEN) {
	    				JOptionPane.showMessageDialog(null, "Password must contain at most " + ChecksimsInitializer.MAX_PASSWORD_LEN + " " + (ChecksimsInitializer.MAX_PASSWORD_LEN == 1 ? "character" : "characters") + ".", "Password Too Long", JOptionPane.ERROR_MESSAGE);
	    			} else if(!password.equals(confirmPassword)) {
	    				JOptionPane.showMessageDialog(null, "Password and confirmation password do not match.", "Passwords Do Not Match", JOptionPane.ERROR_MESSAGE);
	    			} else {
	    				String[] usernames;
	    				try {
	    					usernames = service.getUsernames();
	    				} catch(Exception e) {
	    					JOptionPane.showMessageDialog(null, "Could not get " + service.getName() + " accounts.", service.getName() + " Error", JOptionPane.ERROR_MESSAGE);
	    					return;
	    				}
	    				for(String u : usernames) {
	    					if(username.equals(u)) {
	    						JOptionPane.showMessageDialog(null, "The username you entered is already taken.", "Username Taken", JOptionPane.ERROR_MESSAGE);
	    						return;
	    					}
	    				}
	    				self.service.onCreateNew(username, password);
	    			}
	    		}
	    }, FancyButtonColorTheme.HEADER));
		body.add(createAccountButton);
		
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
		add(body, c);
	}
	
	private String checkInvalidUsername(String username) {
		int len = username.length();
		for(int i = 0; i < len; i++) {
			char c = username.charAt(i);
			if(!(Character.isLetter(c) || Character.isDigit(c))) {
				return Character.toString(c);
			}
		}
		return null;
	}
}
