package omegaui.codeblaze.ui.component;
public interface ValidationAware {
	void validationPassed();
	void validationFailed();
	void resetValidationChecks();
}
