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

public final class FileManager {

	private static LinkedList<CodeEditor> codeEditors = new LinkedList<>();
	private static FileSelectionDialog fileSelectionDialog;

	private static DataBase recentFilesDataBase;
	private static DataBase lastSessionDataBase;

	public static final String RECENT_FILE_DATA_SET_NAME = "Recent Files Since First Startup";

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

	public synchronized static void addRecentFile(String path){
		LinkedList<String> filePaths = recentFilesDataBase.getEntriesAsString(RECENT_FILE_DATA_SET_NAME);
		for(String px : filePaths){
			if(px.equals(path))
				return;
		}
		recentFilesDataBase.addEntry(RECENT_FILE_DATA_SET_NAME, path);
	}

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

	public static void setActiveEditor(File file){
		if(isEditorPresent(file)){
			AppInstanceProvider.getCurrentAppInstance().getTabPanel().setActiveTab(AppInstanceProvider.getCurrentAppInstance().getTabPanel().getTab(file.getAbsolutePath()));
			return;
		}
	}

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

	public static void removeCodeEditor(CodeEditor editor){
		codeEditors.remove(editor);
	}

	public static void closeEditor(CodeEditor editor){
		AppInstanceProvider.getCurrentAppInstance().setMessage("Closing " + editor.getFile().getName() + " ... ", "Closing");
		editor.askAndSaveFile();
		removeCodeEditor(editor);
		AppInstanceProvider.getCurrentAppInstance().getTabPanel().removeTab(AppInstanceProvider.getCurrentAppInstance().getTabPanel().getTabData(editor.getScrollPane()));
		AppInstanceProvider.getCurrentAppInstance().resetMessage();
	}

	public static CodeEditor getEditor(File file){
		for(CodeEditor editor : codeEditors){
			if(editor.getFile().getAbsolutePath().equals(file.getAbsolutePath()))
				return editor;
		}
		return null;
	}

	public static boolean isEditorPresent(File file){
		for(CodeEditor editor : codeEditors){
			if(editor.getFile().getAbsolutePath().equals(file.getAbsolutePath()))
				return true;
		}
		return false;
	}

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

	public static synchronized void performAutoSaveIfPreferred(CodeEditor editor){
		DataEntry entry = get(AUTO_SAVE_FILE_BEFORE_LAUNCH_PROPERTY);
		if(entry != null && entry.getValueAsBoolean())
			editor.saveSilently();
	}

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

	public static synchronized void executeFileLoadedEventScript(CodeEditor editor){
		new Thread(()->{
			File file = editor.getFile();
			ExecutionManager.executeEventScript(file, FILE_LOADED_EVENT_SCRIPTS_DIR_NAME);
		}).start();
	}

	public static synchronized void executeFileSavedEventScript(CodeEditor editor){
		new Thread(()->{
			File file = editor.getFile();
			ExecutionManager.executeEventScript(file, FILE_SAVED_EVENT_SCRIPTS_DIR_NAME);
		}).start();
	}

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
