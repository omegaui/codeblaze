package omegaui.codeblaze.io;
import omegaui.codeblaze.App;
public final class AppInstanceProvider {
	private static App currentAppInstance;

	public static omegaui.codeblaze.App getCurrentAppInstance() {
		return currentAppInstance;
	}
	
	public static void setCurrentAppInstance(omegaui.codeblaze.App currentAppInstance) {
		AppInstanceProvider.currentAppInstance = currentAppInstance;
	}
	
}
