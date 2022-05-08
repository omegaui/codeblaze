package omegaui.codeblaze.io;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Graphics;
import java.awt.Component;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import omegaui.paint.DynoFont;
import omegaui.paint.PixelColor;
public final class UIXManager {
	//Image Object to be used for computing text dimension.
	public static BufferedImage testImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);

	//App Variables
	public static int tabHeight = 30;
	
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
	public static DynoFont UBUNTU_PX14 = new DynoFont("Ubuntu", 14).bold();
	public static DynoFont UBUNTU_PX16 = new DynoFont("Ubuntu", 16).bold();

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
	public static BufferedImage tabsIcon = getIcon("apps-tab-96");

	//Drawers
	
	public static int computeWidth(String name, Font font){
		if(font == null)
			return 8;
		Graphics2D g = (Graphics2D)testImage.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setFont(font);
		return g.getFontMetrics().stringWidth(name);
	}

	public static int computeHeight(Font font){
		if(font == null)
			return 8;
		Graphics g = testImage.getGraphics();
		g.setFont(font);
		return g.getFontMetrics().getHeight();
	}

	public static void drawAtCenter(String text, Graphics2D g, Component c){
		g.drawString(text, c.getWidth()/2 - computeWidth(text, g.getFont())/2,
		c.getHeight()/2 - computeHeight(g.getFont())/2 + g.getFontMetrics().getAscent() - g.getFontMetrics().getDescent() + 1);
	}


	//Theme Providers
	public static boolean isDarkMode(){
		return false;
	}

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
