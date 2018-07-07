package net.lldp.checksims.ui.download;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.lldp.checksims.ui.ChecksimsInitializer;

public class ChooseAccountView extends JPanel {
	private ChecksimsInitializer app;
	
	public ChooseAccountView(ChecksimsInitializer app) {
		this.app = app;
		setBackground(new Color(250, 250, 210));
		
		BorderLayout borderLayout = new BorderLayout();
		setLayout(borderLayout);
		
		JLabel label = new JLabel("HELLO THERE");
		add(label, BorderLayout.CENTER);
	}
}
