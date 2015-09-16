package core.util;

import java.util.IdentityHashMap;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A Nano Timer.
 */
public final class NanoTimer {

	private static final Logger log = LoggerFactory.getLogger(NanoTimer.class);


	/** The timer map. */
	private static final Map<Object, Long> timerMap = new IdentityHashMap<Object, Long>();

	/**
	 * Start the timer.
	 * @param object the object.
	 */
	public static final void start(Object object) {
		if (object == null) {
			throw new NullPointerException();
		}
		timerMap.put(object, Long.valueOf(System.nanoTime()));
	}

	/**
	 * Start the timer.
	 * @param object the object.
	 */
	public static final void start() {
		start(Thread.currentThread());
	}

	/**
	 * Check the timer.
	 * @param object the object.
	 */
	public static final void check(Object object, String id) {
		Long startTime = timerMap.get(object);
		if (startTime != null) {
			long checkTime = System.nanoTime();
			if (log.isDebugEnabled()) log.debug (id + " (" + ((checkTime - startTime.longValue()) / 1000000) + " millis)");
		}
	}

	/**
	 * Check the timer.
	 * @param object the object.
	 */
	public static final void check(String id) {
		check(Thread.currentThread(), id);
	}

	/**
	 * Stop the timer.
	 * @param object the object.
	 */
	public static final void stop(Object object) {
		timerMap.remove(object);
	}

	/**
	 * Stop the timer.
	 * @param object the object.
	 */
	public static final void stop(Object object, String id) {
		check(object, id);
		stop(object);
	}

}
