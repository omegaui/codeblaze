package omegaui.codeblaze.ui.dialog;
import omegaui.codeblaze.App;

import omegaui.component.TextComp;

import javax.swing.JDialog;
import javax.swing.JPanel;

import static omegaui.codeblaze.io.UIXManager.*;
import static omegaui.codeblaze.io.AppDataProvider.*;
import static omegaui.component.animation.Animations.*;

public class PreferencesDialog extends JDialog{

	private App app;

	private TextComp iconComp;
	private TextComp titleComp;
	private TextComp closeComp;
	
	public PreferencesDialog(App app){
		super(app, true);
		setUndecorated(true);
		setLayout(null);
		setTitle("Preferences");
		setIconImage(app.getIconImage());
		setSize(600, 500);
		setLocationRelativeTo(null);
		setResizable(false);
		initUI();
	}

	public void initUI(){
		JPanel panel = new JPanel(null);
		setContentPane(panel);
		panel.setBackground(back3);
		
		iconComp = new TextComp(gearIcon, 48, 48, back3, back3, back3, null);
		iconComp.setBounds(0, 0, 50, 50);
		iconComp.setClickable(false);
		iconComp.setArc(0, 0);
		iconComp.attachDragger(this);
		add(iconComp);

		titleComp = new TextComp("Preferences", back3, back3, GLOW, null);
		titleComp.setBounds(50, 0, getWidth() - 100, 50);
		titleComp.setFont(PX18);
		titleComp.setArc(0, 0);
		titleComp.setClickable(false);
		titleComp.attachDragger(this);
		add(titleComp);

		closeComp = new TextComp(closeIcon, 25, 25, back3, back3, back3, this::dispose);
		closeComp.setBounds(getWidth() - 50, 0, 50, 50);
		closeComp.setArc(0, 0);
		add(closeComp);

		putAnimationLayer(closeComp, getImageSizeAnimationLayer(20, +5, true), ACTION_MOUSE_ENTERED);
	}
}
