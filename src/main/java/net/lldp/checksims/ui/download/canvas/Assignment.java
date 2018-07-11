package net.lldp.checksims.ui.download.canvas;

public class Assignment {
	private int id;
	private String name;
	private Submission[] submissions;
	
	public Assignment(int id, String name) {
		this.id = id;
		this.name = name;
		submissions = null;
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
	
	public Submission[] getSubmissions() {
		return submissions;
	}
	
	public void setSubmissions(Submission[] submissions) {
		this.submissions = submissions;
	}
	
	@Override
	public String toString() {
		return "Assignment [id=" + id + ", name=" + name + "]";
	}
}
