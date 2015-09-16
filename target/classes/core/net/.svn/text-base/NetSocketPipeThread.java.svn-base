package core.net;

import java.io.IOException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.io.StreamInput;
import core.io.StreamOutput;
import core.util.Task;

/**
 * The Net Socket Pipe Thread.
 */
public class NetSocketPipeThread extends Task {

	private static final Logger log = LoggerFactory.getLogger(NetSocketPipeThread.class);


	/** The socket from. */
	private final StreamInput from;
	/** The socket to. */
	private final StreamOutput to;
	/** The number of bytes. */
	private int bytes = 0;

	/**
	 * Returns the bytes.
	 * @return the bytes.
	 */
	public int getBytes() {
		return bytes;
	}

	/**
	 * Creates a new pipe thread.
	 */
	public NetSocketPipeThread(NetSocket from, NetSocket to) throws IOException {
		this(from.getReader(), to.getWriter());
	}

	/**
	 * Creates a new pipe thread.
	 */
	public NetSocketPipeThread(StreamInput from, StreamOutput to) {
		if (from == null || to == null) {
			throw new NullPointerException();
		}
		this.from = from;
		this.to = to;
	}

	/**
	 * Run this pipe.
	 */
	public void runTask() throws Throwable {
		try {
			while (!from.isClosed() && !to.isClosed()) {
				int b = from.read();
				if (b == -1) {
					break;
				}
				bytes++;
				to.write(b);
			}
		} finally {
			to.flush();
		}
	}
}
