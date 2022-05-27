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

package omegaui.codeblaze.ui.panel;
import omegaui.codeblaze.io.ResizeAware;

import omegaui.component.TextComp;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.BorderLayout;

import omegaui.codeblaze.App;

import javax.swing.JPanel;

import static omegaui.codeblaze.io.UIXManager.*;
import static omegaui.codeblaze.io.AppDataProvider.*;
import static omegaui.component.animation.Animations.*;

public final class GlassPanel extends JPanel {
	private App app;

	// Views
	private static LauncherPanel launcherPanel;
	private static FileCreationPanel fileCreationPanel;
	private static RecentFilesPanel recentFilesPanel;

	public GlassPanel(App app){
		super(new BorderLayout());
		this.app = app;
		initUI();
	}

	public void initUI(){

		setBackground(BACKGROUND);
		
		launcherPanel = new LauncherPanel(app);
		fileCreationPanel = new FileCreationPanel(app);
		recentFilesPanel = new RecentFilesPanel(app);

		putToView(launcherPanel);
		
	}

	public void putToView(JPanel panel){
		removeAll();
		add(panel, BorderLayout.CENTER);
		panel.setVisible(false);
		panel.setVisible(true);
		app.getContentPane().doLayout();
	}

	public static omegaui.codeblaze.ui.panel.LauncherPanel getLauncherPanel() {
		return launcherPanel;
	}
	
	public static omegaui.codeblaze.ui.panel.FileCreationPanel getFileCreationPanel() {
		return fileCreationPanel;
	}

	public static omegaui.codeblaze.ui.panel.RecentFilesPanel getRecentFilesPanel() {
		return recentFilesPanel;
	}
	
}
