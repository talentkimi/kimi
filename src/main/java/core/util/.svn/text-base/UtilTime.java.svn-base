package core.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.util.time.Milliseconds;

/**
 * A Utilility Time.
 */
public class UtilTime extends Milliseconds {

	private static final Logger log = LoggerFactory.getLogger(UtilTime.class);


	/** The nanos. */
	private final long nanos;

	/**
	 * Creates a new util time.
	 * 
	 * @param millis
	 *            the millis.
	 */
	public UtilTime(long millis) {
		super(millis);
		this.nanos = 0;
	}

	/**
	 * Creates a new util time.
	 * 
	 * @param millis
	 *            the millis.
	 */
	public UtilTime(Milliseconds millis) {
		this(millis.getMillis());
	}

	/**
	 * Creates a new util time.
	 * 
	 * @param millis
	 *            the millis.
	 */
	public UtilTime(long millis, boolean nanos) {
		super(millis / 1000000);
		this.nanos = millis % 1000000;
	}

	/**
	 * Returns this as a string, but only up to specified the maximum number of
	 * significant parts.
	 */
	public String toString(int numParts) {
		StringBuilder b = new StringBuilder();
		Millis millis = new Millis(getMillis());
		append(millis, b, YEAR, "year", numParts);
		append(millis, b, MONTH, "month", numParts);
		append(millis, b, WEEK, "week", numParts);
		append(millis, b, DAY, "day", numParts);
		append(millis, b, HOUR, "hour", numParts);
		append(millis, b, MINUTE, "minute", numParts);
		append(millis, b, SECOND, "second", numParts);
		append(millis, b, MILLISECOND, "milli", numParts);
		if (millis.parts < numParts && nanos > 0) {
			if (millis.comma)
				b.append(", ");
			b.append(nanos).append(" nanos");
		}
		if (b.length() == 0) {
			return "0 millis";
		}
		return b.toString();
	}

	public String toString() {
		return toString(Integer.MAX_VALUE);
	}

	private static final class Millis {

	private static final Logger log = LoggerFactory.getLogger(Millis.class);


		long time;
		long parts;
		boolean comma;

		Millis(long time) {
			this.time = time;
			this.parts = 0;
			this.comma = false;
		}
	}

	/**
	 * @param b
	 * @param year
	 * @param string
	 * @return
	 */
	private static void append(Millis millis, StringBuilder b, long timeUnit, String description, int numParts) {
		if (millis.parts < numParts) {
			long time = millis.time / timeUnit;
			boolean nonZero = (time > 0);
			if (nonZero) {
				if (millis.comma)
					b.append(", ");
				b.append(time).append(' ').append(description).append(time == 1 ? "" : "s");
				millis.parts++;
				millis.comma = true;
			}
			millis.time -= (time * timeUnit);
		}
	}

	/**
	 * Convert a string representation of a UtilTime back into milliseconds.
	 * 
	 * @param time
	 *            string representation of a UtilTime
	 * @return milliseconds the time in milliseconds or -1 if this is not a
	 *         valid UtilTime string
	 * @see UtilTime#toString()
	 */
	public static long parseUtilTimeString(String time) {
		long timeInMs = 0;

		// The UtilTime string is of the form: "...D days, HH hours, ... , MMM
		// millis"
		// We need to split up these parts and convert each value into
		// milliseconds
		String[] timeparts = time.split(",");
		try {
			for (String timePart : timeparts) {

				// each timePart should now be "NNN timeType" e.g. "23 minutes"
				// (nb. if the
				// value is "1" then there is no trailing "s")
				String[] timeUnit = timePart.trim().split(" ");
				int unit = Integer.parseInt(timeUnit[0]);
				if (timeUnit[1].contains("milli")) {
					timeInMs += unit;
				} else if (timeUnit[1].contains("second")) {
					timeInMs += (unit * Milliseconds.SECOND);
				} else if (timeUnit[1].contains("minute")) {
					timeInMs += (unit * Milliseconds.MINUTE);
				} else if (timeUnit[1].contains("hour")) {
					timeInMs += (unit * Milliseconds.HOUR);
				} else if (timeUnit[1].contains("day")) {
					timeInMs += (unit * Milliseconds.DAY);
				} else if (timeUnit[1].contains("week")) {
					timeInMs += (unit * Milliseconds.WEEK);
				} else if (timeUnit[1].contains("month")) {
					timeInMs += (unit * Milliseconds.MONTH);
				} else if (timeUnit[1].contains("year")) {
					timeInMs += (unit * Milliseconds.YEAR);
				} else {
					throw new NumberFormatException("Unknown time format:" + timeUnit[1]);
				}
			}
		} catch (NumberFormatException e) {
			if (log.isDebugEnabled()) log.debug ("Error parsing UtilTime string: " + time, e);
			return -1;
		}

		return timeInMs;
	}

}
