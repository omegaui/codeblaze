package omegaui.codeblaze.io;
import omegaui.codeblaze.ui.panel.GlassPanel;
public final class AppStateManager {
	
	public static synchronized void initAppState(){
		
		AppResourceManager.checkResources();
		
		AppInstanceProvider.getCurrentAppInstance().switchViewToGlassPane();

//		AppInstanceProvider.getCurrentAppInstance().switchViewToContentPane();
		
	}
	
}
