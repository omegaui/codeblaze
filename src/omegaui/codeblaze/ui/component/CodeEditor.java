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
import org.fife.rsta.ac.LanguageSupportFactory;

import javax.swing.DropMode;

import omegaui.dynamic.database.DataEntry;

import omegaui.codeblaze.ui.dialog.ChoiceDialog;

import omegaui.codeblaze.io.FileManager;
import omegaui.codeblaze.io.AppInstanceProvider;
import omegaui.codeblaze.io.CodeEditorPluginManager;

import java.awt.Font;
import java.awt.Color;

import omegaui.listener.KeyStrokeListener;

import org.fife.ui.rtextarea.RTextScrollPane;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Theme;

import static omegaui.codeblaze.io.UIXManager.*;
import static omegaui.codeblaze.io.AppDataProvider.*;
import static omegaui.codeblaze.io.AppResourceManager.*;
import static omegaui.component.animation.Animations.*;

import static java.awt.event.KeyEvent.*;

public class CodeEditor extends RSyntaxTextArea {
	private String savedText;

	private File file;

	private RTextScrollPane scrollPane;

	private KeyStrokeListener keyStrokeListener;

	public CodeEditor(File file){
		this.file = file;
		initUI();
		initKeyListener();
		initSyntaxScheme(this, file);
		initPlugins();
		loadFile();
	}

	public void initUI(){

		LanguageSupportFactory.get().register(this);
		
		setAnimateBracketMatching(true);
		setAntiAliasingEnabled(true);
		setAutoIndentEnabled(true);
		setAutoscrolls(true);
		setBracketMatchingEnabled(true);
		setCloseCurlyBraces(true);
		setPaintMatchedBracketPair(true);
		setHyperlinksEnabled(true);
		setHyperlinkForeground(GLOW);
		setCodeFoldingEnabled(true);
		setFadeCurrentLineHighlight(false);
		setShowMatchedBracketPopup(true);
		setHighlightSecondaryLanguages(true);
		setDragEnabled(true);
		setDropMode(DropMode.USE_SELECTION);

		scrollPane = new RTextScrollPane(this);
		scrollPane.setLineNumbersEnabled(true);
		scrollPane.getGutter().setCurrentLineNumberColor(CURRENT_LINE_NUMBER_COLOR);
		scrollPane.getGutter().setLineNumberColor(LINE_NUMBER_COLOR);
		scrollPane.getGutter().setLineNumberFont(getFont());
		scrollPane.setBorder(null);

		loadTheme();
	}

	public void initKeyListener(){
		keyStrokeListener = new KeyStrokeListener(this);
		keyStrokeListener.putKeyStroke((e)->{
			FileManager.compileAndExecute(this);
		}, VK_CONTROL, VK_ALT, VK_R).setStopKeys(VK_SHIFT).useAutoReset();
		keyStrokeListener.putKeyStroke((e)->{
			FileManager.compile(this);
		}, VK_CONTROL, VK_B).setStopKeys(VK_ALT, VK_SHIFT).useAutoReset();
		keyStrokeListener.putKeyStroke((e)->{
			FileManager.execute(this);
		}, VK_CONTROL, VK_SHIFT, VK_L).setStopKeys(VK_ALT).useAutoReset();
		keyStrokeListener.putKeyStroke((e)->{
			askAndSaveFile();
		}, VK_CONTROL, VK_S).setStopKeys(VK_ALT, VK_SHIFT).useAutoReset();
		keyStrokeListener.putKeyStroke((e)->{
			saveSilently();
		}, VK_CONTROL, VK_SHIFT, VK_S).setStopKeys(VK_ALT).useAutoReset();
		keyStrokeListener.putKeyStroke((e)->{
			reloadFile();
		}, VK_CONTROL, VK_R).setStopKeys(VK_ALT, VK_SHIFT).useAutoReset();
		keyStrokeListener.putKeyStroke((e)->{
			closeFile();
		}, VK_CONTROL, VK_W).setStopKeys(VK_ALT, VK_SHIFT).useAutoReset();

		keyStrokeListener.putKeyStroke((e)->{
			FileManager.increaseDocumentFontSize();
		}, VK_CONTROL, VK_SHIFT, VK_EQUALS).setStopKeys(VK_ALT);
		keyStrokeListener.putKeyStroke((e)->{
			FileManager.decreaseDocumentFontSize();
		}, VK_CONTROL, VK_SHIFT, VK_MINUS).setStopKeys(VK_ALT);

		keyStrokeListener.putKeyStroke((e)->{
			FileManager.increaseDocumentTabSize();
		}, VK_CONTROL, VK_T, VK_EQUALS).setStopKeys(VK_ALT, VK_SHIFT);
		keyStrokeListener.putKeyStroke((e)->{
			FileManager.decreaseDocumentTabSize();
		}, VK_CONTROL, VK_T, VK_MINUS).setStopKeys(VK_ALT, VK_SHIFT);

		addKeyListener(keyStrokeListener);
	}

	public static void initSyntaxScheme(CodeEditor e, File f){
		if(!f.getName().contains(".") || f.getName().endsWith(".txt"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_NONE);
		else if(f.getName().endsWith(".as"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_ACTIONSCRIPT);
		else if(f.getName().endsWith(".asm"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_ASSEMBLER_X86);
		else if(f.getName().endsWith(".asm"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_ASSEMBLER_6502);
		else if(f.getName().endsWith(".html"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_HTML);
		else if(f.getName().endsWith(".c") || f.getName().endsWith(".C"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_C);
		else if(f.getName().endsWith(".vala"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_CPLUSPLUS);
		else if(f.getName().endsWith(".clj") || f.getName().endsWith(".cljs") || f.getName().endsWith(".cljc") || f.getName().endsWith(".edn"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_CLOJURE);
		else if(f.getName().endsWith(".cpp") || f.getName().endsWith(".CPP"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_CPLUSPLUS);
		else if(f.getName().endsWith(".cs"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_CSHARP);
		else if(f.getName().endsWith(".css"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_CSS);
		else if(f.getName().endsWith(".csv"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_CSV);
		else if(f.getName().endsWith(".d"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_D);
		else if(f.getName().endsWith(".dart"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_DART);
		else if(f.getName().endsWith(".dpr"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_DELPHI);
		else if(f.getName().endsWith(".dtd"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_DTD);
		else if(f.getName().endsWith(".f90"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_FORTRAN);
		else if(f.getName().endsWith(".go"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_GO);
		else if(f.getName().endsWith(".groovy") || f.getName().endsWith(".gradle"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_GROOVY);
		else if(f.getName().endsWith(".html") || f.getName().endsWith(".svg") || f.getName().endsWith(".svelte"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_HTML);
		else if(f.getName().endsWith(".ini"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_INI);
		else if(f.getName().endsWith(".java"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_JAVA);
		else if(f.getName().endsWith(".js") || f.getName().endsWith(".jsx"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_JAVASCRIPT);
		else if(f.getName().endsWith(".ts") || f.getName().endsWith(".tsx"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_TYPESCRIPT);
		else if(f.getName().endsWith(".json"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_JSON_WITH_COMMENTS);
		else if(f.getName().endsWith(".hjson"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_JSP);
		else if(f.getName().endsWith(".tex"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_LATEX);
		else if(f.getName().endsWith(".less"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_LESS);
		else if(f.getName().endsWith(".lsp"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_LISP);
		else if(f.getName().endsWith(".lua"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_LUA);
		else if(f.getName().endsWith("makefile"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_MAKEFILE);
		else if(f.getName().endsWith(".mxml"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_MXML);
		else if(f.getName().endsWith(".nsi"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_NSIS);
		else if(f.getName().endsWith(".pl"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_PERL);
		else if(f.getName().endsWith(".php"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_PHP);
		else if(f.getName().endsWith(".property"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_PROPERTIES_FILE);
		else if(f.getName().endsWith(".py"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_PYTHON);
		else if(f.getName().endsWith(".rb"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_RUBY);
		else if(f.getName().endsWith(".sas"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_SAS);
		else if(f.getName().endsWith(".scala") || f.getName().endsWith(".sc"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_SCALA);
		else if(f.getName().endsWith(".sql"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_SQL);
		else if(f.getName().endsWith(".ts"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_TYPESCRIPT);
		else if(f.getName().endsWith(".sh") || f.getName().endsWith(".run"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_UNIX_SHELL);
		else if(f.getName().endsWith(".bat") || f.getName().endsWith(".cmd"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_WINDOWS_BATCH);
		else if(f.getName().endsWith(".xml") || f.getName().endsWith(".fxml"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_XML);
		else if(f.getName().endsWith(".yaml") || f.getName().endsWith(".yml"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_YAML);
		else if(f.getName().endsWith(".md"))
			e.setSyntaxEditingStyle(CodeEditor.SYNTAX_STYLE_MARKDOWN);
	}

	public void initPlugins(){
		CodeEditorPluginManager.installPlugins(this);
	}
	
	public boolean loadFile(){
		try{
			BufferedReader fread = new BufferedReader(new FileReader(file));
			read(fread, file);
			fread.close();
			savedText = getText();
			FileManager.executeFileLoadedEventScript(this);
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void reloadFile(){
		if(!isFileSaved()){
			int choice = ChoiceDialog.makeChoice("You will lose any modifications, Do you want to continue and Reload the File?", "Yes, Reload and Lose Data", "No, Do Nothing");
			if(choice != ChoiceDialog.CHOICE1){
				AppInstanceProvider.getCurrentAppInstance().setMessage("Reload Operation Cancelled for " + file.getName() + "!", "Cancelled");
				return;
			}
		}
		AppInstanceProvider.getCurrentAppInstance().setMessage("Reloading " + file.getName() + " ... ", "Reloading");
		if(!loadFile()){
			AppInstanceProvider.getCurrentAppInstance().setMessage("Failed to Reload " + file.getName() + ".", "Failed");
		}
		else{
			AppInstanceProvider.getCurrentAppInstance().setMessage("Reloaded " + file.getName() + ".", "Reloaded");
		}
	}

	public boolean isFileSaved(){
		return savedText.equals(getText());
	}

	public void askAndSaveFile(){
		int choice = ChoiceDialog.CHOICE1;
		if(!isFileSaved()){
			choice = ChoiceDialog.makeChoice("Do you want to Save " + file.getName() + " file?", "Yes, Save!", "No, Lose Data!");
		}

		if(choice != ChoiceDialog.CHOICE1){
			AppInstanceProvider.getCurrentAppInstance().setMessage("Save Operation Cancelled for " + file.getName() + "!", "Cancelled");
			return;
		}

		AppInstanceProvider.getCurrentAppInstance().setMessage("Saving " + file.getName() + " ... ", file.getName());
		if(saveFile()){
			AppInstanceProvider.getCurrentAppInstance().setMessage("Saved " + file.getName() + "!", "Saved");
		}
		else{
			AppInstanceProvider.getCurrentAppInstance().setMessage("Failed to Save " + file.getName() + "!", "Failed to Save");
		}
	}

	public boolean saveFile(){
		if(getText().equals(savedText))
			return true;
		if(FileManager.overwriteToFile(file, getText())){
			savedText = getText();
			FileManager.executeFileSavedEventScript(this);
			return true;
		}
		return false;
	}

	public void saveSilently(){
		saveFile();
		AppInstanceProvider.getCurrentAppInstance().setMessage("Saved!, You Called Silent Save File on " + file.getName() + " -- Shortcut : Ctrl + SHIFT + S", "Silent Save");
	}

	public void closeFile(){
		FileManager.closeEditor(this);
	}

	public void loadTheme(){
		try{
			String name = "JetBrains Mono";
			int style = Font.PLAIN;
			int size = 15;

			DataEntry entry = appDataBase().getEntryAt(DOCUMENT_FONT_PROPERTY);
			if(entry != null){
				name = entry.lines().get(0);
				style = Integer.parseInt(entry.lines().get(1));
				size = Integer.parseInt(entry.lines().get(2));
			}

			int tabSize = getTabSize();
			
			entry = appDataBase().getEntryAt(DOCUMENT_TAB_SIZE_PROPERTY);
			if(entry != null)
				tabSize = entry.getValueAsInt();

			Theme.load(getClass().getResourceAsStream("/editor-themes/" + (isDarkMode() ? "dark.xml" : "light.xml"))).apply(this);
			setFont(new Font(name, style, size));
			setTabSize(tabSize);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void setFont(Font font){
		super.setFont(font);
		if(scrollPane != null){
			scrollPane.getGutter().setLineNumberFont(font);
			scrollPane.getGutter().setCurrentLineNumberColor(CURRENT_LINE_NUMBER_COLOR);
			scrollPane.getGutter().setLineNumberColor(LINE_NUMBER_COLOR);
		}
	}

	public java.io.File getFile() {
		return file;
	}

	public RTextScrollPane getScrollPane(){
		return scrollPane;
	}

	public omegaui.listener.KeyStrokeListener getKeyStrokeListener() {
		return keyStrokeListener;
	}

}
