package net.lldp.checksims.ui.download;

import java.io.File;

import net.lldp.checksims.ui.ChecksimsInitializer;

public abstract class Service {
	protected ChecksimsInitializer app;
	protected String name;
	protected String folderName;
	
	public Service(ChecksimsInitializer app, String name, String folderName) {
		this.app = app;
		this.name = name;
		this.folderName = folderName;
		File folder;
		
		try {
			folder = new File(getFolderPath());
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
			return;
		}
		
		if(!folder.exists()) {
			if(!folder.mkdirs()) {
				System.err.println("Couldn't make '" + folderName + "' directory.");
				System.exit(1);
			}
		}
	}
	
	public abstract void onCreateNew();
	
	public abstract void onLoggedIn(String data);

	public String getAccountsFolderPath() throws Exception {
		try {
			return new File(Service.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + "/accounts";
		} catch (Exception e) {
			throw e;
		}
	}
	
	public String getFolderPath() throws Exception {
		String parentPath;
		try {
			parentPath = getAccountsFolderPath();
		} catch (Exception e) {
			throw e;
		}
		return parentPath + "/" + folderName;
	}
	
	public String[] getUsernames() throws Exception {
		File folder;
		
		try {
			folder = new File(getFolderPath());
		} catch(Exception e) {
			throw e;
		}
		if(!(folder.exists() && folder.isDirectory())) {
			throw new Exception("Could not find '" + folderName + "' directory.");
		}
		
		File[] files = folder.listFiles();
		String[] usernames = new String[files.length];
		for(int i = 0; i < files.length; i++) {
			usernames[i] = files[i].getName();
		}
		return usernames;
	}
	
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
