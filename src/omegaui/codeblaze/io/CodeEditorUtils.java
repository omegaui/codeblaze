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
import javax.swing.text.Document;

import omegaui.codeblaze.ui.component.CodeEditor;
/*
 * Contains some common CodeEditor utility methods.
 */
public final class CodeEditorUtils {
	
	/**
	 * Duplicates the selected text in a CodeEditor.
	 * If no text is selected then, the current line from the 0th offset to the CaretPosition is duplicated on the next line.
	 */
	public synchronized static void duplicateSelection(CodeEditor editor){
		String selection = editor.getSelectedText();
		if(selection == null){
			selection = editor.getText().substring(0, editor.getCaretPosition());
			selection = selection.substring(selection.lastIndexOf('\n'));
		}
		editor.insert(selection, editor.getCaretPosition());
	}

	/**
	 * Finds and Highlights the occurences of the current selected text
	 * (Unimplemented yet!)
	 */
	public synchronized static void findSelection(CodeEditor editor){
		
	}
	
}
