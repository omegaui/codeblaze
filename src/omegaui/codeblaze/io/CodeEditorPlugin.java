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
import omegaui.codeblaze.ui.component.CodeEditor;
/*
 * The Plugin interface of the Code Editor.
 * See CodeEditorPluginManager to see how plugins are implanted into the CodeEditor.
 */
public interface CodeEditorPlugin {
	
	/**
	 * Called when the isCompatible(CodeEditor) returns true.
	 * Usually, at this point you want to bind your plugin to the CodeEditor with a KeyListener or something depending upon the feature you are implementing.
	 * See other plugins at omegaui.codeblaze.plugins.codeeditor to find more information.
	 */
	void install(CodeEditor editor);
	
	/**
	 * Called when the plugin is asked to remove its constraints from the editor,
	 * or simply, this should unbind the plugin from the editor.
	 */
	void uninstall(CodeEditor editor);
	
	/**
	 * Using the editor object perform any validation if your plugin is specific to a file or language,
	 * moreover just return true if your plugin does not depends upon the type of file the editor holds.
	 */
	boolean isCompatible(CodeEditor editor);
	
	/**
	 * The Plugin name semantic.
	 * This Should follow package name conventions.
	 * e.g: "basic-code-completion", "extra-shortcuts", and so on.
	 * You can also include version if required but it should look like the following,
	 * e.g: "basic-symbol-completion-1.0", "python-autoindent-1.3", etc.
	 * Moreover, if your plugin is in development phase, then you should include build states as follow,
	 * e.g: "basic-symbol-completion-1.0-beta", "basic-symbol-completion-1.0-alpha", and more.
	 * Remember if your plugin is stable then, including the stable tag is optional, in this case,
	 * "basic-symbol-completion-1.0" and "basic-symbol-completion-1.0-stable" are treated as same plugins.
	 * Happy Hacking!
	 */
	String getName();
}
