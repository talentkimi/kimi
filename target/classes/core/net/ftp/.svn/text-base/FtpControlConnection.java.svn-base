package core.net.ftp;

import java.io.IOException;

import core.io.file.TextFile;
import core.net.NetSocket;

/**
 * The FTP Command Connection.
 */
class FtpControlConnection {

	private final NetSocket connection;

	public void close() {
		connection.close();
	}

	public boolean isClosed() {
		return connection.isClosed();
	}

	protected FtpResponse response() throws IOException {
		FtpResponse response = new FtpResponse();
		while (true) {
			String line = connection.getReader().readLine();
			System.out.println("[Response] " + line);
			if (!response.addLine(line)) {
				break;
			}
		}
		return response;
	}

	protected final void request(String request) throws IOException {
		request = request.trim();
		System.out.println("[Request] " + request);
		connection.getWriter().write(request);
		connection.getWriter().write(TextFile.newLine);
		connection.getWriter().flush();
	}

	protected final FtpCode execute(String request) throws IOException {
		request(request);
		return response().getCode();
	}

	public FtpControlConnection(String host, int port) throws IOException {
		connection = new NetSocket(host, port, false, 10000);
		FtpResponse response = response();
		if (!response.getCode().isPositiveCompletion()) {
			throw new IOException("FTP connection failed " + host + ":" + port);
		}
	}

}
