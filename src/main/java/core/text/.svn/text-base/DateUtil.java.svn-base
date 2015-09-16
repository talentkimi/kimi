package core.text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.IdentityHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * A Utility for thread safe date formatting using cached {@link SimpleDateFormat} objects.
 * <p>
 * This improves speed, memory usage and object-allocation and is thread safe.
 */
public final class DateUtil {

	/** The locale (United Kingdom). */
	public static final Locale DEFAULT_LOCALE = Locale.UK;
	/** The time-zone (GMT). */
	public static final TimeZone DEFAULT_TIME_ZONE = TimeZone.getTimeZone("GMT");

	/**
	 * The thread-local calendar.
	 */
	private static final ThreadLocal<Calendar> localCalendar = new ThreadLocal<Calendar>() {

		protected Calendar initialValue() {
			return new GregorianCalendar(DEFAULT_TIME_ZONE);
		}
	};

	/**
	 * The thread-local map of date formats.
	 */
	private static final ThreadLocal<Map<String, DateFormat>> localDateFormatMap = new ThreadLocal<Map<String, DateFormat>>() {

		protected Map<String, DateFormat> initialValue() {
			return new IdentityHashMap<String, DateFormat>(4);
		}
	};

	/**
	 * Returns the thread-local calendar.
	 * @return the calendar.
	 */
	public static final Calendar getCalendar() {
		return localCalendar.get();
	}
	
	/**
	 * Returns the thread-local date format for the given pattern.
	 * @param pattern the pattern.
	 * @return the date format.
	 */
	public static final DateFormat getDateFormat(String pattern) {
		pattern = pattern.intern();
		Map<String, DateFormat> dateFormatMap = localDateFormatMap.get();
		DateFormat dateFormat = dateFormatMap.get(pattern);
		if (dateFormat == null) {
			dateFormat = new SimpleDateFormat(pattern);
			dateFormatMap.put(pattern, dateFormat);
		}
		Calendar calendar = getCalendar();
		dateFormat.setCalendar(calendar);
		return dateFormat;
	}

	/**
	 * Formats the given date.
	 * @param pattern the pattern.
	 * @param date the date.
	 * @return the date, formatted as a string.
	 */
	public static String format(String pattern, Date date) {
		if (pattern == null || date == null) {
			throw new NullPointerException();
		}
		return getDateFormat(pattern).format(date);
	}

	/**
	 * Parses the given date.
	 * @param pattern the pattern.
	 * @param date the date, formatted as a string.
	 * @return the date.
	 */
	public static Date parse(String pattern, String date) throws ParseException {
		if (pattern == null || date == null) {
			throw new NullPointerException();
		}
		return getDateFormat(pattern).parse(date);
	}

	/**
	 * Inaccessible constructor.
	 */
	private DateUtil() {
	}

}
