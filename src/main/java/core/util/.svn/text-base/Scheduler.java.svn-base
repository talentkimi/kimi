package core.util;

import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import core.lang.thread.PersistentRunnable;
import core.lang.thread.RunnableContainer;
import core.lang.thread.ThreadManager;
import core.lang.thread.ThreadPool.Poolable;
import core.util.time.Milliseconds;

/**
 * A Scheduler.
 * <p>
 * Can be set to terminate if the scheduled task overruns.
 * <p>
 * Holds the exception of a failed run, and prints the stack trace (optional).
 */
public class Scheduler extends Task implements RunnableContainer, PersistentRunnable {

	private static final Logger log = LoggerFactory.getLogger(Scheduler.class);
	
	/** The start time. */
	private long startTime = 0;
	/** The runnable. */
	private final Runnable runnable;
	/** The runnable task name */
	private final String runnableTaskName;
	/** The seconds. */
	private final Milliseconds millis;
	/** Print the stack trace? */
	private boolean printStackTrace = true;
	/** Terminate on overrun? */
	private boolean terminateOnOverrun = false;
	/** The time last updated. */
	private long timeOfLastUpdate = 0;
	/** Terminate. */
	private boolean terminated = false;
	/** Immediate start. */
	private boolean immediateStart;
	
	/**
	 * Returns the runnable task name.
	 */
	public String getTaskName() {
		return runnableTaskName;
	}

	/**
	 * Returns the time of the last update.
	 * @return the time of the last update.
	 */
	public final long getTimeOfLastUpdate() {
		return timeOfLastUpdate;
	}

	/**
	 * Terminate this scheduler.
	 */
	public void terminate() {
		this.terminated = true;
	}

	/**
	 * Returns the time interval.
	 * @return the time interval.
	 */
	public final Milliseconds getMillis() {
		return millis;
	}

	/**
	 * Returns the time interval.
	 * @return the time interval.
	 */
	public final long getTimeInterval() {
		return millis.getMillis();
	}

	/**
	 * Returns the time of the last update.
	 * @return the time of the last update.
	 */
	public final long getTimeOfNextUpdate() {
		return getTimeOfLastUpdate() + getTimeInterval();
	}

	/**
	 * Returns the runnable.
	 * @return the runnable.
	 */
	public final Runnable getRunnable() {
		return runnable;
	}

	/**
	 * Enable printing of the stack trace.
	 * @param enable true to enable.
	 */
	public final void setPrintStackTrace(boolean enable) {
		this.printStackTrace = enable;
	}

	/**
	 * Enable termination on overrun.
	 * @param enable true to enable.
	 */
	public final void setTerminateOnOverrun(boolean enable) {
		this.terminateOnOverrun = enable;
	}

	/**
	 * Creates a new scheduler.
	 * @param r the runnable.
	 */
	public Scheduler(Runnable r, Milliseconds millis) {
		this(r, millis, false);
	}

	/**
	 * Creates a new scheduler.
	 * @param r the runnable.
	 */
	public Scheduler(Runnable r, Milliseconds millis, boolean immediateStart) {
		this(r, millis, (immediateStart ? 0 : System.currentTimeMillis()));
	}

	/**
	 * Creates a new scheduler.
	 * @param r the runnable.
	 */
	protected Scheduler(Runnable r, Milliseconds millis, long timeOfLastUpdate) {
		if (r == null) {
			throw new NullPointerException();
		}
		if (millis.getMillis() < 1) {
			throw new IllegalArgumentException("millis=" + millis);
		}
		this.runnable = r;
		this.millis = millis;
		this.timeOfLastUpdate = timeOfLastUpdate;
		
		String runnableTaskName = r.getClass().getSimpleName();
		if (r instanceof Poolable) {
			runnableTaskName = ((Poolable)r).getPoolableName();
		}
		this.runnableTaskName = runnableTaskName;
	}

	/**
	 * Run the runnable.
	 */
	public final void runTask() throws InterruptedException {
		while (true) {
			waitForNextRun();
			if (terminated) {
				break;
			}
			runRunnable();
		}
	}

	/**
	 * Run the runnable.
	 */
	private final void runRunnable() {
		try {
			if (runnable instanceof Task) {
				Task task = (Task) runnable;
				task.reset();
			}
			startTime = System.currentTimeMillis();
			runnable.run();
		} catch (Throwable t) {
			setThrowable(t);
			if (printStackTrace) {
				if (log.isErrorEnabled()) log.error(t.getMessage(),t);
			}
		} finally {
			startTime = 0;
		}
	}

	/**
	 * Wait for the next run.
	 */
	private final void waitForNextRun() throws InterruptedException {
		long timeNow = System.currentTimeMillis();
		long timeOfNextUpdate = getTimeOfNextUpdate();
		if (timeOfNextUpdate < timeNow) {
			if (terminateOnOverrun) {
				terminated = true;
				return;
			}
			timeOfNextUpdate = timeNow;
		}
		long timeToSleep = timeOfNextUpdate - timeNow;
		timeOfLastUpdate = timeOfNextUpdate;
		if (timeToSleep > 0) {
			Thread.sleep(timeToSleep);
		}
	}

	public void close() {
		terminate();
	}

	public boolean isClosed() {
		return hasFinished();
	}

	public boolean isClosing() {
		return terminated && !hasFinished();
	}
	
	public boolean isRunningRunnable() {
		return startTime > 0;
	}

	public long getRunnableStartTime() {
		return startTime;
	}

	@Override
	protected Executor getExecutor() {
		return ThreadManager.SCHEDULED;
	}
}
