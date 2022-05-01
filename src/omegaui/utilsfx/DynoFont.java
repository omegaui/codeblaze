package omegaui.utilsfx;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.FontPosture;
public class DynoFont {
	
	private Font font;
	
	public DynoFont(String fontName){
		font = Font.font(fontName);
	}
	
	public DynoFont(String fontName, double size){
		font = Font.font(fontName, size);
	}
	
	public DynoFont(String fontName, FontWeight fontWeight, double size){
		font = Font.font(fontName, fontWeight, size);
	}
	
	public DynoFont(String fontName, FontPosture fontPosture, double size){
		font = Font.font(fontName, fontPosture, size);
	}

	public DynoFont bold(){
		return new DynoFont(font.getName(), FontWeight.BOLD, font.getSize());
	}

	public DynoFont italic(){
		return new DynoFont(font.getName(), FontPosture.ITALIC, font.getSize());
	}

	public javafx.scene.text.Font font() {
		return font;
	}
	
}
