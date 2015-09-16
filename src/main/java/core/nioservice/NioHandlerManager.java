package core.nioservice;

import java.nio.channels.SocketChannel;
import java.util.Map;

public interface NioHandlerManager {

	public NioHandler newHandler(SocketChannel socketChannel, long acceptTime);
	public NioHandler getHandler(SocketChannel socketChannel);
	public Map getHandlers();
	public void registerHandler(SocketChannel socket, NioHandler handler);
	public void terminateHandler(SocketChannel socketChannel, NioHandler handler);
	public void terminate();
	public void terminateFailedHandlers();
	public String getStatsString();
}
