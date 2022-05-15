package omegaui.codeblaze.io;
import java.awt.GraphicsEnvironment;
import java.awt.Font;

import omegaui.dynamic.database.DataBase;


import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;

public final class AppResourceManager {

	public static final String USER_HOME = System.getProperty("user.home");
	
	public static final String ROOT_DIR_NAME = ".codeblaze";
	public static final String TEMPLATE_DIR_NAME = "templates";
	public static final String COMPILER_SCRIPT_DIR_NAME = "compiler-scripts";
	public static final String INTERPRETER_SCRIPT_DIR_NAME = "event-scripts";
	public static final String EVENT_SCRIPT_DIR_NAME = "interpreter-scripts";
	public static final String EXE_EXT = File.separator.equals("/") ? ".sh" : ".bat";

	private static DataBase appDataBase;

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

		//Creating up configuration structure
		
		createDir(ROOT_DIR_NAME);
		createDir(ROOT_DIR_NAME, TEMPLATE_DIR_NAME);
		createDir(ROOT_DIR_NAME, COMPILER_SCRIPT_DIR_NAME);
		createDir(ROOT_DIR_NAME, INTERPRETER_SCRIPT_DIR_NAME);
		createDir(ROOT_DIR_NAME, EVENT_SCRIPT_DIR_NAME);
		createDir(ROOT_DIR_NAME, EVENT_SCRIPT_DIR_NAME, "onFileSaved");

		appDataBase = new DataBase(ROOT_DIR_NAME + File.separator + "app.settings");
	}

	public static void createDir(String path){
		File dir = new File(path);
		if(!dir.exists())
			dir.mkdir();
	}

	public static void createDir(String... path){
		File dir = new File(combinePath(path));
		if(!dir.exists())
			dir.mkdir();
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
