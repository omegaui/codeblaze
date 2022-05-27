/*
 * Copyright (C) 2022 Omega UI

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package omegaui.codeblaze;
import omegaui.codeblaze.ui.dialog.PreferencesDialog;

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
import omegaui.codeblaze.io.ExecutionManager;

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

/*
 * The Sole Main Frame of the whole application,
 * Every track ends up joining this.
 */

public class App extends JFrame {
	
	/**
	 * The Main Frame has two view modes,
	 * Content View : Contains the Tool Menu, the Tab System, etc.
	 * Glass View : Contains Views like File Creation Panel, Launcher, etc.
	 */
	public static final int CONTENT_VIEW = 1;
	public static final int GLASS_VIEW = 2;

	/**
	 * Initial View state is set to -1 but is inferred by AppStateManager class.
	 */
	private int viewState = -1;
	
	/**
	 * The Object of the Glass Panel.
	 * GlassPanel gains visibility when switchViewToGlassPane() is called.
	 */
	private GlassPanel glassPanel;
	
	/**
	 * The Object of the Tool Menu.
	 * Gains Visibility when switchViewToContentPane() is called.
	 */
	private ToolMenu toolMenu;
	
	/**
	 * The Object of the Bottom Pane.
	 * Gains Visibility when switchViewToContentPane() is called.
	 */
	private BottomPane bottomPane;

	/**
	 * App<->Wide KeyStrokeListener.
	 * This is directory registered to the KeyBoardFocusManager.
	 */
	private KeyStrokeListener appWideKeyStrokeListener;
	
	/**
	 * The Custom Functionality SplitPanel from omegaide.
	 * Uses Vertical Split Mode.
	 */
	private SplitPanel splitPanel;
	
	/**
	 * The Awesome Custom Tab Panel UI Element written from scratch. 
	 */
	private TabPanel tabPanel;
	
	/**
	 * The Panel that shows any executing process like File Launches and Active Terminals.
	 */
	private ProcessPanel processPanel;
	
	/**
	 * List of AppOperations to be executed after CodeBlaze initializes.
	 */
	private LinkedList<AppOperation> appOpeningOperations = new LinkedList<>();
	
	/**
	 * List of AppOperations to be executed before CodeBlaze quits.
	 */
	private LinkedList<AppOperation> appClosingOperations = new LinkedList<>();
	
	/**
	 * The Object of the Preferences Dialog.
	 */
	private PreferencesDialog preferencesDialog;

	/**
	 * A private constructor to prevent launching multiple instances on the same JVM cause we have some static data like app.settings
	 */
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
		initDefaults();

		initState();

		setVisible(true);

		ExecutionManager.executeEventScripts(CODEBLAZE_READY_EVENT_NAME, CODEBLAZE_READY_EVENT_SCRIPTS_DIR_NAME);
	}
	
	/**
	 * Does what its name says.
	 */
	private void registerAppInstanceProvider(){
		AppInstanceProvider.setCurrentAppInstance(this);
	}

	/**
	 * Triggers AppResourceManager.
	 */
	private void initResources(){
		AppResourceManager.checkResources();
	}

	/**
	 * Initializes App<->Wide KeyStrokeListener with its default shortcuts.
	 */
	private void initAppWideKeyStrokeListener(){
		appWideKeyStrokeListener = new KeyStrokeListener(this);
		//File Menu Shortcuts
		appWideKeyStrokeListener.putKeyStroke((e)->{
			switchToCreateNewFilePanel();
			e.consume();
		}, VK_CONTROL, VK_N).setStopKeys(VK_ALT, VK_SHIFT).useAutoReset();
		appWideKeyStrokeListener.putKeyStroke((e)->{
			FileManager.openFile();
			e.consume();
		}, VK_CONTROL, VK_SHIFT, VK_N).setStopKeys(VK_ALT).useAutoReset();
		appWideKeyStrokeListener.putKeyStroke((e)->{
			switchToRecentFilesPanel();
			e.consume();
		}, VK_CONTROL, VK_SHIFT, VK_R).setStopKeys(VK_ALT).useAutoReset();
		appWideKeyStrokeListener.putKeyStroke((e)->{
			saveAllEditors();
			e.consume();
		}, VK_CONTROL, VK_ALT, VK_S).setStopKeys(VK_SHIFT).useAutoReset();
		appWideKeyStrokeListener.putKeyStroke((e)->{
			showPreferencesDialog();
			e.consume();
		}, VK_CONTROL, VK_ALT, VK_P).setStopKeys(VK_SHIFT).useAutoReset();

		//View Menu Shortcuts
		appWideKeyStrokeListener.putKeyStroke((e)->{
			TerminalManager.openNewTerminal();
			e.consume();
		}, VK_CONTROL, VK_SHIFT, VK_T).setStopKeys(VK_ALT).useAutoReset();

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

	/**
	 * Does what the name spells like.
	 */
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

	/**
	 * Initialized any App Dialogs and Default AppOperations.
	 */
	private void initDefaults(){
		preferencesDialog = new PreferencesDialog(this);
		
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
			boolean silentSave = false;
			DataEntry saveFileOnExitEntry = appDataBase().getEntryAt(AUTO_SAVE_FILE_ON_EXIT_PROPERTY);
			if(saveFileOnExitEntry != null)
				silentSave = saveFileOnExitEntry.getValueAsBoolean();
			if(silentSave)
				saveAllEditors();
			else
				tabPanel.closeAllTabs();
			return true;
		});

		addAppClosingOperation((app)->{
			ExecutionManager.executeEventScriptsAndWait(CODEBLAZE_EXITING_EVENT_NAME, CODEBLAZE_EXITING_EVENT_SCRIPTS_DIR_NAME);
			return true;
		});

	}

	/**
	 * Initialized AppStateManager and Trigger App Opening Operations.
	 */
	private void initState(){
		AppStateManager.initAppState();
		for(AppOperation operation : appOpeningOperations){
			if(!operation.performOperation(this)){
				setMessage("Some Error Occured during initialization, If you launched CodeBlaze from the Command Line there may be some logs.", "Error", "Command Line");
				return;
			}
		}
	}

	/**
	 * Passes Visibility to the FileCreationPanel
	 */
	public void switchToCreateNewFilePanel(){
		switchViewToGlassPane();
		getGlassPanel().putToView(GlassPanel.getFileCreationPanel());
	}

	/**
	 * Passes Visibility to the RecentFilesPanel
	 */
	public void switchToRecentFilesPanel(){
		switchViewToGlassPane();
		getGlassPanel().putToView(GlassPanel.getRecentFilesPanel());
	}

	/**
	 * Puts GlassPanel into view.
	 */
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

	/**
	 * Puts App's Content Pane into view.
	 */
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

	/**
	 * Triggers App Closing Operations and closes the App.
	 */
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

	/**
	 * Resets the message shown on the BottomPane.
	 */
	public void resetMessage(){
		bottomPane.getMessagePane().resetMessage();
	}

	/**
	 * Sets the message to be displayed on the BottomPane's MessageBox.
	 */
	public void setMessage(String text){
		bottomPane.getMessagePane().setMessage(text);
	}

	/**
	 * Sets the message to be displayed on the BottomPane's MessageBox.
	 * Also takes varargs containing texts to be highlighted.
	 */
	public void setMessage(String text, String... highlights){
		bottomPane.getMessagePane().setMessage(text, highlights);
	}

	/**
	 * Builder Form Method to add AppOperation for App Opening Event.
	 */
	public App addAppOpeningOperation(AppOperation appOperation){
		appOpeningOperations.add(appOperation);
		return this;
	}

	/**
	 * Builder Form Method to add AppOperation for App Closing Event.
	 */
	public App addAppClosingOperation(AppOperation appOperation){
		appClosingOperations.add(appOperation);
		return this;
	}

	/**
	 * Saves All the Editors present in the current instance.
	 */
	public void saveAllEditors(){
		tabPanel.getAllEditors().forEach((editor)->editor.saveSilently());
		setMessage("Invoked Silent Save File on All Editors! -- Shortcut: Ctrl + ALT + S", "Silent Save", "All Editors");
	}

	/**
	 * Closes All the Editors present in the current instance.
	 */
	public void closeAllEditors(){
		tabPanel.closeAllTabs();
	}

	/**
	 * Shows the Preferences Dialog
	 */
	public void showPreferencesDialog(){
		preferencesDialog.setVisible(true);
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

	public omegaui.codeblaze.ui.dialog.PreferencesDialog getPreferencesDialog() {
		return preferencesDialog;
	}
	
	/**
	 * The Entry Point of the Whole Application, you know well.
	 */
	public static void main(String[] args){
		new App();
	}
}
