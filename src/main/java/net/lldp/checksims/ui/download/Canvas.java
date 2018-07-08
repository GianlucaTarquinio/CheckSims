package net.lldp.checksims.ui.download;

public class Canvas extends Service {
	public Canvas(String name, String folderName) {
		this.name = name;
		this.folderName = folderName;
	}
	
	@Override
	public void onCreateNew() {
		System.out.println("make a new " + name + " service"); //TEMPORARY
	}

	@Override
	public void onLogIn(String username) {
		System.out.println("log in to " + name + "from " + folderName + "/" + username);
	}
}
