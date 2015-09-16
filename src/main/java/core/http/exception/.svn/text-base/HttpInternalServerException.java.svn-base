package core.http.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An Internet Server Exception.
 */
public class HttpInternalServerException extends RuntimeException {
	
	private static final Logger log = LoggerFactory.getLogger(HttpInternalServerException.class);
	
	/**
	 * Creates an internal server exception.
	 * @param t the throwable.
	 */
	public HttpInternalServerException(Throwable t) {
		super(t.getMessage());
		if (log.isErrorEnabled()) log.error(t.getMessage(),t);
	}

	/**
	 * Creates an internal server exception.
	 * @param t the throwable.
	 */
	public HttpInternalServerException(String message) {
		super(message);
		if (log.isErrorEnabled()) log.error(message);
	}

}