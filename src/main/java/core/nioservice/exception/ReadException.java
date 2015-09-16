package core.nioservice.exception;

import java.io.IOException;

public class ReadException extends NioServiceException {

	public ReadException(IOException e) {
		super(e.getMessage(), e);
	}
}
