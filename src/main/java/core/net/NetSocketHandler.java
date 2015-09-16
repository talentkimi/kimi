package core.net;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import core.lang.thread.ThreadPool.Poolable;

/**
 * A Network Socket Handler.
 */
public abstract class NetSocketHandler implements Poolable {
	
	private static final Logger log = LoggerFactory.getLogger(NetSocketHandler.class);
	
	/** The socket. * */
	private NetSocket socket = null;
	/** The server socket. * */
	private NetServerSocket server = null;
	/** The connection number. * */
	private int connectionNumber = -1;
	/** The start time */
	private long startTime = -1;

//	protected ConnectionRecord myConnectionRecord = null;

	@Override
	public String getPoolableName() {
		return getClass().getSimpleName();
	}

	@Override
	public long getPoolableStartTime() {
		return startTime;
	}

	/**
	 * Returns the socket.
	 * @return the socket.
	 */
	public NetSocket getSocket() {
		return socket;
	}

	/**
	 * Returns the server socket.
	 * @return the server socket.
	 */
	public NetServerSocket getServer() {
		return server;
	}

	/**
	 * Returns the connection number.
	 * @return the connection number.
	 */
	public int getConnectionNumber() {
		return connectionNumber;
	}

	/**
	 * Sets the socket.
	 * @param socket the socket.
	 */
	public void setSocket(NetSocket socket) {
		if (socket == null) {
			throw new NullPointerException();
		}
		this.socket = socket;
	}

	/**
	 * Sets the server socket.
	 * @param server the server socket.
	 */
	public void setServer(NetServerSocket server) {
		if (server == null) {
			throw new NullPointerException();
		}
		this.server = server;
	}

	/**
	 * Sets the connection number.
	 * @param number the connection number.
	 */
	public void setConnectionNumber(int number) {
		if (number < 1) {
			throw new IllegalArgumentException("number=" + number);
		}
		this.connectionNumber = number;
	}

	/**
	 * Runs the handler.
	 */
	public final void run() {
		try {
			startTime = System.currentTimeMillis();
			server.handlerStarted();
			long preHandleTime = System.currentTimeMillis();
			handleSocket();
			long theTime = System.currentTimeMillis() - preHandleTime;
//			QuickStatsEngine2.engine.XML_TIME_INCL_IO.logEvent(theTime);
//			ConnectionMonitor.reportSocketFinished(myConnectionRecord);
		} catch (Throwable t) {
//			ConnectionMonitor.reportSocketThrowable(myConnectionRecord);
			handleThrowable(t);
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
			} finally {
				server.handlerFinished();
				server = null;
			}
		}
	}

	/*
	 * private static int memLogCounter = 0; private void logMemoryLevels() { try { memLogCounter++;
	 * if (memLogCounter > 30) { QuickStatsEmailer.logQuickStatEventAmount("Memory", Memory.used(),
	 * 3, 1000000); memLogCounter = 0; } } catch (Throwable t) {} }
	 */
	/**
	 * Handles the given throwable.
	 * @param t the throwable.
	 */
	protected void handleThrowable(Throwable t) {
		if (log.isErrorEnabled()) log.error("Throwable in NSH RUN: "+ t.getMessage(),t);
	}

	/**
	 * Handle the socket.
	 * @throws IOException if an IO error occurs reading or writing to the socket.
	 */
	protected abstract void handleSocket() throws IOException;

//	public void setConnectionRecord(ConnectionRecord connectionRecord) {
//		myConnectionRecord  = connectionRecord;
//	}

}