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
import omegaui.codeblaze.ui.panel.TabPanel;

import omegaui.codeblaze.ui.listener.TabPanelListener;

import java.util.LinkedList;
public class TabHistory implements TabPanelListener{
	private TabPanel tabPanel;

	private LinkedList<TabData> tabs = new LinkedList<>();

	private Runnable onOutOfTabs = ()->{
		tabs.clear();
		AppInstanceProvider.getCurrentAppInstance().switchViewToGlassPane();
	};

	public TabHistory(TabPanel tabPanel){
		this.tabPanel = tabPanel;
		tabPanel.addTabPanelListener(this);
	}

	@Override
	public void tabActivated(TabData tabData){
		if(tabs.contains(tabData))
			tabs.remove(tabData);
		tabs.add(tabData);
	}

	@Override
	public void tabAdded(TabData tabData) {

	}

	@Override
	public void tabRemoved(TabData tabData) {
		if(!tabs.isEmpty()){
			tabs.remove(tabData);
			if(tabs.size() > 0){
				TabData tx = tabs.getLast();
				if(tabPanel.isTabDataAlreadyPresent(tx))
					tabPanel.setActiveTab(tx);
			}
		}
	}

	@Override
	public void goneEmpty(TabPanel tabPanel) {
		onOutOfTabs.run();
	}

	public void setOnOutOfTabs(Runnable action){
		this.onOutOfTabs = action;
	}

	public java.util.LinkedList getActivatedTabs() {
		return tabs;
	}

}
