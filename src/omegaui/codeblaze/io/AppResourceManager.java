package omegaui.codeblaze.io;
import omegaui.dynamic.database.DataBase;


import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;

public final class AppResourceManager {

	public static final String ROOT_DIR_NAME = ".codeblaze";

	private static DataBase appDataBase = new DataBase(ROOT_DIR_NAME + File.separator + "app.settings");

	public static void checkResources(){
		createDir(ROOT_DIR_NAME);
		createDir(ROOT_DIR_NAME, "templates");
		createDir(ROOT_DIR_NAME, "compiler-scripts");
		createDir(ROOT_DIR_NAME, "interpreter-scripts");
		createDir(ROOT_DIR_NAME, "event-scripts");
		createDir(ROOT_DIR_NAME, "event-scripts", "onFileSaved");
	}

	public static void createDir(String path){
		File dir = new File(path);
		if(!dir.exists())
			dir.mkdir();
	}

	public static void createDir(String... path){
		String pathx = "";
		for(String px : path)
			pathx += px + File.separator;
		pathx = pathx.substring(0, pathx.length() - 1);
		
		File dir = new File(pathx);
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

	public static synchronized DataBase appDataBase(){
		return appDataBase;
	}
}
