package net.lldp.checksims.ui.download;

import net.lldp.checksims.ui.ChecksimsInitializer;

public class CanvasService extends Service {
	public CanvasService(ChecksimsInitializer app, String name, String folderName) {
		super(app, name, folderName);
	}
	
	@Override
	public void onCreateNew() {
		System.out.println("make a new " + name + " account"); //TEMPORARY
	}

	@Override
	public void onLoggedIn(String data) {
		System.out.println("Logging in with " + data); //TEMPORARY
	}
}
