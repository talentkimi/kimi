package core.io;

import java.io.IOException;

/**
 * An error wrapper used to distinguish proxy server side errors from the others
 */
public class ProxyServerSideException extends IOException {

	private static final long serialVersionUID = 5807463908157918763L;

	/**
	 * Constructor
	 * @param message
	 * @param cause
	 */
	public ProxyServerSideException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor
	 * @param message
	 */
	public ProxyServerSideException(String message) {
		super(message);
	}
}