package omegaui.codeblaze.ui.panel;
import omegaui.codeblaze.io.ResizeAware;

import omegaui.component.TextComp;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import omegaui.codeblaze.App;

import javax.swing.JPanel;

import static omegaui.codeblaze.io.UIXManager.*;

public class GlassPanel extends JPanel implements ResizeAware{
	private App app;

	private TextComp appIconComp;
	private TextComp appTitleComp;
	
	public GlassPanel(App app){
		this.app = app;
		initUI();
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
	}

	@Override
	public void manageBounds() {
		appIconComp.setBounds(getWidth()/2 - 200/2, getHeight()/2 - 400/2, 200, 200);
		appTitleComp.setBounds(getWidth()/2 - 200/2, appIconComp.getY() + appIconComp.getHeight(), 200, 100);
	}

	@Override
	public void paint(Graphics graphics){
		manageBounds();
		super.paint(graphics);
	}
}
