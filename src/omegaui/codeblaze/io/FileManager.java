package omegaui.codeblaze.io;
import java.awt.BorderLayout;

import omegaui.codeblaze.ui.dialog.FileSelectionDialog;

import java.io.File;

import omegaui.codeblaze.ui.component.CodeEditor;

import java.util.LinkedList;

import static omegaui.codeblaze.io.UIXManager.*;
import static omegaui.codeblaze.io.AppDataProvider.*;
import static omegaui.component.animation.Animations.*;

public final class FileManager {
	
	private static LinkedList<CodeEditor> codeEditors = new LinkedList<>();
	private static FileSelectionDialog fileSelectionDialog;

	static{
		fileSelectionDialog = new FileSelectionDialog(AppInstanceProvider.getCurrentAppInstance());
	}

	public static void openFile(){
		fileSelectionDialog.setTitle("Open a Local File");
		LinkedList<File> files = fileSelectionDialog.selectFiles();
		if(!files.isEmpty()){
			for(File file : files){
				CodeEditor editor = loadFile(file);
				if(editor != null)
					addNewEditor(editor);
			}
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

	public static void addNewEditor(CodeEditor editor){
		AppInstanceProvider.getCurrentAppInstance().getTabPanel().addTab(
			editor.getFile().getName(), 
			editor.getFile().getAbsolutePath(), 
			editor.getFile().getAbsolutePath(), 
			fileIcon, 
			editor.getScrollPane(), 
			tertiaryColor, 
			null, 
			null
		);
		
		AppInstanceProvider.getCurrentAppInstance().switchViewToContentPane();
	}

	public static void removeCodeEditor(CodeEditor editor){
		codeEditors.remove(editor);
	}
}
