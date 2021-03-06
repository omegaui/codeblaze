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

package omegaui.codeblaze.ui.component;
import omegaui.codeblaze.ui.panel.GlassPanel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;

import omegaui.codeblaze.io.ResizeAware;
import omegaui.codeblaze.io.AppInstanceProvider;
import omegaui.codeblaze.io.FileManager;
import omegaui.codeblaze.io.TerminalManager;

import omegaui.component.TextComp;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import omegaui.codeblaze.App;

import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.UIManager;

import static omegaui.codeblaze.io.UIXManager.*;
import static omegaui.codeblaze.io.AppDataProvider.*;
import static omegaui.component.animation.Animations.*;

public final class ToolMenu extends JPanel implements ResizeAware{

	private App app;

	private TextComp iconComp;

	private Menu fileMenu;
	private Menu codeMenu;
	private Menu viewMenu;
	private Menu helpMenu;

	private MaterialPopup filePopup;
	private MaterialPopup codePopup;
	private MaterialPopup viewPopup;
	private MaterialPopup helpPopup;

	public ToolMenu(App app){
		this.app = app;
		setPreferredSize(new Dimension(100, 45));
		initUI();
	}

	public void initUI(){

		setBackground(back2);

		iconComp = new TextComp(appSmallIcon, back2, back2, back2, null);
		iconComp.setArc(0, 0);
		iconComp.setClickable(false);
		add(iconComp);

		filePopup = new MaterialPopup();
		filePopup.createItem(newIcon, "Create a New File", "Ctrl + N", ()->{
			app.switchToCreateNewFilePanel();
		})
		.createItem(openIcon, "Open a Local File", "Ctrl + SHIFT + N", ()->{
			FileManager.openFile();
		})
		.createItem(recentsIcon, "Recent Files", "Ctrl + SHIFT + R", ()->{
			app.switchToRecentFilesPanel();
		})
		.createItem(saveIcon, "Save All Editors (Silent)", "Ctrl + Alt + S", ()->{
			app.saveAllEditors();
		})
		.createItem(closeIcon, "Close All Editors", "Ctrl + Alt + X", ()->{
			app.closeAllEditors();
		})
		.createItem(gearIcon, "Preferences", "Ctrl + Alt + P", ()->{
			app.showPreferencesDialog();
		})
		.createItem(exitIcon, "Exit", "Alt + F4", AppInstanceProvider.getCurrentAppInstance()::exit);
		fileMenu = new Menu(filePopup, "File");
		filePopup.setContextMenuVisiblityShortcut(KeyEvent.VK_F, fileMenu::showPopup);
		add(fileMenu);

		codePopup = new MaterialPopup();
		codePopup.createItem(increaseFontSizeIcon, "Increase Document Font Size", "Ctrl + SHIFT + +", FileManager::increaseDocumentFontSize)
		.createItem(decreaseFontSizeIcon, "Decrease Document Font Size", "Ctrl + SHIFT + -", FileManager::decreaseDocumentFontSize)
		.createItem(tabKeyIcon, "Increase Document Tab Size", "Ctrl + T + +", FileManager::increaseDocumentTabSize)
		.createItem(tabKeyIcon, "Decrease Document Tab Size", "Ctrl + T + -", FileManager::decreaseDocumentTabSize);
		codeMenu = new Menu(codePopup, "Code");
		codePopup.setContextMenuVisiblityShortcut(KeyEvent.VK_C, codeMenu::showPopup);
		add(codeMenu);

		viewPopup = new MaterialPopup();
		viewPopup.createItem(themeIcon, "Toggle Theme (Requires Restart)", ()->{
			darkmode = !darkmode;
			app.setMessage("Applied " + (isDarkMode() ? "Dark" : "Light") + " Mode, Restart CodeBlaze now for the changes to take effect.", "CodeBlaze", "Restart", "Dark", "Light", "Mode");
		})
		.createItem(terminalIcon, "New Terminal", "Ctrl + SHIFT + T", ()->{
			TerminalManager.openNewTerminal();
		});
		viewMenu = new Menu(viewPopup, "View");
		viewPopup.setContextMenuVisiblityShortcut(KeyEvent.VK_V, viewMenu::showPopup);
		add(viewMenu);

		helpPopup = new MaterialPopup();
		helpPopup.createItem(questionIcon, "How to Manage Templates?", ()->{})
		.createItem(questionIcon, "How to Manage Compile Script?", ()->{})
		.createItem(questionIcon, "How to Manage Execution Script?", ()->{})
		.createItem(questionIcon, "How to Add Event Scripts?", ()->{})
		.createItem(appIcon, "About", ()->{});
		helpMenu = new Menu(helpPopup, "Help");
		helpPopup.setContextMenuVisiblityShortcut(KeyEvent.VK_H, helpMenu::showPopup);
		add(helpMenu);
	}

	@Override
	public void manageBounds(){
		iconComp.setBounds(0, 0, 40, 40);

		fileMenu.setBounds(45, 8, 50, 25);
		codeMenu.setBounds(95, 8, 50, 25);
		viewMenu.setBounds(145, 8, 50, 25);
		helpMenu.setBounds(195, 8, 50, 25);
	}

	@Override
	public void layout(){
		super.layout();
		manageBounds();
	}


	public class Menu extends JComponent {

		private String text;

		private MaterialPopup popup;

		private volatile boolean enter;

		public Menu(MaterialPopup popup, String text) {
			this.text = text;
			this.popup = popup;
			setFont(PX14);
			setSize(100, ToolMenu.this.getHeight());
			setPreferredSize(getSize());
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					enter = true;
					repaint();
				}
				@Override
				public void mouseExited(MouseEvent e) {
					enter = false;
					repaint();
				}
				@Override
				public void mousePressed(MouseEvent e) {
					showPopup();
				}
			});
		}

		public void showPopup(){
			if(popup.isVisible()){
				popup.setVisible(false);
				return;
			}
			popup.setLocation(getX() + AppInstanceProvider.getCurrentAppInstance().getX(), getY() + getHeight() + 15 + getHeight() + AppInstanceProvider.getCurrentAppInstance().getY());
			popup.setVisible(true);
		}

		@Override
		public void paint(Graphics g2D) {
			Graphics2D g = (Graphics2D)g2D;
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g.setColor(back2);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(secondaryColor);
			g.setFont(getFont());
			int x = g.getFontMetrics().stringWidth(text);
			int cx = x;
			x = getWidth()/2 - x/2;
			if(enter) {
				g.setColor(tertiaryColor);
				g.fillRect(x, getHeight() - 3, cx, 2);
			}
			g.setFont(getFont());
			g.drawString(text, x, getFont().getSize());
		}
	}
}

