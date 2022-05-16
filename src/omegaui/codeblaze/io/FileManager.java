package omegaui.codeblaze.io;
import omegaui.dynamic.database.DataBase;

import java.awt.BorderLayout;
		

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
			compileAndExecute(editor.getFile());
		});
		popup.createItem(buildIcon, "Compile", "Ctrl + B", ()->{
			compile(editor.getFile());
		});
		popup.createItem(runIcon, "Execute", "Ctrl + SHIFT + L", ()->{
			execute(editor.getFile());
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

	public static synchronized void compile(File file){
		new Thread(()->{
			AppInstanceProvider.getCurrentAppInstance().setMessage("Compiling " + file.getName() + " ...", "Compiling");
			if(ExecutionManager.compile(file) == 404){
				AppInstanceProvider.getCurrentAppInstance().setMessage("No Compiler Script is available for " + file.getName(), "No");
			}
			else
				AppInstanceProvider.getCurrentAppInstance().resetMessage();
		}).start();
	}

	public static synchronized void execute(File file){
		new Thread(()->{
			AppInstanceProvider.getCurrentAppInstance().setMessage("Executing " + file.getName() + " ...", "Executing");
			if(ExecutionManager.execute(file) == 404){
				AppInstanceProvider.getCurrentAppInstance().setMessage("No Interpreter Script is available for " + file.getName(), "No");
			}
			else
				AppInstanceProvider.getCurrentAppInstance().resetMessage();
		}).start();
	}

	public static synchronized void compileAndExecute(File file){
		new Thread(()->{
			int compilationScriptExitValue = ExecutionManager.compile(file);
			if(compilationScriptExitValue == 404){
				AppInstanceProvider.getCurrentAppInstance().setMessage("No Interpreter Script is available for " + file.getName(), "No");
				execute(file);
			}
			else if(compilationScriptExitValue == 0){
				execute(file);	
			}
		}).start();
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
