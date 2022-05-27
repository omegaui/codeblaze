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
import omegaui.listener.KeyStrokeListener;

import omegaui.paint.PixelColor;

import java.io.File;

import java.util.LinkedList;

import java.awt.TextField;
import java.awt.Color;
import java.awt.Dimension;

import omegaui.codeblaze.App;

import omegaui.component.TextComp;

import omegaui.codeblaze.ui.component.TextInputField;
import omegaui.codeblaze.ui.component.MessagePane;
import omegaui.codeblaze.ui.component.FileInfoComp;

import omegaui.codeblaze.io.ResizeAware;
import omegaui.codeblaze.io.FileManager;
import omegaui.codeblaze.io.AppUtils;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;

import static omegaui.codeblaze.io.UIXManager.*;
import static omegaui.codeblaze.io.AppDataProvider.*;
import static omegaui.component.animation.Animations.*;

import static java.awt.event.KeyEvent.*;

public final class RecentFilesPanel extends JPanel implements ResizeAware{

	private App app;

	private TextComp iconComp;
	private TextComp titleComp;
	private TextComp closeComp;

	private TextInputField textField;

	private LinkedList<FileInfoComp> fileComps = new LinkedList<>();

	private int pointer;

	private JPanel panel;
	private JScrollPane scrollPane;

	private MessagePane messagePane;

	public RecentFilesPanel(App app){
		this.app = app;
		initUI();
		initKeyStrokeListener();
	}

	public void initKeyStrokeListener(){
		KeyStrokeListener listener = new KeyStrokeListener(this);
		listener.putKeyStroke((e)->{
			if(pointer - 1 >= 0){
				fileComps.get(pointer).setEnter(false);
				fileComps.get(--pointer).setEnter(true);
				scrollPane.getVerticalScrollBar().setValue(pointer * 60);
			}
		}, VK_UP).setStopKeys(VK_CONTROL, VK_SHIFT, VK_ALT);
		listener.putKeyStroke((e)->{
			if(pointer + 1 < fileComps.size()){
				fileComps.get(pointer).setEnter(false);
				fileComps.get(++pointer).setEnter(true);
				scrollPane.getVerticalScrollBar().setValue(pointer * 60);
			}
		}, VK_DOWN).setStopKeys(VK_CONTROL, VK_SHIFT, VK_ALT);
		listener.putKeyStroke((e)->{
			fileComps.get(pointer).doClick();
		}, VK_ENTER).setStopKeys(VK_CONTROL, VK_SHIFT, VK_ALT);
		addKeyListener(listener);
		textField.addKeyListener(listener);
	}

	public void initUI(){

		setBackground(BACKGROUND);

		iconComp = new TextComp(recentsIcon, 64, 64, BACKGROUND, BACKGROUND, BACKGROUND, null);
		iconComp.setFont(PX32);
		iconComp.setArc(0, 0);
		iconComp.setClickable(false);
		add(iconComp);

		titleComp = new TextComp("Quick Open a Recent File", BACKGROUND, BACKGROUND, GLOW, null);
		titleComp.setFont(PX32);
		titleComp.setArc(0, 0);
		titleComp.setClickable(false);
		add(titleComp);

		closeComp = new TextComp(cancelIcon, 32, 32, BACKGROUND, BACKGROUND, BACKGROUND, ()->{
			if(FileManager.getCodeEditors().isEmpty())
				app.getGlassPanel().putToView(GlassPanel.getLauncherPanel());
			else
				app.switchViewToContentPane();
		});
		closeComp.setFont(PX32);
		closeComp.setArc(0, 0);
		add(closeComp);

		textField = new TextInputField("Type File Name Here", "");
		textField.setFont(PX16);
		textField.setOnAnyKeyReleased(this::recreateView);
		textField.setOnAction(()->{
			if(!fileComps.isEmpty() && pointer == 0)
				fileComps.get(0).doClick();
		});
		add(textField);

		scrollPane = new JScrollPane(panel = new JPanel(null));
		scrollPane.setBorder(null);
		scrollPane.setLocation(getWidth()/2 - 250, 200);
		scrollPane.setSize(500, getHeight() - 150);
		panel.setBackground(BACKGROUND);
		add(scrollPane);

		messagePane = new MessagePane();
		add(messagePane);

		putAnimationLayer(closeComp, getImageSizeAnimationLayer(20, +5, true), ACTION_MOUSE_ENTERED);
	}

	public void recreateView(){
		panel.removeAll();
		fileComps.clear();

		messagePane.getMessageComp().setHighlightColor(primaryColor);

		LinkedList<String> recentFiles = FileManager.getRecentFilesDataBase().getEntriesAsString(FileManager.RECENT_FILE_DATA_SET_NAME);
		if(recentFiles.isEmpty()){
			messagePane.getMessageComp().setHighlightColor(secondaryColor);
			messagePane.setMessage("You haven't open any file yet, Open some files and get them listed here.", "haven't open any file");
			return;
		}

		String text = textField.getText();

		int blockY = 10;
		int width = scrollPane.getWidth();

		for(int i = recentFiles.size() - 1; i >= 0; i--){
			String path = recentFiles.get(i);
			File file = new File(path);
			if(path.contains(text) || AppUtils.isMatching(path, text)){
				FileInfoComp comp = new FileInfoComp(file, ()->{
					FileManager.openFile(file);
				});
				comp.setBounds(10, blockY, width - 50, 50);
				panel.add(comp);
				fileComps.add(comp);

				blockY += 50 + 10;
			}
		}

		messagePane.setMessage(fileComps.size() + " files were found!", String.valueOf(fileComps.size()));

		panel.setPreferredSize(new Dimension(width - 30, blockY));
		panel.repaint();
		scrollPane.getVerticalScrollBar().setVisible(false);
		scrollPane.getVerticalScrollBar().setVisible(true);

		fileComps.get(pointer).setEnter(true);
	}

	@Override
	public void setVisible(boolean value){
		if(value){
			pointer = 0;
			recreateView();
		}
		super.setVisible(value);
		if(value){
			grabFocus();
		}
	}

	@Override
	public void manageBounds(){
		iconComp.setBounds(50, 42, 70, 70);
		titleComp.setBounds(getWidth()/2 - 400/2, 50, 400, 50);
		closeComp.setBounds(getWidth() - 50 - 40, 52, 40, 40);

		textField.setBounds(getWidth()/2 - getWidth()/6, 150, getWidth()/3, 30);

		scrollPane.setBounds(getWidth()/2 - 250, 200, 500, getHeight() - 250);

		messagePane.setBounds(0, getHeight() - 30, getWidth(), 30);
	}

	@Override
	public void layout(){
		super.layout();
		manageBounds();
	}

}
