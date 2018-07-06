package net.lldp.checksims.ui.file;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

import net.lldp.checksims.ui.AccordionListEntry;
import net.lldp.checksims.ui.ChecksimsInitializer;
import net.lldp.checksims.ui.MainMenu;
import net.lldp.checksims.ui.help.Direction;
import net.lldp.checksims.ui.help.DocumentationProviderPanel;
import net.lldp.checksims.ui.lib.BubbleUpEventDispatcher;

public class FileInputOptionAccordionList extends DocumentationProviderPanel
{
    public static final boolean SingleInput = false;
    private final SortedSet<FileInputOption> fios;
    private final JButton click; // TODO replace with fancy button
    private final JFrame superParent;
    private final JComponent parent;
    private final Boolean multiselect;
    private final String typeText;
    ChecksimsInitializer app;
    
    public FileInputOptionAccordionList(ChecksimsInitializer app, JComponent parent, FileInputType type)
    {
        this(app, parent, type, true);
    }
    
    public FileInputOptionAccordionList(ChecksimsInitializer a, JComponent selectors, FileInputType type, boolean b)
    {
    		app = a;
    		JFrame f = a.getFrame();
        fios = new TreeSet<>(new Comparator<FileInputOption>(){
            @Override
            public int compare(FileInputOption a, FileInputOption b)
            {
                return (int) (a.getID() - b.getID());
            }
        });
        
        typeText = getTypeText(type);
        click = new JButton(typeText + ": Add a directory or turnin zip file");
        FileInputOptionAccordionList self = this;
        click.addMouseListener(new BubbleUpEventDispatcher(this, new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent ae)
            {
                click.setEnabled(multiselect);
                FileInputOption fio = new FileInputOption(self, app, app.getMenu().getNextID(type), 50, 400, type);
                fio.addMouseListener(new BubbleUpEventDispatcher(self));
                fios.add(fio);
                app.getMenu().addEntry(new AccordionListEntry(fio.getPath(), fio.getID()), type);
                Dimension d = parent.getPreferredSize();
                d.setSize(d.getWidth(), d.getHeight()+50);
                parent.setPreferredSize(d);
                
                repopulate();
                
                click.setText(typeText + ": Add another directory or turnin zip file");
            }
            
        }));
        
        superParent = f;
        parent = selectors;
        multiselect = b;
        
        repopulate();
    }
    
    public void addFIO(long ID, String path, FileInputType type) {
    		FileInputOption fio = new FileInputOption(this, app, ID, 50, 400, type);
    		fio.setPath(path);
    		fios.add(fio);
    }

    private void repopulate()
    {
        removeAll();
        setLayout(new GridLayout(fios.size()+1, 1));
        for(FileInputOption fio : fios)
        {
            add(fio);
        }
        add(click);
        Dimension newsize = new Dimension(400, 50 * (fios.size()+1));
        setPreferredSize(newsize);
        superParent.pack();
    }
    
    public void remove(FileInputOption fio)
    {
        fios.remove(fio);
        app.getMenu().removeEntry(fio.getID(), fio.getType());
        Dimension d = parent.getPreferredSize();
        d.setSize(d.getWidth(), d.getHeight()-50);
        parent.setPreferredSize(d);
        click.setEnabled(true);
        repopulate();
    }

    public Set<File> getFileSet()
    {
        Set<File> result = new HashSet<>();
        for(FileInputOption fio : fios)
        {
            File f = fio.asFile();
            if (f == null)
            {
                return null;
            }
            result.add(f);
        }
        return result;
    }
    
    private String getTypeText(FileInputType type) {
    		switch(type) {
    		case SOURCE:
    			return "SOURCE";
    		case ARCHIVE:
    			return "ARCHIVE";
    		case COMMON:
    			return "COMMON CODE";
    		}
    		return "";
    }

    @Override
    public Direction getDialogDirection()
    {
        return Direction.NORTH;
    }

    @Override
    public String getMessageContents()
    {
        return "Use these buttons to select the current class submissions and optionally the archived submissions and common code";
    }
}
