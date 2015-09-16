package core.nioservice;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.nioservice.exception.ClientConnectException;
import core.nioservice.exception.NioServiceException;
import core.nioservice.exception.ReadEOSException;
import core.nioservice.exception.ReadException;
import core.nioservice.exception.WriteException;

/**
 * Similar to NioServer, except that it initiates connections instead of
 * accepting.
 *
 */
public abstract class NioClient extends NioService {

	private static final Logger log = LoggerFactory.getLogger(NioClient.class);

	public NioClient(InetAddress hostAddress, int port, Executor executor, NioHandlerManager handlerManager) throws IOException {
		super(hostAddress, port, executor, handlerManager);
	}

	/**
	 * Use this method to send data to the server
	 * @param data
	 * @param handler
	 * @throws ClientConnectException
	 */
	public void send(byte[] data, NioHandler handler) throws ClientConnectException {

		if (shuttingDown.get()) {
			throw new ClientConnectException("Client is shutting down - Cannot initiate new connections");
		}

		// Start a new connection
		SocketChannel socketChannel = null;
		try {
			socketChannel = this.initiateConnection(handler);
			if (log.isDebugEnabled()) log.debug("NIO Client - Opened connection - " + socketChannel);
		} catch (IOException e) {
			throw new ClientConnectException(e);
		}

		// Register the response handler
		handlerManager.registerHandler(socketChannel, handler);

		// And queue the data we want written
		synchronized (this.pendingData) {
			List<ByteBuffer> queue = (List<ByteBuffer>) this.pendingData.get(socketChannel);
			if (queue == null) {
				queue = new ArrayList<ByteBuffer>();
				this.pendingData.put(socketChannel, queue);
			}
			queue.add(ByteBuffer.wrap(data));
		}

		// Queue a channel registration since the caller is not the 
		// selecting thread. As part of the registration we'll register
		// an interest in connection events. These are raised when a channel
		// is ready to complete connection establishment.
		synchronized(this.pendingChanges) {
			this.pendingChanges.add(new ChangeRequest(socketChannel, ChangeRequest.REGISTER, SelectionKey.OP_CONNECT));
		}

		// Finally, wake up our selecting thread so it can make the required changes
		this.selector.wakeup();
	}

	@Override
	protected void runService() {

		//long t0, t1, t2, t3, t4, t5;
		int count = 0;

		while (running) {

			//t0 = System.currentTimeMillis();

			// Process any pending changes
			synchronized (this.pendingChanges) {
				// TODO: QS pendingChanges.size()
				Iterator<ChangeRequest> changes = this.pendingChanges.iterator();
				while (changes.hasNext()) {
					ChangeRequest change = (ChangeRequest) changes.next();
					switch (change.type) {
					case ChangeRequest.CHANGEOPS:
						SelectionKey key = change.socket.keyFor(this.selector);
						if (key != null && key.isValid()) {
							try {
								key.interestOps(change.ops);
							} catch (CancelledKeyException e) {
								if (log.isErrorEnabled()) {
									log.error(getServiceName() + key, e);
								}
//								QuickStatsEngine2.engine.MISC_ERRORS.logEvent(getServiceName() + " CancelledKeyException");
							}
						}
						break;
					case ChangeRequest.REGISTER:
						try {
							change.socket.register(this.selector, change.ops);
						} catch (ClosedChannelException e) {
							if (log.isErrorEnabled()) {
								log.error(getServiceName(), e);
							}
//							QuickStatsEngine2.engine.MISC_ERRORS.logEvent(getServiceName() + " ClosedChannelException");
						}
						break;
					case ChangeRequest.SHUTDOWN:
						if (log.isWarnEnabled()) {
							log.warn("NioClient ChangeRequest.SHUTDOWN shuttingDown=" + shuttingDown.get() + ", running=" + running);
						}
						// Do nothing
						break;
					}
				}
				this.pendingChanges.clear();
			}

			//t1 = System.currentTimeMillis();
			// TODO: QS t1 - t0 (pending changes process time)

			// TODO: QS selector.keys().size()

			// Wait for an event one of the registered channels
			try {
				this.selector.select(3000);
			} catch (IOException e) {
				if (log.isErrorEnabled()) {
					log.error("EXCEPTION IN NIO SELECTOR: " + e.getMessage(), e);
				}
//				QuickStatsEngine2.engine.MISC_ERRORS.logEvent(getServiceName() + " Selector exception");
				continue;
			} finally {
				//t2 = System.currentTimeMillis();
				// TODO: QS t2 - t1 (select time)
			}

			Set<SelectionKey> selectedKeySet = selector.selectedKeys();
			// TODO: QS selectedKeySet.size()

			int numInvalidKeys = 0;

			// Iterate over the set of keys for which events are available
			Iterator<SelectionKey> selectedKeys = selectedKeySet.iterator();
			while (selectedKeys.hasNext()) {

				//t3 = System.currentTimeMillis();

				SelectionKey key = (SelectionKey) selectedKeys.next();
				selectedKeys.remove();

				if (!key.isValid()) {
					numInvalidKeys++;
					continue;
				}

				NioHandler handler = handlerManager.getHandler((SocketChannel) key.channel());

				try {
					// Check what event is available and deal with it
					if (key.isConnectable()) {
						this.finishConnection(key, handler);
					} else if (key.isReadable()) {
						this.read(key);
					} else if (key.isWritable()) {
						this.write(key, handler);
					}
				} catch (NioServiceException e) {
					if (log.isErrorEnabled()) {
						log.error("EXCEPTION IN " + getServiceName() + ": " + e.getMessage(), e);
					}
//					QuickStatsEngine2.engine.MISC_ERRORS.logEvent(getServiceName() + " " + e.getClass().getSimpleName() + " " + e.getMessage());
					SocketChannel socketChannel = (SocketChannel) key.channel();
					if (socketChannel == null) {
						key.cancel();
					} else {
						synchronized (this.pendingChanges) {
							// Indicate we want the interest ops set changed
							this.pendingChanges.add(new ChangeRequest(socketChannel, ChangeRequest.CHANGEOPS, 0));
						}
					}
					handler.setException(e);
					executor.execute(new HandlerFutureTask(handler));
				}

				//t4 = System.currentTimeMillis();
				// TODO: QS t4 - t3 (time taken to process a selection key)
			}

			//t5 = System.currentTimeMillis();
			// TODO: QS t5 - t2 (time taken to process all selection keys)

			// TODO: QS numInvalidKeys
			// TODO: QS selectedKeySet.size() / numInvalidKeys ratio

			if (numInvalidKeys > 5 && log.isWarnEnabled()) {
				log.warn("NIO " + getServiceName() + " numInvalidKeys: " + numInvalidKeys);
			}
            if(true){
//			if (ScheduledQuickStatsLogger.quickStatsLoggingIsEnabled) {
				nioActiveCount = getActiveCount();
				nioPoolSize = getPoolSize();
				if (nioActiveCount > 10 && log.isWarnEnabled()) {
					log.warn(">>> NioClient active handler threads: " + nioActiveCount + " Pool size: " + nioPoolSize);
				}
				nioQueueSize = getQueueSize();
				nioSocketChannels = selector.keys().size();
				if (handlerManager.getHandlers().size() > 500) {
					if (log.isWarnEnabled()) {
						log.warn(">>> NioClient handlers: " + handlerManager.getHandlers().size() + " keys:" + nioSocketChannels);
					}
//					QuickStatsEngine2.engine.MISC_ERRORS.logEvent("NioClient handlers: Large Number");					
				}
			}

			// Cleanup pendingData map
			cleanupPendingDataMap();

			if (count++ > 300000) {
				logServiceStats();
				count = 0;
			}

			if (shuttingDown.get() && log.isWarnEnabled()) {
				logServiceStats();
				logShuttingDownKeys();
				log.warn(getServiceName() + " shutting down, keys: " + selector.keys().size());
			}

			if (shuttingDown.get() && selector.keys().isEmpty()) {
				running = false;
				if (log.isWarnEnabled()) {
					log.warn("NioClient running: false");
				}
			}
		}
	}

	private static int nioActiveCount = 0;
	private static int nioPoolSize = 0;
	private static int nioQueueSize = 0;
	private static int nioSocketChannels = 0;
	public static int getNioActiveCount() {
		return nioActiveCount;
	}
	public static int getNioPoolSize() {
		return nioPoolSize;
	}
	public static int getNioQueueSize() {
		return nioQueueSize;
	}
	public static int getNioSocketChannels() {
		return nioSocketChannels;
	}

	private void read(SelectionKey key) throws NioServiceException {

		SocketChannel socketChannel = (SocketChannel) key.channel();

		// Clear out our read buffer so it's ready for new data
		this.readBuffer.clear();

		// Attempt to read off the channel
		int numRead;
		try {
			numRead = socketChannel.read(this.readBuffer);
		} catch (IOException e) {
			// The remote entity forcibly closed the connection
			throw new ReadException(e);
		}

		if (numRead == -1) {
			// Remote entity shut the socket down cleanly. Do the
			throw new ReadEOSException("End of stream reached");
		}

		// Skip if there's nothing to read in the channel
		if (numRead > 0) {
			// Handle the response
			this.handleResponse(socketChannel, this.readBuffer.array(), numRead);
		}
	}

	private void handleResponse(SocketChannel socketChannel, byte[] data, int numRead) {

		// Hand the data off to our worker thread
		NioHandler handler = handlerManager.getHandler(socketChannel);
		handler.processData(data, numRead);

		synchronized (this.pendingChanges) {
			// Indicate we want the interest ops set changed
			this.pendingChanges.add(new ChangeRequest(socketChannel, ChangeRequest.CHANGEOPS, 0));
		}
		executor.execute(new HandlerFutureTask(handler));
	}

	private void write(SelectionKey key, NioHandler handler) throws NioServiceException {

		SocketChannel socketChannel = (SocketChannel) key.channel();

		synchronized (this.pendingData) {
			List<ByteBuffer> queue = (List<ByteBuffer>) this.pendingData.get(socketChannel);
			try {
				// Write until there's not more data ...
				while (!queue.isEmpty()) {
					ByteBuffer buf = (ByteBuffer) queue.get(0);
					socketChannel.write(buf);
					if (buf.remaining() > 0) {
						// ... or the socket's buffer fills up
						break;
					}
					queue.remove(0);
				}
			} catch (IOException e) {
				queue.remove(0);
				throw new WriteException(e);
			}

			if (queue.isEmpty()) {
				if (log.isDebugEnabled()) log.debug("NIO Client - Finished writing - " + socketChannel);
				// Notify handler of successful write
				if (handler != null) {
					handler.writeFinished();
				}
				// All data is writen away; Switch back to waiting for data
				key.interestOps(SelectionKey.OP_READ);
			}
		}
	}

	private void finishConnection(SelectionKey key, NioHandler handler) throws ClientConnectException {

		SocketChannel socketChannel = (SocketChannel) key.channel();

		// Finish the connection. If the connection operation failed
		// this will raise an IOException
		try {
			socketChannel.finishConnect();
			if (log.isDebugEnabled()) log.debug("NIO Client - Finished opening connection - " + socketChannel);
		} catch (IOException e) {
			// Cancel the channel's registration with our selector
			//key.cancel();
			throw new ClientConnectException(e);
		}

		// Notify handler of successful connection
		if (handler != null) {
			handler.connectionEstablished();
		}

		// Register an interest in writing on this channel
		key.interestOps(SelectionKey.OP_WRITE);
	}

	protected SocketChannel initiateConnection(NioHandler handler) throws IOException {
		return initiateConnection(this.hostAddress, this.port);
	}

	protected SocketChannel initiateConnection(InetAddress host, int port) throws IOException {
		// Create a non-blocking socket channel
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.configureBlocking(false);

		// Kick off connection establishment
		socketChannel.connect(new InetSocketAddress(host, port));

		return socketChannel;
	}

	@Override
	protected Selector initSelector() throws IOException {
		// Create a new selector
		return SelectorProvider.provider().openSelector();
	}
}
