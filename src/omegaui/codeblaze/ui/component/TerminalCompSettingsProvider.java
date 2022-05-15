package omegaui.codeblaze.ui.component;

import com.jediterm.terminal.TextStyle;

import java.awt.Font;
import java.awt.Color;

import com.jediterm.terminal.emulator.ColorPalette;

import javax.swing.KeyStroke;

import com.jediterm.terminal.ui.settings.DefaultSettingsProvider;

import static omegaui.codeblaze.io.UIXManager.*;
import static omegaui.codeblaze.io.AppDataProvider.*;
import static omegaui.component.animation.Animations.*;

public class TerminalCompSettingsProvider extends DefaultSettingsProvider{

	public static Color[] colors = new Color[16];
	public static ColorPalette colorPalatte;
	static{
		colors[0] = GLOW;
		colors[1] = secondaryColor;
		colors[2] = primaryColor;
		colors[3] = tertiaryColor;
		colors[4] = focusColor;
		colors[5] = Color.decode("#FF8C42");
		colors[6] = Color.decode("#118736");
		colors[7] = Color.decode("#D81159");
		colors[8] = Color.decode("#E0777D");
		colors[9] = Color.decode("#8E3B46");
		colors[10] = Color.decode("#A2AD59");
		colors[11] = Color.decode("#92140C");
		colors[12] = Color.decode("#253237");
		colors[13] = Color.decode("#5C6B73");
		colors[14] = Color.decode("#4C2719");
		colors[15] = isDarkMode() ? Color.decode("#242424") : BACKGROUND;

		colorPalatte = new ColorPalette(){
			@Override
			public Color getBackgroundByColorIndex(int index){
				return colors[index];
			}
			@Override
			public Color getForegroundByColorIndex(int index){
				return colors[index];
			}
		};
	}

	@Override
	public Font getTerminalFont() {
//		return new Font(UIManager.terminalFontName, UIManager.terminalFontState, UIManager.terminalFontSize);
		return PX16;
	}

	@Override
	public float getTerminalFontSize() {
		return 16;
	}

	@Override
	public boolean useInverseSelectionColor() {
		return true;
	}

	@Override
	public ColorPalette getTerminalColorPalette() {
		return colorPalatte;
	}

	@Override
	public boolean useAntialiasing() {
		return true;
	}

	@Override
	public boolean audibleBell() {
		return true;
	}

	@Override
	public boolean scrollToBottomOnTyping() {
		return true;
	}

}
