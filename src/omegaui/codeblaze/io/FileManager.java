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

package omegaui.codeblaze.io;
import omegaui.dynamic.database.DataBase;
import omegaui.dynamic.database.DataEntry;

import java.awt.BorderLayout;
import java.awt.Font;

import omegaui.codeblaze.ui.dialog.FileSelectionDialog;

import java.io.File;
import java.io.PrintWriter;
import java.io.FileOutputStream;

import omegaui.codeblaze.ui.component.CodeEditor;
import omegaui.codeblaze.ui.component.MaterialPopup;

import java.util.LinkedList;

import static omegaui.codeblaze.io.UIXManager.*;
import static omegaui.codeblaze.io.AppDataProvider.*;
import static omegaui.codeblaze.io.AppResourceManager.*;
import static omegaui.codeblaze.io.TemplateManager.*;

import static omegaui.component.animation.Animations.*;
/*
 * FileManager is responsible for operations on files like open, close, create, etc
 * and for managing the recent files list and last session state.
 * Contains utility methods to create, open, overriting the file, etc.
 */
public final class FileManager {

	/**
	 * List of currently opened code editors.
	 */
	private static LinkedList<CodeEditor> codeEditors = new LinkedList<>();
	
	/**
	 * The FileSelectionDialog object used by various view like FileCreationPanel, etc.
	 */
	private static FileSelectionDialog fileSelectionDialog;

	/**
	 * The RecentFilesDataBase object.
	 */
	private static DataBase recentFilesDataBase;
	
	/**
	 * The LastSessionDataBase object.
	 */
	private static DataBase lastSessionDataBase;

	/**
	 * Name of recent files dataSet in RecentFilesDataBase.
	 * See recent-files.data.
	 */
	public static final String RECENT_FILE_DATA_SET_NAME = "Recent Files Since First Startup";

	/**
	 * Initializes and Validates the FileManager's dialogs and databases.
	 */
	public static void init(){
		fileSelectionDialog = new FileSelectionDialog(AppInstanceProvider.getCurrentAppInstance());

		recentFilesDataBase = new DataBase(combinePath(ROOT_DIR_NAME, "recent-files.data"));
		validateRecentFilesDataBase();

		lastSessionDataBase = new DataBase(combinePath(ROOT_DIR_NAME, "last-session.data"));

		AppInstanceProvider.getCurrentAppInstance().addAppClosingOperation((app)->{
			System.out.println("Saving Recent Files DataBase ... Total Recent Files : " + recentFilesDataBase.getEntriesAsString(RECENT_FILE_DATA_SET_NAME).size());
			recentFilesDataBase.save();
			return true;
		});
	}

	/**
	 * Puts a recent file in to recent files list.
	 * Checks for duplication.
	 */
	public synchronized static void addRecentFile(String path){
		LinkedList<String> filePaths = recentFilesDataBase.getEntriesAsString(RECENT_FILE_DATA_SET_NAME);
		for(String px : filePaths){
			if(px.equals(path))
				return;
		}
		recentFilesDataBase.addEntry(RECENT_FILE_DATA_SET_NAME, path);
	}

	/**
	 * Validates the RecentFilesDataBase to remove deleted or moved files from the list.
	 */
	public synchronized static void validateRecentFilesDataBase(){
		LinkedList<String> filePaths = recentFilesDataBase.getEntriesAsString(RECENT_FILE_DATA_SET_NAME);
		
		recentFilesDataBase.clear();
		if(!filePaths.isEmpty()){
			for(String path : filePaths){
				if(new File(path).exists())
					addRecentFile(path);
			}
		}
	}
	
	/**
	 * Generates a new file from the file object.
	 * Returns null if file cannot be created else a message.
	 */
	public static synchronized String createNewFile(File file){
		if(file.exists())
			return "File With this Name Already Exists!";
		try{
			PrintWriter writer = new PrintWriter(new FileOutputStream(file));
			if(isTemplateAvailable(file))
				writer.print(getTemplateContent(file));
			writer.close();

			openFile(file);
		}
		catch(Exception e){
			e.printStackTrace();
			return "Access Denied, Cannot Create the requested file!";
		}
		return null;
	}

	/**
	 * Pops the FileSelectionDialog, opens up file(s) and switches the App's VIEW_STATE to ContentPane.
	 */
	public static void openFile(){
		fileSelectionDialog.setTitle("Open a Local File");
		LinkedList<File> files = fileSelectionDialog.selectFiles();
		if(!files.isEmpty()){
			for(File file : files){
				if(isEditorPresent(file))
					setActiveEditor(file);
				else{
					CodeEditor editor = loadFile(file);
					if(editor != null)
						huntEditor(editor);
				}
			}
			AppInstanceProvider.getCurrentAppInstance().switchViewToContentPane();
		}
	}

	/**
	 * Silently opens the file into the CodeEditor without any acknowledgement.
	 */
	public static void openFile(File file){
		if(isEditorPresent(file))
			setActiveEditor(file);
		else if(file.exists()){
			CodeEditor editor = loadFile(file);
			if(editor != null)
				huntEditor(editor);
		}
		AppInstanceProvider.getCurrentAppInstance().switchViewToContentPane();
	}

	/**
	 * Silently creates and returns the object of the CodeEditor of corresponding the file object.
	 * Returns null if the file doesn't actually exists.
	 */
	public static CodeEditor loadFile(File file){
		if(isEditorPresent(file)){
			return getEditor(file);
		}
		if(file.exists()){
			CodeEditor editor = new CodeEditor(file);
			codeEditors.add(editor);
			addRecentFile(file.getAbsolutePath());
			return editor;
		}
		return null;
	}

	/**
	 * Silently overrites a file with new text.
	 * Returns true if the operation completes successfully.
	 */
	public static boolean overwriteToFile(File file, String text){
		try(PrintWriter writer = new PrintWriter(file)){
			writer.print(text);
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Switches focus to the CodeEditor of the file in the TabPanel.
	 */
	public static void setActiveEditor(File file){
		if(isEditorPresent(file)){
			AppInstanceProvider.getCurrentAppInstance().getTabPanel().setActiveTab(AppInstanceProvider.getCurrentAppInstance().getTabPanel().getTab(file.getAbsolutePath()));
			return;
		}
	}

	/**
	 * Adds the CodeEditor instance to the TabPanel.
	 * I forgot why I named it like this.
	 */
	public static void huntEditor(CodeEditor editor){
		AppInstanceProvider.getCurrentAppInstance().getTabPanel().addTab(
			editor.getFile().getName(),
			editor.getFile().getAbsolutePath(),
			editor.getFile().getAbsolutePath(),
			getPreferredIconForFile(editor.getFile()),
			editor.getScrollPane(),
			getPreferredColorForFile(editor.getFile()),
			()->{
				editor.askAndSaveFile();
				removeCodeEditor(editor);
			},
			createPopup(editor)
		);

		AppInstanceProvider.getCurrentAppInstance().switchViewToContentPane();
	}
	
	/**
	 * Removes the CodeEditor instance from the List of reachable editors.
	 */
	public static void removeCodeEditor(CodeEditor editor){
		codeEditors.remove(editor);
	}
	
	/**
	 * Closes the CodeEditor instance provided entirely.
	 * That is, prompts the save changes dialog, closes its tab and removes it from the codeEditors list.
	 */
	public static void closeEditor(CodeEditor editor){
		AppInstanceProvider.getCurrentAppInstance().setMessage("Closing " + editor.getFile().getName() + " ... ", "Closing");
		editor.askAndSaveFile();
		removeCodeEditor(editor);
		AppInstanceProvider.getCurrentAppInstance().getTabPanel().removeTab(AppInstanceProvider.getCurrentAppInstance().getTabPanel().getTabData(editor.getScrollPane()));
		AppInstanceProvider.getCurrentAppInstance().resetMessage();
	}
	
	/**
	 * Returns the corresponding active CodeEditor of the provided file.
	 * Returns null if not found.
	 */
	public static CodeEditor getEditor(File file){
		for(CodeEditor editor : codeEditors){
			if(editor.getFile().getAbsolutePath().equals(file.getAbsolutePath()))
				return editor;
		}
		return null;
	}

	/**
	 * Checks whether the corresponding active CodeEditor of the provided file is available.
	 * Returns true if found.
	 */
	public static boolean isEditorPresent(File file){
		for(CodeEditor editor : codeEditors){
			if(editor.getFile().getAbsolutePath().equals(file.getAbsolutePath()))
				return true;
		}
		return false;
	}
	
	/**
	 * Creates the popup for the CodeEditor instance to used by the TabComp.
	 */
	public static MaterialPopup createPopup(CodeEditor editor){
		MaterialPopup popup = new MaterialPopup().width(250);
		popup.createItem(cookIcon, "Compile & Run", "Ctrl + ALT + R", ()->{
			compileAndExecute(editor);
		});
		popup.createItem(buildIcon, "Compile", "Ctrl + B", ()->{
			compile(editor);
		});
		popup.createItem(runIcon, "Execute", "Ctrl + SHIFT + L", ()->{
			execute(editor);
		});
		popup.createItem(saveIcon, "Save", "Ctrl + S", ()->{
			editor.askAndSaveFile();
		});
		popup.createItem(saveIcon, "Silent Save", "Ctrl + SHIFT + S", ()->{
			editor.saveSilently();
		});
		popup.createItem(reloadIcon, "Reload", "Ctrl + R", ()->{
			editor.reloadFile();
		});
		return popup;
	}
	
	/**
	 * Saves the CodeEditor's file silently before launching the file if the Auto-Save-File-Before-Launch is enabled.
	 */
	public static synchronized void performAutoSaveIfPreferred(CodeEditor editor){
		DataEntry entry = get(AUTO_SAVE_FILE_BEFORE_LAUNCH_PROPERTY);
		if(entry != null && entry.getValueAsBoolean())
			editor.saveSilently();
	}

	/**
	 * Compiles the CodeEditor's file and opens up the watchdog in the Terminal.
	 */
	public static synchronized void compile(CodeEditor editor){
		new Thread(()->{
			performAutoSaveIfPreferred(editor);
			File file = editor.getFile();
			AppInstanceProvider.getCurrentAppInstance().setMessage("Compiling " + file.getName() + " ...", "Compiling");
			if(ExecutionManager.compile(file) == 404){
				AppInstanceProvider.getCurrentAppInstance().setMessage("No Compiler Script is available for " + file.getName(), "No");
			}
			else
				AppInstanceProvider.getCurrentAppInstance().resetMessage();
		}).start();
	}

	/**
	 * Executes the CodeEditor's file and opens up the watchdog in the Terminal.
	 */
	public static synchronized void execute(CodeEditor editor){
		new Thread(()->{
			performAutoSaveIfPreferred(editor);
			File file = editor.getFile();
			AppInstanceProvider.getCurrentAppInstance().setMessage("Executing " + file.getName() + " ...", "Executing");
			if(ExecutionManager.execute(file) == 404){
				AppInstanceProvider.getCurrentAppInstance().setMessage("No Interpreter Script is available for " + file.getName(), "No");
			}
			else
				AppInstanceProvider.getCurrentAppInstance().resetMessage();
		}).start();
	}

	/**
	 * Compiles and Executes (if the Compilation Process returns 0) the CodeEditor's file and opens up the watchdog in the Terminal.
	 */
	public static synchronized void compileAndExecute(CodeEditor editor){
		new Thread(()->{
			performAutoSaveIfPreferred(editor);
			File file = editor.getFile();
			int compilationScriptExitValue = ExecutionManager.compile(file);
			if(compilationScriptExitValue == 404){
				AppInstanceProvider.getCurrentAppInstance().setMessage("No Interpreter Script is available for " + file.getName(), "No");
				execute(editor);
			}
			else if(compilationScriptExitValue == 0){
				execute(editor);
			}
		}).start();
	}

	/**
	 * Executes the File-Loaded event-script of the CodeEditor's file silently.
	 */
	public static synchronized void executeFileLoadedEventScript(CodeEditor editor){
		new Thread(()->{
			File file = editor.getFile();
			ExecutionManager.executeEventScript(file, FILE_LOADED_EVENT_SCRIPTS_DIR_NAME);
		}).start();
	}

	/**
	 * Executes the File-Saves event-script of the CodeEditor's file silently.
	 */
	public static synchronized void executeFileSavedEventScript(CodeEditor editor){
		new Thread(()->{
			File file = editor.getFile();
			ExecutionManager.executeEventScript(file, FILE_SAVED_EVENT_SCRIPTS_DIR_NAME);
		}).start();
	}

	/**
	 * Increases the Font Size of all the CodeEditor by 1 pixel.
	 */
	public static void increaseDocumentFontSize(){
		if(codeEditors.isEmpty())
			return;
		
		CodeEditor initialEditor = codeEditors.get(0);
		
		Font newFont = new Font(initialEditor.getFont().getName(), initialEditor.getFont().getStyle(), initialEditor.getFont().getSize() + 1);
		codeEditors.forEach((editor)->{
			editor.setFont(newFont);
		});

		appDataBase().updateEntry(DOCUMENT_FONT_PROPERTY, initialEditor.getFont().getName() + "\n" + initialEditor.getFont().getStyle() + "\n" + (initialEditor.getFont().getSize() + 1), 0);
	}

	/**
	 * Decreases the Font Size of all the CodeEditor by 1 pixel.
	 */
	public static void decreaseDocumentFontSize(){
		if(codeEditors.isEmpty())
			return;
		
		CodeEditor initialEditor = codeEditors.get(0);
		if(initialEditor.getFont().getSize() <= 8)
			return;
		
		Font newFont = new Font(initialEditor.getFont().getName(), initialEditor.getFont().getStyle(), initialEditor.getFont().getSize() - 1);
		codeEditors.forEach((editor)->{
			editor.setFont(newFont);
		});
		
		appDataBase().updateEntry(DOCUMENT_FONT_PROPERTY, initialEditor.getFont().getName() + "\n" + initialEditor.getFont().getStyle() + "\n" + (initialEditor.getFont().getSize() - 1), 0);
	}

	/**
	 * Increases the Tab Size of all the CodeEditor by 1 space.
	 */
	public static void increaseDocumentTabSize(){
		if(codeEditors.isEmpty())
			return;
		
		CodeEditor initialEditor = codeEditors.get(0);
		
		int size = initialEditor.getTabSize() + 1;
		codeEditors.forEach((editor)->{
			editor.setTabSize(size);
		});

		appDataBase().updateEntry(DOCUMENT_TAB_SIZE_PROPERTY, String.valueOf(size), 0);
	}

	/**
	 * Decreases the Tab Size of all the CodeEditor by 1 space.
	 */
	public static void decreaseDocumentTabSize(){
		if(codeEditors.isEmpty())
			return;
		
		CodeEditor initialEditor = codeEditors.get(0);
		if(initialEditor.getTabSize() <= 2)
			return;
		
		int size = initialEditor.getTabSize() - 1;
		codeEditors.forEach((editor)->{
			editor.setTabSize(size);
		});

		appDataBase().updateEntry(DOCUMENT_TAB_SIZE_PROPERTY, String.valueOf(size), 0);
	}

	public static java.util.LinkedList getCodeEditors() {
		return codeEditors;
	}

	public static omegaui.codeblaze.ui.dialog.FileSelectionDialog getFileSelectionDialog() {
		return fileSelectionDialog;
	}

	public static omegaui.dynamic.database.DataBase getRecentFilesDataBase() {
		return recentFilesDataBase;
	}

	public static omegaui.dynamic.database.DataBase getLastSessionDataBase() {
		return lastSessionDataBase;
	}
	
}
