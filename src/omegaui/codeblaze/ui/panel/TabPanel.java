/*
 * TabPanel
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package omegaui.codeblaze.ui.panel;

import omegaui.codeblaze.ui.component.CodeEditor;
import omegaui.codeblaze.ui.component.MaterialPopup;

import omegaui.codeblaze.ui.listener.TabPanelListener;

import omegaui.codeblaze.io.TabHistory;
import omegaui.codeblaze.io.TabData;
import omegaui.codeblaze.io.AppUtils;

import javax.imageio.ImageIO;

import org.fife.ui.rtextarea.RTextScrollPane;

import java.util.zip.ZipFile;

import java.awt.image.BufferedImage;

import java.io.File;

import java.util.LinkedList;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.FontMetrics;
import java.awt.Image;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JComponent;
import javax.swing.BorderFactory;

import static omegaui.codeblaze.io.UIXManager.*;
import static omegaui.codeblaze.io.AppDataProvider.*;
import static omegaui.component.animation.Animations.*;

public class TabPanel extends JPanel{

	private TabsHolderPanel tabsHolder;
	private TabCompHolderPanel tabCompHolder;
	private TabHistory tabHistory;

	private LinkedList<TabPanelListener> tabPanelListeners = new LinkedList<>();

	private TabData activeTabData;

	private LinkedList<TabData> tabs = new LinkedList<>();

	public static final int TAB_LOCATION_TOP = 0;
	public static final int TAB_LOCATION_BOTTOM = 1;

	private int tabsPosition;

	private volatile boolean hideOnEmpty = false;

	public TabPanel(int tabsPosition){
		super(new BorderLayout());
		this.tabsPosition = tabsPosition;

		setBackground(back3);
		setBorder(BorderFactory.createLineBorder(BACKGROUND));
		
		init();
	}
	
	public void init(){
		tabsHolder = new TabsHolderPanel(this);
		add(tabsHolder, tabsPosition == TAB_LOCATION_TOP ? BorderLayout.NORTH : BorderLayout.SOUTH);

		tabCompHolder = new TabCompHolderPanel(this);
		add(tabCompHolder, BorderLayout.CENTER);

		tabHistory = new TabHistory(this);
	}

	public JPanel addTab(String name, String fullQualifiedName, String toolTip, BufferedImage image, JComponent component, Runnable removeAction) {
		return addTab(new TabData(name, fullQualifiedName, toolTip, image, component, removeAction));
	}

	public JPanel addTab(String name, String fullQualifiedName, String toolTip, BufferedImage image, JComponent component, Runnable removeAction, MaterialPopup popupWindow) {
		return addTab(new TabData(name, fullQualifiedName, toolTip, image, component, removeAction).setPopup(popupWindow));
	}

	public JPanel addTab(String name, String fullQualifiedName, String toolTip, BufferedImage image, JComponent component, Color tabTextColor, Runnable removeAction, MaterialPopup popupWindow) {
		return addTab(new TabData(name, fullQualifiedName, toolTip, image, component, tabTextColor, removeAction).setPopup(popupWindow));
	}

	public JPanel addTab(TabData tabData){
		if(isTabDataAlreadyPresent(tabData))
			return null;
		boolean wasEmpty = tabs.isEmpty();

		tabs.add(tabData);

		tabsHolder.addTabHolder(tabData);
		tabCompHolder.putTab(tabData);

		activeTabData = tabData;

		if(!isVisible())
			setVisible(true);

		if(!tabPanelListeners.isEmpty())
			tabPanelListeners.forEach(listener->listener.tabAdded(tabData));

		return tabData.getTabHolder().getHolderPanel();
	}

	public boolean isTabDataAlreadyPresent(TabData tabData){
		if(tabs.size() <= 0)
			return false;
		for(TabData dx : tabs){
			if(dx.equals(tabData))
				return true;
		}
		return false;
	}

	public void removeTab(TabData tabData){
		if(tabData == null)
			return;
		
		tabsHolder.removeTabHolder(tabData);
		tabCompHolder.putOffTab(tabData);

		tabs.remove(tabData);

		if(hideOnEmpty && isEmpty())
			setVisible(false);

		if(!tabPanelListeners.isEmpty()){
			tabPanelListeners.forEach(listener->listener.tabRemoved(tabData));
			if(isEmpty())
				tabPanelListeners.forEach(listener->listener.goneEmpty(this));
		}
		repaint();
	}

	public void setActiveTab(TabData tabData){
		if(tabData == null)
			return;
		tabsHolder.showTab(tabData);
		tabCompHolder.showTabComponent(tabData);

		activeTabData = tabData;

		tabPanelListeners.forEach(listener->listener.tabActivated(tabData));
	}
	
	public void setActiveTabIndex(int index){
		TabData tabData = getTabDataAt(index);
		if(tabData == null)
			return;
		tabsHolder.showTab(tabData);
		tabCompHolder.showTabComponent(tabData);

		activeTabData = tabData;

		tabPanelListeners.forEach(listener->listener.tabActivated(tabData));
	}

	public boolean contains(JComponent comp){
		for(TabData dx : tabs){
			if(dx.getComponent() == comp)
				return true;
		}
		return false;
	}

	public TabData getTabData(JComponent comp){
		for(TabData dx : tabs){
			if(dx.getComponent() == comp)
				return dx;
		}
		return null;
	}

	public TabData getTabDataAt(int index){
		return (index >= 0 && index < tabs.size()) ? tabs.get(index) : null;
	}

	public void showTab(JComponent comp){
		setActiveTab(getTabData(comp));
	}

	public int getIndexOf(JComponent comp){
		for(TabData dx : tabs){
			if(dx.getComponent() == comp)
				return tabs.indexOf(dx);
		}
		return -1;
	}

	public boolean viewImage(File file) {
		if(observableImage(file)) {
			AppUtils.browse(file);
			return true;
		}
		return false;
	}

	public static boolean observableImage(File file){
		String n = file.getName();
		return n.endsWith(".png") || n.endsWith(".jpg")
		|| n.endsWith(".jpeg") || n.endsWith(".bmp") || n.endsWith(".gif");
	}

	public boolean viewArchive(File file) {
		if(observableArchive(file)) {
			AppUtils.browse(file);
			return true;
		}
		return false;
	}

	public static boolean observableArchive(File root){
		try{
			ZipFile zipFile = new ZipFile(root);
			zipFile.close();
			return true;
		}
		catch(Exception e){

		}
		return false;
	}

	public CodeEditor getCurrentEditor() {
		if(activeTabData == null)
			return null;
		if(activeTabData.getComponent() instanceof CodeEditor editor)
			return editor;
		return null;
	}

	public void closeAllTabs() {
		tabsHolder.triggerAllClose();
		tabs.clear();

		activeTabData = null;
	}

	public boolean isEmpty(){
		return tabs.isEmpty();
	}

	public TabData getTab(String uniqueName){
		for(TabData tx : tabs){
			if(tx.getUniqueName().equals(uniqueName))
				return tx;
		}
		return null;
	}

	public LinkedList<TabData> getTabs(){
		return tabs;
	}

	public boolean isHideOnEmpty() {
		return hideOnEmpty;
	}

	public void setHideOnEmpty(boolean hideOnEmpty) {
		this.hideOnEmpty = hideOnEmpty;
		if(isEmpty())
			setVisible(false);
	}

	public LinkedList<TabPanelListener> getTabPanelListeners() {
		return tabPanelListeners;
	}

	public void addTabPanelListener(TabPanelListener tabPanelListener) {
		if(!tabPanelListeners.contains(tabPanelListener))
			tabPanelListeners.add(tabPanelListener);
	}

	public TabHistory getTabHistory() {
		return tabHistory;
	}

}
