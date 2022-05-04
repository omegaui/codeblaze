package omegaui.codeblaze.ui.component;
import omegaui.listener.KeyStrokeListener;

import org.fife.ui.rtextarea.RTextScrollPane;

import java.io.File;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import static omegaui.codeblaze.io.UIXManager.*;
import static omegaui.codeblaze.io.AppDataProvider.*;
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
		initSyntaxScheme();
	}

	public void initUI(){
		setAutoscrolls(true);

		scrollPane = new RTextScrollPane(this);
		scrollPane.setLineNumbersEnabled(true);
		scrollPane.getGutter().setCurrentLineNumberColor(GLOW);
		scrollPane.getGutter().setLineNumberColor(HOVER.withOpacity(1f));
	}

	public void initKeyListener(){
		keyStrokeListener = new KeyStrokeListener(this);
//		keyStrokeListener.putKeyStroke((e)->{}, );
		addKeyListener(keyStrokeListener);
	}

	public void initSyntaxScheme(){
		String ext = file.getName();
		if(!ext.contains(".")){
			setSyntaxEditingStyle(SYNTAX_STYLE_NONE);
			return;
		}

//		ext = ext.substring(ext.lastIndexOf('.') + 1);

//		if(ext.equals("java"))
//			setSyntaxEditingStyle();
//		else if(ext.equals(".java"))
			
	}

	public RTextScrollPane getScrollPane(){
		return scrollPane;
	}

	public omegaui.listener.KeyStrokeListener getKeyStrokeListener() {
		return keyStrokeListener;
	}
	
	
}
