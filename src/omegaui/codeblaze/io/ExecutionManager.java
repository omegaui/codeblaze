package omegaui.codeblaze.io;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.LinkedList;

import com.pty4j.PtyProcessBuilder;

import omegaui.codeblaze.ui.component.TerminalComp;

import java.io.File;

import static omegaui.codeblaze.io.ScriptManager.*;
import static omegaui.codeblaze.io.AppResourceManager.*;
import static omegaui.codeblaze.io.UIXManager.*;
import static omegaui.codeblaze.io.AppDataProvider.*;
import static omegaui.component.animation.Animations.*;

public final class ExecutionManager {

	public static int compile(File file){
		if(isCompileScriptAvailable(file)){
			TerminalComp terminal = new TerminalComp(new String[]{
				getCompilerScriptPath(file), file.getAbsolutePath(), file.getName(), file.getParentFile().getAbsolutePath()
			}, combineToAbsolutePath(ROOT_DIR_NAME, COMPILER_SCRIPT_DIR_NAME));
			AppInstanceProvider.getCurrentAppInstance().getProcessPanel().addTab(new TabData(file.getName(), file.getAbsolutePath(), getPlatformImage(), terminal, ()->{
				terminal.exit();
			}));
			terminal.print("Executing " + getCompilerScriptName(file) + " ... ");
			terminal.print("");
			terminal.setOnProcessExited(()->{
				terminal.print("");
				terminal.print("Process Finished with exit code " + terminal.process.exitValue());
			});
			terminal.start();
			while(terminal.process.isAlive());
				return terminal.process.exitValue();
		}
		return 404;
	}

	public static int execute(File file){
		if(isInterpreterScriptAvailable(file)){
			TerminalComp terminal = new TerminalComp(new String[]{
				getInterpreterScriptPath(file), file.getAbsolutePath(), file.getName(), file.getParentFile().getAbsolutePath()
			}, combineToAbsolutePath(ROOT_DIR_NAME, INTERPRETER_SCRIPT_DIR_NAME));
			AppInstanceProvider.getCurrentAppInstance().getProcessPanel().addTab(new TabData(file.getName(), file.getAbsolutePath(), getPlatformImage(), terminal, ()->{
				terminal.exit();
			}));
			terminal.print("Executing " + getInterpreterScriptName(file) + " ... ");
			terminal.print("");
			terminal.setOnProcessExited(()->{
				terminal.print("");
				terminal.print("Process Finished with exit code " + terminal.process.exitValue());
			});
			terminal.start();
			while(terminal.process.isAlive());
				return terminal.process.exitValue();
		}
		return 404;
	}

	public static void executeEventScript(File file, String scriptType){
		Map<String, String> envsX = System.getenv();
		HashMap<String, String> envs = new HashMap<>();
		for(String x : envsX.keySet())
			envs.put(x, envsX.get(x));

		if(!onWindows())
			envs.put("TERM", "xterm-256color");
		
		if(scriptType.equals(FILE_LOADED_EVENT_SCRIPTS_DIR_NAME)){
			if(isFileLoadedEventScriptAvailable(file)){
				try{
					Process p = new PtyProcessBuilder()
					.setCommand(new String[]{ getFileLoadedEventScriptPath(file), file.getAbsolutePath(), file.getName(), file.getParentFile().getAbsolutePath() })
					.setEnvironment(envs)
					.setDirectory(combineToAbsolutePath(ROOT_DIR_NAME, EVENT_SCRIPT_DIR_NAME, FILE_LOADED_EVENT_SCRIPTS_DIR_NAME))
					.setConsole(false)
					.setUseWinConPty(onWindows())
					.start();
					Scanner inputStreamReader = new Scanner(p.getInputStream());
					Scanner errorStreamReader = new Scanner(p.getErrorStream());
					while(p.isAlive()){
						while(inputStreamReader.hasNextLine())
							System.out.println(inputStreamReader.nextLine());
						while(errorStreamReader.hasNextLine())
							System.err.println(errorStreamReader.nextLine());
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		else if(scriptType.equals(FILE_SAVED_EVENT_SCRIPTS_DIR_NAME)){
			if(isFileSavedEventScriptAvailable(file)){
				try{
					Process p = new PtyProcessBuilder()
					.setCommand(new String[]{ getFileSavedEventScriptPath(file), file.getAbsolutePath(), file.getName(), file.getParentFile().getAbsolutePath() })
					.setEnvironment(envs)
					.setDirectory(combineToAbsolutePath(ROOT_DIR_NAME, EVENT_SCRIPT_DIR_NAME, FILE_SAVED_EVENT_SCRIPTS_DIR_NAME))
					.setConsole(false)
					.setUseWinConPty(onWindows())
					.start();
					Scanner inputStreamReader = new Scanner(p.getInputStream());
					Scanner errorStreamReader = new Scanner(p.getErrorStream());
					while(p.isAlive()){
						while(inputStreamReader.hasNextLine())
							System.out.println(inputStreamReader.nextLine());
						while(errorStreamReader.hasNextLine())
							System.err.println(errorStreamReader.nextLine());
					}	
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		else {
			System.out.println(scriptType + " is not available in current version.");
			System.out.println("Must be either 'onFileLoaded' or 'onFileSaved'.");
		}
	}

	public static synchronized void executeEventScripts(String eventType, String eventScriptDir){
		LinkedList<File> files = getAllEventScripts(eventScriptDir);
		if(files.isEmpty())
			return;
		new Thread(()->{
			files.forEach((file)->{
				try{
					Process p = new PtyProcessBuilder()
					.setCommand(new String[]{ file.getAbsolutePath(), eventType })
					.setDirectory(combineToAbsolutePath(ROOT_DIR_NAME, EVENT_SCRIPT_DIR_NAME, eventScriptDir))
					.setConsole(false)
					.setUseWinConPty(onWindows())
					.start();
					Scanner inputStreamReader = new Scanner(p.getInputStream());
					Scanner errorStreamReader = new Scanner(p.getErrorStream());
					while(p.isAlive()){
						while(inputStreamReader.hasNextLine())
							System.out.println(inputStreamReader.nextLine());
						while(errorStreamReader.hasNextLine())
							System.err.println(errorStreamReader.nextLine());
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
			});
		}).start();
	}
	
	public static synchronized void executeEventScriptsAndWait(String eventType, String eventScriptDir){
		LinkedList<File> files = getAllEventScripts(eventScriptDir);
		if(files.isEmpty())
			return;
		files.forEach((file)->{
			try{
				Process p = new PtyProcessBuilder()
				.setCommand(new String[]{ file.getAbsolutePath(), eventType })
				.setDirectory(combineToAbsolutePath(ROOT_DIR_NAME, EVENT_SCRIPT_DIR_NAME, eventScriptDir))
				.setConsole(false)
				.setUseWinConPty(onWindows())
				.start();
				Scanner inputStreamReader = new Scanner(p.getInputStream());
				Scanner errorStreamReader = new Scanner(p.getErrorStream());
				while(p.isAlive()){
					while(inputStreamReader.hasNextLine())
						System.out.println(inputStreamReader.nextLine());
					while(errorStreamReader.hasNextLine())
						System.err.println(errorStreamReader.nextLine());
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		});
	}

}
