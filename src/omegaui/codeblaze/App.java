package omegaui.codeblaze;
import java.awt.event.KeyEvent;

import omegaui.listener.KeyStrokeListener;

import omegaui.codeblaze.ui.component.ToolMenu;
import omegaui.codeblaze.ui.component.BottomPane;

import com.formdev.flatlaf.FlatLightLaf;

import omegaui.codeblaze.io.AppInstanceProvider;
import omegaui.codeblaze.io.AppStateManager;

import omegaui.codeblaze.ui.panel.GlassPanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.BorderFactory;

import static omegaui.codeblaze.io.UIXManager.*;

public class App extends JFrame{

	private GlassPanel glassPanel;

	private ToolMenu toolMenu;
	private BottomPane bottomPane;

	private KeyStrokeListener appWideKeyStrokeListener;

	private App(){
		super("CodeBlaze");

		setSize(1000, 650);
		setMinimumSize(getSize());
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		registerAppInstanceProvider();
		
		initAppWideKeyStrokeListener();
		initUI();

		initState();

		setVisible(true);
	}

	private void registerAppInstanceProvider(){
		AppInstanceProvider.setCurrentAppInstance(this);
	}

	private void initAppWideKeyStrokeListener(){
		appWideKeyStrokeListener = new KeyStrokeListener(this);
		
		KeyEventDispatcher dispatcher = new KeyEventDispatcher(){
			@Override
	        public boolean dispatchKeyEvent(KeyEvent e) {
	        	if (e.getID() == KeyEvent.KEY_PRESSED) {
                	appWideKeyStrokeListener.keyPressed(e);
	            } 
	            else if (e.getID() == KeyEvent.KEY_RELEASED) {
	                appWideKeyStrokeListener.keyReleased(e);
	            }
	            else if (e.getID() == KeyEvent.KEY_TYPED) {
	                appWideKeyStrokeListener.keyTyped(e);
	            }
	            return false;
	        }
		};
		
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(dispatcher);
	}

	private void initUI(){
		toolMenu = new ToolMenu(this);

		bottomPane = new BottomPane(this);
		
		glassPanel = new GlassPanel(this);
	}

	private void initState(){
		AppStateManager.initAppState();
	}

	public void switchViewToGlassPane(){
		remove(toolMenu);
		remove(bottomPane);
		add(glassPanel, BorderLayout.CENTER);
		doLayout();
		getContentPane().setVisible(false);
		getContentPane().setVisible(true);
	}

	public void switchViewToContentPane(){
		add(toolMenu, BorderLayout.NORTH);
		add(bottomPane, BorderLayout.SOUTH);
		remove(glassPanel);
		doLayout();
		getContentPane().setVisible(false);
		getContentPane().setVisible(true);
	}

	public void exit(){
		dispose();
	}

	public omegaui.listener.KeyStrokeListener getAppWideKeyStrokeListener() {
		return appWideKeyStrokeListener;
	}
	
	public omegaui.codeblaze.ui.panel.GlassPanel getGlassPanel() {
		return glassPanel;
	}

	public static void main(String[] args){
		FlatLightLaf.install();

		new App();
	}
}
