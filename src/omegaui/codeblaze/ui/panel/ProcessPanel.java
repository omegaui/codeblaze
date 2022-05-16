package omegaui.codeblaze.ui.panel;
import omegaui.codeblaze.io.TabData;
import omegaui.codeblaze.io.ResizeAware;
import omegaui.codeblaze.io.AppInstanceProvider;

import omegaui.codeblaze.App;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import static omegaui.codeblaze.io.UIXManager.*;
import static omegaui.codeblaze.io.AppDataProvider.*;
import static omegaui.component.animation.Animations.*;

public final class ProcessPanel extends JPanel {

	private App app;
	private TabPanel tabPanel;

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
			setVisible(false);
		});
		add(tabPanel, BorderLayout.CENTER);

	}

	public void addTab(TabData tabData){
		if(tabPanel.getTab(tabData.getUniqueName()) != null){
			optimizeTabName(tabData);
		}
		tabPanel.addTab(tabData);
		if(!isVisible())
			super.setVisible(true);
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
	public void setVisible(boolean value){
		super.setVisible(value);
		if(value){
			setSize(100, 100);
			setPreferredSize(getSize());
			AppInstanceProvider.getCurrentAppInstance().getSplitPanel().setDividerLocation(100);
		}
	}
}
