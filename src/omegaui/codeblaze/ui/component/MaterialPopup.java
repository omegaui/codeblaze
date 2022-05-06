package omegaui.codeblaze.ui.component;
import java.util.LinkedList;

import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;

import java.awt.geom.RoundRectangle2D;

import java.awt.Window.Type;

import omegaui.codeblaze.io.AppInstanceProvider;

import javax.swing.JDialog;
import javax.swing.JPanel;

import static omegaui.codeblaze.io.UIXManager.*;
import static omegaui.codeblaze.io.AppDataProvider.*;
import static omegaui.component.animation.Animations.*;

public final class MaterialPopup extends JDialog implements FocusListener{

	private LinkedList<MaterialPopupItem> items = new LinkedList<>();

	public MaterialPopup(){
		super(AppInstanceProvider.getCurrentAppInstance(), false);
		setUndecorated(true);
		setType(Type.POPUP);
		setLayout(null);
		setResizable(false);
		initUI();
	}

	public void initUI(){
		JPanel panel = new JPanel(null);
		setContentPane(panel);
		setBackground(BACKGROUND);
		panel.setBackground(BACKGROUND);
		addFocusListener(this);
	}

	public MaterialPopup createItem(String text, Runnable action){
		items.add(new MaterialPopupItem(this, text, action));
		add(items.getLast());
		return this;
	}

	public MaterialPopup createItem(String text, String shortcutText, Runnable action){
		items.add(new MaterialPopupItem(this, text, shortcutText, action));
		add(items.getLast());
		return this;
	}

	public void relocate(){
		int blockY = 5;
		int blockX = 5;
		int blockW = getWidth() - 10;
		int blockH = 30;
		for(int i = 0; i < items.size(); i++){
			items.get(i).setBounds(blockX, blockY, blockW, blockH);
			blockY += 5 + blockH;
		}
	}

	@Override
	public void setVisible(boolean value){
		if(value){
			setSize(400, items.size() * 30 + ((items.size() - 1) * 5) + 10);
			relocate();
		}
		super.setVisible(value);
	}

	@Override
	public void focusGained(FocusEvent focusEvent) {

	}

	@Override
	public void focusLost(FocusEvent focusEvent) {
		setVisible(false);
	}

	@Override
	public void setSize(int width, int height){
		super.setSize(width, height);
		setShape(new RoundRectangle2D.Double(0, 0, width, height, 20, 20));
	}

}
