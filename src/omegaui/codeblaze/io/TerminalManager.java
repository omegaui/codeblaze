package omegaui.codeblaze.io;
import omegaui.codeblaze.ui.component.TerminalComp;

import java.util.LinkedList;

import static omegaui.codeblaze.io.UIXManager.*;

public final class TerminalManager {
	private static LinkedList<TerminalComp> terminals = new LinkedList<>();

	public static void openNewTerminal(){
		TerminalComp terminal = createNewTerminal();
		AppInstanceProvider.getCurrentAppInstance().getProcessPanel().addTab(new TabData("Terminal", getPlatformImage(), terminal, ()->{
			terminal.exit();
		}));
		terminal.setOnProcessExited(()->{
			AppInstanceProvider.getCurrentAppInstance().getProcessPanel().removeTab(terminal);
		});
		terminal.start();
	}

	public static TerminalComp createNewTerminal(){
		TerminalComp terminal = new TerminalComp();
		terminals.add(terminal);
		return terminal;
	}

	public static void closeAllTerminal(){
		terminals.forEach((terminal)->terminal.exit());
	}
}
