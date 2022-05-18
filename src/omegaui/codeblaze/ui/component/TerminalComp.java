package omegaui.codeblaze.ui.component;
import com.jediterm.terminal.ui.settings.SettingsProvider;

import omegaui.component.FlexPanel;

import java.awt.event.FocusListener;

import java.awt.Color;

import java.nio.charset.Charset;

import com.jediterm.pty.PtyProcessTtyConnector;

import com.pty4j.PtyProcess;
import com.pty4j.PtyProcessBuilder;

import com.jediterm.terminal.TtyConnector;

import java.util.HashMap;
import java.util.Map;

import com.jediterm.terminal.ui.JediTermWidget;

import javax.swing.JPanel;

import static omegaui.codeblaze.io.UIXManager.*;
import static omegaui.codeblaze.io.AppDataProvider.*;
import static omegaui.codeblaze.io.AppResourceManager.*;
import static omegaui.component.animation.Animations.*;

public class TerminalComp extends JPanel{

	public JediTermWidget widget;

	public PtyProcess process;

	public String[] command = null;

	public String directory;

	private Runnable onProcessExited;

	public TerminalComp(){
		super(null);
		this.directory = USER_HOME;
		init();
	}

	public TerminalComp(SettingsProvider settingsProvider){
		super(null);
		this.directory = USER_HOME;
		init(settingsProvider);
	}

	public TerminalComp(String[] command, String directory, SettingsProvider settingsProvider){
		super(null);
		this.command = command;
		this.directory = directory;
		init(settingsProvider);
	}

	public TerminalComp(String[] command, String directory){
		super(null);
		this.command = command;
		this.directory = directory;
		init();
	}

	public void init(){
		setBackground(TerminalCompSettingsProvider.colors[15]);

		TerminalCompSettingsProvider jtsp = new TerminalCompSettingsProvider();
		widget = new JediTermWidget(jtsp);

		if(command == null)
			widget.setTtyConnector(getConnector(onWindows() ? "cmd.exe" : "/bin/bash"));
		else
			widget.setTtyConnector(getConnector(command));
		
		add(widget);
	}

	public void init(SettingsProvider jtsp){
		widget = new JediTermWidget(jtsp);

		if(command == null)
			widget.setTtyConnector(getConnector(onWindows() ? "cmd.exe" : "/bin/bash"));
		else
			widget.setTtyConnector(getConnector(command));

		add(widget);
	}

	public TtyConnector getConnector(String... command){
		try{
			this.command = command;

			Map<String, String> envsX = System.getenv();
			HashMap<String, String> envs = new HashMap<>();
			for(String x : envsX.keySet())
				envs.put(x, envsX.get(x));

			if(!onWindows())
				envs.put("TERM", "xterm-256color");
			
			process = new PtyProcessBuilder()
	            .setCommand(command)
	            .setEnvironment(envs)
	            .setDirectory(directory)
	            .setConsole(false)
	            .setUseWinConPty(onWindows())
	            .start();
			return new PtyProcessTtyConnector(process, Charset.forName("UTF-8"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public void start(){
		widget.start();
		if(onProcessExited != null){
			new Thread(()->{
				while(process.isAlive());
				try{
					Thread.sleep(100);
				}
				catch(Exception e){
					
				}
				onProcessExited.run();
			}).start();
		}
	}

	public void exit(){
		if(process != null && process.isAlive())
			process.destroyForcibly();
	}

	public void relocate(){
		widget.setBounds(5, 5, getWidth() - 10, getHeight() - 10);
	}

	@Override
	public void addFocusListener(FocusListener focusListener){
		super.addFocusListener(focusListener);
		widget.getTerminalPanel().addFocusListener(focusListener);
	}

	@Override
	public void layout(){
		relocate();
		super.layout();
	}

	public java.lang.Runnable getOnProcessExited() {
		return onProcessExited;
	}

	public void setOnProcessExited(java.lang.Runnable onProcessExited) {
		this.onProcessExited = onProcessExited;
	}

	public void print(String text){
		widget.getTerminal().writeCharacters(text);
		widget.getTerminal().nextLine();
	}
	
	public void clearTerminal(){
		widget.getTerminal().clearScreen();
		widget.getTerminal().cursorPosition(0, 0);
	}

	public String getText(){
		return widget.getTerminalTextBuffer().getScreenLines();
	}

}
