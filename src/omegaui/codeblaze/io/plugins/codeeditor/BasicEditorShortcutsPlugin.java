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
import omegaui.listener.KeyStrokeListener;

import omegaui.codeblaze.ui.component.CodeEditor;

import omegaui.codeblaze.io.CodeEditorPlugin;
import omegaui.codeblaze.io.CodeEditorUtils;

import static java.awt.event.KeyEvent.*;

public class BasicEditorShortcutsPlugin implements CodeEditorPlugin{

	private KeyStrokeListener listener;

	@Override
	public void install(CodeEditor editor) {
		listener = new KeyStrokeListener(editor);

		listener.putKeyStroke((e)->{
			CodeEditorUtils.duplicateSelection(editor);
			e.consume();
		}, VK_CONTROL, VK_D).setStopKeys(VK_ALT, VK_SHIFT);

		editor.addKeyListener(listener);
	}

	@Override
	public void uninstall(CodeEditor editor) {
		editor.removeKeyListener(listener);
	}

	@Override
	public boolean isCompatible(CodeEditor editor) {
		return true;
	}

	@Override
	public String getName() {
		return "basic-editor-shortcuts";
	}

}
