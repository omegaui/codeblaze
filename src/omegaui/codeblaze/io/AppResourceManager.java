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
		createDir(ROOT_DIR_NAME, "templates");
		createDir(ROOT_DIR_NAME, "compiler-scripts");
		createDir(ROOT_DIR_NAME, "interpreter-scripts");
		createDir(ROOT_DIR_NAME, "event-scripts");
		createDir(ROOT_DIR_NAME, "event-scripts", "onFileSaved");

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

	public static synchronized DataBase appDataBase(){
		return appDataBase;
	}
}
