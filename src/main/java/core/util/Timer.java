package core.util;

import java.util.HashMap;
import java.util.WeakHashMap;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * An Accurate Timer.
 */
public final class Timer {

	private static final Logger log = LoggerFactory.getLogger(Timer.class);


	/** The timer map. */
	private static final WeakHashMap<Object, TimerObject> timerMap = new WeakHashMap<Object, TimerObject>();

	/**
	 * Start the timer for the given object.
	 * @param object the object.
	 */
	public static final synchronized TimerObject start(Object object) {
		if (object == null) {
			throw new NullPointerException();
		}
		TimerObject timer = new TimerObject();
		timerMap.put(object, timer);
		timer.start();
		return timer;
	}

	/**
	 * Start the timer for the given object.
	 * @param object the object.
	 */
	public static final TimerObject start() {
		return start(Thread.currentThread());
	}

	/**
	 * Get the timer for the given object, start if necessary.
	 * @param object the object.
	 * @return the timer.
	 */
	private static final TimerObject get(Object object) {
		if (object == null) {
			throw new NullPointerException();
		}
		TimerObject timer = timerMap.get(object);
		if (timer == null) {
			timer = start(object);
		}
		return timer;
	}

	/**
	 * Returns the seconds formatted from the given nanos.
	 * @param nanos the nanos.
	 * @return the seconds.
	 */
	private static final String appendSeconds(StringBuilder builder, long nanos) {
		String time = String.valueOf(nanos);
		if (time.length() > 9) {
			int index = time.length() - 9;
			for (int i = 0; i < time.length(); i++) {
				if (i == index) {
					builder.append('.');
				}
				builder.append(time.charAt(i));
			}
		} else {
			builder.append('0').append('.');
			int zeros = 9 - time.length();
			for (int i = 0; i < zeros; i++) {
				builder.append('0');
			}
			for (int i = 0; i < time.length(); i++) {
				builder.append(time.charAt(i));
			}
		}
		return builder.toString();
	}

	/**
	 * Check the time for the given object.
	 * @param object the object.
	 * @param name the name.
	 */
	public static final void check(Object object, String name) {
		if (object == null) {
			throw new NullPointerException();
		}
		TimerObject timer = get(object);
		long timeNow = System.nanoTime();
		long timeSinceCheck = timeNow - timer.getCheckTime();
		long timeSinceStart = timeNow - timer.getStartTime();

		// Stack Trace
		StringBuilder builder = new StringBuilder();
		StackTraceElement[] elements = new Exception().getStackTrace();
		StackTraceElement element = elements[0];
		for (int i = 1; i < elements.length; i++) {
			element = elements[i];
			if (!element.getClassName().equals("core.util.Timer")) {
				break;
			}
		}
		String className = element.getClassName();
		int index = className.lastIndexOf('.');
		builder.append(className.substring(index+1)).append('.');
		builder.append(element.getMethodName()).append('(');
		builder.append(element.getLineNumber()).append(')').append(' ');

		// Times
		appendSeconds(builder, timeSinceCheck);
		builder.append(" secs, ");
		appendSeconds(builder, timeSinceStart);
		builder.append(" secs");

		// Name
		if (name != null) {
			builder.append(' ');
			builder.append('(');
			builder.append(name);
			builder.append(')');
		}

		// Done
		if (log.isDebugEnabled()) log.debug (String.valueOf(builder));
		timer.check();
	}

	/**
	 * Check the time
	 * @param name the name.
	 */
	public static final void check(String name) {
		check(Thread.currentThread(), name);
	}

	/**
	 * Check the time
	 */
	public static final void check() {
		check(Thread.currentThread(), null);
	}

	/**
	 * Returns the time passed in milliseconds.
	 * @param object the object.
	 * @return the time passed in milliseconds.
	 */
	public static final long getTimePassedInMillis(Object object) {
		return (System.nanoTime() - get(object).getStartTime()) / 1000000;
	}

	/**
	 * Returns the time passed in milliseconds.
	 * @return the time passed in milliseconds.
	 */
	public static final long getTimePassedInMillis() {
		return getTimePassedInMillis(Thread.currentThread());
	}

	public static void main(String[] args) {
		Timer.start();
		Timer.check();
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				if (log.isErrorEnabled()) log.error(e.getMessage(),e);
			}
			Timer.check();
		}
	}

}

final class TimerObject {

	private static final Logger log = LoggerFactory.getLogger(TimerObject.class);


	/** The start time. */
	private long startTime;
	/** The check time. */
	private long checkTime;

	/**
	 * Update the time.
	 */
	public void check() {
		this.checkTime = System.nanoTime();
	}

	/**
	 * Start the start.
	 */
	public void start() {
		this.startTime = System.nanoTime();
	}

	/**
	 * Returns the start time.
	 * @return the start time.
	 */
	public final long getStartTime() {
		return startTime;
	}

	/**
	 * Returns the check time.
	 * @return the check time.
	 */
	public final long getCheckTime() {
		return checkTime;
	}

	/**
	 * Creates a new TimerObject.
	 */
	public TimerObject() {
		this.startTime = System.nanoTime();
		this.checkTime = System.nanoTime();
	}

}
