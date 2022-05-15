package omegaui.codeblaze.io;
import omegaui.codeblaze.ui.component.TerminalComp;

import java.io.File;

import static omegaui.codeblaze.io.ScriptManager.*;
import static omegaui.codeblaze.io.AppResourceManager.*;
import static omegaui.codeblaze.io.UIXManager.*;
import static omegaui.codeblaze.io.AppDataProvider.*;
import static omegaui.component.animation.Animations.*;

public final class ExecutionManager {

	public static boolean compile(File file){
		if(isCompileScriptAvailable(file)){
			TerminalComp terminal = new TerminalComp(new String[]{ 
				getCompilerScriptPath(file), file.getAbsolutePath(), file.getName(), file.getParentFile().getAbsolutePath() 
			}, combineToAbsolutePath(ROOT_DIR_NAME, COMPILER_SCRIPT_DIR_NAME));
			AppInstanceProvider.getCurrentAppInstance().getProcessPanel().addTab(new TabData(file.getName(), getPlatformImage(), terminal, ()->{
				terminal.exit();
			}));
			terminal.setOnProcessExited(()->{
				terminal.print("");
				terminal.print("Process Finished with exit code " + terminal.process.exitValue());
			});
			terminal.start();
			return true;
		}
		return false;
	}

	public static boolean execute(File file){
		return true;
	}

	public static boolean launch(File file){
		return true;
	}
	
}
