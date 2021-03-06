package core.nioservice;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.nioservice.exception.NioServiceException;
import core.nioservice.exception.ReadEOSException;
import core.nioservice.exception.ReadException;
import core.nioservice.exception.ServerAcceptException;
import core.nioservice.exception.WriteException;

/**
 * This class contains server NIO functionality. Connections are accepted on a
 * server socket channel. All IO is performed by the selecting thread itself.
 * The 'select loop' in the runService() method is where most of the action
 * happens.
 * 
 * The selecting thread sits in a loop waiting until one of the channels
 * registered with the selector is in a state that matches the interest
 * operations we've registered for it. The interests ops on a channel may
 * cycle in the following manner.
 * 
 * SelectionKey.OP_ACCEPT
 * SelectionKey.OP_READ
 * SelectionKey.OP_WRITE
 * 
 * When some data has been read, it is handed off to a worker (NioHandler).
 *
 */
public abstract class NioServer extends NioService {

	private static final Logger log = LoggerFactory.getLogger(NioServer.class);

	// The channel on which we'll accept connections
	protected ServerSocketChannel serverChannel;
	protected SelectionKey serverChannelKey;

	public NioServer(InetAddress hostAddress, int port, Executor executor, NioHandlerManager handlerManager) throws IOException {
		super(hostAddress, port, executor, handlerManager);
	}

	/**
	 * Use this method to send data back to the client
	 * @param socket
	 * @param data
	 */
	public void send(SocketChannel socket, byte[] data) {
		synchronized (this.pendingChanges) {
			// Indicate we want the interest ops set changed
			this.pendingChanges.add(new ChangeRequest(socket, ChangeRequest.CHANGEOPS, SelectionKey.OP_WRITE));

			// And queue the data we want written
			synchronized (this.pendingData) {
				List<ByteBuffer> queue = (List<ByteBuffer>) this.pendingData.get(socket);
				if (queue == null) {
					queue = new ArrayList<ByteBuffer>();
					this.pendingData.put(socket, queue);
				}
				queue.add(ByteBuffer.wrap(data));
			}
		}

		// Finally, wake up our selecting thread so it can make the required changes
		this.selector.wakeup();
	}

	public void runService() {

		//long t0, t1, t2, t3, t4, t5;
		int count = 0;

		while (running) {

			//t0 = System.currentTimeMillis();

			// Process any pending changes
			synchronized (pendingChanges) {
				// TODO: QS pendingChanges.size()
				Iterator<ChangeRequest> changes = pendingChanges.iterator();
				while (changes.hasNext()) {
					ChangeRequest change = (ChangeRequest) changes.next();
					switch (change.type) {
					case ChangeRequest.CHANGEOPS:
						SelectionKey key = change.socket.keyFor(selector);
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
					case ChangeRequest.SHUTDOWN:
						if (log.isWarnEnabled()) {
							log.warn("NioServer ChangeRequest.SHUTDOWN shuttingDown=" + shuttingDown.get() + ", running=" + running);
						}
						if (serverChannelKey != null && serverChannelKey.isValid()) {
							serverChannelKey.cancel();
						}
						break;
					case ChangeRequest.SERVER_DISABLE:
					case ChangeRequest.SERVER_ENABLE:
						if (log.isWarnEnabled()) {
							log.warn("NioServer " + change.type + " shuttingDown=" + shuttingDown.get() + ", running=" + running);
						}
						if (serverChannelKey != null && serverChannelKey.isValid()) {
							serverChannelKey.interestOps(change.ops);
							if (log.isWarnEnabled()) {
								log.warn("NioServer serverChannelKey.interestOps=" + change.ops);
							}
						} else {
							if (log.isWarnEnabled()) {
								log.warn("NioServer FAILED key: " + serverChannelKey + (serverChannelKey == null? "": " valid=" + serverChannelKey.isValid()));
							}
						}
						break;
					}
				}
				pendingChanges.clear();
			}

			//t1 = System.currentTimeMillis();
			// TODO: QS t1 - t0 (pending changes process time)

			// TODO: QS number of all channels/connections - selector.keys().size()

			// Wait for an event one of the registered channels
			try {
				selector.select(3000);
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

				NioHandler handler = null;
				// Get handler for non-server channels only (An 'isAcceptable' key must be a server socket channel).
				if (!key.isAcceptable()) {
					handler = handlerManager.getHandler((SocketChannel) key.channel());
				}

				try {
					// Check what event is available and deal with it
					if (key.isAcceptable()) {
						// TODO: If we are going to use a ConnectionRecord for connection monitor it should go somewhere here
						this.accept(key);
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
					if (handler != null) {
						handler.setException(e);
						executor.execute(new HandlerFutureTask(handler));
					}
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
					log.warn(">>> NioServer active handler threads: " + nioActiveCount + " Pool size: " + nioPoolSize);
				}
				nioQueueSize = getQueueSize();
				nioSocketChannels = selector.keys().size();
				if (handlerManager.getHandlers().size() > 500) {
					if (log.isWarnEnabled()) {
						log.warn(">>> NioServer handlers: " + handlerManager.getHandlers().size() + " keys:" + nioSocketChannels);
					}
//					QuickStatsEngine2.engine.MISC_ERRORS.logEvent("NioServer handlers: Large Number");					
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
					log.warn("NioServer running: false");
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

	private void accept(SelectionKey key) throws ServerAcceptException {

		long acceptStartTime = System.currentTimeMillis();

		// For an accept to be pending the channel must be a server socket channel.
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

		SocketChannel socketChannel = null;

		try {

			// Accept the connection and make it non-blocking
			socketChannel = serverSocketChannel.accept();
			Socket socket = socketChannel.socket();
			socketChannel.configureBlocking(false);

			// Register the new SocketChannel with our Selector, indicating
			// we'd like to be notified when there's data waiting to be read
			socketChannel.register(this.selector, SelectionKey.OP_READ);

		} catch (IOException e) {
			throw new ServerAcceptException(e);
		}

		if (log.isDebugEnabled()) log.debug("NIO Server - Accepted connection - " + socketChannel);

		handlerManager.newHandler(socketChannel, acceptStartTime);
	}

	private void read(SelectionKey key) throws NioServiceException {

		SocketChannel socketChannel = (SocketChannel) key.channel();
		NioHandler handler = handlerManager.getHandler(socketChannel);

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
			// Remote entity shut the socket down cleanly
			throw new ReadEOSException("End of stream reached");
		}

		// Skip if there's nothing to read in the channel
		if (numRead > 0) {
			// Hand the data off to our worker thread
			handler.processData(this.readBuffer.array(), numRead);

			synchronized (this.pendingChanges) {
				// Indicate we want the interest ops set changed
				this.pendingChanges.add(new ChangeRequest(socketChannel, ChangeRequest.CHANGEOPS, 0));
			}
			executor.execute(new HandlerFutureTask(handler));
		}
	}

	private void write(SelectionKey key, NioHandler handler) throws WriteException {

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
				if (log.isDebugEnabled()) log.debug("NIO Server - Finished writing - " + socketChannel);
				writeFinished(socketChannel, handler);
			}
		}
	}

	protected abstract void writeFinished(SocketChannel socketChannel, NioHandler handler);

	protected Selector initSelector() throws IOException {
		// Create a new selector
		Selector socketSelector = SelectorProvider.provider().openSelector();

		// Create a new non-blocking server socket channel
		serverChannel = ServerSocketChannel.open();
		serverChannel.configureBlocking(false);

		// Bind the server socket to the specified address and port
		InetSocketAddress isa = new InetSocketAddress(hostAddress, port);
		serverChannel.socket().bind(isa);

		// Register the server socket channel, indicating an interest in 
		// accepting new connections
		serverChannelKey = serverChannel.register(socketSelector, SelectionKey.OP_ACCEPT);

		return socketSelector;
	}
}
