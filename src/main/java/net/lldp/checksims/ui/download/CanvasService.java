package net.lldp.checksims.ui.download;

import net.lldp.checksims.ui.ChecksimsInitializer;

public class CanvasService extends Service {
	public CanvasService(ChecksimsInitializer app, String name, String folderName) {
		super(app, name, folderName);
	}
	
	@Override
	public void onCreateNew(String username, String password) {
		System.out.println("make a new account called '" + username + "' with password '" + password + "'"); //TEMPORARY
	}

	@Override
	public void onLoggedIn(String data) {
		System.out.println("Logging in with " + data); //TEMPORARY
	}
}
