package omegaui.codeblaze.ui.panel;
import omegaui.codeblaze.ui.component.TextInputField;


import omegaui.component.TextComp;
import omegaui.component.EdgeComp;

import omegaui.codeblaze.App;

import javax.swing.JPanel;

import omegaui.codeblaze.io.ResizeAware;

import static omegaui.codeblaze.io.UIXManager.*;
import static omegaui.codeblaze.io.AppDataProvider.*;
import static omegaui.component.animation.Animations.*;

public final class FileCreationPanel extends JPanel implements ResizeAware{

	private App app;

	private TextComp iconComp;
	private TextComp titleComp;
	private TextComp closeComp;

	private EdgeComp fileNameLabel;
	private TextInputField fileNameField;

	private EdgeComp parentDirLabel;
	private TextInputField parentDirField;

	public FileCreationPanel(App app){
		this.app = app;
		initUI();
	}

	public void initUI(){
		setBackground(BACKGROUND);
		
		iconComp = new TextComp(fileIcon, 64, 64, BACKGROUND, BACKGROUND, BACKGROUND, null);
		iconComp.setFont(PX32.bold());
		iconComp.setArc(0, 0);
		iconComp.setClickable(false);
		add(iconComp);
		
		titleComp = new TextComp("Create New File", BACKGROUND, BACKGROUND, GLOW, null);
		titleComp.setFont(PX32.bold());
		titleComp.setArc(0, 0);
		titleComp.setClickable(false);
		add(titleComp);
		
		closeComp = new TextComp(cancelIcon, 32, 32, BACKGROUND, BACKGROUND, BACKGROUND, ()->{
			app.getGlassPanel().putToView(GlassPanel.getLauncherPanel());
		});
		closeComp.setFont(PX32.bold());
		closeComp.setArc(0, 0);
		add(closeComp);

		fileNameLabel = new EdgeComp("File Name With Extension", GLOW, HOVER, GLOW, null);
		fileNameLabel.setFont(PX14.bold());
		fileNameLabel.setEnabled(false);
		add(fileNameLabel);

		fileNameField = new TextInputField("Enter File Name", "");
		fileNameField.setFont(PX16.bold());
		add(fileNameField);

		parentDirLabel = new EdgeComp("Parent Directory", GLOW, HOVER, GLOW, null);
		parentDirLabel.setFont(PX14.bold());
		parentDirLabel.setEnabled(false);
		add(parentDirLabel);

		parentDirField = new TextInputField("~/codeblaze-source-files");
		parentDirField.setFont(PX16.bold());
		parentDirField.setEditable(false);
		add(parentDirField);

		putAnimationLayer(closeComp, getImageSizeAnimationLayer(20, +5, true), ACTION_MOUSE_ENTERED);
	}

	@Override
	public void manageBounds(){
		iconComp.setBounds(50, 42, 70, 70);
		titleComp.setBounds(getWidth()/2 - 300/2, 50, 300, 50);
		closeComp.setBounds(getWidth() - 50 - 40, 52, 40, 40);

		fileNameLabel.setBounds(titleComp.getX() - 250, 150, 250, 30);
		fileNameField.setBounds(titleComp.getX() + 20, 150, getWidth()/3, 30);

		parentDirLabel.setBounds(titleComp.getX() - 250, 200, 250, 30);
		parentDirField.setBounds(titleComp.getX() + 20, 200, getWidth()/3, 30);
	}

	@Override
	public void layout(){
		super.layout();
		manageBounds();
	}
	
}
