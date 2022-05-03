package omegaui.codeblaze.ui.panel;
import omegaui.component.TextComp;

import omegaui.codeblaze.App;

import omegaui.codeblaze.io.ResizeAware;

import javax.swing.JPanel;

import static omegaui.codeblaze.io.UIXManager.*;
import static omegaui.codeblaze.io.AppDataProvider.*;
import static omegaui.component.animation.Animations.*;

public final class LauncherPanel extends JPanel implements ResizeAware{

	private App app;
	
	private TextComp appIconComp;
	private TextComp appTitleComp;
	private TextComp appVersionComp;

	// Actions

	private TextComp createFileComp;
	private TextComp openFileComp;
	
	public LauncherPanel(App app){
		this.app = app;
		initUI();
		manageBounds();
	}

	public void initUI(){
		setBackground(BACKGROUND);
		
		appIconComp = new TextComp(appIcon, BACKGROUND, BACKGROUND, BACKGROUND, null);
		appIconComp.setClickable(false);
		appIconComp.setArc(0, 0);
		add(appIconComp);

		appTitleComp = new TextComp("CodeBlaze", BACKGROUND, BACKGROUND, GLOW, null);
		appTitleComp.setFont(PX44);
		appTitleComp.setClickable(false);
		appTitleComp.setArc(0, 0);
		add(appTitleComp);

		appVersionComp = new TextComp(appVersionSemantic(), BACKGROUND, BACKGROUND, GLOW, null);
		appVersionComp.setFont(PX28);
		appVersionComp.setClickable(false);
		appVersionComp.setArc(0, 0);
		add(appVersionComp);

		initActions();
	}
	
	public void initActions(){
		createFileComp = new TextComp("Create New File", HOVER, BACKGROUND, tertiaryColor, ()->{
			app.getGlassPanel().putToView(GlassPanel.getFileCreationPanel());
		});
		createFileComp.setFont(PX18.bold());
		createFileComp.setArc(6, 6);
		add(createFileComp);
		
		openFileComp = new TextComp("Open a Local File", HOVER, BACKGROUND, tertiaryColor, null);
		openFileComp.setFont(PX18.bold());
		openFileComp.setArc(6, 6);
		add(openFileComp);
	}

	@Override
	public void manageBounds() {
		appIconComp.setBounds(getWidth()/2 - 200/2, getHeight()/2 - 400/2, 200, 200);
		appTitleComp.setBounds(getWidth()/2 - 200/2, appIconComp.getY() + appIconComp.getHeight(), 200, 60);
		appVersionComp.setBounds(getWidth()/2 - 150/2, appTitleComp.getY() + appTitleComp.getHeight(), 150, 30);

		createFileComp.setBounds(getWidth()/2 - 250/2, getHeight() - 150, 250, 30);
		openFileComp.setBounds(getWidth()/2 - 250/2, createFileComp.getY() + createFileComp.getHeight() + 5, 250, 30);
	}
	
	@Override
	public void layout(){
		super.layout();
		manageBounds();
	}
}
