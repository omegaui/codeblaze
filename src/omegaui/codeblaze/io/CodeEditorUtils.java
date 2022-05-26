package omegaui.codeblaze.io;
import javax.swing.text.Document;

import omegaui.codeblaze.ui.component.CodeEditor;
public final class CodeEditorUtils {

	public synchronized static void duplicateSelection(CodeEditor editor){
		String selection = editor.getSelectedText();
		if(selection == null){
			selection = editor.getText().substring(0, editor.getCaretPosition());
			selection = selection.substring(selection.lastIndexOf('\n'));
		}
		editor.insert(selection, editor.getCaretPosition());
	}
	
}
