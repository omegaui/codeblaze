package omegaui.codeblaze.io;
public final class AppDataProvider {
	public static final String appVersionSemantic(){
		return appVersion() + "-" + buildState();
	}

	public static final float appVersion(){
		return 1.0f;
	}

	public static final String buildState(){
		return "alpha";
	}
}
