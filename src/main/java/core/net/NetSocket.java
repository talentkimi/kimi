package core.net;

import java.io.Closeable;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.io.ConnectTimeoutException;
import core.io.StreamReader;
import core.io.StreamWriter;

/**
 * A Network Socket.
 */
public class NetSocket implements Closeable {

	private static final Logger log = LoggerFactory.getLogger(NetSocket.class);


	/** The Network Host Resolver. */
	private static final NetHostResolver HOST_RESOLVER = new NetHostResolver();
	/** The Trust Manager. */
	private static NetTrustManager defaultTrustManager = null;

	public static int sockets = 0;
	public static int connected = 0;
	public static int closed = 0;

	/**
	 * Set the trust manager.
	 * @param manager the manager.
	 */
	public static void setTrustManager(NetTrustManager manager) {
		defaultTrustManager = manager;
	}

	/** The creation time. */
	private final long creationTime;
	/** The socket. * */
	private volatile Socket socket;
	/** The reader stream. * */
	private volatile StreamReader reader;
	/** The writer stream. * */
	private volatile StreamWriter writer;

	/**
	 * Returns the creation time.
	 * @return the creation time.
	 */
	public final long getCreationTime() {
		return creationTime;
	}

	/**
	 * Sets the character set.
	 * @param charset the character set.
	 */
	public final void setCharset(String charset) {
		reader.setCharset(charset);
		writer.setCharset(charset);
	}

	/**
	 * Returns the writer stream.
	 * @return the writer stream.
	 */
	public final StreamWriter getWriter() throws IOException {
		if (writer == null) {
			throw new IOException("connection is closed");
		}
		return writer;
	}

	/**
	 * Returns the reader stream.
	 * @return the reader stream.
	 */
	public final StreamReader getReader() throws IOException {
		if (reader == null) {
			throw new IOException("connection is closed");
		}
		return reader;
	}

	/**
	 * Returns the underlying socket.
	 * @return the underlying socket.
	 */
	public final Socket getSocket() throws IOException {
		if (socket == null) {
			throw new IOException("connection is closed");
		}
		return socket;
	}

	/**
	 * Returns this socket as a secure socket.
	 * @param host the host computer.
	 * @param port the port.
	 * @throws IOException if an IO error occurs creating the secure socket.
	 */
	public NetSocket toSecureSocket(String host, int port, NetTrustManager trustManager) throws IOException {
		SSLSocketFactory factory;
		if (trustManager != null) {
			factory = trustManager.getSocketFactory();
		} else {
			factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
		}
		Socket secureSocket = factory.createSocket(getSocket(), host, port, true);
		NetSocket netSocket = new NetSocket(secureSocket);
		return netSocket;
	}

	/**
	 * Returns this socket as a secure socket.
	 * @param host the host computer.
	 * @param port the port.
	 * @throws IOException if an IO error occurs creating the secure socket.
	 */
	public NetSocket toSecureSocket(String host, int port) throws IOException {
		return toSecureSocket(host, port, defaultTrustManager);
	}

	/**
	 * Returns this socket as a secure socket.
	 * @param url the url.
	 * @throws IOException if an IO error occurs creating the secure socket.
	 */
	public NetSocket toSecureSocket(NetUrl url) throws IOException {
		return toSecureSocket(url.getHost(), url.getPort());
	}

	/**
	 * Creates a new socket wrapped around the given socket.
	 * @param socket the socket.
	 * @throws IOException if an IO error occurs accepting the socket.
	 */
	public NetSocket(Socket socket) throws IOException {
		if (socket == null) {
			throw new NullPointerException();
		}
		// addSocket(socket);
		this.socket = socket;
		this.creationTime = System.currentTimeMillis();
		this.reader = new StreamReader(socket);
		this.writer = new StreamWriter(socket);
	}

	public NetSocket(SocketAddress bindAddress, String host, int port, boolean ssl, int timeout) throws IOException {
		this(newSocket(bindAddress, host, port, ssl, timeout, defaultTrustManager, null));
	}

	public NetSocket(String host, int port, boolean ssl, int timeout) throws IOException {
		this(newSocket(null, host, port, ssl, timeout, defaultTrustManager, null));
	}

	public NetSocket(String host, int port, boolean ssl, int timeout, HttpsProtocol[] protocols) throws IOException {
		this(newSocket(null, host, port, ssl, timeout, defaultTrustManager, protocols));
	}

	/**
	 * Creates a new socket connecting to the host on the given port with the given connection timeout.
	 * @param host the host computer.
	 * @param port the port.
	 * @param ssl true if this connection should be secure.
	 * @param timeout the connection timeout.
	 * @param manager the trust manager.
	 * @throws IOException if an IO error occurs connecting to the host.
	 */
	public NetSocket(String host, int port, boolean ssl, int timeout, NetTrustManager manager, HttpsProtocol[] protocols) throws IOException {
		this(newSocket(null, host, port, ssl, timeout, manager, protocols));
	}

	/**
	 * Creates a new socket connecting to the host on the given port with the given connection timeout.
	 * @param host the host computer.
	 * @param port the port.
	 * @param timeout the connection timeout.
	 */
	private static final Socket newSocket(SocketAddress bindAddress, String host, int port, boolean ssl, int timeout, NetTrustManager trustManager, HttpsProtocol[] protocols) throws IOException {
		long start = System.currentTimeMillis();
		try {
			InetSocketAddress address = HOST_RESOLVER.resolve(host, port);
			int connectTimeout = (int) HOST_RESOLVER.getTimeout(host) * 2;
			start = System.currentTimeMillis();
			for (int i = 0; i < 6; i++) {
				try {
					// if (log.isDebugEnabled()) log.debug ("[Connect Attempt] " + (i + 1) + " " + host + ":" + port);
					Socket socket;
					if (ssl) {
						if (trustManager != null) {
							socket = trustManager.getSocketFactory().createSocket();
						} else {
							socket = SSLSocketFactory.getDefault().createSocket();
						}
						if (protocols != null) {
							final String[] prots = new String[protocols.length];
							for (short j = 0; j < protocols.length; j++)
								prots[j] = protocols[j].name();
							((SSLSocket) socket).setEnabledProtocols(prots);
						}

					} else {
						socket = new Socket(Proxy.NO_PROXY);
					}

					SocketAddress newSocketBindingAddress = getNewSocketBindingAddress();

					if (bindAddress != null) {
						if (log.isDebugEnabled()) log.debug ("[Bind] " + bindAddress);
						socket.bind(bindAddress);
					} else if (newSocketBindingAddress != null) {
						if (log.isDebugEnabled()) log.debug ("[Bind new Socket IP] " + newSocketBindingAddress);
						socket.bind(newSocketBindingAddress);
					}
					socket.connect(address, connectTimeout);
					long finish = System.currentTimeMillis();
					HOST_RESOLVER.setTimeout(host, finish - start);
					// if (log.isDebugEnabled()) log.debug ("[Connected] " + address + " (" + (finish - start) + " millis) after " + i + " retries");
					// if (log.isDebugEnabled()) log.debug ("[Connect Success] " + host + ":" + port + " (" + connectTimeout + ")");
					return socket;
				} catch (SocketTimeoutException ste) {
					long finish = System.currentTimeMillis();
					if (timeout < finish - start) {
						break;
					}
					connectTimeout *= 3;
					// if (log.isDebugEnabled()) log.debug ("[Connect Retry] " + host + ":" + port + " (" + connectTimeout + ")");
				}
			}
		} catch (IOException ioe) {
			throw new SocketException(host + ":" + port + " (secure? " + ssl + ") " + ioe.getMessage());
		}
		long finish = System.currentTimeMillis();
		throw new ConnectTimeoutException(host + ":" + port + " (secure? " + ssl + ") " + ((finish - start)/1000) + " secs timedout");
	}

	private final static SocketAddress getNewSocketBindingAddress() throws UnknownHostException {
		SocketAddress bindingAddress = null;
//		String ipAddressString = TripPlannerServerConfig.getServerConnectIp();
		String ipAddressString = null;
		if (ipAddressString != null && !ipAddressString.equals("")) {
			IpAddress ipAddress = new IpAddress(ipAddressString);
			byte[] rawIpAddress = new byte[4];
			rawIpAddress[0] = (byte) ipAddress.getPart1();
			rawIpAddress[1] = (byte) ipAddress.getPart2();
			rawIpAddress[2] = (byte) ipAddress.getPart3();
			rawIpAddress[3] = (byte) ipAddress.getPart4();
			bindingAddress = new InetSocketAddress(InetAddress.getByAddress(rawIpAddress), 0);
		}
		return bindingAddress;
	}

	/**
	 * Set the read timeout.
	 * @param timeoutInMillis the timeout.
	 */
	public final void setReadTimeout(int timeoutInMillis) throws SocketException {
		socket.setSoTimeout(timeoutInMillis);
	}

	public final boolean isClosed() {
		return socket == null || socket.isClosed();
	}

	/**
	 * Close this socket.
	 */
	@Override
	public void close() {
		
		// A little bit paranoia, but guarantee that no NullPointerException will be thrown in multithreading environment.
		final Socket s = socket;
		if (s != null) {
			socket = null;
			try {
				s.close();
			} catch (IOException ioe) {
				if (log.isErrorEnabled()) log.error(ioe.getMessage(),ioe);
			}
		}

		final StreamReader r = reader;
		if (r != null) {
			reader = null;
			try {
				r.close();
			} catch (IOException ioe) {
				if (log.isErrorEnabled()) log.error(ioe.getMessage(),ioe);
			}
		}
		
		final StreamWriter w = writer;
		if (w != null) {
			writer = null;
			try {
				w.close();
			} catch (IOException ioe) {
				if (log.isErrorEnabled()) log.error(ioe.getMessage(),ioe);
			}
		}
	}

}
