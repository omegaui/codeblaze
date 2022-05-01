package omegaui.codeblaze.io;
import omegaui.codeblaze.io.view.LauncherViewFX;

import omegaui.codeblaze.App;

import javafx.scene.Scene;
public final class ViewManager {
	
	private static App app;
	
	private static Scene scene;

	//Available Roots
	private static LauncherViewFX launcherViewFX;

	public static void init(App app){
		ViewManager.app = app;
		initStartup();
	}

	public static void initStartup(){

		launcherViewFX = new LauncherViewFX();
		
		scene = new Scene(launcherViewFX.getRoot());
		
		app.getPrimaryStage().setScene(scene);
		
	}

	public static javafx.scene.Scene getScene() {
		return scene;
	}
	
}
