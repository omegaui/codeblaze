package omegaui.codeblaze.io;
import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import omegaui.paint.DynoFont;
import omegaui.paint.PixelColor;
public final class UIXManager {
	//Colors
	public static PixelColor BACKGROUND = new PixelColor(255, 255, 255);
	public static PixelColor GLOW = new PixelColor(40, 40, 40);
	
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

	//Icons
	public static BufferedImage appIcon = getIcon("blaze-182");

	//Resource Providers
	public static synchronized BufferedImage getIcon(String name){
		try{
			return ImageIO.read(UIXManager.class.getResourceAsStream("/icons/icons8-" + name + ".png"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
