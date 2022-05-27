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
import omegaui.dynamic.database.DataEntry;

import java.util.LinkedList;

import omegaui.codeblaze.ui.dialog.FileSelectionDialog;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.File;

import omegaui.codeblaze.ui.component.TextInputField;
import omegaui.codeblaze.ui.component.MessagePane;


import omegaui.component.TextComp;
import omegaui.component.EdgeComp;

import omegaui.codeblaze.App;

import javax.swing.JPanel;

import omegaui.codeblaze.io.ResizeAware;
import omegaui.codeblaze.io.AppInstanceProvider;
import omegaui.codeblaze.io.AppUtils;
import omegaui.codeblaze.io.FileManager;
import omegaui.codeblaze.io.AppResourceManager;

import static omegaui.codeblaze.io.UIXManager.*;
import static omegaui.codeblaze.io.AppDataProvider.*;
import static omegaui.component.animation.Animations.*;

public final class FileCreationPanel extends JPanel implements ResizeAware {

	private App app;

	private TextComp iconComp;
	private TextComp titleComp;
	private TextComp closeComp;

	private EdgeComp fileNameLabel;
	private TextInputField fileNameField;

	private EdgeComp parentDirLabel;
	private TextInputField parentDirField;

	private TextComp manageTemplateComp;
	
	private TextComp createComp;

	private MessagePane messagePane;

	public FileCreationPanel(App app){
		this.app = app;
		initUI();
	}

	public void initUI(){
		setBackground(BACKGROUND);
		
		iconComp = new TextComp(fileIcon, 64, 64, BACKGROUND, BACKGROUND, BACKGROUND, null);
		iconComp.setFont(PX32.bold());
		iconComp.setArc(0, 0);
		iconComp.setClickable(false);
		add(iconComp);
		
		titleComp = new TextComp("Create New File", BACKGROUND, BACKGROUND, GLOW, null);
		titleComp.setFont(PX32.bold());
		titleComp.setArc(0, 0);
		titleComp.setClickable(false);
		add(titleComp);
		
		closeComp = new TextComp(cancelIcon, 32, 32, BACKGROUND, BACKGROUND, BACKGROUND, ()->{
			if(FileManager.getCodeEditors().isEmpty())
				app.getGlassPanel().putToView(GlassPanel.getLauncherPanel());
			else
				app.switchViewToContentPane();
		});
		closeComp.setFont(PX32.bold());
		closeComp.setArc(0, 0);
		add(closeComp);

		fileNameLabel = new EdgeComp("File Name With Extension", GLOW, HOVER, GLOW, null);
		fileNameLabel.setFont(PX14.bold());
		fileNameLabel.setEnabled(false);
		add(fileNameLabel);

		fileNameField = new TextInputField("Enter File Name", "");
		fileNameField.setFont(PX16.bold());
		fileNameField.setOnAction(()->createComp.runnable.run());
		fileNameField.setValidationTask((field)->{
			return !field.getText().isEmpty();
		});
		add(fileNameField);

		parentDirLabel = new EdgeComp("Parent Directory", GLOW, HOVER, GLOW, null);
		parentDirLabel.setFont(PX14.bold());
		parentDirLabel.setEnabled(false);
		add(parentDirLabel);

		String path = AppResourceManager.combinePath(AppResourceManager.USER_HOME, "Documents");
		DataEntry entry = AppResourceManager.appDataBase().getEntryAt(AppResourceManager.FILE_CREATION_DIR_PROPERTY);
		if(entry != null)
			path = entry.getValue();
		parentDirField = new TextInputField(path.substring(path.lastIndexOf(File.separatorChar) + 1));
		parentDirField.setFont(PX16.bold());
		parentDirField.setEditable(false);
		parentDirField.setToolTipText(path);
		parentDirField.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e){
				FileSelectionDialog dialog = FileManager.getFileSelectionDialog();
				dialog.setTitle("Select a Directory");
				LinkedList<File> files = dialog.selectDirectories();
				if(!files.isEmpty()){
					File dir = files.get(0);
					parentDirField.setToolTipText(dir.getAbsolutePath());
					parentDirField.setText(dir.getName());
					AppResourceManager.appDataBase().updateEntry(AppResourceManager.FILE_CREATION_DIR_PROPERTY, dir.getAbsolutePath(), 0);
				}
			}
		});
		add(parentDirField);

		manageTemplateComp = new TextComp("Manage Templates", HOVER, BACKGROUND, GLOW, ()->{
			AppUtils.browse(new File(".codeblaze", "templates"));
		});
		manageTemplateComp.setImage(templateIcon, 64, 64);
		manageTemplateComp.setArc(6, 6);
		manageTemplateComp.setImageCoordinates(10, 70/2 - 64/2);
		manageTemplateComp.setFont(PX16.bold());
		manageTemplateComp.setTextAlignment(TextComp.TEXT_ALIGNMENT_RIGHT);
		manageTemplateComp.setTextLeftAlignmentMargin(10);
		add(manageTemplateComp);

		createComp = new TextComp("Create", HOVER, BACKGROUND, GLOW, this::validateProvidedData);
		createComp.setFont(PX18.bold());
		createComp.setArc(6, 6);
		add(createComp);

		messagePane = new MessagePane();
		add(messagePane);

		putAnimationLayer(closeComp, getImageSizeAnimationLayer(20, +5, true), ACTION_MOUSE_ENTERED);
	}

	public void validateProvidedData(){
		String log = FileManager.createNewFile(new File(parentDirField.getToolTipText(), fileNameField.getText()));
		if(log != null){
			messagePane.setMessage(log, "Access Denied", "Already Exists");
			fileNameField.validationFailed();
		}
	}

	@Override
	public void manageBounds(){
		iconComp.setBounds(50, 42, 70, 70);
		titleComp.setBounds(getWidth()/2 - 300/2, 50, 300, 50);
		closeComp.setBounds(getWidth() - 50 - 40, 52, 40, 40);

		int width = 250 + getWidth()/3;
		int x = getWidth()/2 - width/2;
		
		fileNameLabel.setBounds(x, 150, 250, 30);
		fileNameField.setBounds(x + fileNameLabel.getWidth() + 10, 150, getWidth()/3, 30);

		parentDirLabel.setBounds(x, 200, 250, 30);
		parentDirField.setBounds(x + parentDirLabel.getWidth() + 10, 200, getWidth()/3, 30);

		manageTemplateComp.setBounds(getWidth()/2 - 220/2, getHeight()/2, 220, 70);
		createComp.setBounds(getWidth()/2 - 100/2, getHeight() - 100, 100, 30);

		messagePane.setBounds(0, getHeight() - 30, getWidth(), 30);
	}

	@Override
	public void layout(){
		super.layout();
		manageBounds();
	}
	
}
