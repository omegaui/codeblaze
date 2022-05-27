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
import omegaui.codeblaze.io.plugins.codeeditor.BasicSymbolCompletionPlugin;
import omegaui.codeblaze.io.plugins.codeeditor.BasicEditorShortcutsPlugin;

import omegaui.codeblaze.ui.component.CodeEditor;

import java.util.LinkedList;
public final class CodeEditorPluginManager {
	
	private static LinkedList<CodeEditorPlugin> plugins = new LinkedList<>();
	
	static{
		add(new BasicSymbolCompletionPlugin());
		add(new BasicEditorShortcutsPlugin());
	}

	public static void add(CodeEditorPlugin plugin){
		plugins.add(plugin);
	}

	public static void remove(CodeEditorPlugin plugin){
		plugins.remove(plugin);
	}

	public static void installPlugins(CodeEditor editor){
		getAvailablePlugins(editor).forEach((plugin)->{
			plugin.install(editor);
		});
	}

	public static void uninstallPlugins(CodeEditor editor){
		getAvailablePlugins(editor).forEach((plugin)->{
			plugin.uninstall(editor);
		});
	}

	public static LinkedList<CodeEditorPlugin> getAvailablePlugins(CodeEditor editor){
		LinkedList<CodeEditorPlugin> availablePlugins = new LinkedList<>();
		String language = editor.getSyntaxEditingStyle();
		for(CodeEditorPlugin px : plugins){
			if(px.isCompatible(editor))
				availablePlugins.add(px);
		}
		return availablePlugins;
	}

}
