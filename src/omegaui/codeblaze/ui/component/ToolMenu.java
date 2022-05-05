package omegaui.codeblaze.ui.component;
import java.awt.Dimension;

import omegaui.codeblaze.App;

import javax.swing.JPanel;

import static omegaui.codeblaze.io.UIXManager.*;
import static omegaui.codeblaze.io.AppDataProvider.*;
import static omegaui.component.animation.Animations.*;

public final class ToolMenu extends JPanel {

	private App app;

	public ToolMenu(App app){
		this.app = app;
		setPreferredSize(new Dimension(100, 100));
		initUI();
	}

	public void initUI(){

		setBackground(BACKGROUND);
		
	}

	
	
}
