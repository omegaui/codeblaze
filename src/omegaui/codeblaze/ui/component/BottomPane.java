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
import java.awt.Dimension;

import omegaui.codeblaze.io.ResizeAware;

import omegaui.codeblaze.App;

import javax.swing.JPanel;

import static omegaui.codeblaze.io.UIXManager.*;
import static omegaui.codeblaze.io.AppDataProvider.*;
import static omegaui.component.animation.Animations.*;

public final class BottomPane extends JPanel implements ResizeAware{
	private App app;

	private MessagePane messagePane;

	public BottomPane(App app){
		this.app = app;
		setPreferredSize(new Dimension(100, 30));
		initUI();
	}

	public void initUI(){

		setBackground(BACKGROUND);
		
		messagePane = new MessagePane();
		add(messagePane);
	}

	@Override
	public void manageBounds(){
		messagePane.setBounds(0, 0, getWidth(), getHeight());
	}

	@Override
	public void layout(){
		super.layout();
		manageBounds();
	}

	public omegaui.codeblaze.ui.component.MessagePane getMessagePane() {
		return messagePane;
	}
	
}
