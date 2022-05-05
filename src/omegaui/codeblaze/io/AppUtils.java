package omegaui.codeblaze.io;
import java.awt.Desktop;

import java.io.File;
public class AppUtils {
	public static void browse(File file){
		try{
			Desktop.getDesktop().open(file);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
