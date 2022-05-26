package omegaui.codeblaze.io;
import omegaui.codeblaze.ui.component.CodeEditor;
public interface CodeEditorPlugin {
	void install(CodeEditor editor);
	void uninstall(CodeEditor editor);
	boolean isCompatible(CodeEditor editor);
	String getName();
}
