package omegaui.codeblaze.ui.panel;
import omegaui.component.TextComp;

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
		
		closeComp = new TextComp(cancelIcon, 32, 32, BACKGROUND, BACKGROUND, BACKGROUND, null);
		closeComp.setFont(PX32.bold());
		closeComp.setArc(0, 0);
		add(closeComp);

		putAnimationLayer(closeComp, getImageSizeAnimationLayer(20, +5, true), ACTION_MOUSE_ENTERED);
	}

	@Override
	public void manageBounds(){
		iconComp.setBounds(50, 42, 70, 70);
		titleComp.setBounds(getWidth()/2 - 300/2, 50, 300, 50);
		closeComp.setBounds(getWidth() - 50 - 40, 52, 40, 40);
	}

	@Override
	public void layout(){
		super.layout();
		manageBounds();
	}
	
}
