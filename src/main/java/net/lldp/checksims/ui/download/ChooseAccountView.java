package net.lldp.checksims.ui.download;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import net.lldp.checksims.ui.ChecksimsColors;
import net.lldp.checksims.ui.ChecksimsInitializer;

public class ChooseAccountView extends JPanel {
	private ChecksimsInitializer app;
	
	public ChooseAccountView(ChecksimsInitializer app) {
		this.app = app;
		
		JPanel header = new JPanel();
		header.setBackground(ChecksimsColors.WPI_GREY);
		
		BorderLayout borderLayout = new BorderLayout();
		header.setLayout(borderLayout);
		
		JLabel title = new JLabel("Choose Account");
		title.setFont(new Font("Tahoma", Font.PLAIN, 40));
		title.setBorder(new EmptyBorder(10, 10, 10, 10));
		header.add(title, BorderLayout.LINE_START);
		
		JPanel body = new JPanel();
		JScrollPane scroll = new JScrollPane(body);removeAll();
		scroll.setBorder(new EmptyBorder(0, 0, 0, 0));
		body.setBackground(ChecksimsColors.PRETTY_GREY);
		
		GridBagConstraints c = new GridBagConstraints();
		GridBagLayout bodyLayout = new GridBagLayout();
		body.setLayout(bodyLayout);
		c = new GridBagConstraints();
		
		int count = 40;
		JLabel[] labels = new JLabel[count];
		for(int i = 0; i < count; i++) {
			labels[i] = new JLabel("WILL IT SCROLL???????????");
			c.gridx = 0;
			c.gridy = i;
			c.weightx = 1;
			c.weighty = 1;
			body.add(labels[i], c);
		}
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		setBackground(ChecksimsColors.PRETTY_GREY);
		c = new GridBagConstraints();
		
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
