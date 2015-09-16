package core.nioservice.test;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import core.nioservice.NioHandler;
import core.nioservice.NioHandlerManager;

public class EchoHandlerManager implements NioHandlerManager {

	private EchoServer server;
	private Map<SocketChannel, EchoHandler> handlers;

	public EchoHandlerManager(EchoServer server) {
		this.handlers = new Hashtable<SocketChannel, EchoHandler>();
		this.server = server;
	}

	@Override
	public EchoHandler newHandler(SocketChannel socketChannel, long acceptStartTime) {
		EchoHandler handler = getHandler(socketChannel);
		if (handler == null) {
			handler = new EchoHandler(server, socketChannel);
			handlers.put(socketChannel, handler);
		}
		return handler;
	}

	@Override
	public EchoHandler getHandler(SocketChannel socketChannel) {
		return handlers.get(socketChannel);
	}

	@Override
	public Map<SocketChannel, EchoHandler> getHandlers() {
		return handlers;
	}

	@Override
	public void registerHandler(SocketChannel socket, NioHandler handler) {
		handlers.put(socket, (EchoHandler) handler);
	}

	@Override
	public void terminateHandler(SocketChannel socketChannel, NioHandler handler) {
		EchoHandler hndlr = null;
		if (socketChannel == null) {
			hndlr = (EchoHandler) handler;
		} else {
			hndlr = (EchoHandler) handlers.remove(socketChannel);
			if (hndlr == null) {
				hndlr = (EchoHandler) handler;
			}
		}
		if (hndlr != null) {
			hndlr.terminate();
		}
	}

	@Override
	public void terminate() {
		synchronized (handlers) {
			Iterator<SocketChannel> removals = handlers.keySet().iterator();
			while (removals.hasNext()) {
				SocketChannel channel = (SocketChannel) removals.next();
				terminateHandler(channel, null);
			}
			handlers.clear();
		}
	}

	@Override
	public void terminateFailedHandlers() {
		synchronized (handlers) {
			Iterator<SocketChannel> removals = handlers.keySet().iterator();
			while (removals.hasNext()) {
				SocketChannel channel = (SocketChannel) removals.next();
				EchoHandler handler = handlers.get(channel);
				if (handler.getException() != null) {
					handlers.remove(channel);
					handler.terminate();
					try {
						channel.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public String getStatsString() {
		return "";
	}
}
