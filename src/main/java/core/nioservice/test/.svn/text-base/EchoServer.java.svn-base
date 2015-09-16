package core.nioservice.test;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import core.nioservice.NioHandler;
import core.nioservice.NioHandlerManager;
import core.nioservice.NioServer;

public class EchoServer extends NioServer {

	public EchoServer(InetAddress hostAddress, int port, ThreadPoolExecutor executor, NioHandlerManager handlerManager) throws IOException {
		super(hostAddress, port, executor, handlerManager);
	}

	@Override
	protected void writeFinished(SocketChannel socketChannel, NioHandler handler) {
		// All data has been written away; Switch back to waiting for data
		socketChannel.keyFor(selector).interestOps(SelectionKey.OP_READ);
		
	}

	@Override
	protected ThreadPoolExecutor getExecutor() {
		return (ThreadPoolExecutor) executor;
	}

	@Override
	protected int getActiveCount() {
		return getExecutor().getActiveCount();
	}

	@Override
	protected int getPoolSize() {
		return getExecutor().getPoolSize();
	}

	@Override
	protected int getQueueSize() {
		return getExecutor().getQueue().size();
	}

	@Override
	protected void shutdownExecutor() {
		getExecutor().shutdownNow();
	}

	@Override
	protected void shutdownComplete() {
		// Do nothing
	}

	/**
	 * Main method to test a Nio server that echoes data back.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {

			ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 30, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
			EchoServer server = new EchoServer(null, 9100, executor, null);
			server.setHandlerManager(new EchoHandlerManager(server));
			new Thread(server).start();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
