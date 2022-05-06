package omegaui.codeblaze.io;
import java.awt.Color;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import omegaui.paint.DynoFont;
import omegaui.paint.PixelColor;
public final class UIXManager {
	//Colors
	public static PixelColor BACKGROUND = new PixelColor(255, 255, 255);
	public static PixelColor GLOW = new PixelColor(80, 80, 80);
	public static PixelColor HOVER = new PixelColor(200, 200, 200, 100);

	public static PixelColor primaryColor = new PixelColor(26, 36, 219);
	public static PixelColor secondaryColor = new PixelColor(223, 33, 15);
	public static PixelColor focusColor = new PixelColor(126, 20, 219);
	public static PixelColor warningColor = new PixelColor(200, 103, 0);
	public static PixelColor tertiaryColor = new PixelColor(16, 62, 110);

	public static PixelColor primaryColorShade = primaryColor.withOpacity(0.2f);
	public static PixelColor secondaryColorShade = secondaryColor.withOpacity(0.2f);
	public static PixelColor focusColorShade = focusColor.withOpacity(0.2f);
	public static PixelColor warningColorShade = warningColor.withOpacity(0.2f);
	public static PixelColor tertiaryColorShade = tertiaryColor.withOpacity(0.2f);
	
	public static PixelColor back1 = new PixelColor(Color.decode("#f3f3f3"));
	public static PixelColor back2 = new PixelColor(Color.decode("#fcfcfc"));
	public static PixelColor back3 = new PixelColor(Color.decode("#eaeaea"));
	
	//Fonts
	public static DynoFont PX12 = new DynoFont("Ubuntu Mono", 12).bold();
	public static DynoFont PX14 = new DynoFont("Ubuntu Mono", 14).bold();
	public static DynoFont PX16 = new DynoFont("Ubuntu Mono", 16).bold();
	public static DynoFont PX18 = new DynoFont("Ubuntu Mono", 18).bold();
	public static DynoFont PX20 = new DynoFont("Ubuntu Mono", 20).bold();
	public static DynoFont PX22 = new DynoFont("Ubuntu Mono", 22).bold();
	public static DynoFont PX24 = new DynoFont("Ubuntu Mono", 24).bold();
	public static DynoFont PX26 = new DynoFont("Ubuntu Mono", 26).bold();
	public static DynoFont PX28 = new DynoFont("Ubuntu Mono", 28).bold();
	public static DynoFont PX30 = new DynoFont("Ubuntu Mono", 30).bold();
	public static DynoFont PX32 = new DynoFont("Ubuntu Mono", 32).bold();
	public static DynoFont PX34 = new DynoFont("Ubuntu Mono", 34).bold();
	public static DynoFont PX36 = new DynoFont("Ubuntu Mono", 36).bold();
	public static DynoFont PX38 = new DynoFont("Ubuntu Mono", 38).bold();
	public static DynoFont PX40 = new DynoFont("Ubuntu Mono", 40).bold();
	public static DynoFont PX42 = new DynoFont("Ubuntu Mono", 42).bold();
	public static DynoFont PX44 = new DynoFont("Ubuntu Mono", 44).bold();
	public static DynoFont PX46 = new DynoFont("Ubuntu Mono", 46).bold();
	public static DynoFont PX48 = new DynoFont("Ubuntu Mono", 48).bold();
	
	public static DynoFont UBUNTU_PX12 = new DynoFont("Ubuntu", 12).bold();

	//Icons
	public static BufferedImage appIcon = getIcon("blaze-182");
	public static BufferedImage appSmallIcon = getIcon("blaze-32");
	
	public static BufferedImage backIcon = getIcon("back-64");
	public static BufferedImage cancelIcon = getIcon("cancel-64");
	public static BufferedImage fileIcon = getIcon("file-64");
	public static BufferedImage folderIcon = getIcon("folder-50");
	public static BufferedImage homeIcon = getIcon("home-folder-48");
	public static BufferedImage levelUpIcon = getIcon("up-3-50");
	public static BufferedImage templateIcon = getIcon("template-64");

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
