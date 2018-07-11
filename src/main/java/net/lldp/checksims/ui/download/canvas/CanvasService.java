package net.lldp.checksims.ui.download.canvas;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import javax.json.Json;
import javax.json.JsonArray;
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
			app.setPanel(new CanvasSubmissionBrowser(app, this, username));
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
	
	public Course[] getCanvasData() {
		Course[] courses = getTeacherAndTACourses();
		for(Course c : courses) {
			c.setAssignments(getAssignments(c.getId()));
			for(Assignment a : c.getAssignments()) {
				a.setSubmissions(getSubmissions(c.getId(), a.getId()));
			}
		}
		return courses;
	}
	
	private Course[] getTeacherAndTACourses() {
		Course[] taCourses = getCourses("ta");
		Course[] teacherCourses = getCourses("teacher");
		
		HashMap<String, Course> duplicateMap = new HashMap<String, Course>();
		for(Course c : taCourses) {
			duplicateMap.put(c.getUuid(), c);
		}
		for(Course c : teacherCourses) {
			duplicateMap.put(c.getUuid(), c);
		}
		return duplicateMap.values().toArray(new Course[0]);
	}
	
	private Course[] getCourses(String type) {
		int status = 0;
		JsonArray courses;
		try {
			URL url = new URL(baseUrl + "/api/v1/courses?enrollment_type=" + type);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			setAuthHeader(con);
			status = con.getResponseCode();
			JsonReader jr = Json.createReader(con.getInputStream());
			courses = jr.readArray();
		} catch(Exception e) {
			if(status == 401) {
				if(authFailed) {
					authFailed = false;
					handleAuthFailure();
					return new Course[0];
				}
				authFailed = true;
				try {
					refreshAuth();
				} catch(Exception e1) {
					handleAuthFailure();
				}
				return getCourses(type);
			}
			e.printStackTrace();
			return new Course[0];
		}
		JsonObject course;
		int len = courses.size();
		Course result[] = new Course[len];
		for(int i = 0; i < len; i++) {
			course = (JsonObject) courses.get(i);
			result[i] = new Course(course.getInt("id"), course.getString("name"), course.getString("uuid"));
		}
		return result;
	}
	
	private Assignment[] getAssignments(int courseId) {
		int status = 0;
		JsonArray assignments;
		try {
			URL url = new URL(baseUrl + "/api/v1/courses/" + courseId + "/assignments");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			setAuthHeader(con);
			status = con.getResponseCode();
			JsonReader jr = Json.createReader(con.getInputStream());
			assignments = jr.readArray();
		} catch(Exception e) {
			if(status == 401) {
				if(authFailed) {
					authFailed = false;
					handleAuthFailure();
					return new Assignment[0];
				}
				authFailed = true;
				try {
					refreshAuth();
				} catch(Exception e1) {
					handleAuthFailure();
				}
				return getAssignments(courseId);
			}
			e.printStackTrace();
			return new Assignment[0];
		}
		JsonObject assignment;
		int len = assignments.size();
		Assignment result[] = new Assignment[len];
		for(int i = 0; i < len; i++) {
			assignment = (JsonObject) assignments.get(i);
			result[i] = new Assignment(assignment.getInt("id"), assignment.getString("name"));
		}
		return result;
	}
	
	private Submission[] getSubmissions(int courseId, int assignmentId) {
		int status = 0;
		JsonArray submissions;
		try {
			URL url = new URL(baseUrl + "/api/v1/courses/" + courseId + "/assignments/" + assignmentId + "/submissions");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			setAuthHeader(con);
			status = con.getResponseCode();
			JsonReader jr = Json.createReader(con.getInputStream());
			submissions = jr.readArray();
		} catch(Exception e) {
			if(status == 401) {
				if(authFailed) {
					authFailed = false;
					handleAuthFailure();
					return new Submission[0];
				}
				authFailed = true;
				try {
					refreshAuth();
				} catch(Exception e1) {
					handleAuthFailure();
				}
				return getSubmissions(courseId, assignmentId);
			}
			e.printStackTrace();
			return new Submission[0];
		}
		JsonObject submission;
		int len = submissions.size();
		Submission result[] = new Submission[len];
		for(int i = 0; i < len; i++) {
			submission = (JsonObject) submissions.get(i);
			result[i] = new Submission(submission.getInt("id"), submission.getInt("user_id"), submission.getJsonArray("attachments"));
		}
		return result;
	}
	
	private void handleAuthFailure() {
		JOptionPane.showMessageDialog(null, "This is most likely a temporary problem with Canvas.", "Authentication Failed", JOptionPane.ERROR_MESSAGE);
	}
	
	@Override
	public void onCreateNew(String username, String password) {
		startOAuth(username, password);
	}

	@Override
	public void onLoggedIn(String username, String data) {
		refreshToken = data;
		try {
			refreshAuth();
		} catch(Exception e) {
			String error = e.getMessage();
			if(error == null) {
				e.printStackTrace();
				error = "Your accuont was not created.";
			}
			JOptionPane.showMessageDialog(null, error, "Log In Failed.", JOptionPane.ERROR_MESSAGE);
		}
		app.setPanel(new CanvasSubmissionBrowser(app, this, username));
	}
}
