package core.nioservice.exception;

import java.io.IOException;

public class WriteException extends NioServiceException {

	public WriteException(IOException e) {
		super(e.getMessage(), e);
	}
}
