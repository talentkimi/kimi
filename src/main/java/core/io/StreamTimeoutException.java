package core.io;


/**
 * Stream Timeout Exception
 */
final public class StreamTimeoutException extends TimeoutException {

	static final long serialVersionUID = 7311081720392390179L;

	/**
	 * Constructor
	 */
	public StreamTimeoutException() {
		super();
	}

	/**
	 * Constructor
	 * @param message
	 */
	public StreamTimeoutException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * @param cause
	 */
	public StreamTimeoutException(Throwable cause) {
		super(cause);
	}
}