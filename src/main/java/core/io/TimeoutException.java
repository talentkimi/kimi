package core.io;

import java.io.IOException;

/**
 * General superclass for timeout-exceptions.
 * Created for simpler management of WebSpiderStage.handleIOThrowable. 
 * @author Dimitrijs
 */
public class TimeoutException extends IOException {
	private static final long serialVersionUID = 1L;

	public TimeoutException() {
		super();
	}

	public TimeoutException(String message, Throwable cause) {
		super(message, cause);
	}

	public TimeoutException(String message) {
		super(message);
	}

	public TimeoutException(Throwable cause) {
		super(cause);
	}
}
