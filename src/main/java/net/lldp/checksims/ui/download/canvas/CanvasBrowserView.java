package net.lldp.checksims.ui.download.canvas;

import java.awt.Dimension;
import java.net.CookieHandler;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.sun.webkit.network.CookieManager;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class CanvasBrowserView extends JDialog {	
	public CanvasBrowserView(JFrame parent, CanvasService canvasService) {
		super(parent, "Log In To Canvas");
		setModal(true);
		setMinimumSize(new Dimension(800, 600));
        setResizable(false);
		setLocationRelativeTo(null);
		
		JFXPanel panel = new JFXPanel();
		
		Platform.setImplicitExit(false);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				CookieHandler.setDefault(new CookieManager());
				WebView webView = new WebView();
				WebEngine webEngine = webView.getEngine();
				webEngine.load(CanvasService.baseUrl + "/login/oauth2/auth?client_id=" + CanvasService.id + "&response_type=code&redirect_uri=urn:ietf:wg:oauth:2.0:oob");
			
				String accessCodeUrl = CanvasService.baseUrl + "/login/oauth2/auth?code=";
				String accessFailureUrl = CanvasService.baseUrl + "/login/oauth2/auth?error=access_denied";
				webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
					if (Worker.State.SUCCEEDED.equals(newValue)) {
		            		String url = webEngine.getLocation();
		            		if(url.startsWith(accessCodeUrl)) {
		            			setCode(canvasService, url.substring(accessCodeUrl.length()));
		                } else if(url.equals(accessFailureUrl)) {
		                		setCode(canvasService, null);
		                }
		            }
		        });
				
				panel.setScene(new Scene(webView));
			}	
		});
		
				
		getContentPane().add(panel);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	private void setCode(CanvasService canvasService, String code) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				canvasService.setAuthCode(code);
    				dispose();
			}
		});
	}
}
