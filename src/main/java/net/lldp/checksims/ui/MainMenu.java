package net.lldp.checksims.ui;

import java.util.ArrayList;

import net.lldp.checksims.ui.file.FileInputOption;
import net.lldp.checksims.ui.file.FileInputType;

public class MainMenu {
	private ArrayList<AccordionListEntry> sourceEntrys, archiveEntrys, commonEntrys;
	private long nextSourceId, nextArchiveId, nextCommonId;
	private FileInputOption currentFIO;
	
	public MainMenu() {
		sourceEntrys = new ArrayList<AccordionListEntry>();
		archiveEntrys = new ArrayList<AccordionListEntry>();
		commonEntrys = new ArrayList<AccordionListEntry>();
		nextSourceId = 0;
		nextArchiveId = 0;
		nextCommonId = 0;
		this.currentFIO = null;
	}
	
	public void addEntry(AccordionListEntry entry, FileInputType type) {
		switch(type) {
		case SOURCE:
			sourceEntrys.add(entry);
			nextSourceId++;
			break;
		case ARCHIVE:
			archiveEntrys.add(entry);
			nextArchiveId++;
			break;
		case COMMON:
			commonEntrys.add(entry);
			nextCommonId++;
			break;
		}
	}
	
	public void removeEntry(long ID, FileInputType type) {
		ArrayList<AccordionListEntry> entrys;
		switch(type) {
		case SOURCE:
			entrys = sourceEntrys;
			break;
		case ARCHIVE:
			entrys = archiveEntrys;
			break;
		case COMMON:
			entrys = commonEntrys;
			break;
		default:
			entrys = new ArrayList<AccordionListEntry>();
		}
		for(int i = 0; i < entrys.size(); i++) {
			if(entrys.get(i).getID() == ID) {
				entrys.remove(i);
				return;
			}
		}
	}
	
	public void setEntry(long ID, String path, FileInputType type) {
		ArrayList<AccordionListEntry> entrys;
		switch(type) {
		case SOURCE:
			entrys = sourceEntrys;
			break;
		case ARCHIVE:
			entrys = archiveEntrys;
			break;
		case COMMON:
			entrys = commonEntrys;
			break;
		default:
			entrys = new ArrayList<AccordionListEntry>();
		}
		for(int i = 0; i < entrys.size(); i++) {
			if(entrys.get(i).getID() == ID) {
				entrys.get(i).setPath(path);
				return;
			}
		}
	}
	
	public ArrayList<AccordionListEntry> getEntrys(FileInputType type) {
		switch(type) {
		case SOURCE:
			return sourceEntrys;
		case ARCHIVE:
			return archiveEntrys;
		case COMMON:
			return commonEntrys;
		}
		return new ArrayList<AccordionListEntry>();
	}

	public long getNextID(FileInputType type) {
		switch(type) {
		case SOURCE:
			return nextSourceId;
		case ARCHIVE:
			return nextArchiveId;
		case COMMON:
			return nextCommonId;
		}
		return 0;
	}
	
	public void setCurrentFIO(FileInputOption fio) {
		currentFIO = fio;
	}
	
	public void setCurrentFIOPath(String path) {
		if(currentFIO != null) {
			currentFIO.setPath(path);
		}
	}
}
