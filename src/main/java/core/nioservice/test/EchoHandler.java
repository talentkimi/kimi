package core.nioservice.test;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import core.nioservice.NioHandler;

public class EchoHandler implements NioHandler {

	private EchoServer server;
	private SocketChannel channel;
	private byte[] data;
	private Exception exception;

	public EchoHandler(EchoServer server, SocketChannel channel) {
		this.server = server;
		this.channel = channel;
		this.data = null;
		this.exception = null;
	}

	@Override
	public void processData(byte[] data, int count) {
		this.data = new byte[count];
		System.arraycopy(data, 0, this.data, 0, count);
	}

	@Override
	public void send() {
		this.server.send(channel, this.data);
	}

	@Override
	public void setException(Exception e) {
		this.exception = e;
	}

	@Override
	public Exception getException() {
		return this.exception;
	}

	@Override
	public void connectionEstablished() {
		// Do nothing
	}

	@Override
	public void writeFinished() {
		// Do nothing
	}

	@Override
	public void terminate() {
		this.server = null;
		try {
			this.channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.data = null;
		this.exception = null;
	}

	@Override
	public void run() {
		if (exception == null) {
			this.send();
		} else {
			//exception.printStackTrace();
		}
	}
}
