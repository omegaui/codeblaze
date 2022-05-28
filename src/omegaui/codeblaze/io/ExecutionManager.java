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
/*
 * ExecutionManager is responsible for launching processes.
 * Processes of File Compilation, Execution, and other Event Scripts get executed from here.
 * To understand how compilation, execution and event-scripts see wiki.
 */
public final class ExecutionManager {
	
	/**
	 * Compiles the file if its corresponding shell script is available.
	 */
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

	/**
	 * Executes the file if its corresponding shell script is available.
	 */
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

	/**
	 * Fires the event script of corresponding file of scriptType.
	 * scriptType can be "onFileSaved", etc,
	 * See the available directories at .codeblaze/event-scripts to know more.
	 */
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

	/**
	 * Fires the event script of corresponding app event of type eventType in a non-blocking way.
	 * eventType can be "onCodeBlazeReady", etc,
	 * See the available directories at .codeblaze/event-scripts to know more.
	 */
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
	
	/**
	 * Fires the event script of corresponding app event of type eventType in a blocking way.
	 * eventType can be "onCodeBlazeReady", etc,
	 * See the available directories at .codeblaze/event-scripts to know more.
	 */
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
