package net.lldp.checksims.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.lldp.checksims.algorithm.AlgorithmRegistry;
import net.lldp.checksims.algorithm.SimilarityDetector;
import net.lldp.checksims.parse.Percentable;
import net.lldp.checksims.ui.buttons.FancyButtonAction;
import net.lldp.checksims.ui.buttons.FancyButtonColorTheme;
import net.lldp.checksims.ui.buttons.FancyButtonMouseListener;
import net.lldp.checksims.ui.download.Service;
import net.lldp.checksims.ui.file.FileInputOptionAccordionList;
import net.lldp.checksims.ui.file.FileInputType;
import net.lldp.checksims.ui.help.Direction;
import net.lldp.checksims.ui.help.DocumentationProvider;
import net.lldp.checksims.ui.help.DocumentationProviderPanel;
import net.lldp.checksims.ui.help.DocumentationProviderRegistry;
import net.lldp.checksims.ui.lib.BubbleUpEventDispatcher;

public class MainMenuView extends JPanel {
	ChecksimsInitializer app;
	MainMenu menu;
	JPanel sessionPanel = null;
	JLabel usernameLabel = null;
	JLabel logOutButton = null;
	
	public MainMenuView(ChecksimsInitializer a, MainMenu m) throws IOException {
		app = a;
		menu = m;
		JFrame f = app.getFrame();
		JButton checkSims = new JButton("Run CheckSims!");
        JButton helpMode = new JButton("Enable Help");
        
        JList<SimilarityDetector<? extends Percentable>> list =
                new JList<SimilarityDetector<? extends Percentable>>(
                        new Vector<SimilarityDetector<? extends Percentable>>(
                                AlgorithmRegistry.getInstance().getSupportedImplementations()));
        
        InputStream stream = ChecksimsInitializer.class.getResourceAsStream("/net/lldp/checksims/ui/logo.png");
        BufferedImage logoIMG = ImageIO.read(stream);
        
        JPanel logo = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                g.drawImage(logoIMG, 0, 0, null);
            }
        };
        logo.setMinimumSize(new Dimension(600, 175));
        logo.setMaximumSize(new Dimension(600, 175));
        logo.setPreferredSize(new Dimension(600, 175));
        
        JPanel selectors = new JPanel();
        FileInputOptionAccordionList subs = new FileInputOptionAccordionList(app, selectors, FileInputType.SOURCE);
        for(AccordionListEntry e : menu.getEntrys(FileInputType.SOURCE)) {
        		subs.addFIO(e.getID(), e.getPath(), FileInputType.SOURCE);
        }
        FileInputOptionAccordionList archs = new FileInputOptionAccordionList(app, selectors, FileInputType.ARCHIVE);
        for(AccordionListEntry e : menu.getEntrys(FileInputType.ARCHIVE)) {
	    		archs.addFIO(e.getID(), e.getPath(), FileInputType.ARCHIVE);
	    }
        FileInputOptionAccordionList common = new FileInputOptionAccordionList(app, selectors, FileInputType.COMMON, FileInputOptionAccordionList.SingleInput);
        for(AccordionListEntry e : menu.getEntrys(FileInputType.COMMON)) {
	    		common.addFIO(e.getID(), e.getPath(), FileInputType.COMMON);
	    }
        JPanel bot = new JPanel();
        
        subs.setBackground(ChecksimsColors.WPI_GREY);
        archs.setBackground(ChecksimsColors.WPI_GREY);
        bot.setBackground(ChecksimsColors.WPI_GREY);
        
        bot.add(checkSims);
        bot.add(helpMode);
        
        helpMode.addActionListener(new ActionListener() {
            boolean toggle = false;
            @Override
            public void actionPerformed(ActionEvent e)
            {
                toggle = !toggle;
                for(DocumentationProvider dp : DocumentationProviderRegistry.getAll())
                {
                    if (toggle)
                    {
                        dp.enableHelpMode();
                        helpMode.setText("Disable Help");
                    }
                    else
                    {
                        dp.disableHelpMode();
                        helpMode.setText("Enable Help");
                    }
                }
                f.revalidate();
                f.repaint();
            }
        });
        JPanel UI = new JPanel();
        selectors.setMinimumSize(new Dimension(400, 175));
        selectors.setPreferredSize(new Dimension(400, 175));
        selectors.setLayout(new BoxLayout(selectors, BoxLayout.Y_AXIS));
        selectors.add(subs);
        selectors.add(archs);
        selectors.add(common);
        
        DocumentationProviderPanel algorithm = new DocumentationProviderPanel() {
            @Override
            public Direction getDialogDirection()
            {
                return Direction.EAST;
            }

            @Override
            public String getMessageContents()
            {
                return "Select an Algorithm to use";
            }
        };
        algorithm.setMinimumSize(new Dimension(200, 175));
        algorithm.setPreferredSize(new Dimension(200, 175));
        list.setMinimumSize(new Dimension(200, 175));
        algorithm.add(list, BorderLayout.CENTER);
        list.addMouseListener(new BubbleUpEventDispatcher(algorithm));
        
        UI.setLayout(new BoxLayout(UI, BoxLayout.X_AXIS));
        UI.add(algorithm);
        UI.add(selectors);
        
        sessionPanel = new JPanel();
        sessionPanel.setLayout(new BorderLayout());
        sessionPanel.setBackground(ChecksimsColors.PRETTY_GREY);
        
        usernameLabel = new JLabel("HELLO");
        usernameLabel.setFont(new Font(usernameLabel.getFont().getFontName(), Font.PLAIN, 13));
        usernameLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        logOutButton = new JLabel("Log Out");
        logOutButton.setFont(new Font(logOutButton.getFont().getFontName(), Font.PLAIN, 13));
        logOutButton.setBorder(new EmptyBorder(5, 5, 5, 5));
        MainMenuView self = this;
        logOutButton.addMouseListener(new FancyButtonMouseListener(logOutButton, new FancyButtonAction() {
	    		@Override
	    		public void performAction() {
	    			self.app.setSessionUsername(null);
	    			self.app.setSessionService(null);
	    			updateSessionPanel();
	    		}
	    }, FancyButtonColorTheme.BROWSE));
        
        sessionPanel.add(usernameLabel, BorderLayout.WEST);
        sessionPanel.add(logOutButton, BorderLayout.EAST);
        sessionPanel.setVisible(false);
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(sessionPanel);
        add(logo);
        add(UI);
        add(bot);
        
        checkSims.addActionListener(new RunChecksimsListener(app, list, subs, archs, common));
	}
	
	public void updateSessionPanel() {
		String username = app.getSessionUsername();
		Service service = app.getSessionService();
		if(username == null || service == null) {
			sessionPanel.setVisible(false);
			usernameLabel.setText("");
		} else {
			usernameLabel.setText("Logged in as: " + username + " (" + service.getName() + ")");
			sessionPanel.setVisible(true);
		}
	}
}
