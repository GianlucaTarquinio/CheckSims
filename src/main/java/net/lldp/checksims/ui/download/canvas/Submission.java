package net.lldp.checksims.ui.download.canvas;

import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.time.ZonedDateTime;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

public class Submission {
	private int id;
	private int user_id;
	private JsonArray attachments;
	
	public Submission(int id, int user_id, JsonArray attachments) {
		this.id = id;
		this.user_id = user_id;
		this.attachments = attachments;
	}
	
	public void downloadAttachments(String dirPath) {
		if(attachments == null) return;
		for(JsonValue jv : attachments) {
			try {
				JsonObject a = (JsonObject) jv;
				URL url = new URL(a.getString("url"));
				ReadableByteChannel rbc = Channels.newChannel(url.openStream());
				FileOutputStream fos = new FileOutputStream(dirPath +"/" + a.getString("uuid") + "_" + a.getString("display_name"));
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getUser_id() {
		return user_id;
	}
	
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	
	public JsonArray getAttachments() {
		return attachments;
	}
	
	public void setAttachments(JsonArray attachments) {
		this.attachments = attachments;
	}
	
	@Override
	public String toString() {
		return "Submission [id=" + id + ", user_id=" + user_id + "]";
	}
}
