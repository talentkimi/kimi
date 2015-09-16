package core.util;

import java.util.concurrent.Executor;

import core.lang.thread.ThreadManager;
import core.util.time.Milliseconds;

/**
 * A Scheduled Task.
 */
public abstract class ScheduledTask extends Task {

	/** The scheduler. */
	private Scheduler scheduler = null;
	
	/**
	 * Schedule this task. If the task is already scheduled and using the given time it does not reschedule.
	 * @param millis the milliseconds.
	 */
	public synchronized void schedule(Milliseconds millis) {
		if (scheduler != null) {
			if (millis.equals(scheduler.getMillis())) {
				return;
			}
			scheduler.terminate();
			scheduler = null;
		}
		scheduler = new Scheduler(this, millis);
		scheduler.start();
	}

	protected Executor getExecutor() {
		return ThreadManager.SCHEDULED;
	}
}
