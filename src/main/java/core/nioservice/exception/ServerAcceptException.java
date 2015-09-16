package core.nioservice.exception;

import java.io.IOException;

public class ServerAcceptException extends NioServiceException {

	public ServerAcceptException(IOException e) {
		super(e.getMessage(), e);
	}
}
