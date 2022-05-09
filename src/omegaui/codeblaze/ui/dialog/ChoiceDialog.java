package omegaui.codeblaze.ui.dialog;
import omegaui.listener.KeyStrokeListener;

import omegaui.codeblaze.io.AppInstanceProvider;

import java.awt.geom.RoundRectangle2D;

import java.awt.Graphics2D;
import java.awt.Toolkit;

import omegaui.component.TextComp;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import static omegaui.codeblaze.io.UIXManager.*;
import static omegaui.codeblaze.io.AppDataProvider.*;
import static omegaui.component.animation.Animations.*;

import static java.awt.event.KeyEvent.*;

public class ChoiceDialog extends JDialog {
	
	private TextComp textComp;
	private TextComp choice1Comp;
	private TextComp choice2Comp;
	private TextComp cancelComp;

	public static final int CHOICE1 = 0;
	public static final int CHOICE2 = 1;
	public static final int CANCEL = 2;
	public int choice = CANCEL;

	public static ChoiceDialog choiceDialog;

	public ChoiceDialog(JFrame frame){
		super(frame, true);
		setTitle("Choice Dialog");
		setLayout(null);
		setUndecorated(true);
		JPanel panel = new JPanel(null);
		setContentPane(panel);
		panel.setBackground(back1);
		init();
		initKeyStrokeListener();
		setSize(500, 150);
	}

	public void init(){
		textComp = new TextComp("Question?", back3, back3, GLOW, null);
		textComp.setFont(PX14);
		textComp.setArc(0, 0);
		textComp.setClickable(false);
		textComp.attachDragger(this);
		add(textComp);

		choice1Comp = new TextComp("Choice 1", tertiaryColorShade, back1, tertiaryColor, ()->{
			choice = CHOICE1;
			dispose();
		});
		choice1Comp.setArc(5, 5);
		choice1Comp.setFont(UBUNTU_PX14);
		add(choice1Comp);

		choice2Comp = new TextComp("Choice 2", tertiaryColorShade, back1, focusColor, ()->{
			choice = CHOICE2;
			dispose();
		});
		choice2Comp.setArc(5, 5);
		choice2Comp.setFont(UBUNTU_PX14);
		add(choice2Comp);

		cancelComp = new TextComp("Cancel", focusColorShade, primaryColorShade, primaryColor, this::dispose);
		cancelComp.setFont(PX14);
		cancelComp.setArc(5, 5);
		add(cancelComp);
	}

	public void initKeyStrokeListener(){
		KeyStrokeListener listener = new KeyStrokeListener(this);
		listener.putKeyStroke((e)->{ choice1Comp.runnable.run(); }, VK_A).setStopKeys(VK_CONTROL, VK_SHIFT, VK_ALT).useAutoReset();
		listener.putKeyStroke((e)->{ choice2Comp.runnable.run(); }, VK_B).setStopKeys(VK_CONTROL, VK_SHIFT, VK_ALT).useAutoReset();
		listener.putKeyStroke((e)->{ cancelComp.runnable.run(); }, VK_C).setStopKeys(VK_CONTROL, VK_SHIFT, VK_ALT).useAutoReset();
		addKeyListener(listener);
	}

	public static int makeChoice(String question, String choice1, String choice2){
		if(choiceDialog == null)
			choiceDialog = new ChoiceDialog(AppInstanceProvider.getCurrentAppInstance());

		int width = computeWidth(question, PX14) + 20;

		if(width > 500 && width <= Toolkit.getDefaultToolkit().getScreenSize().width)
			choiceDialog.setSize(width, choiceDialog.getHeight());
		else
			choiceDialog.setSize(500, 150);
		
		choiceDialog.choice = CANCEL;
		choiceDialog.textComp.setText(question);
		choiceDialog.choice1Comp.setText(choice1);
		choiceDialog.choice2Comp.setText(choice2);

		choiceDialog.choice1Comp.setSize(computeWidth(choice1, UBUNTU_PX14) + 10, 25);
		choiceDialog.choice2Comp.setSize(computeWidth(choice2, UBUNTU_PX14) + 10, 25);

		choiceDialog.choice1Comp.setLocation(choiceDialog.getWidth() - 10 - choiceDialog.choice1Comp.getWidth() - 10 - choiceDialog.choice2Comp.getWidth(), choiceDialog.getHeight() - 40);
		choiceDialog.choice2Comp.setLocation(choiceDialog.getWidth() - 10 - choiceDialog.choice2Comp.getWidth(), choiceDialog.getHeight() - 40);

		choiceDialog.setVisible(true);
		return choiceDialog.choice;
	}

	@Override
	public void setSize(int w, int h){
		super.setSize(w, h);
		setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
		setLocationRelativeTo(null);
		textComp.setBounds(0, getHeight()/2 - 15 - 25/2, getWidth(), 30);
		cancelComp.setBounds(getWidth()/2 - 90/2, 200, 90, 25);
	}
}