package net.lldp.checksims.ui.download;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Group;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import net.lldp.checksims.ui.ChecksimsColors;
import net.lldp.checksims.ui.ChecksimsInitializer;
import net.lldp.checksims.ui.buttons.FancyButtonAction;
import net.lldp.checksims.ui.buttons.FancyButtonColorTheme;
import net.lldp.checksims.ui.buttons.FancyButtonMouseListener;

public class ChooseAccountView extends JPanel {
	private ChecksimsInitializer app;
	
	public ChooseAccountView(ChecksimsInitializer app) throws Exception {
		this.app = app;
		
		JPanel header = new JPanel();
		header.setBackground(ChecksimsColors.WPI_GREY);
		
		BorderLayout borderLayout = new BorderLayout();
		header.setLayout(borderLayout);
		
		JLabel title = new JLabel("Choose Account");
		title.setFont(new Font(title.getFont().getFontName(), Font.PLAIN, 40));
		title.setBorder(new EmptyBorder(10, 10, 10, 10));
		header.add(title, BorderLayout.WEST);
		
		int iconHeight = Math.max(title.getMinimumSize().height - 10, 1);
		ImageIcon homeImage = new ImageIcon(new ImageIcon(getClass().getResource("/net/lldp/checksims/ui/home_icon.png")).getImage().getScaledInstance((int) Math.floor(iconHeight * 1.7778), iconHeight, Image.SCALE_SMOOTH), "Go home.");		
		JLabel goHome = new JLabel(homeImage);
		goHome.setBorder(new EmptyBorder(10, 10, 10, 10));
		ChooseAccountView self = this;
		goHome.addMouseListener(new FancyButtonMouseListener(goHome, new FancyButtonAction() {
	    		@Override
	    		public void performAction() {
	    			self.app.goToMain();
	    		}
	    }, FancyButtonColorTheme.BROWSE));
		header.add(goHome, BorderLayout.EAST);
		
		Service[] services = app.getServices();
		AccountList[] accountLists = new AccountList[services.length];
		
		JPanel body = new JPanel();
		JScrollPane scroll = new JScrollPane(body);
		scroll.setBorder(new EmptyBorder(0, 0, 0, 0));
		body.setBackground(ChecksimsColors.PRETTY_GREY);
		
		GroupLayout bodyLayout = new GroupLayout(body);
		body.setLayout(bodyLayout);
		
		Group horizontalGroup = bodyLayout.createParallelGroup();
		Group verticalGroup = bodyLayout.createSequentialGroup();
		
		int height;
		for(int i = 0; i < services.length; i++) {
			try {
				accountLists[i] = new AccountList(app, services[i]);
			} catch(Exception e) {
				throw e;
			}
			height = accountLists[i].getMinimumSize().height;
			accountLists[i].minimizePreferredSize();
			horizontalGroup.addComponent(accountLists[i]);
			verticalGroup.addComponent(accountLists[i], GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE);
		}
		
		bodyLayout.setAutoCreateGaps(true);
		bodyLayout.setAutoCreateContainerGaps(true);
		
		bodyLayout.setHorizontalGroup(horizontalGroup);
		bodyLayout.setVerticalGroup(verticalGroup);
		
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		setBackground(ChecksimsColors.PRETTY_GREY);
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;	
		add(header, c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 1000;
		c.fill = GridBagConstraints.BOTH;
		add(scroll, c);
	}
	
}
