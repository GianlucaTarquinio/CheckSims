package net.lldp.checksims.ui.download.canvas;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.concurrent.Worker;
import javafx.application.Platform;

public class OAuthLogin extends Application {
	private static CanvasService canvasService;
	
	public static void create(CanvasService canvasService) {
		OAuthLogin.canvasService = canvasService;
		Application.launch();
	}
	
	@Override
	public void start(Stage stage) {
		WebView webView = new WebView();
		WebEngine webEngine = webView.getEngine();
		webEngine.load(CanvasService.baseUrl + "/login/oauth2/auth?client_id=" + CanvasService.id + "&response_type=code&redirect_uri=urn:ietf:wg:oauth:2.0:oob");
	
		String accessCodeUrl = CanvasService.baseUrl + "/login/oauth2/auth?code=";
		String accessFailureUrl = CanvasService.baseUrl + "/login/oauth2/auth?error=access_denied";
		webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (Worker.State.SUCCEEDED.equals(newValue)) {
            		String url = webEngine.getLocation();
            		if(url.startsWith(accessCodeUrl)) {
            			stage.close();
            			canvasService.setAuthCode(url.substring(accessCodeUrl.length()));
                } else if(url.equals(accessFailureUrl)) {
                		stage.close();
                }
            }
        });
		
		VBox vbox = new VBox();
		vbox.getChildren().add(webView);
		Scene scene = new Scene(vbox);
		stage.setScene(scene);
		stage.show();
	}
}
