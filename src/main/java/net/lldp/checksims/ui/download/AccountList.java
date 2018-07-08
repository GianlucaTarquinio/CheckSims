package net.lldp.checksims.ui.download;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import net.lldp.checksims.ui.ChecksimsInitializer;
import net.lldp.checksims.ui.buttons.FancyButtonAction;
import net.lldp.checksims.ui.buttons.FancyButtonColorTheme;
import net.lldp.checksims.ui.buttons.FancyButtonMouseListener;

public class AccountList extends JPanel {
	private ChecksimsInitializer app;
	private Service service;
	
	public AccountList(ChecksimsInitializer app, Service service) throws Exception {
		this.app = app;
		this.service = service;
		String name = service.getName();
		
		JPanel topBar = new JPanel();
		topBar.setBackground(Color.decode("#999999"));
		
		BorderLayout topBarLayout = new BorderLayout();
		topBar.setLayout(topBarLayout);
		
		JLabel nameLabel = new JLabel(name.substring(0,  1).toUpperCase() + name.substring(1));
		nameLabel.setFont(new Font(nameLabel.getFont().getFontName(), Font.PLAIN, 25));
		nameLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
		topBar.add(nameLabel, BorderLayout.LINE_START);
		
		JLabel addAccountButton = new JLabel("Add Account");
		addAccountButton.setFont(new Font(addAccountButton.getFont().getFontName(), Font.PLAIN, 25));
		addAccountButton.setBorder(new EmptyBorder(10, 10, 10, 10));
		AccountList self = this;
		addAccountButton.addMouseListener(new FancyButtonMouseListener(addAccountButton, new FancyButtonAction() {
	    		@Override
	    		public void performAction() {
	    			self.service.onCreateNew();
	    		}
	    }, FancyButtonColorTheme.BROWSE));
		topBar.add(addAccountButton, BorderLayout.LINE_END);
		
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
		
		String[] usernames;
		try {
			usernames = service.getUsernames();
		} catch(Exception e) {
			throw e;
		}

		c.weighty = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		JPanel[] listItems = new JPanel[usernames.length];
		for(int i = 0; i < usernames.length; i++) {
			JPanel listItem = new JPanel();
			listItem.setLayout(new BorderLayout());
			JLabel usernameLabel = new JLabel(usernames[i]);
			usernameLabel.setFont(new Font(usernameLabel.getFont().getFontName(), Font.PLAIN, 17));
			usernameLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
			listItem.add(usernameLabel, BorderLayout.LINE_START);
			
			c.weighty = 1;
			c.gridy = i + 1;
			listItems[i] = listItem;
			add(listItems[i], c);
		}
	}
}
