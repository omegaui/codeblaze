package omegaui.codeblaze.io;
import java.util.LinkedList;

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

	public static String getFileLoadedEventScriptPath(File file){
		return combineToAbsolutePath(ROOT_DIR_NAME, EVENT_SCRIPT_DIR_NAME, FILE_LOADED_EVENT_SCRIPTS_DIR_NAME, getFileEventScriptName(file));
	}

	public static String getFileSavedEventScriptPath(File file){
		return combineToAbsolutePath(ROOT_DIR_NAME, EVENT_SCRIPT_DIR_NAME, FILE_SAVED_EVENT_SCRIPTS_DIR_NAME, getFileEventScriptName(file));
	}

	public static String getCompilerScriptName(File file){
		return "compile-" + getQualifiedName(file) + EXE_EXT;
	}

	public static String getInterpreterScriptName(File file){
		return "interpret-" + getQualifiedName(file) + EXE_EXT;
	}
	
	public static String getFileEventScriptName(File file){
		return "script-" + getQualifiedName(file) + EXE_EXT;
	}

	public static LinkedList<File> getAllEventScripts(String eventSciprtDir){
		LinkedList<File> eventScripts = new LinkedList<>();
		File dir = new File(combineToAbsolutePath(ROOT_DIR_NAME, EVENT_SCRIPT_DIR_NAME, eventSciprtDir));
		if(!dir.exists())
			return eventScripts;
		File[] F = dir.listFiles();
		for(File fx : F){
			if(fx.getName().endsWith(EXE_EXT))
				eventScripts.add(fx);
		}
		return eventScripts;
	}

	public static boolean isCompileScriptAvailable(File file){
		return isPathExists(ROOT_DIR_NAME, COMPILER_SCRIPT_DIR_NAME, getCompilerScriptName(file));
	}
	
	public static boolean isInterpreterScriptAvailable(File file){
		return isPathExists(ROOT_DIR_NAME, INTERPRETER_SCRIPT_DIR_NAME, getInterpreterScriptName(file));
	}
	
	public static boolean isFileLoadedEventScriptAvailable(File file){
		return isPathExists(ROOT_DIR_NAME, EVENT_SCRIPT_DIR_NAME, FILE_LOADED_EVENT_SCRIPTS_DIR_NAME, getFileEventScriptName(file));
	}
	
	public static boolean isFileSavedEventScriptAvailable(File file){
		return isPathExists(ROOT_DIR_NAME, EVENT_SCRIPT_DIR_NAME, FILE_SAVED_EVENT_SCRIPTS_DIR_NAME, getFileEventScriptName(file));
	}

}
