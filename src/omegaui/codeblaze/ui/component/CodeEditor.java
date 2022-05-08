package omegaui.codeblaze.ui.component;
import java.awt.Font;
import java.awt.Color;

import omegaui.listener.KeyStrokeListener;

import org.fife.ui.rtextarea.RTextScrollPane;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;

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
		initSyntaxScheme(this, file);
		loadFile();
	}

	public void initUI(){
		setAutoscrolls(true);
		setFont(new Font("JetBrains Mono", Font.PLAIN, 15));

		scrollPane = new RTextScrollPane(this);
		scrollPane.setLineNumbersEnabled(true);
		scrollPane.getGutter().setCurrentLineNumberColor(Color.BLACK);
		scrollPane.getGutter().setLineNumberColor(GLOW);
		scrollPane.getGutter().setLineNumberFont(getFont());
	}

	public void initKeyListener(){
		keyStrokeListener = new KeyStrokeListener(this);
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
	}

	public void loadFile(){
		try{
			BufferedReader fread = new BufferedReader(new FileReader(file));
			read(fread, file);
			fread.close();
			savedText = getText();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public RTextScrollPane getScrollPane(){
		return scrollPane;
	}

	public omegaui.listener.KeyStrokeListener getKeyStrokeListener() {
		return keyStrokeListener;
	}
	
	
}