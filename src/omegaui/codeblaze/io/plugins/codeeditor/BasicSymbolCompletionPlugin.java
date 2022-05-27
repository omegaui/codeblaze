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

package omegaui.codeblaze.io.plugins.codeeditor;
import omegaui.codeblaze.ui.component.CodeEditor;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import omegaui.codeblaze.io.CodeEditorPlugin;
public class BasicSymbolCompletionPlugin implements CodeEditorPlugin, KeyListener{

	private CodeEditor editor;
	
	@Override
	public void install(CodeEditor editor) {
		this.editor = editor;
		editor.addKeyListener(this);
	}

	@Override
	public void uninstall(CodeEditor editor) {
		editor.removeKeyListener(this);
	}

	@Override
	public boolean isCompatible(CodeEditor editor){
		return !editor.getSyntaxEditingStyle().equals(CodeEditor.SYNTAX_STYLE_NONE);
	}

	@Override
	public String getName() {
		return "basic-symbol-completion";
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if(code == KeyEvent.VK_BACK_SPACE)
			autoSymbolExclusion(e);
		else
			autoSymbolCompletion(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	//Managing Smart type code completion
	private void autoSymbolExclusion(KeyEvent e) {
		try {
			switch(editor.getText().charAt(editor.getCaretPosition() - 1)) {
				case '\"':
					if(editor.getText().charAt(editor.getCaretPosition()) == '\"')
						editor.getDocument().remove(editor.getCaretPosition(), 1);
					break;
				case '\'':
					if(editor.getText().charAt(editor.getCaretPosition()) == '\'')
						editor.getDocument().remove(editor.getCaretPosition(), 1);
					break;
				case '<':
					if(editor.getText().charAt(editor.getCaretPosition()) == '>')
						editor.getDocument().remove(editor.getCaretPosition(), 1);
					break;
				case '(':
					if(editor.getText().charAt(editor.getCaretPosition()) == ')')
						editor.getDocument().remove(editor.getCaretPosition(), 1);
					break;
				case '[':
					if(editor.getText().charAt(editor.getCaretPosition()) == ']')
						editor.getDocument().remove(editor.getCaretPosition(), 1);
					break;
				default:
			}
		}
		catch(Exception ex){
			
		}
	}

	private void autoSymbolCompletion(KeyEvent e) {
		try {
			switch (e.getKeyChar()) {
				case '(':
					if(editor.getText().charAt(editor.getCaretPosition()) != ')'){
						editor.insert(")", editor.getCaretPosition());
						editor.setCaretPosition(editor.getCaretPosition() - 1);
					}
					break;
				case '[':
					if(editor.getText().charAt(editor.getCaretPosition()) != ']'){
						editor.insert("]", editor.getCaretPosition());
						editor.setCaretPosition(editor.getCaretPosition() - 1);
					}
					break;
				case '\"':
					if(editor.getText().charAt(editor.getCaretPosition() - 1) != '\\'){
						editor.insert("\"", editor.getCaretPosition());
						editor.setCaretPosition(editor.getCaretPosition() - 1);
					}
					break;
				case '\'':
					if(editor.getText().charAt(editor.getCaretPosition() - 1) != '\\'){
						editor.insert("\'", editor.getCaretPosition());
						editor.setCaretPosition(editor.getCaretPosition() - 1);
					}
					break;
				default:
					break;
			}
		}
		catch(Exception ex) {
			
		}
	}

}
