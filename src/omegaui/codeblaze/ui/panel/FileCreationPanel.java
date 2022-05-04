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

	private TextComp manageTemplateComp;
	
	private TextComp createComp;

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
		fileNameField.setOnAction(()->createComp.runnable.run());
		fileNameField.setValidationTask((field)->{
			return !field.getText().isEmpty();
		});
		add(fileNameField);

		parentDirLabel = new EdgeComp("Parent Directory", GLOW, HOVER, GLOW, null);
		parentDirLabel.setFont(PX14.bold());
		parentDirLabel.setEnabled(false);
		add(parentDirLabel);

		parentDirField = new TextInputField("~/codeblaze-source-files");
		parentDirField.setFont(PX16.bold());
		parentDirField.setEditable(false);
		add(parentDirField);

		manageTemplateComp = new TextComp("Manage Templates", HOVER, BACKGROUND, GLOW, null);
		manageTemplateComp.setImage(templateIcon, 64, 64);
		manageTemplateComp.setArc(6, 6);
		manageTemplateComp.setImageCoordinates(10, 70/2 - 64/2);
		manageTemplateComp.setFont(PX16.bold());
		manageTemplateComp.setTextAlignment(TextComp.TEXT_ALIGNMENT_RIGHT);
		add(manageTemplateComp);

		createComp = new TextComp("Create", HOVER, BACKGROUND, GLOW, ()->{
			validateProvidedData();
		});
		createComp.setFont(PX18.bold());
		createComp.setArc(6, 6);
		add(createComp);

		putAnimationLayer(closeComp, getImageSizeAnimationLayer(20, +5, true), ACTION_MOUSE_ENTERED);
	}

	public void validateProvidedData(){
		
	}

	@Override
	public void manageBounds(){
		iconComp.setBounds(50, 42, 70, 70);
		titleComp.setBounds(getWidth()/2 - 300/2, 50, 300, 50);
		closeComp.setBounds(getWidth() - 50 - 40, 52, 40, 40);

		fileNameLabel.setBounds(titleComp.getX() - 200, 150, 250, 30);
		fileNameField.setBounds(titleComp.getX() + 70, 150, getWidth()/3, 30);

		parentDirLabel.setBounds(titleComp.getX() - 200, 200, 250, 30);
		parentDirField.setBounds(titleComp.getX() + 70, 200, getWidth()/3, 30);

		manageTemplateComp.setBounds(getWidth()/2 - 220/2, getHeight()/2, 220, 70);
		createComp.setBounds(getWidth()/2 - 100/2, getHeight() - 100, 100, 30);
	}

	@Override
	public void layout(){
		super.layout();
		manageBounds();
	}
	
}
