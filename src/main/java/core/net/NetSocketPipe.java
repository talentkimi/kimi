package core.net;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Network Socket Pipe.
 */
public class NetSocketPipe extends NetServerSocket {

	private static final Logger log = LoggerFactory.getLogger(NetSocketPipe.class);


	/** The host. */
	private final String host;
	/** The port. */
	private final int port;

	/**
	 * Creates a new socket pipe.
	 * @param port the port.
	 * @param connections the connections.
	 */
	public NetSocketPipe(String remoteHost, int remotePort, int localPort, int connections) throws IOException {
		super(localPort, connections);
		this.host = remoteHost;
		this.port = remotePort;
	}

	/**
	 * Returns a new handler.
	 * @return a new handler.
	 */
	protected NetSocketHandler newHandler() {
		try {
			NetSocket remote = new NetSocket(host, port, false, 10000);
			return new NetSocketPipeHandler(remote);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		try {
			new NetSocketPipe("localmail.travelfusion.com", 25, 12345, 20);
			if (log.isDebugEnabled()) log.debug ("New server");
		} catch (Exception e) {
			if (log.isErrorEnabled()) log.error(e.getMessage(),e);
		}
	}

	@Override
	public void runTask() throws Throwable {
		// TODO Auto-generated method stub
		
	}

}

class NetSocketPipeHandler extends NetSocketHandler {

	private static final Logger log = LoggerFactory.getLogger(NetSocketPipeHandler.class);


	/** The remote. */
	private NetSocket remote;

	/**
	 * Creates a new pipe.
	 * @param remote the server.
	 */
	public NetSocketPipeHandler(NetSocket remote) {
		this.remote = remote;
	}

	/**
	 * Handle the socket.
	 */
	protected void handleSocket() throws IOException {
		try {
			NetSocket local = getSocket();
			new NetSocketPipeThread(local, remote).start();
			new NetSocketPipeThread(remote, local).start();
		} finally {
			remote.close();
		}
	}

}
