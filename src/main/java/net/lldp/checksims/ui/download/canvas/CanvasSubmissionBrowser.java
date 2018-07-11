package net.lldp.checksims.ui.download.canvas;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.lldp.checksims.ui.ChecksimsInitializer;

public class CanvasSubmissionBrowser extends JPanel {
	ChecksimsInitializer app;
	CanvasService canvasService;
	Course[] courses;
	
	public CanvasSubmissionBrowser(ChecksimsInitializer app, CanvasService canvasService, String username) {
		this.app = app;
		this.canvasService = canvasService;
		
		courses = canvasService.getCanvasData();
		
		add(new JLabel("Hello"));
		System.out.println(buildDataString(courses));
	}
	
	private String buildDataString(Course[] courses) {
		String data = "";
		for(Course c : courses) {
			data += c.toString() + '\n';
			for(Assignment a : c.getAssignments()) {
				data += '\t' + a.toString() + '\n';
				for(Submission s : a.getSubmissions()) {
					data += "\t\t" + s.toString() + '\n';
				}
			}
		}
		return data.substring(0, data.length() - 1);
	}
}
