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
import java.awt.GraphicsEnvironment;
import java.awt.Font;

import omegaui.dynamic.database.DataBase;
import omegaui.dynamic.database.DataEntry;


import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;

import static omegaui.codeblaze.io.UIXManager.*;
/*
 * Keeps an account of App Resources required for launch.
 */
public final class AppResourceManager {
	
	/**
	 * Holds the User's home directory path
	 */
	public static final String USER_HOME = System.getProperty("user.home");
	
	/**
	 * Event Constants
	 */
	public static final String CODEBLAZE_READY_EVENT_NAME = "codeblaze-ready";
	public static final String CODEBLAZE_EXITING_EVENT_NAME = "codeblaze-exiting";
	
	/**
	 * Properties are the data-set names of the appDataBase (app.settings),
	 * and the Values are the entries of these data-sets.
	 */
	public static final String APP_DARK_THEME_VALUE = "dark";
	public static final String APP_LIGHT_THEME_VALUE = "light";
	public static final String APP_THEME_MODE_PROPERTY = "App Theme Mode";
	
	public static final String LAST_SESSION_FILES_PROPERTY = "Files Left Open in the Previous Session";
	public static final String LAST_FOCUSSED_EDITOR_PROPERTY = "Last Focussed Editor in the Previous Session";
	public static final String LAST_APP_WINDOW_STATE_PROPERTY = "App Window Extended State Value";
	public static final String FILE_CREATION_DIR_PROPERTY = "Preferred File Creation Directory";
	
	public static final String DOCUMENT_FONT_PROPERTY = "Document Font";
	public static final String DOCUMENT_TAB_SIZE_PROPERTY = "Document Tab Size";
	
	public static final String AUTO_SAVE_FILE_ON_EXIT_PROPERTY = "Auto Save File on Exit";
	public static final String AUTO_SAVE_FILE_BEFORE_LAUNCH_PROPERTY = "Auto Save File before Launch";
	
	/**
	 * The configuration directories in the .codeblaze folder.
	 */
	public static final String ROOT_DIR_NAME = ".codeblaze";
	public static final String TEMPLATE_DIR_NAME = "templates";
	public static final String COMPILER_SCRIPT_DIR_NAME = "compiler-scripts";
	public static final String INTERPRETER_SCRIPT_DIR_NAME = "interpreter-scripts";
	public static final String EVENT_SCRIPT_DIR_NAME = "event-scripts";
	public static final String FILE_LOADED_EVENT_SCRIPTS_DIR_NAME = "onFileLoaded";
	public static final String FILE_SAVED_EVENT_SCRIPTS_DIR_NAME = "onFileSaved";
	public static final String CODEBLAZE_READY_EVENT_SCRIPTS_DIR_NAME = "onCodeBlazeReady";
	public static final String CODEBLAZE_EXITING_EVENT_SCRIPTS_DIR_NAME = "onCodeBlazeExiting";
	
	/**
	 * The extension of the executable files of the current Platform.
	 */
	public static final String EXE_EXT = File.separator.equals("/") ? ".sh" : ".bat";

	/**
	 * The DataBase object of the app.settings file.
	 */
	private static DataBase appDataBase;

	/**
	 * By default updates the theme changes and saves the database.
	 */
	public static synchronized void saveAppDataBase(){
		appDataBase.updateEntry(APP_THEME_MODE_PROPERTY, isDarkMode() ? APP_DARK_THEME_VALUE : APP_LIGHT_THEME_VALUE, 0);
		appDataBase.save();
	}

	/**
	 * Returns the first value at the corresponding property in the database (app.settings)
	 */
	public static synchronized DataEntry get(String property){
		return appDataBase.getEntryAt(property);
	}

	/**
	 * Checks and Generates any missing resource required during startup.
	 */
	public static void checkResources(){
		//Installing Fonts
		try{
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, AppResourceManager.class.getResourceAsStream("/Ubuntu-Bold.ttf")));
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, AppResourceManager.class.getResourceAsStream("/UbuntuMono-Bold.ttf")));
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, AppResourceManager.class.getResourceAsStream("/JetBrainsMono-Regular.ttf")));
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, AppResourceManager.class.getResourceAsStream("/JetBrainsMono-Bold.ttf")));
		}
		catch(Exception e){
			e.printStackTrace();
		}

		//Building up configuration structure
		createDir(ROOT_DIR_NAME);
		createDir(ROOT_DIR_NAME, TEMPLATE_DIR_NAME);
		createDir(ROOT_DIR_NAME, COMPILER_SCRIPT_DIR_NAME);
		createDir(ROOT_DIR_NAME, INTERPRETER_SCRIPT_DIR_NAME);
		createDir(ROOT_DIR_NAME, EVENT_SCRIPT_DIR_NAME);
		createDir(ROOT_DIR_NAME, EVENT_SCRIPT_DIR_NAME, FILE_LOADED_EVENT_SCRIPTS_DIR_NAME);
		createDir(ROOT_DIR_NAME, EVENT_SCRIPT_DIR_NAME, FILE_SAVED_EVENT_SCRIPTS_DIR_NAME);
		createDir(ROOT_DIR_NAME, EVENT_SCRIPT_DIR_NAME, CODEBLAZE_READY_EVENT_SCRIPTS_DIR_NAME);
		createDir(ROOT_DIR_NAME, EVENT_SCRIPT_DIR_NAME, CODEBLAZE_EXITING_EVENT_SCRIPTS_DIR_NAME);

		copyResourceIfNotExists("/.codeblaze/app.settings", combineToAbsolutePath(ROOT_DIR_NAME, "app.settings"));
		
		appDataBase = new DataBase(ROOT_DIR_NAME + File.separator + "app.settings");
		darkmode = appDataBase().getEntryAt(APP_THEME_MODE_PROPERTY).getValue().equals(APP_DARK_THEME_VALUE);

		copyResourceIfNotExists("/pty4j-native-libs/darwin/libpty.dylib", combineToAbsolutePath("darwin", "libpty.dylib"));
		copyResourceIfNotExists("/pty4j-native-libs/freebsd/x86/libpty.so", combineToAbsolutePath("freebsd", "x86", "libpty.so"));
		copyResourceIfNotExists("/pty4j-native-libs/freebsd/x86-64/libpty.so", combineToAbsolutePath("freebsd", "x86-64", "libpty.so"));
		copyResourceIfNotExists("/pty4j-native-libs/linux/x86/libpty.so", combineToAbsolutePath("linux", "x86", "libpty.so"));
		copyResourceIfNotExists("/pty4j-native-libs/linux/x86-64/libpty.so", combineToAbsolutePath("linux", "x86-64", "libpty.so"));
		copyResourceIfNotExists("/pty4j-native-libs/win/x86/winpty-agent.exe", combineToAbsolutePath("win", "x86", "winpty-agent.exe"));
		copyResourceIfNotExists("/pty4j-native-libs/win/x86/winpty.dll", combineToAbsolutePath("win", "x86", "winpty.dll"));
		copyResourceIfNotExists("/pty4j-native-libs/win/x86-64/winpty-agent.exe", combineToAbsolutePath("win", "x86-64", "winpty-agent.exe"));
		copyResourceIfNotExists("/pty4j-native-libs/win/x86-64/winpty.dll", combineToAbsolutePath("win", "x86-64", "winpty.dll"));
		copyResourceIfNotExists("/pty4j-native-libs/win/x86-64/cyglaunch.exe", combineToAbsolutePath("win", "x86-64", "cyglaunch.exe"));
		copyResourceIfNotExists("/pty4j-native-libs/win/x86-64/win-helper.dll", combineToAbsolutePath("win", "x86-64", "win-helper.dll"));
	}

	/**
	 * Creates a directory if doesn't already exists.
	 */
	public static void createDir(String path){
		File dir = new File(path);
		if(!dir.exists())
			dir.mkdirs();
	}

	/**
	 * Creates a directory if doesn't already exists by combining the varargs containing path.
	 */
	public static void createDir(String... path){
		createDir(combinePath(path));
	}

	/**
	 * Copies an internal resource to the target external path.
	 */
	public static void copyResource(String resourcePath, String targetPath){
		try{
			String targetDir = targetPath.substring(0, targetPath.lastIndexOf(File.separatorChar));
			createDir(targetDir);

			FileOutputStream out = new FileOutputStream(targetPath);
			InputStream in = AppResourceManager.class.getResourceAsStream(resourcePath);
			while(in.available() > 0){
				out.write(in.read());
			}
			out.close();
			in.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Copies an internal resource to the target external path if not already present.
	 */
	public static void copyResourceIfNotExists(String resourcePath, String targetPath){
		if(!isPathExists(targetPath))
			copyResource(resourcePath, targetPath);
	}

	/**
	 * Combines the varargs path and returns if the path exists.
	 */
	public static synchronized boolean isPathExists(String... path){
		return new File(combinePath(path)).exists();
	}

	/**
	 * Combines the varargs path and returns the combined path.
	 */
	public static synchronized String combinePath(String... path){
		String pathx = "";
		for(String px : path)
			pathx += px + File.separator;
		pathx = pathx.substring(0, pathx.length() - 1);
		return pathx;
	}

	/**
	 * Combines the varargs path and returns the absolute path.
	 */
	public static synchronized String combineToAbsolutePath(String... path){
		String pathx = "";
		for(String px : path)
			pathx += px + File.separator;
		pathx = pathx.substring(0, pathx.length() - 1);
		return new File(pathx).getAbsolutePath();
	}
	
	/**
	 * Returns the appDataBase instance.
	 */
	public static synchronized DataBase appDataBase(){
		return appDataBase;
	}
}
