package omegaui.codeblaze.ui.panel;
import omegaui.paint.PixelColor;

import java.io.File;

import java.util.LinkedList;

import java.awt.TextField;
import java.awt.Color;
import java.awt.Dimension;

import omegaui.codeblaze.App;

import omegaui.component.TextComp;

import omegaui.codeblaze.ui.component.TextInputField;
import omegaui.codeblaze.ui.component.MessagePane;

import omegaui.codeblaze.io.ResizeAware;
import omegaui.codeblaze.io.FileManager;
import omegaui.codeblaze.io.AppUtils;

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
		textField.setOnAnyKeyReleased(this::recreateView);
		add(textField);

		scrollPane = new JScrollPane(panel = new JPanel(null));
		scrollPane.setBorder(null);
		scrollPane.setLocation(getWidth()/2 - 250, 200);
		scrollPane.setSize(500, getHeight() - 150);
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
		
		String text = textField.getText();

		int blockY = 5;
		int width = scrollPane.getWidth();
		
		for(String path : recentFiles){
			File file = new File(path);
			if(path.contains(text) || AppUtils.isMatching(path, text)){
				Color fileColor = getPreferredColorForFile(file);
				TextComp comp = new TextComp(file.getName(), HOVER, BACKGROUND, fileColor, ()->{
					FileManager.openFile(file);
				});
				comp.setFont(PX16);
				comp.setArc(6, 6);
				comp.setImageCoordinates(9, 9);
				comp.setImage(getPreferredIconForFile(file), 32, 32);
				comp.setTextAlignment(TextComp.TEXT_ALIGNMENT_LEFT);
				comp.setTextLeftAlignmentMargin(45);
				comp.setBounds(5, blockY, width - 10, 40);
				panel.add(comp);
				fileComps.add(comp);

				blockY += 40 + 5;
			}
		}
		
		panel.setPreferredSize(new Dimension(width - 10, blockY - 40));
		messagePane.setMessage(fileComps.size() + " files were found.", String.valueOf(fileComps.size()));
		panel.repaint();
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

		scrollPane.setLocation(getWidth()/2 - 250, 200);

		messagePane.setBounds(0, getHeight() - 30, getWidth(), 30);
	}

	@Override
	public void layout(){
		super.layout();
		manageBounds();
	}

}
