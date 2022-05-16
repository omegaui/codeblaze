package omegaui.codeblaze.ui.panel;
import omegaui.codeblaze.io.TabData;
import omegaui.codeblaze.io.AppInstanceProvider;

import omegaui.codeblaze.App;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import static omegaui.codeblaze.io.UIXManager.*;
import static omegaui.codeblaze.io.AppDataProvider.*;
import static omegaui.component.animation.Animations.*;

public final class ProcessPanel extends JPanel {

	private App app;
	private TabPanel tabPanel;

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

	}

	public void addTab(TabData tabData){
		if(tabPanel.getTab(tabData.getUniqueName()) != null){
			optimizeTabName(tabData);
		}
		tabPanel.addTab(tabData);
		if(!isVisible())
			setVisible(true);
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
			if(lastDividerLocation == -1)
				lastDividerLocation = AppInstanceProvider.getCurrentAppInstance().getHeight() - lastHeight;
			AppInstanceProvider.getCurrentAppInstance().getSplitPanel().setDividerLocation(lastDividerLocation);
		}
	}
}
