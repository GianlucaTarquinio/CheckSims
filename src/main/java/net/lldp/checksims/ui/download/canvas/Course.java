package net.lldp.checksims.ui.download.canvas;

public class Course {
	private int id;
	private String name;
	private String uuid;
	private Assignment[] assignments;
	
	public Course(int id, String name, String uuid) {
		this.id = id;
		this.name = name;
		this.uuid = uuid;
		assignments = null;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public Assignment[] getAssignments() {
		return assignments;
	}
	
	public void setAssignments(Assignment[] assignments) {
		this.assignments = assignments;
	}
	
	@Override
	public String toString() {
		return "Course [id=" + id + ", name=" + name + ", uuid=" + uuid + "]";
	}
}