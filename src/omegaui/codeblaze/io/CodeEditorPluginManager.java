package omegaui.codeblaze.io;
import omegaui.codeblaze.io.plugins.codeeditor.BasicSymbolCompletionPlugin;

import omegaui.codeblaze.ui.component.CodeEditor;

import java.util.LinkedList;
public final class CodeEditorPluginManager {
	
	private static LinkedList<CodeEditorPlugin> plugins = new LinkedList<>();
	
	static{
		add(new BasicSymbolCompletionPlugin());
	}

	public static void add(CodeEditorPlugin plugin){
		plugins.add(plugin);
	}

	public static void remove(CodeEditorPlugin plugin){
		plugins.remove(plugin);
	}

	public static void installPlugins(CodeEditor editor){
		getAvailablePlugins(editor).forEach((plugin)->{
			System.out.println(plugin.getName());
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
			if(px.getLanguage().equals("*") || px.getLanguage().equals(language))
				availablePlugins.add(px);
		}
		return availablePlugins;
	}

}
