package omegaui.codeblaze.ui.component;
import java.awt.Dimension;

import omegaui.codeblaze.io.ResizeAware;

import omegaui.codeblaze.App;

import javax.swing.JPanel;

import static omegaui.codeblaze.io.UIXManager.*;
import static omegaui.codeblaze.io.AppDataProvider.*;
import static omegaui.component.animation.Animations.*;

public final class BottomPane extends JPanel implements ResizeAware{
	private App app;

	private MessagePane messagePane;

	public BottomPane(App app){
		this.app = app;
		setPreferredSize(new Dimension(100, 30));
		initUI();
	}

	public void initUI(){

		setBackground(BACKGROUND);
		
		messagePane = new MessagePane();
		add(messagePane);
	}

	@Override
	public void manageBounds(){
		messagePane.setBounds(0, 0, getWidth(), getHeight());
	}

	@Override
	public void layout(){
		super.layout();
		manageBounds();
	}

	public omegaui.codeblaze.ui.component.MessagePane getMessagePane() {
		return messagePane;
	}
	
}
