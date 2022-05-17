package omegaui.codeblaze.io;
import java.awt.GraphicsEnvironment;
import java.awt.Font;

import omegaui.dynamic.database.DataBase;


import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;

import static omegaui.codeblaze.io.UIXManager.*;

public final class AppResourceManager {

	public static final String USER_HOME = System.getProperty("user.home");
	
	public static final String APP_DARK_THEME_VALUE = "dark";
	public static final String APP_LIGHT_THEME_VALUE = "light";
	
	public static final String APP_THEME_MODE_PROPERTY = "App Theme Mode";
	public static final String LAST_SESSION_FILES_PROPERTY = "Files Left Open in the Previous Session";
	public static final String LAST_FOCUSSED_EDITOR_PROPERTY = "Last Focussed Editor in the Previous Session";
	public static final String LAST_APP_WINDOW_STATE_PROPERTY = "App Window Extended State Value";
	public static final String FILE_CREATION_DIR_PROPERTY = "Preferred File Creation Directory";
	
	public static final String ROOT_DIR_NAME = ".codeblaze";
	public static final String TEMPLATE_DIR_NAME = "templates";
	public static final String COMPILER_SCRIPT_DIR_NAME = "compiler-scripts";
	public static final String INTERPRETER_SCRIPT_DIR_NAME = "interpreter-scripts";
	public static final String EVENT_SCRIPT_DIR_NAME = "event-scripts";
	public static final String EXE_EXT = File.separator.equals("/") ? ".sh" : ".bat";

	private static DataBase appDataBase;

	public static synchronized void saveAppDataBase(){
		appDataBase.updateEntry(APP_THEME_MODE_PROPERTY, isDarkMode() ? APP_DARK_THEME_VALUE : APP_LIGHT_THEME_VALUE, 0);
		appDataBase.save();
	}

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
		createDir(ROOT_DIR_NAME, EVENT_SCRIPT_DIR_NAME, "onFileSaved");

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

	public static void createDir(String path){
		File dir = new File(path);
		if(!dir.exists())
			dir.mkdirs();
	}

	public static void createDir(String... path){
		createDir(combinePath(path));
	}

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
	
	public static void copyResourceIfNotExists(String resourcePath, String targetPath){
		if(!isPathExists(targetPath))
			copyResource(resourcePath, targetPath);
	}

	public static synchronized boolean isPathExists(String... path){
		return new File(combinePath(path)).exists();
	}

	public static synchronized String combinePath(String... path){
		String pathx = "";
		for(String px : path)
			pathx += px + File.separator;
		pathx = pathx.substring(0, pathx.length() - 1);
		return pathx;
	}

	public static synchronized String combineToAbsolutePath(String... path){
		String pathx = "";
		for(String px : path)
			pathx += px + File.separator;
		pathx = pathx.substring(0, pathx.length() - 1);
		return new File(pathx).getAbsolutePath();
	}

	public static synchronized DataBase appDataBase(){
		return appDataBase;
	}
}
