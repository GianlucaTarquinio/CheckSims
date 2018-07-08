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
package net.lldp.checksims.ui.file;

import java.awt.Dimension;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.Image;

import net.lldp.checksims.ui.ChecksimsInitializer;
import net.lldp.checksims.ui.buttons.FancyButtonAction;
import net.lldp.checksims.ui.buttons.FancyButtonColorTheme;
import net.lldp.checksims.ui.buttons.FancyButtonMouseListener;
import net.lldp.checksims.ui.download.ChooseAccountView;

/**
 * A simple UI for file picking with a cancel, view, and browse display
 * used in accordions for batch file picking
 * @author ted
 *
 */
public class FileInputOption extends JPanel
{
    private static class FieldEditorAction implements FancyButtonAction
    {
        private final JTextField path;
        private final JFileChooser fc;
        private long ID;
        private ChecksimsInitializer app;
        private FileInputType type;
        
        public FieldEditorAction(JTextField path, ChecksimsInitializer app, long ID, FileInputType type)
        {
        		this.app = app;
            this.path = path;
            this.ID = ID;
            this.type = type;
            
            fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fc.setCurrentDirectory(new java.io.File("."));
            fc.setDialogTitle("title");
            fc.setAcceptAllFileFilterUsed(false);
        }
        
        @Override
        public void performAction()
        {
            int returnVal = fc.showOpenDialog(path);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                path.setText(file.getAbsolutePath());
                app.getMenu().setEntry(ID, path.getText(), type);
            }
        }
        
    }
    
    private final long ID;
    private final JTextField path;
    private ChecksimsInitializer app;
    private FileInputType type;
    
    /**
     * create a default FileInputOption UI
     * @param parent the parent accordion
     * @param ID the numeric ID of this option
     * @param height the height of this option
     * @param width the width of this option
     * @param hasDownloadButton true if this option should have a download button
     */
    public FileInputOption(FileInputOptionAccordionList parent, ChecksimsInitializer a, long ID, int height, int width, FileInputType type)
    {
    		app = a;
        this.ID = ID;
        this.type = type;
        FileInputOption self = this;
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        
        JLabel close = new JLabel(" x ", SwingConstants.CENTER);
        JLabel browse = new JLabel(" ... ", SwingConstants.CENTER);
        
        path = new JTextField();
        
        path.setEditable(false);
        
        close.setPreferredSize(new Dimension(height, height));
        browse.setPreferredSize(new Dimension(height, height));
        
        close.setMinimumSize(new Dimension(height, height));
        browse.setMinimumSize(new Dimension(height, height));
        
        close.setMaximumSize(new Dimension(height, height));
        browse.setMaximumSize(new Dimension(height, height));
        
        path.setPreferredSize(new Dimension(width-2*height, height));
        setPreferredSize(new Dimension(width, height));
        
        browse.addMouseListener(new FancyButtonMouseListener(browse, new FieldEditorAction(path, app, ID, type), FancyButtonColorTheme.BROWSE));
        close.addMouseListener(new FancyButtonMouseListener(close, new FancyButtonAction(){
            @Override
            public void performAction()
            {
                parent.remove(self);
            }
        }, FancyButtonColorTheme.CLOSE));
        
        add(close);
        add(path);
        add(browse);       
        if(type == FileInputType.SOURCE) {
	    		int iconHeight = Math.max(height - 10, 1);
	    		ImageIcon downloadImage = new ImageIcon(new ImageIcon(getClass().getResource("/net/lldp/checksims/ui/download_icon.png")).getImage().getScaledInstance(iconHeight, iconHeight, Image.SCALE_SMOOTH), "Download files");
	    		JLabel download = new JLabel(downloadImage);
	    		download.setPreferredSize(new Dimension(height, height));
	    		download.setMinimumSize(new Dimension(height, height));
	    		download.setMaximumSize(new Dimension(height, height));
	    		
	    		download.addMouseListener(new FancyButtonMouseListener(download, new FancyButtonAction() {
	        		@Override
	        		public void performAction() {
	        			try {
	        				app.setPanel(new ChooseAccountView(app));
	        			} catch(Exception e) {
	        				app.UhOhException(e);
	        			}
	        		}
	        }, FancyButtonColorTheme.BROWSE));
	    		
	    		add(download);
	    }
    }

    /**
     * @return the ID of this FileInputOption
     */
    public long getID()
    {
        return ID;
    }

    /**
     * get the file from this option
     * @return get the file that was returned by the picker
     */
    public File asFile()
    {
        return new File(path.getText());
    }
    
    public String getPath() {
    		return path.getText();
    }
    
    public void setPath(String path) {
    		this.path.setText(path);
    }
    
    public FileInputType getType() {
    		return type;
    }
}
