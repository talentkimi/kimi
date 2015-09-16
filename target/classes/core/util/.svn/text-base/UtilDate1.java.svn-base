package core.util;

import java.text.ParseException;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import core.text.DateUtil;
import core.text.StringParsable;
import core.text.Text;
import core.util.time.Milliseconds;

/**
 * A Utility Date (in GMT).
 */
public class UtilDate1 extends GregorianCalendar implements StringParsable {

	/** The locale (United Kingdom). */
	private static final Locale DEFAULT_LOCALE = Locale.UK;
	/** The time-zone (GMT). */
	private static final TimeZone DEFAULT_TIME_ZONE = TimeZone.getTimeZone("GMT");

	/**
	 * Set the default Timezone.
	 */
	static {
		TimeZone.setDefault(DEFAULT_TIME_ZONE);
	}

	/** The day names. */
	public static final String[] DAY_NAMES = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
	/** The month names. */
	public static final String[] MONTH_NAMES = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

	/** The date format. */
	public static final String FORMAT_PATTERN = "dd/MM/yy-HH:mm";
	/** The date format. */
	public static final String FORMAT_PATTERN_YEAR = "dd/MM/yyyy-HH:mm";
	/** The date format. */
	public static final String FORMAT_PATTERN_SECONDS = "dd/MM/yyyy-HH:mm:ss";
	/** The date format. */
	public static final String DOB_PATTERN = "dd/MM/yy";

	/** The pattern. */
	private final String pattern;

	/**
	 * Returns the pattern.
	 * @return the pattern.
	 */
	public final String getPattern() {
		return pattern;
	}

	/**
	 * Returns a string representation of this date.
	 * @return a string representation of this date.
	 */
	public String toString() {
		return toString(pattern);
	}

	/**
	 * Returns the time in seconds.
	 * @return the time in seconds.
	 */
	public final int getTimeInSeconds() {
		long millis = getTimeInMillis();
		return (int) (millis / 1000);
	}

	/**
	 * Returns the time in minutes.
	 * @return the time in minutes.
	 */
	public final int getTimeInMinutes() {
		long millis = getTimeInMillis();
		return (int) (millis / (1000 * 60));
	}

	/**
	 * Returns the time in hours.
	 * @return the time in hours.
	 */
	public final int getTimeInHours() {
		long millis = getTimeInMillis();
		return (int) (millis / (1000 * 60 * 60));
	}

	/**
	 * Returns the time in days.
	 * @return the time in days.
	 */
	public final short getTimeInDays() {
		long millis = getTimeInMillis();
		return (short) (millis / (1000 * 60 * 60 * 24));
	}

	/**
	 * Returns true if this equals the given object.
	 * @param object the object.
	 * @return true if this equals the given object.
	 */
	public boolean equals(Object object) {
		if (object instanceof UtilDate1) {
			UtilDate1 date = (UtilDate1) object;
			return date.getTimeInMillis() == this.getTimeInMillis();
		}
		return false;
	}

	/**
	 * Returns the difference in millis.
	 * @param date the date.
	 * @return the difference.
	 */
	public long getDifferenceInMillis(UtilDate1 date) {
		long time1 = this.getTimeInMillis();
		long time2 = date.getTimeInMillis();
		if (time1 < time2) {
			return time2 - time1;
		} else {
			return time1 - time2;
		}
	}

	/**
	 * Returns the date in milliseconds parsed from the given date.
	 * @param pattern the date pattern.
	 * @param date the date string.
	 * @return the date in milliseconds
	 */
	public static final long parseMillis(String pattern, String date) throws ParseException {
		// return new SimpleDateFormat(pattern).parse(date).getTime();
		return DateUtil.parse(pattern, date).getTime();
	}

	/**
	 * Creates a new date with the given milliseconds.
	 * @param millis the milliseconds.
	 */
	public UtilDate1(long millis, String pattern) {
		super(DEFAULT_TIME_ZONE, DEFAULT_LOCALE);
		this.pattern = pattern;
		setTimeInMillis(millis);
	}

	/**
	 * Creates a new date with the given milliseconds.
	 * @param millis the milliseconds.
	 */
	public UtilDate1(int seconds, String pattern) {
		this(seconds * 1000l, pattern);
	}

	/**
	 * Creates a new date with the given milliseconds.
	 * @param millis the milliseconds.
	 */
	public UtilDate1(long millis) {
		this(millis, FORMAT_PATTERN);
	}

	/**
	 * Creates a new date with the given milliseconds.
	 * @param millis the milliseconds.
	 */
	public UtilDate1(int seconds) {
		this(seconds * 1000l, FORMAT_PATTERN);
	}

	/**
	 * Creates a new date.
	 */
	public UtilDate1() {
		this(System.currentTimeMillis());
	}

	/**
	 * Creates a new date parsed from the given date string.
	 * @param pattern the date pattern.
	 * @param date the date string.
	 * @throws ParseException if the date is incorrectly formatted.
	 */
	public UtilDate1(String pattern, String date) throws ParseException {
		this(parseMillis(pattern, date), pattern);
	}

	/**
	 * Creates a new date.
	 * @param date the date.
	 */
	public UtilDate1(String date) throws ParseException {
		this(FORMAT_PATTERN, date);
	}

	/**
	 * Creates a new date.
	 * @param date the date.
	 */
	public UtilDate1(UtilDate1 date) {
		this(date.getTimeInMillis(), date.getPattern());
	}

	/**
	 * Returns a string representation of this date.
	 * @param pattern the date format pattern.
	 */
	public String toString(String pattern) {
		if (pattern.equals(FORMAT_PATTERN)) {
			pattern = FORMAT_PATTERN_YEAR;
		}
		// return new SimpleDateFormat(pattern).format(getTime());
		return DateUtil.format(pattern, getTime());
	}

	/**
	 * Returns the day name.
	 * @return the day name.
	 */
	public String getDayName() {
		return DAY_NAMES[get(DAY_OF_WEEK) - 1];
	}

	/**
	 * Returns the month name.
	 * @return the month name.
	 */
	public String getMonthName() {
		return MONTH_NAMES[get(MONTH)];
	}

	/**
	 * Returns the second.
	 * @return the second.
	 */
	public int getSecond() {
		return get(SECOND);
	}

	/**
	 * Returns the millisecond.
	 * @return the millisecond.
	 */
	public int getMillisecond() {
		return get(MILLISECOND);
	}

	/**
	 * Returns the minute.
	 * @return the minute.
	 */
	public int getMinute() {
		return get(MINUTE);
	}

	/**
	 * Returns the hour of the day.
	 * @return the hour of the day.
	 */
	public int getHour() {
		return get(HOUR_OF_DAY);
	}

	/**
	 * Returns the day of the month.
	 * @return the day of the month.
	 */
	public int getDay() {
		return get(DAY_OF_MONTH);
	}

	/**
	 * Returns the day of the week.
	 * @return the day of the week.
	 */
	public int getDayOfWeek() {
		return get(DAY_OF_WEEK);
	}

	/**
	 * Returns the month index.
	 * @return the month index.
	 */
	public int getMonthIndex() {
		return get(MONTH);
	}

	/**
	 * Returns the month.
	 * @return the month.
	 */
	public int getMonth() {
		return getMonthIndex() + 1;
	}

	/**
	 * Returns the year.
	 * @return the year.
	 */
	public int getYear() {
		return get(YEAR);
	}

	/**
	 * Sets the year.
	 * @param year the year.
	 */
	public void setYear(int year) {
		set(YEAR, year);
	}

	/**
	 * Returns true if it is morning.
	 * @return true if it is morning.
	 */
	public boolean isMorning() {
		return getHour() < 12;
	}

	/**
	 * Add the given number of years.
	 * @param years the years.
	 */
	public void addYears(int years) {
		add(YEAR, years);
	}

	/**
	 * Add the given number of months.
	 * @param months the months.
	 */
	public void addMonths(int months) {
		add(MONTH, months);
	}

	/**
	 * Add the given number of milliseconds.
	 * @param millis the milliseconds.
	 */
	public void addMillis(int millis) {
		add(MILLISECOND, millis);
	}

	/**
	 * Add the given number of days.
	 * @param days the days.
	 */
	public void addDays(int days) {
		add(DAY_OF_MONTH, days);
	}

	/**
	 * Add the given number of hours.
	 * @param hours the hours.
	 */
	public void addHours(int hours) {
		add(HOUR, hours);
	}

	/**
	 * Add the given number of minutes.
	 * @param minutes the minutes.
	 */
	public void addMinutes(int minutes) {
		add(MINUTE, minutes);
	}

	/**
	 * Add the given number of seconds.
	 * @param seconds the seconds.
	 */
	public void addSeconds(int seconds) {
		add(SECOND, seconds);
	}

	/**
	 * Add the given number of milliseconds.
	 * @param milliseconds the milliseconds.
	 */
	public void addMilliseconds(int milliseconds) {
		add(MILLISECOND, milliseconds);
	}

	/**
	 * Add the given number of days.
	 * @param days the days.
	 */
	public UtilDate1 add(Milliseconds millis) {
		add(MILLISECOND, (int) millis.getMillis());
		return this;
	}

	/**
	 * Returns the month name shortened to the given length.
	 * @param length the length.
	 * @return the month name.
	 */
	public String getMonthName(int length) {
		if (length < 0)
			throw new IllegalArgumentException("length=" + length);
		if (length == 0) {
			return getMonthName();
		} else {
			return getMonthName().substring(0, length);
		}
	}

	/**
	 * Returns the month name shortened to the given length.
	 * @param length the length.
	 * @return the month name.
	 */
	public static String getMonthName(int month, int length) {
		if (length < 0) {
			throw new IllegalArgumentException("length=" + length);
		}
		if (length == 0) {
			return MONTH_NAMES[month - 1];
		} else {
			return MONTH_NAMES[month - 1].substring(0, length);
		}
	}

	/**
	 * Returns the number of days between this and the given date.
	 * @param date the date.
	 * @return the number.
	 */
	public final int getDaysBetween(UtilDate1 date) {
		UtilDate1 thisDate = new UtilDate1(getTimeInMillis());
		int amount = 1;
		if (thisDate.after(date)) {
			amount = -1;
		}
		int days = 0;
		while (!thisDate.onSameDayAs(date)) {
			thisDate.add(DAY_OF_WEEK, amount);
			days++;
		}
		return days;
	}

	/**
	 * Returns the month number.
	 * @param name the month name.
	 * @return the month number.
	 */
	public static String getMonthNumber(String name) {
		name = name.toLowerCase();
		for (int i = 0; i < MONTH_NAMES.length; i++) {
			if (MONTH_NAMES[i].toLowerCase().startsWith(name)) {
				return Text.getTwoDigitString(i + 1);
			}
		}
		throw new IllegalArgumentException(name);
	}

	/**
	 * Returns the two digit second.
	 * @return the two digit second.
	 */
	public String getTwoDigitSecond() {
		return Text.getTwoDigitString(getSecond());
	}

	/**
	 * Returns the two digit minute.
	 * @return the two digit minute.
	 */
	public String getTwoDigitMinute() {
		return Text.getTwoDigitString(getMinute());
	}

	/**
	 * Returns the two digit hour.
	 * @return the two digit hour.
	 */
	public String getTwoDigitHour() {
		return Text.getTwoDigitString(getHour());
	}

	/**
	 * Returns the two digit day.
	 * @return the two digit day.
	 */
	public String getTwoDigitDay() {
		return Text.getTwoDigitString(getDay());
	}

	/**
	 * Returns the two digit month.
	 * @return the two digit month.
	 */
	public String getTwoDigitMonth() {
		return Text.getTwoDigitString(getMonth());
	}

	/**
	 * Returns the two digit year.
	 * @return the two digit year.
	 */
	public String getTwoDigitYear() {
		return Text.getTwoDigitString(getYear());
	}

	/**
	 * Returns true if this date is on the same year as the given date.
	 * @param date the date to compare.
	 * @return true if this date is on the same year as the given date.
	 */
	public boolean onSameYearAs(UtilDate1 date) {
		return getYear() == date.getYear();
	}

	/**
	 * Returns true if this date is on the same month as the given date.
	 * @param date the date to compare.
	 * @return true if this date is on the same month as the given date.
	 */
	public boolean onSameMonthAs(UtilDate1 date) {
		return getMonth() == date.getMonth() && onSameYearAs(date);
	}

	/**
	 * Returns true if this date is on the same day as the given date.
	 * @param date the date to compare.
	 * @return true if this date is on the same day as the given date.
	 */
	public boolean onSameDayAs(UtilDate1 date) {
		return getDay() == date.getDay() && onSameMonthAs(date);
	}

	/**
	 * Returns true if this date is on the same hour as the given date.
	 * @param date the date to compare.
	 * @return true if this date is on the same hour as the given date.
	 */
	public boolean onSameHourAs(UtilDate1 date) {
		return getHour() == date.getHour() && onSameDayAs(date);
	}

	/**
	 * Returns true if this date is on the same minute as the given date.
	 * @param date the date to compare.
	 * @return true if this date is on the same minute as the given date.
	 */
	public boolean onSameMinuteAs(UtilDate1 date) {
		return getMinute() == date.getMinute() && onSameHourAs(date);
	}

	/**
	 * Returns true if this date is on the same second as the given date.
	 * @param date the date to compare.
	 * @return true if this date is on the same second as the given date.
	 */
	public boolean onSameSecondAs(UtilDate1 date) {
		return getSecond() == date.getSecond() && onSameMinuteAs(date);
	}

	/**
	 * Returns a copy of this webdate.
	 * @return a copy of this webdate.
	 */
	public UtilDate1 copy() {
		return new UtilDate1(getTimeInMillis(), getPattern());
	}

	/**
	 * Subtract the given date.
	 * @param date the date.
	 * @return the resulting time difference.
	 */
	public long difference(UtilDate1 date) {
		return Math.abs(getTimeInMillis() - date.getTimeInMillis());
	}

	/**
	 * Parse this from the given string.
	 * @param date the string.
	 * @throws ParseException if the string is a bad format.
	 */
	public void fromString(String date) throws ParseException {
		setTimeInMillis(parseMillis(pattern, date));
	}

	/**
	 * Set this date as now!
	 */
	public void setNow() {
		setTimeInMillis(System.currentTimeMillis());
	}

	/**
	 * Set the hours.
	 * @param hours the hours.
	 */
	public void setHours(int hours) {
		set(HOUR_OF_DAY, hours);
	}

	/**
	 * Set the days.
	 * @param days the days.
	 */
	public void setDays(int days) {
		set(DAY_OF_MONTH, days);
	}

	/**
	 * Set the month.
	 * @param sets the hours with the silly java offset.
	 */
	public void setMonth(int month) {
		set(MONTH, month - 1);
	}

	/**
	 * Set the minutes.
	 * @param minutes the minutes.
	 */
	public void setMinutes(int minutes) {
		set(MINUTE, minutes);
	}

	/**
	 * Set the seconds.
	 * @param seconds the seconds.
	 */
	public void setSeconds(int seconds) {
		set(SECOND, seconds);
	}

	/**
	 * Set the milliseconds.
	 * @param milliseconds the milliseconds.
	 */
	public void setMilliseconds(int milliseconds) {
		set(MILLISECOND, milliseconds);
	}

	/**
	 * Rounds this date to the nearest time unit given in milliseconds. For example, if you want to round this date to the nearest minute, execute: round(1000 * 60);
	 * @param unit the time unit in milliseconds.
	 * @return this date.
	 */
	public UtilDate1 round(long unit) {
		long millis = getTimeInMillis();
		long msmodstep = millis % unit;
		long stepminmod = unit - msmodstep;
		long stepdiv2 = unit / 2;
		if (msmodstep > stepdiv2) {
			this.setTimeInMillis(millis + stepminmod);
		} else {
			this.setTimeInMillis(millis - msmodstep);
		}
		return this;
	}

	/**
	 * Returns the time at the beginning of the given unit.
	 * @param unit the unit time in milliseconds.
	 * @return the time.
	 */
	public long getBeginMillis(long unit) {
		long millis = getTimeInMillis();
		long difference = millis % unit;
		return millis -= difference;
	}

	/**
	 * Returns the time at the end of the given unit.
	 * @param unit the unit time in milliseconds.
	 * @return the time.
	 */
	public long getEndMillis(long unit) {
		return getBeginMillis(unit) + unit;
	}

	/**
	 * Returns the time at the beginning of the given unit.
	 * @param unit the unit time in milliseconds.
	 * @return the time.
	 */
	public long getBeginMillis(Milliseconds unit) {
		return getBeginMillis(unit.getMillis());
	}

	/**
	 * Returns the time at the end of the given unit.
	 * @param unit the unit time in milliseconds.
	 * @return the time.
	 */
	public long getEndMillis(Milliseconds unit) {
		return getEndMillis(unit.getMillis());
	}
}
