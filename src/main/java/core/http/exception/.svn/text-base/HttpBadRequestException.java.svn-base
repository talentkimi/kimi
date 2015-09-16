package core.http.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Bad Request Exception.
 */
public class HttpBadRequestException extends HttpException {
	
	private static final Logger log = LoggerFactory.getLogger(HttpBadRequestException.class);
	
	/**
	 * Creates a new bad request exception.
	 * @param message the message.
	 */
	public HttpBadRequestException(String message) {
		super(CODE_400, null, message, null);
	}

	/**
	 * Creates a new bad request exception.
	 * @param t the throwable.
	 */
	public HttpBadRequestException(Throwable t) {
		this(t.getMessage());
		if (log.isErrorEnabled()) log.error(t.getMessage(),t);
	}

}
