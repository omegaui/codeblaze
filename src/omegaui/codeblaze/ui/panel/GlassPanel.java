package omegaui.codeblaze.ui.panel;
import omegaui.codeblaze.io.ResizeAware;

import omegaui.component.TextComp;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.BorderLayout;

import omegaui.codeblaze.App;

import javax.swing.JPanel;

import static omegaui.codeblaze.io.UIXManager.*;
import static omegaui.codeblaze.io.AppDataProvider.*;
import static omegaui.component.animation.Animations.*;

public final class GlassPanel extends JPanel {
	private App app;

	// Views
	private static LauncherPanel launcherPanel;
	private static FileCreationPanel fileCreationPanel;
	private static TemplateManagementPanel templateManagementPanel;

	public GlassPanel(App app){
		super(new BorderLayout());
		this.app = app;
		initUI();
	}

	public void initUI(){
		launcherPanel = new LauncherPanel(app);
		fileCreationPanel = new FileCreationPanel(app);
		templateManagementPanel = new TemplateManagementPanel(app);
		
		putToView(launcherPanel);
	}

	public void putToView(JPanel panel){
		removeAll();
		add(panel, BorderLayout.CENTER);
		panel.setVisible(false);
		panel.setVisible(true);
		app.getContentPane().doLayout();
	}

	public static omegaui.codeblaze.ui.panel.LauncherPanel getLauncherPanel() {
		return launcherPanel;
	}
	
	public static omegaui.codeblaze.ui.panel.FileCreationPanel getFileCreationPanel() {
		return fileCreationPanel;
	}

	public static omegaui.codeblaze.ui.panel.TemplateManagementPanel getTemplateManagementPanel() {
		return templateManagementPanel;
	}
	
}
