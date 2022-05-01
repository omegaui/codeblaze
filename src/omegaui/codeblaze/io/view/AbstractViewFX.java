package omegaui.codeblaze.io.view;
import javafx.scene.Parent;
public abstract class AbstractViewFX {
	public AbstractViewFX(){
		init();
	}

	public abstract void init();
	public abstract Parent getRoot();
}
