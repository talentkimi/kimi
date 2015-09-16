package core.net.ftp;

import java.io.IOException;
import java.net.ServerSocket;

import core.io.StreamReader;
import core.net.NetSocket;

class FtpDataConnection implements FtpCommands {

	public static FtpDataConnection connect(Ftp ftp, FtpMode mode) throws IOException {
		switch (mode) {
			case ACTIVE :
				ServerSocket server = new ServerSocket(0);
				FtpInetAddress address1 = new FtpInetAddress(server.getInetAddress().getLocalHost(), server.getLocalPort());
				ftp.getControl().execute(SET_TRANSFER_TYPE + " I");
				ftp.getControl().execute(SET_ACTIVE_MODE + ' ' + address1);
				return new FtpDataConnection(server);
			case PASSIVE :
				ftp.getControl().request(SET_PASSIVE_MODE);
				FtpResponse response = ftp.getControl().response();
				if (!response.getCode().isPositiveCompletion()) {
					return null;
				}
				ftp.getControl().execute(SET_TRANSFER_TYPE + " I");
				String line = response.getLastLine();
				int open = line.indexOf('(');
				int close = line.indexOf(')', open);
				FtpInetAddress address2 = FtpInetAddress.parseFtpInetAddress(line.substring(open + 1, close));
				NetSocket socket = new NetSocket(address2.getAddress().getHostAddress(), address2.getPort(), false, 10000);
				return new FtpDataConnection(socket);
		}
		throw new IllegalArgumentException("unknown mode: '" + mode + "'");
	}

	private final ServerSocket server;
	private NetSocket socket = null;

	private final synchronized NetSocket getSocket() throws IOException {
		if (socket == null) {
			socket = new NetSocket(server.accept());
		}
		return socket;
	}
	
	public StreamReader downloadReader() throws IOException {
		return getSocket().getReader();
		
	}
	public byte[] download() throws IOException {
		long timeStarted = System.currentTimeMillis();
		byte[] data = getSocket().getReader().readToByteArray();
		long timeFinished = System.currentTimeMillis();
		System.out.println("[Download] " + data.length + " bytes in " + (timeFinished - timeStarted) + " millis");
		return data;
	}

	public void upload(byte[] data) throws IOException {
		long timeStarted = System.currentTimeMillis();
		getSocket().getWriter().write(data);
		getSocket().getWriter().flush();
		long timeFinished = System.currentTimeMillis();
		System.out.println("[Upload] " + data.length + " bytes in " + (timeFinished - timeStarted) + " millis");
	}

	public void close() throws IOException {
		if (socket != null) {
			socket.close();
		}
		if (server != null) {
			server.close();
		}
	}

	private FtpDataConnection(NetSocket socket) {
		this.server = null;
		this.socket = socket;
	}

	private FtpDataConnection(ServerSocket server) {
		this.server = server;
		this.socket = null;
	}
}