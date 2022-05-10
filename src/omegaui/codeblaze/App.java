package omegaui.codeblaze;
import omegaui.component.io.AppOperation;

import java.util.LinkedList;

import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import omegaui.listener.KeyStrokeListener;

import omegaui.codeblaze.ui.component.ToolMenu;
import omegaui.codeblaze.ui.component.BottomPane;

import com.formdev.flatlaf.FlatLightLaf;

import omegaui.codeblaze.io.AppInstanceProvider;
import omegaui.codeblaze.io.AppStateManager;
import omegaui.codeblaze.io.FileManager;

import omegaui.codeblaze.ui.panel.GlassPanel;
import omegaui.codeblaze.ui.panel.SplitPanel;
import omegaui.codeblaze.ui.panel.TabPanel;

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

public class App extends JFrame {

	public static final int CONTENT_VIEW = 1;
	public static final int GLASS_VIEW = 2;
	
	private int viewState = -1;

	private GlassPanel glassPanel;

	private ToolMenu toolMenu;
	private BottomPane bottomPane;

	private KeyStrokeListener appWideKeyStrokeListener;

	private SplitPanel splitPanel;
	private TabPanel tabPanel;

	private LinkedList<AppOperation> appClosingOperations = new LinkedList<>();

	private App(){
		super("CodeBlaze");

		setSize(1000, 650);
		setMinimumSize(getSize());
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		registerAppInstanceProvider();
		
		initAppWideKeyStrokeListener();
		initUI();
		initDefaultAppOperations();

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

		tabPanel = new TabPanel(TabPanel.TAB_LOCATION_TOP);

		splitPanel = new SplitPanel(SplitPanel.VERTICAL_SPLIT);
		splitPanel.setTopComponent(tabPanel);
	}

	private void initDefaultAppOperations(){
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				exit();
			}
		});
		
		addAppClosingOperation((app)->{
			tabPanel.closeAllTabs();
			return true;
		});
	}

	private void initState(){
		AppStateManager.initAppState();
	}

	public void switchViewToGlassPane(){
		viewState = GLASS_VIEW;
		remove(toolMenu);
		remove(bottomPane);
		remove(splitPanel);
		add(glassPanel, BorderLayout.CENTER);
		doLayout();
		getContentPane().setVisible(false);
		getContentPane().setVisible(true);
	}

	public void switchViewToContentPane(){
		viewState = CONTENT_VIEW;
		remove(glassPanel);
		add(toolMenu, BorderLayout.NORTH);
		add(bottomPane, BorderLayout.SOUTH);
		add(splitPanel, BorderLayout.CENTER);
		doLayout();
		getContentPane().setVisible(false);
		getContentPane().setVisible(true);
	}

	public void exit(){
		for(AppOperation operation : appClosingOperations){
			if(!operation.performOperation(this)){
				setMessage("A fatal error has occured while closing the App, Aborting Exit!. Save All your editors and kill the app.", "fatal", "closing", "kill the app");
				return;
			}
		}
		dispose();
	}

	public void setMessage(String text){
		bottomPane.getMessagePane().setMessage(text);
	}

	public void setMessage(String text, String... highlights){
		bottomPane.getMessagePane().setMessage(text, highlights);
	}

	public App addAppClosingOperation(AppOperation appOperation){
		appClosingOperations.add(appOperation);
		return this;
	}

	public omegaui.listener.KeyStrokeListener getAppWideKeyStrokeListener() {
		return appWideKeyStrokeListener;
	}
	
	public omegaui.codeblaze.ui.panel.GlassPanel getGlassPanel() {
		return glassPanel;
	}

	public omegaui.codeblaze.ui.panel.TabPanel getTabPanel() {
		return tabPanel;
	}

	public omegaui.codeblaze.ui.component.ToolMenu getToolMenu() {
		return toolMenu;
	}
	
	public omegaui.codeblaze.ui.component.BottomPane getBottomPane() {
		return bottomPane;
	}

	public int getViewState() {
		return viewState;
	}

	public static void main(String[] args){
		FlatLightLaf.install();

		new App();
	}
}
