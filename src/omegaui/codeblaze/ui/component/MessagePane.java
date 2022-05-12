package omegaui.codeblaze.ui.component;
import omegaui.codeblaze.io.ResizeAware;

import java.awt.Dimension;
import java.awt.BorderLayout;

import javax.swing.JPanel;

import omegaui.component.TextComp;

import static omegaui.codeblaze.io.UIXManager.*;
import static omegaui.codeblaze.io.AppDataProvider.*;
import static omegaui.component.animation.Animations.*;

public final class MessagePane extends JPanel {

	private TextComp messageComp;
	private String defaultText = "Status of any running process will appear here!";
	
	public MessagePane(){
		super(new BorderLayout());
		initUI();
	}

	public void initUI(){
		messageComp = new TextComp(defaultText, HOVER, BACKGROUND, GLOW, null);
		messageComp.setFont(PX14);
		messageComp.setArc(0, 0);
		messageComp.addHighlightText("running process");
		messageComp.setHighlightColor(primaryColor);
		messageComp.setTextAlignment(TextComp.TEXT_ALIGNMENT_LEFT);
		messageComp.setTextLeftAlignmentMargin(10);
		add(messageComp, BorderLayout.CENTER);
	}

	public void setMessage(String text){
		messageComp.setText(text);
	}

	public void setMessage(String text, String... highlights){
		setMessage(text);
		messageComp.addHighlightText(highlights);
	}

	public void resetMessage(){
		messageComp.setText(defaultText);
		messageComp.addHighlightText("running process");
	}

	public omegaui.component.TextComp getMessageComp() {
		return messageComp;
	}
	
}
