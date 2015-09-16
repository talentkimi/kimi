package core.nioservice.exception;

public class NioServiceException extends Exception {

	public NioServiceException(String message) {
		super(message);
	}

	public NioServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
