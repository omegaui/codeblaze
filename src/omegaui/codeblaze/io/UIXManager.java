package omegaui.codeblaze.io;
import javafx.scene.paint.Color;

import omegaui.utilsfx.DynoFont;

import javafx.scene.image.Image;
public final class UIXManager {

	//Colors
	public static Color TEXT_COLOR1 = Color.rgb(26, 36, 219);
	public static Color TEXT_COLOR2 = Color.web("#F4767A");
	public static Color TEXT_COLOR3 = Color.rgb(126, 20, 219);
	public static Color TEXT_COLOR4 = Color.rgb(200, 103, 0);
	public static Color TEXT_COLOR5 = Color.rgb(16, 62, 110);
	public static Color GLOW = Color.rgb(40, 40, 40);
	
	//Fonts
	public static DynoFont PX12 = new DynoFont("Ubuntu Mono", 12);
	public static DynoFont PX14 = new DynoFont("Ubuntu Mono", 14);
	public static DynoFont PX16 = new DynoFont("Ubuntu Mono", 16);
	public static DynoFont PX18 = new DynoFont("Ubuntu Mono", 18);
	public static DynoFont PX20 = new DynoFont("Ubuntu Mono", 20);
	public static DynoFont PX22 = new DynoFont("Ubuntu Mono", 22);
	public static DynoFont PX24 = new DynoFont("Ubuntu Mono", 24);
	public static DynoFont PX26 = new DynoFont("Ubuntu Mono", 26);
	public static DynoFont PX28 = new DynoFont("Ubuntu Mono", 28);
	public static DynoFont PX30 = new DynoFont("Ubuntu Mono", 30);
	public static DynoFont PX32 = new DynoFont("Ubuntu Mono", 32);
	public static DynoFont PX34 = new DynoFont("Ubuntu Mono", 34);
	public static DynoFont PX36 = new DynoFont("Ubuntu Mono", 36);
	public static DynoFont PX38 = new DynoFont("Ubuntu Mono", 38);
	public static DynoFont PX40 = new DynoFont("Ubuntu Mono", 40);
	public static DynoFont PX42 = new DynoFont("Ubuntu Mono", 42);
	public static DynoFont PX44 = new DynoFont("Ubuntu Mono", 44);
	public static DynoFont PX46 = new DynoFont("Ubuntu Mono", 46);
	public static DynoFont PX48 = new DynoFont("Ubuntu Mono", 48);
	
	//Image-Icons
	public static Image appImageIcon = getImageIcon("blaze-182");

	//Icon Loaders
	public synchronized static Image getImageIcon(String name){
		return new Image(UIXManager.class.getResource("/icons/icons8-" + name + ".png").toExternalForm());
	}
}
