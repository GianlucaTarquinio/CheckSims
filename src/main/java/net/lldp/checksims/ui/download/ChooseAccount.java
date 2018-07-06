package net.lldp.checksims.ui.download;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ChooseAccount extends JPanel {
	public ChooseAccount() {
		setBackground(new Color(250, 250, 210));
		
		BorderLayout borderLayout = new BorderLayout();
		setLayout(borderLayout);
		
		JLabel label = new JLabel("HELLO THERE");
		add(label, BorderLayout.CENTER);
	}
}
