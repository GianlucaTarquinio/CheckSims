package net.lldp.checksims.ui.download;

import java.io.File;
import java.nio.file.Files;

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
	
	public abstract void onCreateNew(String username, String password);
	
	public abstract void onLoggedIn(String username, String data);

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
		
		return folder.list();
	}
	
	public String[] getUserInfo(String username) throws Exception {
		File file;
		try {
			file = new File(getFolderPath() + "/" + username);
		} catch(Exception e) {
			throw e;
		}
		
		if(!(file.exists() && file.isDirectory())) {
			throw new Exception("Could not find directory for '" + username + "'");
		}
		
		File data = new File(file.getPath() + "/data");
		File chk = new File(file.getPath() + "/chk");
		if(!(data.exists() && data.isFile())) {
			throw new Exception("Could not find data file for '" + username + "'");
		}
		if(!(chk.exists() && chk.isFile())) {
			throw new Exception("Could not find chk file for '" + username + "'");
		}
		
		return new String[] { new String(Files.readAllBytes(data.toPath())), new String(Files.readAllBytes(chk.toPath())) };
	}
	
	public void addUser(String username, String dataString, String chkString) throws Exception {
		File folder;
		try {
			folder = new File(getFolderPath() + "/" + username);
		} catch(Exception e) {
			throw e;
		}
		
		if(folder.exists()) {
			throw new Exception("User already exists");
		}
		
		if(!folder.mkdir()) {
			throw new Exception("Could not make folder for new user");
		}
		
		File data = new File(folder.getPath() + "/data");
		File chk = new File(folder.getPath() + "/chk");
		try {
			Files.write(data.toPath(), dataString.getBytes());
			Files.write(chk.toPath(), chkString.getBytes());
		} catch(Exception e) {
			throw e;
		}
	}
	
	public boolean removeUser(String username) throws Exception {
		File file;
		try {
			file = new File(getFolderPath() + "/" + username);
		} catch(Exception e) {
			throw e;
		}
		
		if(!(file.exists() && file.isDirectory())) {
			throw new Exception("Could not find directory for '" + username + "'");
		}
		
		File data = new File(file.getPath() + "/data");
		File chk = new File(file.getPath() + "/chk");
		if(!(data.exists() && data.isFile())) {
			throw new Exception("Could not find data file for '" + username + "'");
		}
		if(!(chk.exists() && chk.isFile())) {
			throw new Exception("Could not find chk file for '" + username + "'");
		}
		
		if(!(file.canWrite() || file.getParentFile().canWrite())) {
			return false;
		}
		
		data.delete();
		chk.delete();
		return file.delete();
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
