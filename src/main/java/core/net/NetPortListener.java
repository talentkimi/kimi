package core.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetPortListener {

	private static final Logger log = LoggerFactory.getLogger(NetPortListener.class);
	
	private ServerSocket serverSocket;

	public NetPortListener(int port) throws IOException {
		this.serverSocket = new ServerSocket(port);
	}

	public void run() {
		while (true) {
			try {

				Socket socket = serverSocket.accept();
				InputStream input = socket.getInputStream();
				while (true) {
					System.out.print((char) input.read());
				}

			} catch (Throwable t) {
				if (log.isErrorEnabled()) log.error(t.getMessage(),t);
			}
		}
	}

	public static void main(String[] args) {
		try {
			int port = Integer.parseInt(args[0]);
			new NetPortListener(port).run();
		} catch (IOException e) {
			if (log.isErrorEnabled()) log.error(e.getMessage(),e);
		}
	}

}
