package omegaui.codeblaze.io;
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
import static omegaui.codeblaze.io.TemplateManager.*;

import static omegaui.component.animation.Animations.*;

public final class FileManager {

	private static LinkedList<CodeEditor> codeEditors = new LinkedList<>();
	private static FileSelectionDialog fileSelectionDialog;

	static{
		fileSelectionDialog = new FileSelectionDialog(AppInstanceProvider.getCurrentAppInstance());
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
				CodeEditor editor = loadFile(file);
				if(editor != null)
					huntEditor(editor);
			}
		}
	}

	public static void openFile(File file){
		if(file.exists()){
			CodeEditor editor = loadFile(file);
			if(editor != null)
				huntEditor(editor);
		}
	}

	public static CodeEditor loadFile(File file){
		if(file.exists()){
			CodeEditor editor = new CodeEditor(file);
			codeEditors.add(editor);
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

	public static void huntEditor(CodeEditor editor){
		AppInstanceProvider.getCurrentAppInstance().getTabPanel().addTab(
			editor.getFile().getName(), 
			editor.getFile().getAbsolutePath(), 
			editor.getFile().getAbsolutePath(), 
			getPreferredIconForFile(editor.getFile()), 
			editor.getScrollPane(), 
			tertiaryColor, 
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

	public static MaterialPopup createPopup(CodeEditor editor){
		MaterialPopup popup = new MaterialPopup().width(250);
		popup.createItem("Compile", "Ctrl + B", ()->{
			
		});
		popup.createItem("Execute", "Ctrl + L", ()->{
			
		});
		popup.createItem("Save", "Ctrl + S", ()->{
			editor.askAndSaveFile();
		});
		popup.createItem("Silent Save", "Ctrl + SHIFT + S", ()->{
			editor.saveSilently();
		});
		popup.createItem("Reload", "Ctrl + R", ()->{
			editor.reloadFile();
		});
		return popup;
	}

	public static java.util.LinkedList getCodeEditors() {
		return codeEditors;
	}
	
	public static omegaui.codeblaze.ui.dialog.FileSelectionDialog getFileSelectionDialog() {
		return fileSelectionDialog;
	}

}
