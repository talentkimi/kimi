package core.nioservice;

import java.nio.channels.SocketChannel;

public class ChangeRequest {

	public static final int REGISTER = 1;
	public static final int CHANGEOPS = 2;
	public static final int SHUTDOWN = 3;
	public static final int SERVER_DISABLE = 4;
	public static final int SERVER_ENABLE = 5;

	public SocketChannel socket;
	public int type;
	public int ops;

	public ChangeRequest(SocketChannel socket, int type, int ops) {
		this.socket = socket;
		this.type = type;
		this.ops = ops;
	}
}
