package net.lldp.checksims.ui;

public class AccordionListEntry {
	private String path;
	private long ID;
	
	public AccordionListEntry(long ID) {
		path = null;
		this.ID = ID;
	}
	
	public AccordionListEntry(String path, long ID) {
		this.path = path;
		this.ID = ID;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getID() {
		return ID;
	}

	public void setID(long ID) {
		this.ID = ID;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccordionListEntry other = (AccordionListEntry) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
}
