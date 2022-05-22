package omegaui.codeblaze.ui.dialog;
import omegaui.dynamic.database.DataEntry;

import java.awt.Dimension;

import omegaui.codeblaze.App;

import omegaui.component.TextComp;
import omegaui.component.VerticalBox;
import omegaui.component.SwitchComp;
import omegaui.component.HorizontalBox;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.BoxLayout;

import static omegaui.codeblaze.io.UIXManager.*;
import static omegaui.codeblaze.io.AppDataProvider.*;
import static omegaui.codeblaze.io.AppResourceManager.*;
import static omegaui.component.animation.Animations.*;

public class PreferencesDialog extends JDialog{

	private App app;

	private TextComp iconComp;
	private TextComp titleComp;
	private TextComp closeComp;

	private VerticalBox panel;
	private TextComp autoSaveFileOnExitLabel;
	private SwitchComp autoSaveFileOnExitSwitch;
	private TextComp autoSaveBeforeLaunchLabel;
	private SwitchComp autoSaveBeforeLaunchSwitch;

	public PreferencesDialog(App app){
		super(app, true);
		setUndecorated(true);
		setLayout(null);
		setTitle("Preferences");
		setIconImage(app.getIconImage());
		setSize(500, 400);
		setLocationRelativeTo(null);
		setResizable(false);
		initUI();
	}

	public void initUI(){
		JPanel contentPane = new JPanel(null);
		setContentPane(contentPane);
		contentPane.setBackground(back3);

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

		panel = new VerticalBox();
		panel.setBounds(5, 60, getWidth() - 10, 80);
		panel.setBackground(back3);
		add(panel);

		autoSaveFileOnExitLabel = new TextComp("Auto Save Files on Exit", "Enabling this feature will Silently Save Files without letting prompting any choice dialog.", back3, back3, GLOW, null);
		autoSaveFileOnExitLabel.setFont(PX16);
		autoSaveFileOnExitLabel.setClickable(false);
		autoSaveFileOnExitLabel.setArc(0, 0);

		autoSaveFileOnExitSwitch = new SwitchComp(false, HOVER, GLOW, back3, (value)->{
			appDataBase().updateEntry(AUTO_SAVE_FILE_ON_EXIT_PROPERTY, String.valueOf(value), 0);
		});
		autoSaveFileOnExitSwitch.setInBallColor(back3);

		panel.push(HorizontalBox.create(back3).push(autoSaveFileOnExitLabel).push(autoSaveFileOnExitSwitch));

		autoSaveBeforeLaunchLabel = new TextComp("Auto Save Files before Launch", "Enabling this feature will Silently Save Files before compile and run.", back3, back3, GLOW, null);
		autoSaveBeforeLaunchLabel.setFont(PX16);
		autoSaveBeforeLaunchLabel.setClickable(false);
		autoSaveBeforeLaunchLabel.setArc(0, 0);

		autoSaveBeforeLaunchSwitch = new SwitchComp(false, HOVER, GLOW, back3, (value)->{
			appDataBase().updateEntry(AUTO_SAVE_FILE_BEFORE_LAUNCH_PROPERTY, String.valueOf(value), 0);
		});
		autoSaveBeforeLaunchSwitch.setInBallColor(back3);

		panel.push(HorizontalBox.create(back3).push(autoSaveBeforeLaunchLabel).push(autoSaveBeforeLaunchSwitch));
	}

	private void loadCurrentPreferences(){
		DataEntry saveFileOnExitEntry = appDataBase().getEntryAt(AUTO_SAVE_FILE_ON_EXIT_PROPERTY);
		if(saveFileOnExitEntry != null)
			autoSaveFileOnExitSwitch.setOn(saveFileOnExitEntry.getValueAsBoolean());

		DataEntry saveFileOnLaunchEntry = appDataBase().getEntryAt(AUTO_SAVE_FILE_BEFORE_LAUNCH_PROPERTY);
		if(saveFileOnLaunchEntry != null)
			autoSaveBeforeLaunchSwitch.setOn(saveFileOnLaunchEntry.getValueAsBoolean());
	}

	@Override
	public void setVisible(boolean value){
		if(value)
			loadCurrentPreferences();
		super.setVisible(value);
	}
}
