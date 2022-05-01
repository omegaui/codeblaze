package omegaui.codeblaze.io.view;
import javafx.scene.image.ImageView;

import javafx.geometry.Pos;
import javafx.geometry.Insets;

import javafx.scene.text.Text;

import javafx.scene.Parent;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import static omegaui.codeblaze.io.UIXManager.*;

public class LauncherViewFX extends AbstractViewFX{

	private BorderPane pane;
	
	@Override
	public void init() {
		pane = new BorderPane();

		VBox vbox = new VBox();
		vbox.setPadding(new Insets(50));
		vbox.setSpacing(5);
		vbox.setAlignment(Pos.CENTER);

		ImageView appIconView = new ImageView(appImageIcon);
		vbox.getChildren().add(appIconView);
		
		Text appTitle = new Text("CodeBlaze");
		appTitle.setFont(PX32.bold().font());
		appTitle.setFill(GLOW);
		vbox.getChildren().add(appTitle);

		pane.setCenter(vbox);
	}

	@Override
	public Parent getRoot() {
		return pane;
	}
	
}
