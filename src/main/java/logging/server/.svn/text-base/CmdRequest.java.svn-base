package logging.server;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

import logging.StreamUtils;


public abstract class CmdRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	private final String host;
	private final int port;
	
	public CmdRequest(String host, int port) {
		super();
		this.host = host;
		this.port = port;
	}

	/**
	 * Sends itself to remote server (using serialisation) for processing.
	 * This method should be called by implementations to process request remotely. 
	 * @return Instance of {@code CmdResponseOk} if processing was successful. Instance of {@code CmdResponseFailure} otherwise.
	 * @throws IOException if exception occurs. 
	 */
	public CmdResponse send() throws IOException {
		final long t1 = System.nanoTime();
		final Socket socket = new Socket(host, port);
		try {
			StreamUtils.writeObject(socket, this);
			final CmdResponse result = (CmdResponse) StreamUtils.readObject(socket);
			result.setProcessingTime(System.nanoTime() - t1);
			return result;
		} catch (ClassNotFoundException ex) {
			throw new IOException(ex);
		} finally {
			socket.close();
		}
	}
	
	/**
	 * Called by engine on remote server.
	 * @return Instance of {@code CmdResponseOk} if processing was successful. Instance of {@code CmdResponseFailure} otherwise.
	 */
	public abstract CmdResponse process();
	
}
