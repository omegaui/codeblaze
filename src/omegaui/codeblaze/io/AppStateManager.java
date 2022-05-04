package omegaui.codeblaze.io;
public final class AppStateManager {
	
	public static synchronized void initAppState(){
		
		AppResourceManager.checkResources();
		
		AppInstanceProvider.getCurrentAppInstance().getGlassPane().setVisible(true);
		
	}
	
}
