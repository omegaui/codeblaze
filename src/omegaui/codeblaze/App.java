package omegaui.codeblaze;
import omegaui.codeblaze.io.ViewManager;

import javafx.stage.Stage;

import javafx.application.Application;
public class App extends Application{

	private Stage primaryStage;
	
	@Override
	public void start(Stage stage) {
		this.primaryStage = stage;
		
		ViewManager.init(this);
		
		stage.centerOnScreen();
		stage.setAlwaysOnTop(true);
		stage.show();
	}

	public javafx.stage.Stage getPrimaryStage() {
		return primaryStage;
	}
	
	public static void main(String[] args){
		launch(args);
	}
}
