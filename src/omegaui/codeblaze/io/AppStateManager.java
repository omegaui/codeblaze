package omegaui.codeblaze.io;
public final class AppStateManager {
	
	public static synchronized void initAppState(){
		
		AppInstanceProvider.getCurrentAppInstance().switchViewToGlassPane();

//		AppInstanceProvider.getCurrentAppInstance().switchViewToContentPane();
		
	}
	
}
