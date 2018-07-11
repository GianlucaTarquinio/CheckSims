package net.lldp.checksims.ui.download;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import net.lldp.checksims.ui.ChecksimsInitializer;
import net.lldp.checksims.ui.buttons.FancyButtonAction;
import net.lldp.checksims.ui.buttons.FancyButtonColorTheme;
import net.lldp.checksims.ui.buttons.FancyButtonMouseListener;

public class AccountList extends JPanel {
	private ChecksimsInitializer app;
	private ChooseAccountView chooseAccountView;
	private Service service;
	private String[] usernames;
	private JPanel[] listItems;
	
	public AccountList(ChecksimsInitializer app, ChooseAccountView cav, Service service) throws Exception {
		this.app = app;
		chooseAccountView = cav;
		this.service = service;
		String name = service.getName();
		
		JPanel topBar = new JPanel();
		topBar.setBackground(Color.decode("#999999"));
		
		BorderLayout topBarLayout = new BorderLayout();
		topBar.setLayout(topBarLayout);
		
		JLabel nameLabel = new JLabel(name.substring(0,  1).toUpperCase() + name.substring(1));
		nameLabel.setFont(new Font(nameLabel.getFont().getFontName(), Font.PLAIN, 25));
		nameLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
		topBar.add(nameLabel, BorderLayout.WEST);
		
		JLabel addAccountButton = new JLabel("Add Account");
		addAccountButton.setFont(new Font(addAccountButton.getFont().getFontName(), Font.PLAIN, 25));
		addAccountButton.setBorder(new EmptyBorder(10, 10, 10, 10));
		AccountList self = this;
		addAccountButton.addMouseListener(new FancyButtonMouseListener(addAccountButton, new FancyButtonAction() {
	    		@Override
	    		public void performAction() {
	    			app.setPanel(new CreateAccountView(app, chooseAccountView, service));
	    		}
	    }, FancyButtonColorTheme.BROWSE));
		topBar.add(addAccountButton, BorderLayout.EAST);
		setBackground(Color.decode("#F5F5F5"));
		
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();
		
		c.weightx = 1;
		c.weighty = 1.3;
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		add(topBar, c);
		
		try {
			usernames = service.getUsernames();
		} catch(Exception e) {
			throw e;
		}

		c.weighty = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		listItems = new JPanel[usernames.length];
		if(usernames.length == 0) {
			c.gridy = 1;
			JLabel emptyMessage = new JLabel("You have no " + service.getName() + " accounts.");
			emptyMessage.setFont(new Font(emptyMessage.getFont().getFontName(), Font.PLAIN, 14));
			emptyMessage.setBorder(new EmptyBorder(10, 10, 10, 10));
			add(emptyMessage, c);
		} else {
			for(int i = 0; i < usernames.length; i++) {
				int index = i;
				
				JPanel listItem = new JPanel();
				listItem.setLayout(new BorderLayout());
				
				JLabel usernameLabel = new JLabel(usernames[i]);
				usernameLabel.setFont(new Font(usernameLabel.getFont().getFontName(), Font.PLAIN, 17));
				usernameLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
				listItem.add(usernameLabel, BorderLayout.WEST);
				
				JPanel buttonPanel = new JPanel();
				buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
				
				JLabel loginButton = new JLabel("Log In");
				loginButton.setFont(new Font(loginButton.getFont().getFontName(), Font.PLAIN, 17));
				loginButton.setBorder(new EmptyBorder(10, 10, 10, 10));
				loginButton.addMouseListener(new FancyButtonMouseListener(loginButton, new FancyButtonAction() {
			    		@Override
			    		public void performAction() {
			    			logIn(index);
			    		}
			    }, FancyButtonColorTheme.BROWSE));
				buttonPanel.add(loginButton);
				
				JLabel deleteButton = new JLabel("Delete");
				deleteButton.setFont(new Font(deleteButton.getFont().getFontName(), Font.PLAIN, 17));
				deleteButton.setBorder(new EmptyBorder(10, 10, 10, 10));
				deleteButton.addMouseListener(new FancyButtonMouseListener(deleteButton, new FancyButtonAction() {
			    		@Override
			    		public void performAction() {
			    			delete(index);
			    		}
			    }, FancyButtonColorTheme.DELETE));
				buttonPanel.add(deleteButton);
				
				listItem.add(buttonPanel, BorderLayout.EAST);
				
				c.gridy = i + 1;
				listItems[i] = listItem;
				add(listItems[i], c);
			}
		}
	}
	
	public void minimizePreferredSize() {
		setPreferredSize(new Dimension(getPreferredSize().width, getMinimumSize().height));
	}
	
	private void logIn(int index) {
		String data;
		String[] info;
		try {
			JPanel inputPanel = new JPanel();
			inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
			
			JLabel passwordLabel = new JLabel("Type the password for '" + usernames[index] + "'");
			passwordLabel.setAlignmentX(LEFT_ALIGNMENT);
			inputPanel.add(passwordLabel);
			
			JPasswordField passwordField = new JPasswordField(ChecksimsInitializer.MAX_USERNAME_LEN);
			passwordField.setAlignmentX(LEFT_ALIGNMENT);
			inputPanel.add(passwordField);
			
			String[] options = new String[]{ "OK", "Cancel" };
			int option = JOptionPane.showOptionDialog(null, inputPanel, "Log In To " + service.getName(), JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			if(option != 0) {
				return;
			} 
			
			char[] pass = passwordField.getPassword();
			if(pass.length == 0) {
				JOptionPane.showMessageDialog(null, "The password you entered is incorrect.", "Incorrect Password", JOptionPane.ERROR_MESSAGE);
			}
			
			info = service.getUserInfo(usernames[index]);
			data = Encryption.decrypt(info[0], new String(pass));
		} catch(Exception e) {
			app.UhOhException(e);
			return;
		}
		if(data == null || !Encryption.scryptCheck(data, info[1])) {
			JOptionPane.showMessageDialog(null, "The password you entered is incorrect.", "Incorrect Password", JOptionPane.ERROR_MESSAGE);
		} else {
			service.onLoggedIn(usernames[index], data);
		}
	}
	
	private void delete(int index) {
		Object[] options = { "OK", "Cancel" };
		int result = JOptionPane.showOptionDialog(null, "Are you sure you want to permanently delete '" + usernames[index] + "' from your '" + service.getName() + "' accounts?", "Delete '" + usernames[index] + "'", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
		if(result == 0) {
			boolean deleted;
			try {
				deleted = service.removeUser(usernames[index]);
			} catch(Exception e) {
				app.UhOhException(e);
				return;
			}
			if(deleted) {
				remove(listItems[index]);
				revalidate();
				minimizePreferredSize();
				getParent().revalidate();
			} else {
				JOptionPane.showMessageDialog(null, "'" + usernames[index] + "' could not be deleted.", "Delete Failed", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
