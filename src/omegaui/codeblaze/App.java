package omegaui.codeblaze;
import omegaui.codeblaze.ui.component.ToolMenu;
import omegaui.codeblaze.ui.component.BottomPane;

import com.formdev.flatlaf.FlatLightLaf;

import omegaui.codeblaze.io.AppInstanceProvider;
import omegaui.codeblaze.io.AppStateManager;

import omegaui.codeblaze.ui.panel.GlassPanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.BorderFactory;

import static omegaui.codeblaze.io.UIXManager.*;

public class App extends JFrame{

	private GlassPanel glassPanel;

	private ToolMenu toolMenu;
	private BottomPane bottomPane;

	private App(){
		super("CodeBlaze");

		setSize(1000, 650);
		setMinimumSize(getSize());
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		registerAppInstanceProvider();
		initUI();

		initState();

		setVisible(true);
	}

	private void registerAppInstanceProvider(){
		AppInstanceProvider.setCurrentAppInstance(this);
	}

	private void initUI(){
		toolMenu = new ToolMenu(this);

		bottomPane = new BottomPane(this);
		
		glassPanel = new GlassPanel(this);
	}

	private void initState(){
		AppStateManager.initAppState();
	}

	public void switchViewToGlassPane(){
		remove(toolMenu);
		remove(bottomPane);
		add(glassPanel, BorderLayout.CENTER);
		doLayout();
		getContentPane().setVisible(false);
		getContentPane().setVisible(true);
	}

	public void switchViewToContentPane(){
		add(toolMenu, BorderLayout.NORTH);
		add(bottomPane, BorderLayout.SOUTH);
		remove(glassPanel);
		doLayout();
		getContentPane().setVisible(false);
		getContentPane().setVisible(true);
	}

	public void exit(){
		dispose();
	}

	public omegaui.codeblaze.ui.panel.GlassPanel getGlassPanel() {
		return glassPanel;
	}

	public static void main(String[] args){
		FlatLightLaf.install();

		new App();
	}
}
