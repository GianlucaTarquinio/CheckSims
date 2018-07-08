package net.lldp.checksims.ui.download;

public abstract class Service {
	protected String name;
	protected String folderName;
	
	public abstract void onCreateNew();
	
	public abstract void onLogIn(String username);

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
}
