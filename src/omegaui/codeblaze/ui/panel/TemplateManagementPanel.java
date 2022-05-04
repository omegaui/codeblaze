package omegaui.codeblaze.ui.panel;
import omegaui.codeblaze.App;

import omegaui.codeblaze.io.ResizeAware;

import javax.swing.JPanel;

import static omegaui.codeblaze.io.UIXManager.*;
import static omegaui.codeblaze.io.AppDataProvider.*;
import static omegaui.component.animation.Animations.*;

public final class TemplateManagementPanel extends JPanel implements ResizeAware{

	private App app;

	public TemplateManagementPanel(App app){
		this.app = app;
		initUI();
	}

	public void initUI(){
		
		setBackground(BACKGROUND);
		
	}

	@Override
	public void manageBounds() {
		
	}

	@Override
	public void layout(){
		super.layout();
		manageBounds();
	}

}
