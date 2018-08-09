/*
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License (the "License").
 * You may not use this file except in compliance with the License.
 *
 * See LICENSE.txt included in this distribution for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at LICENSE.txt.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 * Copyright (c) 2014-2016 Ted Meyer, Nicholas DeMarinis, Matthew Heon, and Dolan Murvihill
 */
package net.lldp.checksims.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import net.lldp.checksims.ui.buttons.FancyButtonAction;
import net.lldp.checksims.ui.buttons.FancyButtonColorTheme;
import net.lldp.checksims.ui.buttons.FancyButtonMouseListener;
import net.lldp.checksims.ui.download.Service;
import net.lldp.checksims.ui.download.canvas.CanvasService;

/**
 * 
 * @author ted
 *
 * Main class for checksims GUI
 */
public class ChecksimsInitializer extends JPanel
{
	public final static int MIN_USERNAME_LEN = 1;
	public final static int MAX_USERNAME_LEN = 25;
	public final static int MIN_PASSWORD_LEN = 5;
	public final static int MAX_PASSWORD_LEN = 100;
	public final static File DEFAULT_DOWNLOAD_PATH = new File(System.getProperty("user.home") + "/checksims/downloads");
	public final static File TEMPORARY_DOWNLOAD_PATH = new File(".temp");
	public final static String DEFAULT_CODE_SUFFIXES = ".java, .c, .h, .cpp, .py, .py3";
	
	private String sessionUsername = null;
	private Service sessionService = null;
	
    private final JFrame titleableFrame;
    private JPanel currentView = this;
    private MainMenu menu;
    private MainMenuView menuView;
    private final Service[] services = { new CanvasService(this, "Canvas", "canvas") };
    
    /**
     * Use this panel to show exceptions. Hide the other UI components
     * maybe show a helpful tip
     * @param e an exception to show
     * @param helpfulTip a custom tip to the user (maybe how to fix what they did wrong)
     */
    public void UhOhException(Exception e, String helpfulTip)
    {
        if (helpfulTip == null || helpfulTip != null)
        { // just show the exception
        		JPanel panel = new JPanel();
        		panel.setLayout(new BorderLayout());
            JTextPane jta = new JTextPane();
            jta.setBackground(panel.getBackground());
            jta.setBorder(new EmptyBorder(10, 10, 10, 10));
            jta.setEditable(false);
            panel.add(jta, BorderLayout.NORTH);
            
            JLabel backToMain = new JLabel("Back to Main Menu");
            backToMain.setFont(new Font(backToMain.getFont().getFontName(), Font.PLAIN, 20));
            backToMain.setBorder(new EmptyBorder(10, 10, 10, 10));
            backToMain.addMouseListener(new FancyButtonMouseListener(backToMain, new FancyButtonAction() {
	    	    		@Override
	    	    		public void performAction() {
	    	    			menuView.enableChecksimsButton();
	    	    			setTitle("Checksims Similarity Detector");
	    	    			goToMain();
	    	    		}
	    	    }, FancyButtonColorTheme.BROWSE));
            
            panel.add(backToMain, BorderLayout.SOUTH);
            StyledDocument doc = jta.getStyledDocument();
            if(e.getMessage() != null) {
	            try {
	            		doc.insertString(doc.getLength(), e.getMessage() + '\n' + '\n', null);
	            } catch(BadLocationException e1) {
	            		e.printStackTrace();
	            }
            }
            for(StackTraceElement ste : e.getStackTrace())
            {
                try
                {
                    doc.insertString(doc.getLength(), ste.toString() + '\n', null);
                }
                catch (BadLocationException e1)
                {
                    e1.printStackTrace();
                }
            }
            
            setPanel(panel);
            titleableFrame.setTitle("Checksims Error report. Please send this to the developers");
        }
    }
    
    /**
     * Used for catchall errors, usually the fault of developers rather than users
     * @param e just an exception
     */
    public void UhOhException(Exception e)
    {
        UhOhException(e, null);
    }

    private ChecksimsInitializer(JFrame f) throws IOException {
        titleableFrame = f;
        menu = new MainMenu();
        menuView = new MainMenuView(this, menu);
        setPanel(menuView);
    }
    
    public static void main(String ... args) throws IOException
    {
        JFrame f = new JFrame();
        f.setTitle("Checksims Similarity Detector");
        f.setMinimumSize(new Dimension(600, 350));
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new GridLayout(1, 1));
        f.add(new ChecksimsInitializer(f));
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    public JFrame getFrame() {
    		return this.titleableFrame;
    }
    
    public JFrame getWindow()
    {
        return this.titleableFrame;
    }
    
    public MainMenu getMenu() {
    		return menu;
    }
    
    public void setPanel(JPanel view) {
		if(currentView != null){
			titleableFrame.remove(currentView);
		}
		currentView = view;
		titleableFrame.setContentPane(currentView);
		currentView.setVisible(true);
		titleableFrame.setVisible(true);
	}
    
    public void goToMain() {
    		menu.setCurrentFIO(null);
    		menuView.updateSessionPanel();
    		setPanel(menuView);
    }
    
    public Service[] getServices() {
    		return services;
    }
    
    public String getSessionUsername() {
		return sessionUsername;
	}

	public void setSessionUsername(String sessionUsername) {
		this.sessionUsername = sessionUsername;
	}
    
    public Service getSessionService() {
		return sessionService;
	}

	public void setSessionService(Service sessionService) {
		this.sessionService = sessionService;
	}
	
	public void setTitle(String title) {
		titleableFrame.setTitle(title);
	}
}
