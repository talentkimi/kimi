package core.util;

import java.util.Calendar;

import core.util.time.Days;
import core.util.time.Milliseconds;

/**
 * A Daily Scheduler.
 */
public class DailyScheduler extends Scheduler {

	/**
	 * Returns the time of the last update.
	 * @param millis the millis.
	 * @return the time.
	 */
	private static final long getTimeOfLastUpdate(Milliseconds millis) {
		if (millis.getMillis() >= new Days(1).getMillis()) {
			throw new IllegalArgumentException("millis=" + millis);
		}
		UtilDate date = new UtilDate();
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		date.add(Calendar.MILLISECOND, (int) millis.getMillis());
		long time = date.getTimeInMillis();
		if (time > System.currentTimeMillis()) {
			time -= new Days(1).getMillis();
		}
		// if (log.isDebugEnabled()) log.debug (String.valueOf(date));
		// if (log.isDebugEnabled()) log.debug (new UtilDate(time));
		return time;
	}

	/**
	 * Creates a new daily scheduler.
	 * @param r the runnable.
	 * @param millis the time of the update.
	 */
	public DailyScheduler(Runnable r, Milliseconds millis) {
		super(r, new Days(1), getTimeOfLastUpdate(millis));
	}

}
