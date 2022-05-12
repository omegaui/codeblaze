package omegaui.codeblaze.ui.panel;
import java.util.LinkedList;

import java.awt.TextField;

import omegaui.codeblaze.App;

import omegaui.component.TextComp;

import omegaui.codeblaze.ui.component.TextInputField;
import omegaui.codeblaze.ui.component.MessagePane;

import omegaui.codeblaze.io.ResizeAware;
import omegaui.codeblaze.io.FileManager;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;

import static omegaui.codeblaze.io.UIXManager.*;
import static omegaui.codeblaze.io.AppDataProvider.*;
import static omegaui.component.animation.Animations.*;

public final class RecentFilesPanel extends JPanel implements ResizeAware{

	private App app;

	private TextComp iconComp;
	private TextComp titleComp;
	private TextComp closeComp;

	private TextInputField textField;

	private LinkedList<TextComp> fileComps = new LinkedList<>();

	private JPanel panel;
	private JScrollPane scrollPane;

	private MessagePane messagePane;

	public RecentFilesPanel(App app){
		this.app = app;
		initUI();
	}

	public void initUI(){

		setBackground(BACKGROUND);

		iconComp = new TextComp(recentsIcon, 64, 64, BACKGROUND, BACKGROUND, BACKGROUND, null);
		iconComp.setFont(PX32);
		iconComp.setArc(0, 0);
		iconComp.setClickable(false);
		add(iconComp);

		titleComp = new TextComp("Quick Open a Recent File", BACKGROUND, BACKGROUND, GLOW, null);
		titleComp.setFont(PX32);
		titleComp.setArc(0, 0);
		titleComp.setClickable(false);
		add(titleComp);

		closeComp = new TextComp(cancelIcon, 32, 32, BACKGROUND, BACKGROUND, BACKGROUND, ()->{
			if(FileManager.getCodeEditors().isEmpty())
				app.getGlassPanel().putToView(GlassPanel.getLauncherPanel());
			else
				app.switchViewToContentPane();
		});
		closeComp.setFont(PX32);
		closeComp.setArc(0, 0);
		add(closeComp);

		textField = new TextInputField("Type File Name Here", "");
		textField.setFont(PX16);
		add(textField);

		scrollPane = new JScrollPane(panel = new JPanel());
		scrollPane.setBorder(null);
		panel.setBackground(BACKGROUND);
		add(scrollPane);

		messagePane = new MessagePane();
		add(messagePane);

		putAnimationLayer(closeComp, getImageSizeAnimationLayer(20, +5, true), ACTION_MOUSE_ENTERED);
	}

	public void recreateView(){
		panel.removeAll();
		fileComps.clear();
		
		messagePane.getMessageComp().setHighlightColor(primaryColor);

		LinkedList<String> recentFiles = FileManager.getRecentFilesDataBase().getEntriesAsString(FileManager.RECENT_FILE_DATA_SET_NAME);
		if(recentFiles.isEmpty()){
			messagePane.getMessageComp().setHighlightColor(secondaryColor);
			messagePane.setMessage("You haven't open any file yet, Open some files and get them listed here.", "haven't open any file");
			return;
		}

		messagePane.setMessage(recentFiles.size() + " files were opened since first startup that are still available on the disk.", String.valueOf(recentFiles.size()));
	}

	@Override
	public void setVisible(boolean value){
		if(value){
			recreateView();
		}
		super.setVisible(value);
	}

	@Override
	public void manageBounds(){
		iconComp.setBounds(50, 42, 70, 70);
		titleComp.setBounds(getWidth()/2 - 400/2, 50, 400, 50);
		closeComp.setBounds(getWidth() - 50 - 40, 52, 40, 40);

		textField.setBounds(getWidth()/2 - getWidth()/6, 150, getWidth()/3, 30);

		scrollPane.setBounds(getWidth()/2 - 250, 200, 500, getHeight() - 220);

		messagePane.setBounds(0, getHeight() - 30, getWidth(), 30);
	}

	@Override
	public void layout(){
		super.layout();
		manageBounds();
	}

}
