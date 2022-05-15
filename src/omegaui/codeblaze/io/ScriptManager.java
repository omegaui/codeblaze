package omegaui.codeblaze.io;
import java.io.File;

import static omegaui.codeblaze.io.AppResourceManager.*;

public final class ScriptManager {

	public static String getQualifiedName(File file){
		String name = file.getName();
		if(name.contains("."))
			name = name.substring(name.lastIndexOf('.') + 1);
		return name;
	}

	public static String getCompilerScriptPath(File file){
		return combineToAbsolutePath(ROOT_DIR_NAME, COMPILER_SCRIPT_DIR_NAME, getCompilerScriptName(file));
	}

	public static String getInterpreterScriptPath(File file){
		return combineToAbsolutePath(ROOT_DIR_NAME, INTERPRETER_SCRIPT_DIR_NAME, getInterpreterScriptName(file));
	}

	public static String getCompilerScriptName(File file){
		return "compile-" + getQualifiedName(file) + EXE_EXT;
	}

	public static String getInterpreterScriptName(File file){
		return "interpret-" + getQualifiedName(file) + EXE_EXT;
	}
	
	public static boolean isCompileScriptAvailable(File file){
		return isPathExists(ROOT_DIR_NAME, COMPILER_SCRIPT_DIR_NAME, getCompilerScriptName(file));
	}
	
	public static boolean isInterpreterScriptAvailable(File file){
		return isPathExists(ROOT_DIR_NAME, INTERPRETER_SCRIPT_DIR_NAME, getInterpreterScriptName(file));
	}
	
}
