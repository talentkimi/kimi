package core.util.time;

/**
 * A Stop Watch.
 */
public class StopWatch {

	/** The time. */
	private final Milliseconds time;
	/** The stop time. */
	private long stopTime = 0;

	/**
	 * Start this stopwatch.
	 */
	public void start() {
		this.stopTime = System.currentTimeMillis() + time.getMillis();
	}

	/**
	 * Stop this stopwatch.
	 */
	public void stop() {
		this.stopTime = System.currentTimeMillis();
	}

	/**
	 * Returns true if this stopwatch is running.
	 * @return true if this stopwatch is running.
	 */
	public boolean isRunning() {
		return stopTime > System.currentTimeMillis();
	}

	/**
	 * Creates a new StopWatch.
	 * @param time the time.
	 */
	public StopWatch(Milliseconds time) {
		if (time.getMillis() < 1) {
			throw new IllegalArgumentException("time=" + time);
		}
		this.time = time;
	}

	/**
	 * Returns the time remaining.
	 * @return the time remaining.
	 */
	public long getTimeRemaining() {
		long now = System.currentTimeMillis();
		if (stopTime > now) {
			return stopTime - now;
		}
		return 0;
	}

}
