package omegaui.codeblaze.io;
import java.util.LinkedList;

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

	public static boolean isMatching(String text, String match){
		LinkedList<String> parts = new LinkedList<>();
		
		String part = "";

		for(int i = 0; i < match.length(); i++){
			char ch = match.charAt(i);
			if(Character.isUpperCase(ch)){
				if(!part.isBlank())
					parts.add(part);
				part = "" + ch;
			}
			else
				part += ch;
		}
		
		if(!part.equals(""))
			parts.add(part);
		
		if(parts.isEmpty())
			return false;
		
		int index = -1;
		if(parts.size() > 1){
			int nextIndex = 0;
			for(int i = 0; i < parts.size() - 1; i++){
				index = text.indexOf(parts.get(i));
				if(index < 0 || index >= text.length())
					return false;
				nextIndex = text.indexOf(parts.get(i + 1), index + 1);
				if(nextIndex < 0 || nextIndex <= index)
					return false;
			}
		}
		else
			index = text.indexOf(part);
		return index >= 0;
	}

}
