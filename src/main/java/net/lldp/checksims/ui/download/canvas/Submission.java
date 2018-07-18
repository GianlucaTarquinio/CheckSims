package net.lldp.checksims.ui.download.canvas;

import java.awt.Component;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.time.ZonedDateTime;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

import net.lldp.checksims.ui.download.TurninConverter;

public class Submission {
	private int id;
	private int userId;
	private JsonArray attachments;
	private CanvasService canvasService;
	
	public Submission(int id, int userId, JsonArray attachments, CanvasService canvasService) {
		this.id = id;
		this.userId = userId;
		this.attachments = attachments;
		this.canvasService = canvasService;
	}
	
	public void downloadAttachments(String dirPath) {
		if(attachments == null) return;
		for(JsonValue jv : attachments) {
			try {
				JsonObject a = (JsonObject) jv;
				URL url = new URL(a.getString("url"));
				ReadableByteChannel rbc = Channels.newChannel(url.openStream());
				FileOutputStream fos = new FileOutputStream(dirPath + "/" + a.getString("display_name"));
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void download(File from, File to, String suffixes) throws Exception {
		char[] safeName = getName().toCharArray();
		char c;
		for(int i = 0; i < safeName.length; i++) {
			c = safeName[i];
			if(!(Character.isLetter(c) || Character.isDigit(c))) {
				safeName[i] = '_';
			} else if(Character.isUpperCase(c)) {
				safeName[i] = Character.toLowerCase(c);
			}
		}
		String folderName = new String(safeName);
		File toFolder;
		try {
			toFolder = new File(TurninConverter.getUnusedName(to, folderName));
		} catch(Exception e) {
			throw e;
		}
		if(!toFolder.mkdirs()) {
			throw new Exception("Could not make directory: '" + toFolder.getAbsolutePath() + "'");
		}
		File fromFolder = new File(from.getPath() + "/" + "submission");
		if(!fromFolder.mkdirs()) {
			throw new Exception("Could not make directory: '" + fromFolder.getAbsolutePath() + "'");
		}
		downloadAttachments(fromFolder.getPath());
		try {
			TurninConverter.formatSubmission(fromFolder, toFolder, suffixes);
		} catch(Exception e) {
			throw e;
		}
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public String getName() {
		return canvasService.getUserName(userId);
	}
	
	public JsonArray getAttachments() {
		return attachments;
	}
	
	public void setAttachments(JsonArray attachments) {
		this.attachments = attachments;
	}
	
	@Override
	public String toString() {
		return "Submission [id=" + id + ", userId=" + userId + "]";
	}
}
