package core.net;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.io.StreamOutput;
import core.util.Task;
import core.util.time.Milliseconds;

/**
 * An Output Stream Flusher!
 */
public class NetOutputFlusher extends Task {

	private static final Logger log = LoggerFactory.getLogger(NetOutputFlusher.class);


	/** The frequency. */
	private final Milliseconds frequency;
	/** The output. */
	private final StreamOutput output;

	/**
	 * Creates a new NetOutputFlusher.
	 * @param output the output.
	 * @param frequency the timeout.
	 */
	public NetOutputFlusher(StreamOutput output, Milliseconds frequency) {
		if (output == null || frequency == null) {
			throw new NullPointerException();
		}
		if (frequency.getMillis() < new Milliseconds(100).getMillis()) {
			throw new IllegalArgumentException("timeout=" + frequency);
		}
		this.frequency = frequency;
		this.output = output;

	}

	/**
	 * Run this task.
	 */
	public void runTask() throws Throwable {
		while (!output.isClosed()) {
			output.flush();
			Thread.sleep(frequency.getMillis());
		}
	}

}
