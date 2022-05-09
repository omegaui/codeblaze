package omegaui.codeblaze.ui.component;
import omegaui.component.TextComp;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;

import static omegaui.codeblaze.io.UIXManager.*;
import static omegaui.codeblaze.io.AppDataProvider.*;
import static omegaui.component.animation.Animations.*;

public final class MaterialPopupItem extends JComponent{

	private MaterialPopup popup;
	
	private String text;
	private String shortcutText;

	private Runnable action;
	
	private TextComp itemNameComp;
	
	public MaterialPopupItem(MaterialPopup popup, String text, Runnable action){
		this(popup, text, "", action, GLOW, "");
	}
	
	public MaterialPopupItem(MaterialPopup popup, String text, String shortcutText, Runnable action){
		this(popup, text, shortcutText, action, GLOW, "");
	}
	
	public MaterialPopupItem(MaterialPopup popup, String text, String shortcutText, Runnable action, Color highlightColor, String... highlights){
		this.popup = popup;
		this.text = text;
		this.shortcutText = shortcutText;
		this.action = action;
		initUI(highlightColor, highlights);
	}

	public void initUI(Color highlightColor, String... highlights){
		itemNameComp = new TextComp(text, tertiaryColorShade, BACKGROUND, GLOW, ()->{
			popup.setVisible(false);
			if(action != null)
				action.run();
		}){
			@Override
			public void draw(Graphics2D g){
				g.setColor(color3);
				g.setFont(UBUNTU_PX12);
				g.drawString(shortcutText, getWidth() - g.getFontMetrics().stringWidth(shortcutText) - 10, getHeight()/2 - g.getFontMetrics().getHeight()/2 + g.getFontMetrics().getAscent() - g.getFontMetrics().getDescent() + 1);
			}
		};
		itemNameComp.setFont(PX14);
		itemNameComp.setHighlightColor(highlightColor);
		itemNameComp.addHighlightText(highlights);
		itemNameComp.setTextAlignment(TextComp.TEXT_ALIGNMENT_LEFT);
		itemNameComp.setTextLeftAlignmentMargin(20);
		itemNameComp.setToolTipText(shortcutText);
		add(itemNameComp);
	}

	public void doClick(){
		itemNameComp.runnable.run();
	}

	public void setEnter(boolean value){
		itemNameComp.setEnter(value);
	}
	
	@Override
	public void layout(){
		itemNameComp.setBounds(0, 0, getWidth(), getHeight());
		super.layout();
	}

	@Override
	public void paint(Graphics graphics){
		Graphics2D g = (Graphics2D)graphics;
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		super.paint(g);
	}
	
}
