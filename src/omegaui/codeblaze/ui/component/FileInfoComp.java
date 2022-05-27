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
import java.awt.Graphics;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

import omegaui.codeblaze.io.ResizeAware;

import omegaui.component.TextComp;

import java.awt.image.BufferedImage;

import java.io.File;

import javax.swing.JComponent;

import static omegaui.codeblaze.io.UIXManager.*;
import static omegaui.codeblaze.io.AppDataProvider.*;
import static omegaui.component.animation.Animations.*;

public final class FileInfoComp extends JComponent implements ResizeAware, MouseListener {

	private File file;
	
	private Runnable onAction;

	private BufferedImage image;
	
	private TextComp imageComp;
	private TextComp nameComp;
	private TextComp parentComp;

	private volatile boolean mouseEnter = false;

	public FileInfoComp(File file, Runnable onAction){
		this.file = file;
		this.onAction = onAction;
		this.image = getPreferredIconForFile(file);
		initUI();
	}

	public void initUI(){
		addMouseListener(this);
		
		imageComp = new TextComp("", BACKGROUND, BACKGROUND, BACKGROUND, onAction);
		imageComp.setArc(6, 6);
		imageComp.addMouseListener(this);
		add(imageComp);

		nameComp = new TextComp(file.getName(), BACKGROUND, BACKGROUND, getPreferredColorForFile(file), onAction);
		nameComp.setFont(PX16);
		nameComp.setArc(0, 0);
		nameComp.addMouseListener(this);
		nameComp.setTextAlignment(TextComp.TEXT_ALIGNMENT_LEFT);
		add(nameComp);

		parentComp = new TextComp(file.getParentFile().getAbsolutePath(), BACKGROUND, BACKGROUND, warningColor, onAction);
		parentComp.setFont(UBUNTU_PX12);
		parentComp.setArc(0, 0);
		parentComp.addMouseListener(this);
		parentComp.setTextAlignment(TextComp.TEXT_ALIGNMENT_LEFT);
		add(parentComp);
	}

	@Override
	public void paint(Graphics g){
		super.paint(g);
		if(mouseEnter){
			g.setColor(HOVER);
			g.fillRect(0, 0, getWidth(), getHeight());
		}
	}

	@Override
	public void manageBounds(){
		imageComp.setBounds(5, 5, getHeight() - 10, getHeight() - 10);
		imageComp.setImage(image, getHeight() - 5, getHeight() - 5);

		nameComp.setBounds(getHeight(), 5, getWidth() - getHeight() - 10, 25);
		
		parentComp.setBounds(getHeight(), getHeight() - 20, getWidth() - getHeight() - 5, 25);
	}

	@Override
	public void layout(){
		super.layout();
		manageBounds();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(onAction != null)
			onAction.run();
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		setEnter(false);
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		setEnter(true);
	}

	public void setEnter(boolean mouseEnter){
		this.mouseEnter = mouseEnter;
		repaint();
	}

	public void doClick(){
		onAction.run();
	}
}
