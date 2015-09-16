package engine;

/**
 * A wrapper used to distinguish stage loading errors from others
 * @see StageClassLoader
 */
public class StageClassLoadingException extends Exception {

	private static final long serialVersionUID = 1496025062321534886L;

	/**
	 * Constructor
	 * @param message
	 */
	public StageClassLoadingException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * @param throwable
	 */
	public StageClassLoadingException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Constructor
	 * @param message
	 * @param throwable
	 */
	public StageClassLoadingException(String message, Throwable throwable) {
		super(message, throwable);
	}
}