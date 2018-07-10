package net.lldp.checksims.ui.download.canvas;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.swing.JOptionPane;

import net.lldp.checksims.ui.ChecksimsInitializer;
import net.lldp.checksims.ui.download.Encryption;
import net.lldp.checksims.ui.download.Service;

public class CanvasService extends Service {
	public static String id = "77820000000000045";
	private static String secret = "ttrQKCNzVTOw9KXYOfj0JAkpBFzZvg2ODjO6XWSRxiupI6rzFq2xojnpP1N34t6V";
	
	public static String baseUrl = "https://wpi.test.instructure.com";
	
	private String authCode = null;
	private String accessToken = null;
	private String refreshToken = null;
	
	private static boolean authFailed = false;
	
	public CanvasService(ChecksimsInitializer app, String name, String folderName) {
		super(app, name, folderName);
	}
	
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	
	private void startOAuth(String username, String password) {
		CanvasBrowserView browser = new CanvasBrowserView(app.getFrame(), this);
		browser.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {
				//Do nothing
			}

			@Override
			public void windowClosing(WindowEvent e) {
				//Do nothing
			}

			@Override
			public void windowClosed(WindowEvent e) {
				try {
					useAuthCode(username, password);
				} catch(Exception e1) {
					String error = e1.getMessage();
					if(error == null) {
						error = "Your account was not created.";
						e1.printStackTrace();
					}
					JOptionPane.showMessageDialog(null, error, "Account Creation Failed", JOptionPane.ERROR_MESSAGE);
				}
			}

			@Override
			public void windowIconified(WindowEvent e) {
				//Do nothing
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				//Do nothing
			}

			@Override
			public void windowActivated(WindowEvent e) {
				//Do nothing
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				//Do nothing
			}
		});
	}
	
	public void useAuthCode(String username, String password) throws Exception {
		String code = authCode;
		authCode = null;
		if(code == null) {
			throw new Exception("Canvas login failed.");
		}
		JsonObject response;
		try {
			String params = "grant_type=authorization_code&client_id=" + id + "&client_secret=" + secret + "&redirect_uri=urn:ietf:wg:oauth:2.0:oob&code=" + code;
			byte[] data = params.getBytes(StandardCharsets.UTF_8);
			URL url = new URL("https://wpi.test.instructure.com/login/oauth2/token");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
			con.setRequestProperty( "charset", "utf-8");
			con.setRequestProperty( "Content-Length", Integer.toString(data.length));
			con.setDoOutput(true);
			try(DataOutputStream dos = new DataOutputStream(con.getOutputStream())) {
			   dos.write(data);
			}
			JsonReader jr = Json.createReader(con.getInputStream());
			response = jr.readObject();
		} catch(Exception e) {
			throw e;
		}
		accessToken = response.getString("access_token");
		refreshToken = response.getString("refresh_token");
		try {
			saveToken(username, password);
		} catch(Exception e) {
			throw e;
		}
	}
	
	private void saveToken(String username, String password) throws Exception {
		String data = Encryption.encrypt(refreshToken, password);
		String chk = Encryption.scrypt(refreshToken);
		try {
			addUser(username, data, chk);
		} catch(Exception e) {
			throw e;
		}
	}
	
	private void setAuthHeader(HttpURLConnection c) {
		c.setRequestProperty("Authorization", "Bearer " + accessToken);
	}
	
	private void refreshAuth() throws Exception {
		JsonObject response;
		int status = 0;
		try {
			String params = "grant_type=refresh_token&client_id=" + id + "&client_secret=" + secret + "&refresh_token=" + refreshToken;
			byte[] data = params.getBytes(StandardCharsets.UTF_8);
			URL url = new URL("https://wpi.test.instructure.com/login/oauth2/token");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
			con.setRequestProperty( "charset", "utf-8");
			con.setRequestProperty( "Content-Length", Integer.toString(data.length));
			con.setDoOutput(true);
			try(DataOutputStream dos = new DataOutputStream(con.getOutputStream())) {
			   dos.write(data);
			}
			status = con.getResponseCode();
			JsonReader jr = Json.createReader(con.getInputStream());
			response = jr.readObject();
		} catch(Exception e) {
			if(status != 0) {
				System.out.println("Canvas token refresh response code was " + status);
				return;
			}
			throw e;
		}
		accessToken = response.getString("access_token");
	}
	
	@Override
	public void onCreateNew(String username, String password) {
		startOAuth(username, password);
	}

	@Override
	public String onLoggedIn(String data) {
		refreshToken = data;
		try {
			refreshAuth();
		} catch(Exception e) {
			String message = e.getMessage();
			if(message == null) {
				e.printStackTrace();
				return "Your accuont was not created.";
			}
			return message;
		}
		return null;
	}
}
