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
import javax.swing.plaf.ColorUIResource;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatDarkLaf;

import java.io.File;

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

	public static volatile boolean darkmode = true;

	//Colors
	public static PixelColor BACKGROUND = new PixelColor(255, 255, 255);
	public static PixelColor GLOW = new PixelColor(80, 80, 80);
	public static PixelColor HOVER = new PixelColor(200, 200, 200, 100).withOpacity(0.2f);

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

	public static Color LINE_NUMBER_COLOR = Color.BLACK;
	public static Color CURRENT_LINE_NUMBER_COLOR = primaryColor;

	//File Colors
	public static Color ANY_COLOR = tertiaryColor;
	public static Color SOURCE_COLOR = focusColor;
	public static Color BYTE_COLOR = new Color(150, 150, 50, 220);
	public static Color IMAGE_COLOR = new Color(50, 100, 50, 220);
	public static Color LINUX_COLOR = new Color(175, 50, 50, 220);
	public static Color EMPTY_COLOR = Color.LIGHT_GRAY;
	public static Color WEB_COLOR = focusColor;
	public static Color XML_COLOR = LINUX_COLOR;
	public static Color ARCHIVE_COLOR = ANY_COLOR;

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

	//App Icons
	public static BufferedImage appIcon = getAppIcon();
	public static BufferedImage appSmallIcon = getAppIcon32();

	//FileSelectionDialog Icons
	public static BufferedImage backIcon = getIcon("back-64");
	public static BufferedImage cancelIcon = getIcon("cancel-64");
	public static BufferedImage fileIcon = getIcon("file-64");
	public static BufferedImage folderIcon = getIcon("folder-50");
	public static BufferedImage homeIcon = getIcon("home-folder-48");
	public static BufferedImage levelUpIcon = getIcon("up-3-50");
	public static BufferedImage tabsIcon = getIcon("apps-tab-96");

	//Template Icon
	public static BufferedImage templateIcon = getIcon("template-64");

	//Language Icons
	public static BufferedImage cLangIcon = getIcon("c-programming-48");
	public static BufferedImage cPlusPlusIcon = getIcon("c++-48");
	public static BufferedImage rustIcon = getIcon("rust-48");
	public static BufferedImage pythonIcon = getIcon("python-48");
	public static BufferedImage javaIcon = getIcon("java-48");
	public static BufferedImage kotlinIcon = getIcon("kotlin-48");
	public static BufferedImage groovyIcon = getIcon("groovy-48");
	public static BufferedImage webIcon = getIcon("web-app-64");
	public static BufferedImage dartIcon = getIcon("dart-48");
	public static BufferedImage juliaIcon = getIcon("julia-48");
	public static BufferedImage xmlIcon = getIcon("xml-48");

	//File Icons
	public static BufferedImage imageIcon = getIcon("panorama-96");
	public static BufferedImage archiveIcon = getIcon("archive-48");
	public static BufferedImage gearIcon = getIcon("gear-48");

	//Platform Icons
	public static BufferedImage linuxIcon = getIcon("penguin-50");
	public static BufferedImage windowsIcon = getIcon("windows-10-50");
	public static BufferedImage appleIcon = getIcon("apple-50");

	//File Action Icons
	public static BufferedImage closeIcon = getIcon("close-window-50");
	public static BufferedImage openIcon = getIcon("open-58");
	public static BufferedImage newIcon = getIcon("new-48");
	public static BufferedImage recentsIcon = getIcon("time-card-48");
	public static BufferedImage saveIcon = getIcon("hero-64");
	public static BufferedImage exitIcon = getIcon("exit-sign-48");

	//Code Action Icons
	public static BufferedImage increaseFontSizeIcon = getIcon("increase-font-48");
	public static BufferedImage decreaseFontSizeIcon = getIcon("decrease-font-48");
	public static BufferedImage tabKeyIcon = getIcon("tab-key-48");

	//View Action Icons
	public static BufferedImage focusIcon = getIcon("focus-48");
	public static BufferedImage themeIcon = getIcon("change-theme-48");
	public static BufferedImage terminalIcon = getIcon("console-48");

	//Help Action Icons
	public static BufferedImage questionIcon = getIcon("question-64");

	//Execution Panel Icons
	public static BufferedImage runIcon = getIcon("launch-48");
	public static BufferedImage buildIcon = getIcon("built-48");
	public static BufferedImage cookIcon = getIcon("cook-48");
	public static BufferedImage reloadIcon = getIcon("reload-48");

	//Platform Related
	public static boolean onWindows(){
		return File.separator.equals("\\");
	}

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

	//Theme Management
	public static void initDarkMode(){
//		appIcon = getAppIcon();
//		appSmallIcon = getAppIcon32();
		
		FlatDarkLaf.install();

		Color x = Color.decode("#24d673");
		Color y = Color.decode("#2A2A2A");
		javax.swing.UIManager.put("ToolTip.foreground", new ColorUIResource(Color.WHITE));
		javax.swing.UIManager.put("ToolTip.background", new ColorUIResource(y));
		javax.swing.UIManager.put("Button.foreground", new ColorUIResource(Color.WHITE));
		javax.swing.UIManager.put("Button.background", new ColorUIResource(y));
		javax.swing.UIManager.put("Label.foreground", new ColorUIResource(Color.WHITE));
		javax.swing.UIManager.put("Label.background", new ColorUIResource(y));
		javax.swing.UIManager.put("ToolTip.font", PX14);
		javax.swing.UIManager.put("Button.font", PX14);
		javax.swing.UIManager.put("Label.font", PX14);
		javax.swing.UIManager.put("ScrollBar.thumb", new ColorUIResource(x));
		javax.swing.UIManager.put("ScrollBar.track", new ColorUIResource(y));
		javax.swing.UIManager.put("ScrollPane.background", new ColorUIResource(y));

		primaryColor = new PixelColor(Color.decode("#f0b40f"));
		secondaryColor = new PixelColor(Color.decode("#D34D42"));
		focusColor = new PixelColor(Color.decode("#24d673"));
		warningColor = new PixelColor(Color.decode("#EB7201"));
		tertiaryColor = new PixelColor(Color.decode("#7f6021"));

		primaryColorShade = primaryColor.withOpacity(0.2f);
		secondaryColorShade = secondaryColor.withOpacity(0.2f);
		focusColorShade = focusColor.withOpacity(0.2f);
		warningColorShade = warningColor.withOpacity(0.2f);
		tertiaryColorShade = tertiaryColor.withOpacity(0.2f);

		back1 = new PixelColor(Color.decode("#252526"));
		back2 = new PixelColor(Color.decode("#2A2A2A"));
		back3 = new PixelColor(Color.decode("#303030"));

		GLOW = new PixelColor(Color.WHITE);
		BACKGROUND = new PixelColor(Color.decode("#1e1e1e"));
		HOVER = new PixelColor(51, 51, 51, 140).withOpacity(0.2f);

		ANY_COLOR = tertiaryColor;
		SOURCE_COLOR = focusColor;
		WEB_COLOR = focusColor;

		LINE_NUMBER_COLOR = Color.decode("#606364");
		CURRENT_LINE_NUMBER_COLOR = Color.decode("#A4A3A1");

	}

	public static boolean isDarkMode(){
		return darkmode;
	}

	//Resource Providers
	public static synchronized BufferedImage getAppIcon(){
		try{
			String name = "blaze-182";
			return ImageIO.read(UIXManager.class.getResource("/icons/icons8-" + name + ".png"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static synchronized BufferedImage getAppIcon32(){
		try{
			String name = "blaze-32";
			return ImageIO.read(UIXManager.class.getResource("/icons/icons8-" + name + ".png"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static synchronized BufferedImage getIcon(String name){
		try{
			return ImageIO.read(UIXManager.class.getResource("/icons/icons8-" + name + ".png"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static BufferedImage getPreferredIconForFile(File file){
		if(file.isDirectory())
			return folderIcon;
		if(file.getName().contains(".")){
			String ext = file.getName().substring(file.getName().lastIndexOf('.'));
			if(ext.equals(".png") || ext.equals(".jpg") || ext.equals(".jpeg") || ext.equals(".bmp") || ext.equals(".gif") || ext.equals(".svg") || ext.equals(".ico") || ext.equals(".jp2"))
				return imageIcon;
			if(ext.equals(".java") || ext.equals(".class"))
				return javaIcon;
			if(ext.equals(".py"))
				return pythonIcon;
			if(ext.equals(".kt"))
				return kotlinIcon;
			if(ext.equals(".groovy"))
				return groovyIcon;
			if(ext.equals(".dart"))
				return dartIcon;
			if(ext.equals(".js") || ext.equals(".css") || ext.equals(".html") || ext.equals(".jsx") || ext.equals(".ts") || ext.equals(".tsx"))
				return webIcon;
			if(ext.equals(".c"))
				return cLangIcon;
			if(ext.equals(".cpp"))
				return cPlusPlusIcon;
			if(ext.equals(".rs"))
				return rustIcon;
			if(ext.equals(".sh") || ext.equals(".run") || ext.equalsIgnoreCase(".appimage") || ext.equals(".deb") || ext.equals(".so") || ext.equals(".rpm"))
				return linuxIcon;
			if(ext.equalsIgnoreCase(".fxml") || ext.equals(".xml"))
				return xmlIcon;
			if(ext.equals(".cmd") || ext.equals(".bat") || ext.equals(".exe") || ext.equals(".msi") || ext.equals(".dll"))
				return windowsIcon;
			if(ext.equals(".dmg") || file.getName().endsWith(".dylib"))
				return appleIcon;
			if(file.getName().endsWith(".zip") || file.getName().endsWith(".7z") || file.getName().endsWith(".tar") || file.getName().endsWith(".tar.gz")  || file.getName().endsWith(".tar.xz") || file.getName().endsWith(".jar"))
				return archiveIcon;
		}
		return fileIcon;
	}

	public static BufferedImage getPlatformImage(){
		String name = System.getProperty("os.name");
		if(name.contains("indows"))
			return windowsIcon;
		else if(name.contains("nux"))
			return linuxIcon;
		return appleIcon;
	}


	public static Color getPreferredColorForFile(File file){
		if(file.isDirectory()){
			if(file.getName().charAt(0) == '.')
				return warningColor;
			return GLOW;
		}
		Color res = ANY_COLOR;
		if(!file.getName().contains(".")){
			return res;
		}
		if(file.getName().endsWith(".java") || file.getName().endsWith(".rs") || file.getName().endsWith(".py") || file.getName().endsWith(".groovy") || file.getName().endsWith(".kt")) {
			res = SOURCE_COLOR;
		}
		else if(file.getName().endsWith(".class")){
			res = BYTE_COLOR;
		}
		else if(file.getName().endsWith(".exe") || file.getName().endsWith(".msi")){
			res = primaryColor;
		}
		else if(file.getName().endsWith(".dmg")){
			res = primaryColor;
		}
		else if(file.getName().endsWith(".dll") || file.getName().endsWith(".so") || file.getName().endsWith(".dylib")){
			res = secondaryColor;
		}
		else if(file.getName().endsWith(".deb") || file.getName().endsWith(".run")
		|| file.getName().endsWith(".sh")){
			res = LINUX_COLOR;
		}
		else if(file.getName().endsWith(".dependencies") || file.getName().endsWith(".sources")
		|| file.getName().endsWith(".natives") || file.getName().endsWith(".resources")
		|| file.getName().endsWith(".projectInfo") || file.getName().endsWith(".modules")
		|| file.getName().endsWith(".snippets") || file.getName().endsWith(".args")){
			res = primaryColor;
		}
		else if(file.getName().startsWith(".")){
			res = warningColor;
		}
		else if(file.getName().endsWith(".js") || file.getName().endsWith(".html")){
			res = WEB_COLOR;
		}
		else if(file.getName().endsWith(".xml") || file.getName().endsWith(".fxml")){
			res = XML_COLOR;
		}
		else if(file.getName().endsWith(".txt")){
			res = focusColor;
		}
		else if(file.getName().endsWith(".png") || file.getName().endsWith(".jpg")
		|| file.getName().endsWith(".jpeg") || file.getName().endsWith(".gif")
		|| file.getName().endsWith(".jp2") || file.getName().endsWith(".bmp")
		|| file.getName().endsWith(".ico") || file.getName().endsWith(".svg")){
			res = IMAGE_COLOR;
		}
		else if(file.getName().endsWith(".zip") || file.getName().endsWith(".7z") ||
		file.getName().endsWith(".tar") || file.getName().endsWith(".tar.gz")
		|| file.getName().endsWith(".jar")){
			res = ARCHIVE_COLOR;
		}
		return res;
	}

}
