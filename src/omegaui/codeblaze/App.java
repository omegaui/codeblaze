package omegaui.codeblaze;
import org.fife.ui.rtextarea.RTextScrollPane;

import java.io.File;

import omegaui.dynamic.database.DataBase;
import omegaui.dynamic.database.DataEntry;

import com.formdev.flatlaf.FlatLightLaf;

import omegaui.component.io.AppOperation;

import java.util.LinkedList;

import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import omegaui.listener.KeyStrokeListener;

import omegaui.codeblaze.ui.component.ToolMenu;
import omegaui.codeblaze.ui.component.BottomPane;
import omegaui.codeblaze.ui.component.CodeEditor;

import omegaui.codeblaze.io.AppInstanceProvider;
import omegaui.codeblaze.io.AppStateManager;
import omegaui.codeblaze.io.FileManager;
import omegaui.codeblaze.io.UIXManager;
import omegaui.codeblaze.io.AppResourceManager;
import omegaui.codeblaze.io.TerminalManager;

import omegaui.codeblaze.ui.panel.GlassPanel;
import omegaui.codeblaze.ui.panel.SplitPanel;
import omegaui.codeblaze.ui.panel.TabPanel;
import omegaui.codeblaze.ui.panel.ProcessPanel;

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
import static omegaui.codeblaze.io.AppResourceManager.*;

import static java.awt.event.KeyEvent.*;

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
	private ProcessPanel processPanel;

	private LinkedList<AppOperation> appOpeningOperations = new LinkedList<>();
	private LinkedList<AppOperation> appClosingOperations = new LinkedList<>();

	private App(){
		super("CodeBlaze");

		setSize(1000, 650);
		setMinimumSize(getSize());
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setIconImage(appIcon);

		registerAppInstanceProvider();

		initResources();

		initAppWideKeyStrokeListener();
		initUI();
		initDefaultAppOperations();

		initState();

		setVisible(true);
	}

	private void registerAppInstanceProvider(){
		AppInstanceProvider.setCurrentAppInstance(this);
	}

	private void initResources(){
		AppResourceManager.checkResources();
	}

	private void initAppWideKeyStrokeListener(){
		appWideKeyStrokeListener = new KeyStrokeListener(this);
		appWideKeyStrokeListener.putKeyStroke((e)->{
			switchToCreateNewFilePanel();
			e.consume();
		}, VK_CONTROL, VK_N).setStopKeys(VK_ALT, VK_SHIFT);
		appWideKeyStrokeListener.putKeyStroke((e)->{
			FileManager.openFile();
			e.consume();
		}, VK_CONTROL, VK_SHIFT, VK_N).setStopKeys(VK_ALT);
		appWideKeyStrokeListener.putKeyStroke((e)->{
			switchToRecentFilesPanel();
			e.consume();
		}, VK_CONTROL, VK_SHIFT, VK_R).setStopKeys(VK_ALT);
		appWideKeyStrokeListener.putKeyStroke((e)->{
			saveAllEditors();
			e.consume();
		}, VK_CONTROL, VK_ALT, VK_S).setStopKeys(VK_SHIFT);

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

		if(!appDataBase().getEntryAt("App Theme Mode").getValue().equals("light"))
			initDarkMode();
		else
			FlatLightLaf.install();

		setBackground(back2);

		toolMenu = new ToolMenu(this);

		bottomPane = new BottomPane(this);

		glassPanel = new GlassPanel(this);

		tabPanel = new TabPanel(TabPanel.TAB_LOCATION_TOP);

		processPanel = new ProcessPanel(this);

		splitPanel = new SplitPanel(SplitPanel.VERTICAL_SPLIT);
		splitPanel.setTopComponent(tabPanel);
		splitPanel.setBottomComponent(processPanel);

	}

	private void initDefaultAppOperations(){
		FileManager.init();

		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				exit();
			}
		});

		addAppOpeningOperation((app)->{
			DataEntry extendedStateProperty = FileManager.getLastSessionDataBase().getEntryAt(LAST_APP_WINDOW_STATE_PROPERTY);
			if(extendedStateProperty != null){
				setExtendedState(extendedStateProperty.getValueAsInt());
			}
			LinkedList<DataEntry> entries = FileManager.getLastSessionDataBase().getEntries(LAST_SESSION_FILES_PROPERTY);
			if(!entries.isEmpty())
				switchViewToContentPane();
			else
				return true;
			System.out.println("Restoring " + entries.size() + " Editors!");
			for(DataEntry entry : entries){
				File file = new File(entry.lines().get(0));
				if(file.exists()){
					FileManager.openFile(file);
				}
			}
			new Thread(()->{
				try{
					Thread.sleep(100);
				}catch(Exception e){
					
				}
				for(DataEntry entry : entries){
					File file = new File(entry.lines().get(0));
					CodeEditor editor = FileManager.getEditor(file);
					if(editor != null){
						editor.setCaretPosition(Integer.parseInt(entry.lines().get(1)));
						editor.getScrollPane().getVerticalScrollBar().setValue(Integer.parseInt(entry.lines().get(2)));
					}
				}
				DataEntry entry = FileManager.getLastSessionDataBase().getEntryAt(LAST_FOCUSSED_EDITOR_PROPERTY);
				if(entry != null){
					File file = new File(entry.getValue());
					if(file.exists()){
						tabPanel.setActiveTab(tabPanel.getTab(file.getAbsolutePath()));
					}
				}
			}).start();
			return true;
		});

		addAppClosingOperation((app)->{
			TerminalManager.closeAllTerminal();
			return true;
		});

		addAppClosingOperation((app)->{
			AppResourceManager.saveAppDataBase();
			return true;
		});

		addAppClosingOperation((app)->{
			DataBase lastSessionDataBase = FileManager.getLastSessionDataBase();
			lastSessionDataBase.clear();
			lastSessionDataBase.addEntry(LAST_APP_WINDOW_STATE_PROPERTY, String.valueOf(getExtendedState()));
			if(tabPanel.isEmpty()){
				lastSessionDataBase.save();
				return true;
			}
			LinkedList<CodeEditor> allEditors = FileManager.getCodeEditors();
			System.out.println("Found " + allEditors.size() + " Editors Left Open!");
			System.out.print("Saving Session Data ... ");
			for(CodeEditor editor : allEditors){
				lastSessionDataBase.addEntry(LAST_SESSION_FILES_PROPERTY, editor.getFile().getAbsolutePath() + "\n" + editor.getCaretPosition() + "\n" + editor.getScrollPane().getVerticalScrollBar().getValue());
			}
			CodeEditor editor = (CodeEditor)(((RTextScrollPane)(tabPanel.getLastActiveTabData().getComponent())).getTextArea());
			lastSessionDataBase.addEntry(LAST_FOCUSSED_EDITOR_PROPERTY, editor.getFile().getAbsolutePath());
			lastSessionDataBase.save();
			System.out.println("Done!");
			return true;
		});

		addAppClosingOperation((app)->{
			tabPanel.closeAllTabs();
			return true;
		});

	}

	private void initState(){
		AppStateManager.initAppState();
		for(AppOperation operation : appOpeningOperations){
			if(!operation.performOperation(this)){
				setMessage("Some Error Occured during initialization, If you launched CodeBlaze from the Command Line there may be some logs.", "Error", "Command Line");
				return;
			}
		}
	}

	public void switchToCreateNewFilePanel(){
		switchViewToGlassPane();
		getGlassPanel().putToView(GlassPanel.getFileCreationPanel());
	}

	public void switchToRecentFilesPanel(){
		switchViewToGlassPane();
		getGlassPanel().putToView(GlassPanel.getRecentFilesPanel());
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
		if(tabPanel.getTabs().isEmpty())
			return;
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
		System.exit(0);
	}

	public void resetMessage(){
		bottomPane.getMessagePane().resetMessage();
	}

	public void setMessage(String text){
		bottomPane.getMessagePane().setMessage(text);
	}

	public void setMessage(String text, String... highlights){
		bottomPane.getMessagePane().setMessage(text, highlights);
	}

	public App addAppOpeningOperation(AppOperation appOperation){
		appOpeningOperations.add(appOperation);
		return this;
	}

	public App addAppClosingOperation(AppOperation appOperation){
		appClosingOperations.add(appOperation);
		return this;
	}

	public void saveAllEditors(){
		tabPanel.getAllEditors().forEach((editor)->editor.saveSilently());
		setMessage("You invoked Silent Save File on All Editors! -- Shortcut: Ctrl + ALT + S", "Silent Save", "All Editors");
	}

	public void closeAllEditors(){
		tabPanel.closeAllTabs();
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

	public omegaui.codeblaze.ui.panel.ProcessPanel getProcessPanel() {
		return processPanel;
	}

	public omegaui.codeblaze.ui.component.ToolMenu getToolMenu() {
		return toolMenu;
	}

	public omegaui.codeblaze.ui.component.BottomPane getBottomPane() {
		return bottomPane;
	}

	public omegaui.codeblaze.ui.panel.SplitPanel getSplitPanel() {
		return splitPanel;
	}

	public int getViewState() {
		return viewState;
	}

	public static void main(String[] args){
		new App();
	}
}
