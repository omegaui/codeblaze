package omegaui.codeblaze.io.view;
import io.github.palexdev.materialfx.enums.ButtonType;

import io.github.palexdev.materialfx.utils.NodeUtils;

import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXButton;

import javafx.scene.image.ImageView;

import javafx.geometry.Pos;
import javafx.geometry.Insets;

import javafx.scene.text.Text;

import javafx.scene.Parent;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

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

		Text appVersion = new Text("v1.0-alpha");
		appVersion.setFont(PX22.font());
		appVersion.setFill(GLOW);
		vbox.getChildren().add(appVersion);

		HBox hbox = new HBox();
		hbox.setPadding(new Insets(50));
		hbox.setAlignment(Pos.CENTER);

		MFXButton nextButton = new MFXButton("");
		nextButton.setButtonType(ButtonType.RAISED);
		nextButton.setGraphic(new ImageView(nextPageIcon));
		hbox.getChildren().add(nextButton);

		pane.setCenter(vbox);
		pane.setBottom(hbox);
	}

	@Override
	public Parent getRoot() {
		return pane;
	}

}
