package omegaui.codeblaze.io;
import java.util.Scanner;

import java.io.File;

import static omegaui.codeblaze.io.AppResourceManager.*;

public final class TemplateManager {
	public static synchronized File getCorrespondingTemplateFile(File file){
		String fileName = file.getName();
		if(fileName.contains("."))
			fileName = fileName.substring(fileName.lastIndexOf('.'));
		return new File(combinePath(ROOT_DIR_NAME, TEMPLATE_DIR_NAME, "template" + fileName));
	}
	
	public static synchronized boolean isTemplateAvailable(File file){
		String fileName = file.getName();
		if(fileName.contains("."))
			fileName = fileName.substring(fileName.lastIndexOf('.'));
		return isPathExists(ROOT_DIR_NAME, TEMPLATE_DIR_NAME, "template" + fileName);
	}
	
	public static synchronized String getTemplateContent(File file){
		String content = "";
		try(Scanner reader = new Scanner(getCorrespondingTemplateFile(file))){
			while(reader.hasNextLine())
				content += reader.nextLine() + "\n";
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return content;
	}
}
