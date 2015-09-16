package core.util;

import java.util.Calendar;

import core.util.time.Days;
import core.util.time.Milliseconds;
import core.util.time.Weeks;

/**
 * A Weekly Scheduler.
 */
public class WeeklyScheduler extends Scheduler {

	/**
	 * Returns the time of the last update.
	 * @param dayOfWeek
	 * @param millis the millis.
	 * @return the time.
	 */
	private static final long getTimeOfLastUpdate(int dayOfWeek, Milliseconds millis) {
		if (millis.getMillis() >= new Days(1).getMillis()) {
			throw new IllegalArgumentException("millis=" + millis);
		}
		UtilDate date = new UtilDate();
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		date.add(Calendar.MILLISECOND, (int) millis.getMillis());
		while (date.get(Calendar.DAY_OF_WEEK) != dayOfWeek) {
			date.add(Calendar.DAY_OF_WEEK, -1);
		}
		long time = date.getTimeInMillis();
		if (time > System.currentTimeMillis()) {
			time -= new Weeks(1).getMillis();
		}
		// if (log.isDebugEnabled()) log.debug (String.valueOf(date));
		// if (log.isDebugEnabled()) log.debug (new UtilDate(time));
		// if (log.isDebugEnabled()) log.debug (new UtilDate(time).getDayName());
		return time;
	}

	/**
	 * Creates a new weekly scheduler.
	 * @param r the runnable.
	 * @param dayOfWeek the day of the week.
	 * @param millis the time of the update.
	 */
	public WeeklyScheduler(Runnable r, int dayOfWeek, Milliseconds millis) {
		super(r, new Weeks(1), getTimeOfLastUpdate(dayOfWeek, millis));
	}

}
