package net.lldp.checksims.ui.download;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.lldp.checksims.ui.ChecksimsInitializer;

public class AccountList extends JPanel {
	private ChecksimsInitializer app;
	private String name;
	
	public AccountList(ChecksimsInitializer a, String n) {
		app = a;
		name = n.substring(0, 1).toUpperCase() + n.substring(1).toLowerCase();
		
		JLabel label = new JLabel(name);
		add(label);
	}
}
