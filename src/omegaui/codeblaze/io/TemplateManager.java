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
