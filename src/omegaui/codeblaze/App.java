package omegaui.codeblaze;
import omegaui.codeblaze.io.AppInstanceProvider;
import omegaui.codeblaze.io.AppStateManager;

import omegaui.codeblaze.ui.panel.GlassPanel;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import static omegaui.codeblaze.io.UIXManager.*;

public class App extends JFrame{

	private GlassPanel glassPanel;
	
	private App(){
		super("CodeBlaze");
		setLayout(new BorderLayout());
		setSize(1000, 650);
		setMinimumSize(getSize());
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		registerAppInstanceProvider();
		
		initContentPanel();
		initGlassUI();
		initUI();

		initState();
		
		setVisible(true);
	}

	private void registerAppInstanceProvider(){
		AppInstanceProvider.setCurrentAppInstance(this);
	}

	private void initContentPanel(){
		JPanel contentPanel = new JPanel(getLayout());
		contentPanel.setBackground(BACKGROUND);
		setBackground(BACKGROUND);
		setContentPane(contentPanel);
	}

	private void initGlassUI(){
		glassPanel = new GlassPanel(this);
		setGlassPane(glassPanel);
	}

	private void initUI(){
		glassPanel = new GlassPanel(this);
		setGlassPane(glassPanel);
	}

	private void initState(){
		AppStateManager.initAppState();
	}

	public omegaui.codeblaze.ui.panel.GlassPanel getGlassPanel() {
		return glassPanel;
	}
	
	public static void main(String[] args){
		new App();
	}
}
