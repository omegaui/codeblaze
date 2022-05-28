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
/*
 * CodeEditorPluginManager manages plugin implantation into the CodeEditor.
 * Contains a mutable list of available plugins.
 * Deals in Installation and Uninstallation of CodeEditor plugins.
 * Can provide the list of all Compatible plugins for a CodeEditor instance.
 */
public final class CodeEditorPluginManager {
	
	/**
	 * List of available CodeEditorPlugins.
	 * To add/remove your own plugin see add(CodeEditorPlugin) and remove(CodeEditorPlugin)
	 */
	private static final LinkedList<CodeEditorPlugin> plugins = new LinkedList<>();
	
	/*
	 * Some Default Plugins are getting implanted here.
	 */
	static{
		add(new BasicSymbolCompletionPlugin());
		add(new BasicEditorShortcutsPlugin());
	}
	
	/**
	 * Adds a CodeEditorPlugin to the list.
	 */
	public static void add(CodeEditorPlugin plugin){
		plugins.add(plugin);
	}

	/**
	 * Removes a CodeEditorPlugin from the list.
	 */
	public static void remove(CodeEditorPlugin plugin){
		plugins.remove(plugin);
	}

	/**
	 * Installs all compatible plugin to a CodeEditor.
	 */
	public static void installPlugins(CodeEditor editor){
		getAvailablePlugins(editor).forEach((plugin)->{
			plugin.install(editor);
		});
	}

	/**
	 * Uninstall all compatible plugins from a CodeEditor.
	 */
	public static void uninstallPlugins(CodeEditor editor){
		getAvailablePlugins(editor).forEach((plugin)->{
			plugin.uninstall(editor);
		});
	}

	/**
	 * Returns the list of available plugins for a CodeEditor.
	 */
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
