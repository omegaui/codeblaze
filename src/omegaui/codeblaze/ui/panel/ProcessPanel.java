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
import omegaui.component.EdgeComp;
import omegaui.component.TextComp;

import omegaui.codeblaze.io.TabData;
import omegaui.codeblaze.io.AppInstanceProvider;

import omegaui.codeblaze.App;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JComponent;

import static omegaui.codeblaze.io.UIXManager.*;
import static omegaui.codeblaze.io.AppDataProvider.*;
import static omegaui.component.animation.Animations.*;

public final class ProcessPanel extends JPanel {

	private App app;

	private TabPanel tabPanel;
	private ActionBar actionBar;

	private int lastDividerLocation = -1;
	private int lastHeight = 400;

	public ProcessPanel(App app){
		super(new BorderLayout());
		this.app = app;
		super.setVisible(false);
		initUI();
	}

	public void initUI(){

		setBackground(back2);

		tabPanel = new TabPanel(TabPanel.TAB_LOCATION_TOP);
		tabPanel.getTabHistory().setOnOutOfTabs(()->{
			lastDividerLocation = AppInstanceProvider.getCurrentAppInstance().getSplitPanel().getDividerLocation();
			lastHeight = getHeight();
			setVisible(false);
		});
		add(tabPanel, BorderLayout.CENTER);

		actionBar = new ActionBar();
		add(actionBar, BorderLayout.NORTH);

	}

	public void addTab(TabData tabData){
		if(tabPanel.getTab(tabData.getUniqueName()) != null){
			optimizeTabName(tabData);
		}
		tabPanel.addTab(tabData);
		if(!isVisible())
			setVisible(true);
	}

	public TabData getTabData(JComponent comp){
		return tabPanel.getTabData(comp);
	}

	public void removeTab(TabData tabData){
		tabPanel.removeTab(tabData);
	}

	public void removeTab(JComponent comp){
		removeTab(getTabData(comp));
	}

	public void optimizeTabName(TabData tabData){
		int count = 0;
		for(TabData tx : tabPanel.getTabs()){
			if(tx.getUniqueName().startsWith(tabData.getUniqueName()))
				count++;
		}
		tabData.setUniqueName(tabData.getUniqueName() + count);
		tabData.setName(tabData.getName() + " " + count);
	}

	@Override
	public void layout(){
		super.layout();
		if(isVisible())
			actionBar.computeBounds();
	}

	@Override
	public void setVisible(boolean value) {
		if(tabPanel.isEmpty() || !value){
			super.setVisible(false);
			return;
		}

		super.setVisible(true);

		try{
			setPreferredSize(new Dimension(AppInstanceProvider.getCurrentAppInstance().getWidth(), getHeight() > 450 ? getHeight() : 450));
			AppInstanceProvider.getCurrentAppInstance().getSplitPanel().setDividerLocation(AppInstanceProvider.getCurrentAppInstance().getHeight() - 400);
		}
		catch(Exception e) {

		}
	}


	private class ActionBar extends JComponent{

		private EdgeComp labelComp;

		private TextComp lockedComp;

		private TextComp closeAllComp;

		private TextComp collapseComp;

		private TextComp hideComp;

		private int dividerY;

		private volatile boolean collapseMode = false;
		private volatile boolean locked = true;

		public ActionBar(){
			setLayout(null);
			setSize(100, 25);
			setPreferredSize(getSize());
			setBackground(BACKGROUND);
			init();
		}

		public void init(){
			labelComp = new EdgeComp("Process Panel", HOVER, HOVER, focusColor, ()->{});
			labelComp.setBounds(1, 25/2 - 20/2, computeWidth(labelComp.getText(), PX14) + 20, 20);
			labelComp.setFont(PX14);
			labelComp.setUseFlatLineAtBack(true);
			labelComp.setEnabled(false);
			add(labelComp);

			lockedComp = new TextComp("", secondaryColorShade, HOVER, GLOW, ()->{
				locked = !locked;
				lockedComp.setText(!locked ? "Unlocked" : "Locked");
				lockedComp.setToolTipText(!locked ? "Panel can be resized!" : "Panel cannot be resized!");
				lockedComp.setColors(locked ? secondaryColorShade : primaryColorShade,
					locked ? secondaryColor : primaryColor,
					Color.WHITE);
			});
			lockedComp.setFont(PX14);
			lockedComp.setArc(5, 5);
			lockedComp.setText(!locked ? "Unlocked" : "Locked");
			lockedComp.setToolTipText(!locked ? "Panel can be resized!" : "Panel cannot be resized!");
			lockedComp.setColors(locked ? secondaryColorShade : primaryColorShade,
				locked ? secondaryColor : primaryColor,
				Color.WHITE);
			add(lockedComp);

			closeAllComp = new TextComp("Close All", secondaryColorShade, HOVER, GLOW, ()->{
				tabPanel.closeAllTabs();
			});
			closeAllComp.setFont(PX14);
			closeAllComp.setArc(5, 5);
			add(closeAllComp);

			collapseComp = new TextComp("Collapse", primaryColorShade, HOVER, GLOW, ()->{
				collapseMode = !collapseMode;
				collapseComp.setText(collapseMode ? "Expand" : "Collapse");
				resizeDivider();
				if(!collapseMode)
					AppInstanceProvider.getCurrentAppInstance().getSplitPanel().setDividerLocation(dividerY);
			});
			collapseComp.setFont(PX14);
			collapseComp.setArc(5, 5);
			add(collapseComp);

			hideComp = new TextComp("Hide", warningColorShade, HOVER, GLOW, this::hide);
			hideComp.setFont(PX14);
			hideComp.setArc(5, 5);
			add(hideComp);
		}

		public void resizeDivider(){
			if(collapseMode)
				AppInstanceProvider.getCurrentAppInstance().getSplitPanel().setDividerLocation(AppInstanceProvider.getCurrentAppInstance().getSplitPanel().getHeight() - getHeight() - tabHeight - AppInstanceProvider.getCurrentAppInstance().getSplitPanel().getDividerSize());
			else if(locked && AppInstanceProvider.getCurrentAppInstance().getSplitPanel().getDividerLocation() != dividerY){
				AppInstanceProvider.getCurrentAppInstance().getSplitPanel().setDividerLocation(dividerY);
			}
		}

		public void computeDividerY(){
			dividerY = AppInstanceProvider.getCurrentAppInstance().getHeight() - 400;
		}

		public void hide(){
			ProcessPanel.this.setVisible(false);
		}

		public void computeBounds(){
			lockedComp.setBounds(getWidth() - 320 - 6, 25/2 - 20/2, 80, 20);
			closeAllComp.setBounds(getWidth() - 240 - 4, 25/2 - 20/2, 80, 20);
			collapseComp.setBounds(getWidth() - 160 - 2, 25/2 - 20/2, 80, 20);
			hideComp.setBounds(getWidth() - 80, 25/2 - 20/2, 80, 20);
			computeDividerY();
			resizeDivider();
		}
	}

}
