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
