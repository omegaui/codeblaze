package omegaui.codeblaze.io;
import java.awt.BorderLayout;

import omegaui.codeblaze.ui.dialog.FileSelectionDialog;

import java.io.File;

import omegaui.codeblaze.ui.component.CodeEditor;

import java.util.LinkedList;
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
			CodeEditor editor = loadFile(files.get(0));
			if(editor != null)
				focusTo(editor);
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

	public static void focusTo(CodeEditor editor){
		AppInstanceProvider.getCurrentAppInstance().add(editor.getScrollPane(), BorderLayout.CENTER);
		
		AppInstanceProvider.getCurrentAppInstance().switchViewToContentPane();
	}
}
