package core.util;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import core.text.DateUtil;
import core.text.StringParsable;
import core.text.Text;
import core.util.time.Milliseconds;

/**
 * A GMT Date. A light-weight version of GregorianCalendar.
 * <p>
 * N.B. This class is NOT thread safe.
 * <p>
 * N.B. Months range from 1 to 12, not 0 to 11 like Calendar, the month index methods act like the Calendar month.
 */
public final class UtilDate implements Comparable<UtilDate>, StringParsable {

	/** The day names. */
	public static final String[] DAY_NAMES = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
	/** The month names. */
	public static final String[] MONTH_NAMES = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	/** The days per month. */
	public static final byte[] DAYS_PER_MONTH = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

	/** The number of milliseconds per second. */
	public static final int MILLISECONDS_PER_SECOND = 1000;
	/** The number of seconds per minute. */
	public static final int SECONDS_PER_MINUTE = 60;
	/** The number of minutes per hour. */
	public static final int MINUTES_PER_HOUR = 60;
	/** The number of hours per day. */
	public static final int HOURS_PER_DAY = 24;
	/** The number of months per year. */
	public static final int MONTHS_PER_YEAR = 12;

	/** The date format. */
	public static final String FORMAT_PATTERN = "dd/MM/yy-HH:mm";
	/** The date format. */
	public static final String FORMAT_PATTERN_YEAR = "dd/MM/yyyy-HH:mm";
	/** The date format. */
	public static final String FORMAT_PATTERN_SECONDS = "dd/MM/yyyy-HH:mm:ss";
	/** The date format. */
	public static final String FORMAT_PATTERN_MILLIS = "dd/MM/yyyy-HH:mm:ss SSS";
	/** The date format. */
	public static final String DOB_PATTERN = "dd/MM/yy";

	/**
	 * Set the default Timezone.
	 */
	static {
		TimeZone.setDefault(DateUtil.DEFAULT_TIME_ZONE);
	}

	/**
	 * Validate the given day.
	 * @param day the day.
	 * @param month the month.
	 * @param year the year.
	 */
	private static final void validateDayOfMonth(int day, int month, int year) {
		int monthIndex = month - 1;
		if (day > DAYS_PER_MONTH[monthIndex]) {

			// Leap year?
			if (monthIndex == Calendar.FEBRUARY) {
				Calendar calendar = DateUtil.getCalendar();
				calendar.set(Calendar.YEAR, year);
				calendar.set(Calendar.MONTH, Calendar.FEBRUARY);
				calendar.set(Calendar.DATE, day);
				if (calendar.get(Calendar.DATE) != day) {
					throw new IllegalArgumentException("day=" + day + ", month=" + month + ", year=" + year + " (not leap year)");
				}
			}
			throw new IllegalArgumentException("day=" + day + ", month=" + month);
		}
	}

	/** The invalid date flag. */
	private static final short INVALID_DATE = Short.MIN_VALUE;
	/** The invalid time in milliseconds flag. */
	private static final long INVALID_TIME_IN_MILLISECONDS = Long.MIN_VALUE;

	/** The date format/parse pattern. */
	private String pattern = FORMAT_PATTERN;

	/** The year. */
	private short year;
	/** The month. */
	private byte month;
	/** The day. */
	private byte day;

	/** The hour. */
	private byte hour;
	/** The minute. */
	private byte minute;
	/** The second. */
	private byte second;
	/** The millisecond. */
	private short millisecond = INVALID_DATE;

	/** The time in milliseconds. */
	private long timeInMilliseconds = INVALID_TIME_IN_MILLISECONDS;

	/**
	 * Creates a quick date.
	 * @param year the year.
	 * @param month the month.
	 * @param day the day.
	 * @param hour the hour.
	 * @param minute the minute.
	 * @param second the second.
	 */
	public UtilDate(int year, int month, int day, int hour, int minute, int second, int millisecond) {
		if (year < 1900 || year > 2100) {
			throw new IllegalArgumentException("year=" + year);
		}
		if (month < 1 || month > 12) {
			throw new IllegalArgumentException("month=" + month);
		}
		if (day < 1 || day > 31) {
			throw new IllegalArgumentException("day=" + day);
		}
		if (hour < 0 || hour > 23) {
			throw new IllegalArgumentException("hour=" + hour);
		}
		if (minute < 0 || minute > 59) {
			throw new IllegalArgumentException("minute=" + minute);
		}
		if (second < 0 || second > 59) {
			throw new IllegalArgumentException("minute=" + minute);
		}
		if (millisecond < 0 || millisecond > 999) {
			throw new IllegalArgumentException("millisecond=" + millisecond);
		}
		if (day > 28) {
			validateDayOfMonth(day, month, year);
		}
		this.year = (short) year;
		this.month = (byte) month;
		this.day = (byte) day;
		this.hour = (byte) hour;
		this.minute = (byte) minute;
		this.second = (byte) second;
		this.millisecond = (short) millisecond;
	}

	/**
	 * Creates a quick date.
	 * @param year the year.
	 * @param month the month.
	 * @param day the day.
	 * @param hour the hour.
	 * @param minute the minute.
	 * @param second the second.
	 */
	public UtilDate(int year, int month, int day, int hour, int minute, int second) {
		this(year, month, day, hour, minute, second, 0);
	}

	/**
	 * Creates a quick date.
	 * @param year the year.
	 * @param month the month.
	 * @param day the day.
	 * @param hour the hour.
	 * @param minute the minute.
	 */
	public UtilDate(int year, int month, int day, int hour, int minute) {
		this(year, month, day, hour, minute, 0, 0);
	}

	/**
	 * Creates a quick date.
	 * @param year the year.
	 * @param month the month.
	 * @param day the day.
	 */
	public UtilDate(int year, int month, int day) {
		this(year, month, day, 0, 0, 0, 0);
	}

	/**
	 * Creates a new date with the given milliseconds.
	 * @param millis the milliseconds.
	 */
	public UtilDate(long millis, String pattern) {
		this.pattern = pattern;
		setTimeInMillis(millis);
	}

	/**
	 * Creates a new date with the given milliseconds.
	 * @param millis the milliseconds.
	 */
	public UtilDate(String date) throws ParseException {
		this.pattern = FORMAT_PATTERN;
		long millis = parseMillis(pattern, date);
		setTimeInMillis(millis);
	}

	/**
	 * Creates a new date with the given milliseconds.
	 * @param millis the milliseconds.
	 */
	public UtilDate(UtilDate date) {
		this.pattern = date.pattern;
		this.year = date.year;
		this.month = date.month;
		this.day = date.day;
		this.hour = date.hour;
		this.minute = date.minute;
		this.second = date.second;
		this.millisecond = date.millisecond;
		this.timeInMilliseconds = date.timeInMilliseconds;
	}

	/**
	 * Creates a new date with the given milliseconds.
	 * @param millis the milliseconds.
	 */
	public UtilDate(int seconds, String pattern) {
		this(seconds * 1000l, pattern);
	}

	/**
	 * Creates a new date with the given milliseconds.
	 * @param millis the milliseconds.
	 */
	public UtilDate(long millis) {
		this(millis, FORMAT_PATTERN);
	}

	/**
	 * Creates a new date with the given milliseconds.
	 * @param millis the milliseconds.
	 */
	public UtilDate(int seconds) {
		this(seconds * 1000l, FORMAT_PATTERN);
	}

	/**
	 * Creates a new date.
	 */
	public UtilDate() {
		this(System.currentTimeMillis());
	}

	/**
	 * Creates a quick date.
	 * @param pattern the pattern to use for parsing and formatting.
	 * @param date the date string to parse.
	 */
	public UtilDate(String pattern, String date) throws ParseException {
		this.timeInMilliseconds = parseMillis(pattern, date);
		this.pattern = pattern;
	}

	/**
	 * Returns the date in milliseconds parsed from the given date.
	 * @param pattern the date pattern.
	 * @param date the date string.
	 * @return the date in milliseconds
	 */
	public static final long parseMillis(String pattern, String date) throws ParseException {
		return DateUtil.parse(pattern, date).getTime();
	}

	/**
	 * Returns a thread-local calendar initialised to this date.
	 * @return a thread-local calendar initialised to this date.
	 */
	protected final Calendar getCalendar() {
		Calendar calendar = DateUtil.getCalendar();
		if (millisecond == INVALID_DATE) {
			calendar.setTimeInMillis(timeInMilliseconds);
		} else {
			calendar.set(year, month - 1, day, hour, minute, second);
			calendar.set(Calendar.MILLISECOND, millisecond);
		}
		return calendar;
	}

	/**
	 * Validates the time in milliseconds if necessary.
	 */
	protected final void validateTimeInMilliseconds() {
		if (timeInMilliseconds == INVALID_TIME_IN_MILLISECONDS) {
			Calendar calendar = DateUtil.getCalendar();
			calendar.set(year, month - 1, day, hour, minute, second);
			calendar.set(Calendar.MILLISECOND, millisecond);
			timeInMilliseconds = calendar.getTimeInMillis();
		}
	}

	/**
	 * Invalidates the time in milliseconds.
	 */
	protected final void invalidateTimeInMilliseconds() {
		timeInMilliseconds = INVALID_TIME_IN_MILLISECONDS;
	}

	/**
	 * Validates the date if necessary.
	 */
	protected final void validateDate() {
		if (millisecond == INVALID_DATE) {
			Calendar calendar = DateUtil.getCalendar();
			calendar.setTimeInMillis(timeInMilliseconds);
			setDate(calendar);
		}
	}

	/**
	 * Invalidates the date.
	 */
	protected final void invalidateDate() {
		millisecond = INVALID_DATE;
	}

	/**
	 * Returns the hash code.
	 * @return the hash code.
	 * @see java.util.Date#hashCode()
	 */
	public final int hashCode() {
		long millis = getTimeInMillis();
		return (int) millis ^ (int) (millis >> 32);
	}

	/**
	 * Returns true if this is equal to the given object.
	 * @param object the object.
	 * @return true if this is equal to the given object.
	 */
	public final boolean equals(Object object) {
		if (object == null) {
			return false;
		}
		if (object == this) {
			return true;
		}
		if (object instanceof UtilDate) {
			UtilDate date = (UtilDate) object;
			this.validateDate();
			date.validateDate();
			return this.year == date.year && this.month == date.month && this.day == date.day && this.hour == date.hour && this.minute == date.minute && this.second == date.second && this.millisecond == date.millisecond;
		}
		return false;
	}

	// SETTERS //

	/**
	 * Sets the time in milliseconds.
	 * @param millis the milliseconds.
	 */
	public final void setTimeInMillis(long millis) {
		if (this.timeInMilliseconds != millis) {
			invalidateDate();
			this.timeInMilliseconds = millis;
			this.validateDate();
		}
	}

	/**
	 * Sets the time in milliseconds as now.
	 */
	public final void setTimeInMillisNow() {
		setTimeInMillis(System.currentTimeMillis());
	}

	/**
	 * Set this date as now!
	 */
	public void setNow() {
		setTimeInMillis(System.currentTimeMillis());
	}

	/**
	 * Sets the date from the given calendar.
	 * @param calendar the calendar.
	 */
	public final void setDate(Calendar calendar) {
		year = (short) calendar.get(Calendar.YEAR);
		month = (byte) (calendar.get(Calendar.MONTH) + 1);
		day = (byte) calendar.get(Calendar.DATE);
		hour = (byte) calendar.get(Calendar.HOUR_OF_DAY);
		minute = (byte) calendar.get(Calendar.MINUTE);
		second = (byte) calendar.get(Calendar.SECOND);
		millisecond = (short) calendar.get(Calendar.MILLISECOND);
	}

	/**
	 * Sets the pattern, setting it to null will set the default pattern.
	 * @param pattern the pattern.
	 */
	public final void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * Sets the year.
	 * @param year the year.
	 */
	public final void setYear(int year) {
		if (year < 1900 || year > 2100) {
			throw new IllegalArgumentException("year=" + year);
		}
		validateDate();
		if (this.year != year) {
			this.year = (short) year;
			invalidateTimeInMilliseconds();
		}
	}

	/**
	 * Sets the month.
	 * @param month the month.
	 */
	public final void setMonth(int month) {
		validateDate();
		if (month >= 1 && month <= 12) {
			if (this.month != month) {
				this.month = (byte) month;
				invalidateTimeInMilliseconds();
			}
		} else {
			Calendar calendar = getCalendar();
			calendar.set(Calendar.MONTH, month - 1);
			setDate(calendar);
			invalidateTimeInMilliseconds();
		}
	}

	/**
	 * Sets the day.
	 * @param day the day.
	 */
	public final void setDays(int day) {
		validateDate();
		if (day >= 1 && day <= 28) {
			if (this.day != day) {
				this.day = (byte) day;
				invalidateTimeInMilliseconds();
			}
		} else {
			Calendar calendar = getCalendar();
			calendar.set(Calendar.DAY_OF_MONTH, day);
			setDate(calendar);
			invalidateTimeInMilliseconds();
		}
	}

	/**
	 * Sets the hour.
	 * @param hour the hour.
	 */
	public final void setHours(int hour) {
		validateDate();
		if (hour >= 0 && hour <= 23) {
			if (this.hour != hour) {
				this.hour = (byte) hour;
				invalidateTimeInMilliseconds();
			}
		} else {
			Calendar calendar = getCalendar();
			calendar.set(Calendar.HOUR_OF_DAY, hour);
			setDate(calendar);
			invalidateTimeInMilliseconds();
		}
	}

	/**
	 * Sets the minutes.
	 * @param minute the minute.
	 */
	public final void setMinutes(int minute) {
		validateDate();
		if (minute >= 0 && minute <= 59) {
			if (this.minute != minute) {
				this.minute = (byte) minute;
				invalidateTimeInMilliseconds();
			}
		} else {
			Calendar calendar = getCalendar();
			calendar.set(Calendar.MINUTE, minute);
			setDate(calendar);
			invalidateTimeInMilliseconds();
		}
	}

	/**
	 * Sets the seconds.
	 * @param seconds the seconds.
	 */
	public final void setSeconds(int seconds) {
		validateDate();
		if (seconds >= 0 && seconds <= 59) {
			if (this.second != seconds) {
				this.second = (byte) seconds;
				invalidateTimeInMilliseconds();
			}
		} else {
			Calendar calendar = getCalendar();
			calendar.set(Calendar.SECOND, seconds);
			setDate(calendar);
			invalidateTimeInMilliseconds();
		}
	}

	/**
	 * Sets the milliseconds.
	 * @param milliseconds the milliseconds.
	 */
	public final void setMilliseconds(int milliseconds) {
		validateDate();
		if (milliseconds >= 0 && milliseconds <= 999) {
			if (this.millisecond != milliseconds) {
				this.millisecond = (byte) milliseconds;
				invalidateTimeInMilliseconds();
			}
		} else {
			Calendar calendar = getCalendar();
			calendar.set(Calendar.MILLISECOND, milliseconds);
			setDate(calendar);
			invalidateTimeInMilliseconds();
		}
	}

	/**
	 * Returns true if it is morning.
	 * @return true if it is morning.
	 */
	public boolean isMorning() {
		return getHour() < 12;
	}

	// CALENDAR FIELDS & METHODS

	public static final int SATURDAY = Calendar.SATURDAY;
	public static final int SUNDAY = Calendar.SUNDAY;
	public static final int MONDAY = Calendar.MONDAY;
	public static final int TUESDAY = Calendar.TUESDAY;
	public static final int WEDNESDAY = Calendar.WEDNESDAY;
	public static final int THURSDAY = Calendar.THURSDAY;
	public static final int FRIDAY = Calendar.FRIDAY;

	public static final int MILLISECOND = Calendar.MILLISECOND;
	public static final int SECOND = Calendar.SECOND;
	public static final int MINUTE = Calendar.MINUTE;
	public static final int HOUR = Calendar.HOUR;
	public static final int HOUR_OF_DAY = Calendar.HOUR_OF_DAY;
	public static final int DAY_OF_WEEK = Calendar.DAY_OF_WEEK;
	public static final int DAY_OF_MONTH = Calendar.DAY_OF_MONTH;
	public static final int DAY_OF_YEAR = Calendar.DAY_OF_YEAR;
	public static final int DATE = Calendar.DATE;
	public static final int MONTH = Calendar.MONTH;
	public static final int YEAR = Calendar.YEAR;

	public int get(int field) {
		switch (field) {
			case MILLISECOND :
				return millisecond;
			case SECOND :
				return second;
			case MINUTE :
				return minute;
			case HOUR_OF_DAY :
				return hour;
			case DAY_OF_MONTH :
				return day;
			case MONTH :
				return month - 1;
			case YEAR :
				return year;

			case DAY_OF_YEAR :
				return getCalendar().get(DAY_OF_YEAR);
			case DAY_OF_WEEK :
				return getCalendar().get(DAY_OF_WEEK);
			case HOUR :
				return getCalendar().get(HOUR);
			default :
				throw new IllegalArgumentException("unknown field: " + field);
		}
	}

	/**
	 * Calendar set method.
	 * @param field the field.
	 * @param value the value.
	 */
	public void set(int field, int value) {
		switch (field) {
			case MILLISECOND :
				setMilliseconds(value);
				break;
			case SECOND :
				setSeconds(value);
				break;
			case MINUTE :
				setMinutes(value);
				break;
			case HOUR_OF_DAY :
				setHours(value);
				break;
			case DAY_OF_MONTH :
				setDays(value);
				break;
			case MONTH :
				setMonth(value + 1);
				break;
			case YEAR :
				setYear(value);
				break;

			case DAY_OF_YEAR :
				Calendar calendar = getCalendar();
				calendar.set(DAY_OF_YEAR, value);
				setDate(calendar);
				invalidateTimeInMilliseconds();
			case DAY_OF_WEEK :
				calendar = getCalendar();
				calendar.set(DAY_OF_WEEK, value);
				setDate(calendar);
				invalidateTimeInMilliseconds();
			case HOUR :
				calendar = getCalendar();
				calendar.set(HOUR, value);
				setDate(calendar);
				invalidateTimeInMilliseconds();
			default :
				throw new IllegalArgumentException("unknown field: " + field);
		}
	}

	/**
	 * Calendar set method.
	 * @param field the field.
	 * @param value the value.
	 */
	public void add(int field, int value) {
		switch (field) {
			case MILLISECOND :
				addMilliseconds(value);
				break;
			case SECOND :
				addSeconds(value);
				break;
			case MINUTE :
				addMinutes(value);
				break;
			case HOUR :
			case HOUR_OF_DAY :
				addHours(value);
				break;
			// case DATE :
			case DAY_OF_YEAR :
			case DAY_OF_WEEK :
			case DAY_OF_MONTH :
				addDays(value);
				break;
			case MONTH :
				addMonths(value);
				break;
			case YEAR :
				addYears(value);
				break;
			default :
				throw new IllegalArgumentException("unknown field: " + field);
		}
	}

	/**
	 * Gets what the first day of the week is.
	 * @return the first day of the week.
	 */
	public int getFirstDayOfWeek() {
		return getCalendar().getFirstDayOfWeek();
	}

	// GETTERS //

	/**
	 * Returns the pattern.
	 * @return the pattern.
	 */
	public final String getPattern() {
		return pattern;
	}

	/**
	 * Returns the time in milliseconds.
	 * @return the time in milliseconds.
	 */
	public final long getTimeInMillis() {
		validateTimeInMilliseconds();
		return timeInMilliseconds;
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
	 * Returns the time as a {@link java.util.Date}.
	 * @return the time as a {@link java.util.Date}.
	 */
	public final Date getTime() {
		return new Date(getTimeInMillis());
	}

	/**
	 * Returns the month name.
	 * @return the month name.
	 */
	public final String getMonthName() {
		validateDate();
		return MONTH_NAMES[month - 1];
	}

	/**
	 * Returns the year.
	 * @return the year.
	 */
	public final int getYear() {
		validateDate();
		return year;
	}

	/**
	 * Returns the month.
	 * @return the month.
	 */
	public final int getMonth() {
		validateDate();
		return month;
	}

	/**
	 * Returns the month.
	 * @return the month.
	 */
	public final int getMonthIndex() {
		return getMonth() - 1;
	}

	/**
	 * Returns the day.
	 * @return the day.
	 */
	public final int getDay() {
		validateDate();
		return day;
	}

	/**
	 * Returns the day of the week.
	 * @return the day of the week.
	 */
	public int getDayOfWeek() {
		return get(DAY_OF_WEEK);
	}

	/**
	 * Returns the hour.
	 * @return the hour.
	 */
	public final int getHour() {
		validateDate();
		return hour;
	}

	/**
	 * Returns the minute.
	 * @return the minute.
	 */
	public final int getMinute() {
		validateDate();
		return minute;
	}

	/**
	 * Returns the second.
	 * @return the second.
	 */
	public final int getSecond() {
		validateDate();
		return second;
	}

	/**
	 * Returns the millisecond (i.e. a value between 0 and 999 = do not confuse with getTimeInMillis).
	 * @return the millisecond.
	 */
	public final int getMillisecond() {
		validateDate();
		return millisecond;
	}

	/**
	 * Returns the day name.
	 * @return the day name.
	 */
	public String getDayName() {
		return DAY_NAMES[get(DAY_OF_WEEK) - 1];
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
	public final int getDaysBetween(UtilDate date) {
		UtilDate thisCopy = new UtilDate(getTimeInMillis());
		int amount = 1;
		if (thisCopy.after(date)) {
			amount = -1;
		}
		int days = 0;
		while (!thisCopy.onSameDayAs(date)) {
			thisCopy.addDays(amount);
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

	// ADD DATE //

	/**
	 * Add the given number of years.
	 * @param years the number of years to add.
	 */
	public final void addYears(int years) {
		if (years != 0) {
			validateDate();
			this.year += years;
			invalidateTimeInMilliseconds();
		}
	}

	/**
	 * Add the given number of months.
	 * @param months the number of months to add.
	 */
	public final void addMonths(int months) {
		if (months != 0) {
			validateDate();
			int newValue = this.month + months;
			if (newValue >= 1 && newValue <= MONTHS_PER_YEAR) {
				this.month = (byte) newValue;
			} else {
				Calendar calendar = getCalendar();
				calendar.set(Calendar.MONTH, newValue - 1);
				setDate(calendar);
			}
			invalidateTimeInMilliseconds();
		}
	}

	/**
	 * Add the given number of days.
	 * @param days the number of days to add.
	 */
	public final void addDays(int days) {
		if (days != 0) {
			validateDate();
			int newValue = this.day + days;
			if (newValue >= 1 && newValue <= DAYS_PER_MONTH[month - 1]) {
				this.day = (byte) newValue;
			} else {
				Calendar calendar = getCalendar();
				calendar.set(Calendar.DATE, newValue);
				setDate(calendar);
			}
			invalidateTimeInMilliseconds();
		}
	}

	/**
	 * Add the given number of hours.
	 * @param hours the number of hours to add.
	 */
	public final void addHours(int hours) {
		if (hours != 0) {
			validateDate();
			int newValue = this.hour + hours;
			if (newValue >= 0 && newValue < HOURS_PER_DAY) {
				this.hour = (byte) newValue;
			} else {
				Calendar calendar = getCalendar();
				calendar.set(Calendar.HOUR_OF_DAY, newValue);
				setDate(calendar);
			}
			invalidateTimeInMilliseconds();
		}
	}

	/**
	 * Add the given number of minutes.
	 * @param minutes the number of minutes to add.
	 */
	public final void addMinutes(int minutes) {
		if (minutes != 0) {
			validateDate();
			int newValue = this.minute + minutes;
			if (newValue >= 0 && newValue < MINUTES_PER_HOUR) {
				this.minute = (byte) newValue;
			} else {
				Calendar calendar = getCalendar();
				calendar.set(Calendar.MINUTE, newValue);
				setDate(calendar);
			}
			invalidateTimeInMilliseconds();
		}
	}

	/**
	 * Add the given number of seconds.
	 * @param seconds the number of seconds to add.
	 */
	public final void addSeconds(int seconds) {
		if (seconds != 0) {
			validateDate();
			int newValue = this.second + seconds;
			if (newValue >= 0 && newValue < SECONDS_PER_MINUTE) {
				this.second = (byte) newValue;
			} else {
				Calendar calendar = getCalendar();
				calendar.set(Calendar.SECOND, newValue);
				setDate(calendar);
			}
			invalidateTimeInMilliseconds();
		}
	}

	/**
	 * Add the given number of milliseconds.
	 * @param milliseconds the number of milliseconds to add.
	 */
	public final void addMilliseconds(int milliseconds) {
		if (milliseconds != 0) {
			validateDate();
			int newValue = this.millisecond + milliseconds;
			if (newValue >= 0 && newValue < MILLISECONDS_PER_SECOND) {
				this.millisecond = (short) newValue;
			} else {
				Calendar calendar = getCalendar();
				calendar.set(Calendar.MILLISECOND, newValue);
				setDate(calendar);
			}
			invalidateTimeInMilliseconds();
		}
	}

	/**
	 * Add the given number of milliseconds.
	 * @param millis the milliseconds.
	 */
	public UtilDate add(Milliseconds millis) {
		addMilliseconds((int) millis.getMillis());
		return this;
	}

	// AFTER DATE COMPARISONS //

	/**
	 * Returns true if this date is after the given date.
	 * @param date the date.
	 * @return true if this date is after the given date.
	 */
	public final boolean after(long timeInMillis) {
		return compareTo(timeInMillis) > 0;
	}

	/**
	 * Returns true if this date is after the given date.
	 * @param date the date.
	 * @return true if this date is after the given date.
	 */
	public final boolean after(UtilDate date) {
		return after(date.getTimeInMillis());
	}

	/**
	 * Returns true if this date is after the given date.
	 * @param date the date.
	 * @return true if this date is after the given date.
	 */
	public final boolean after(Date date) {
		return after(date.getTime());
	}

	/**
	 * Returns true if this date is after the given date.
	 * @param date the date.
	 * @return true if this date is after the given date.
	 */
	public final boolean after(String date) throws ParseException {
		return after(parseMillis(FORMAT_PATTERN, date));
	}

	// BEFORE DATE COMPARISONS

	/**
	 * Returns true if this date is before the given date.
	 * @param date the date.
	 * @return true if this date is before the given date.
	 */
	public final boolean before(long timeInMillis) {
		return compareTo(timeInMillis) < 0;
	}

	/**
	 * Returns true if this date is after the given date.
	 * @param date the date.
	 * @return true if this date is after the given date.
	 */
	public final boolean before(UtilDate date) {
		return before(date.getTimeInMillis());
	}

	/**
	 * Returns true if this date is after the given date.
	 * @param date the date.
	 * @return true if this date is after the given date.
	 */
	public final boolean before(Date date) {
		return before(date.getTime());
	}

	/**
	 * Returns true if this date is after the given date.
	 * @param date the date.
	 * @return true if this date is after the given date.
	 */
	public final boolean before(String date) throws ParseException {
		return before(parseMillis(FORMAT_PATTERN, date));
	}

	// SAME DATE COMPARISONS //

	/**
	 * Returns true if this is on the same hour as the given date.
	 * @param date the date.
	 * @return true if this is on the same hour as the given date.
	 */
	public final boolean onSameSecondAs(UtilDate date) {
		this.validateDate();
		date.validateDate();
		return this.year == date.year && this.month == date.month && this.day == date.day && this.hour == date.hour && this.minute == date.minute && this.second == date.second;
	}

	/**
	 * Returns true if this is on the same hour as the given date.
	 * @param date the date.
	 * @return true if this is on the same hour as the given date.
	 */
	public final boolean onSameMinuteAs(UtilDate date) {
		this.validateDate();
		date.validateDate();
		return this.year == date.year && this.month == date.month && this.day == date.day && this.hour == date.hour && this.minute == date.minute;
	}

	/**
	 * Returns true if this is on the same hour as the given date.
	 * @param date the date.
	 * @return true if this is on the same hour as the given date.
	 */
	public final boolean onSameHourAs(UtilDate date) {
		this.validateDate();
		date.validateDate();
		return this.year == date.year && this.month == date.month && this.day == date.day && this.hour == date.hour;
	}

	/**
	 * Returns true if this is on the same day as the given date.
	 * @param date the date.
	 * @return true if this is on the same day as the given date.
	 */
	public final boolean onSameDayAs(UtilDate date) {
		this.validateDate();
		date.validateDate();
		return this.year == date.year && this.month == date.month && this.day == date.day;
	}

	/**
	 * Returns true if this is on the same month as the given date.
	 * @param date the date.
	 * @return true if this is on the same month as the given date.
	 */
	public final boolean onSameMonthAs(UtilDate date) {
		this.validateDate();
		date.validateDate();
		return this.year == date.year && this.month == date.month;
	}

	/**
	 * Returns true if this is on the same year as the given date.
	 * @param date the date.
	 * @return true if this is on the same year as the given date.
	 */
	public final boolean onSameYearAs(UtilDate date) {
		this.validateDate();
		date.validateDate();
		return this.year == date.year;
	}

	// TO STRING //

	/**
	 * Returns this date as a string.
	 * @param pattern the pattern.
	 * @return this date as a string.
	 */
	public final String toString(String pattern) {
		if (pattern.equals(FORMAT_PATTERN)) {
			pattern = FORMAT_PATTERN_YEAR;
		}
		return DateUtil.format(pattern, getTime());
	}

	/**
	 * Returns this date as a string.
	 * @return this date as a string.
	 */
	public final String toString() {

		// Default pattern
		if (pattern == null) {
			validateDate();
			StringBuilder date = new StringBuilder(16);

			// Year-Month-Day
			date.append(year);
			date.append('-');
			if (month < 10) {
				date.append('0');
			}
			date.append(month);
			date.append('-');
			if (day < 10) {
				date.append('0');
			}
			date.append(day);
			date.append(' ');

			// Hour:Minute
			if (hour < 10) {
				date.append('0');
			}
			date.append(hour);
			date.append(':');
			if (minute < 10) {
				date.append('0');
			}
			date.append(minute);

			return date.toString();
		}
		return toString(pattern);
	}

	/**
	 * Returns a copy of this webdate.
	 * @return a copy of this webdate.
	 */
	public UtilDate copy() {
		return new UtilDate(getTimeInMillis(), getPattern());
	}

	/**
	 * Returns the difference in millis.
	 * @param date the date.
	 * @return the difference.
	 */
	public long getDifferenceInMillis(UtilDate date) {
		long time1 = this.getTimeInMillis();
		long time2 = date.getTimeInMillis();
		if (time1 < time2) {
			return time2 - time1;
		} else {
			return time1 - time2;
		}
	}

	/**
	 * Subtract the given date.
	 * @param date the date.
	 * @return the resulting time difference.
	 */
	public long difference(UtilDate date) {
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
	 * Rounds this date to the nearest time unit given in milliseconds. For example, if you want to round this date to the nearest minute, execute: round(1000 * 60);
	 * @param unit the time unit in milliseconds.
	 * @return this date.
	 */
	public UtilDate round(long unit) {
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

	/**
	 * Compares this to the given date.
	 * @param date the date.
	 * @return the comparison.
	 */
	public final int compareTo(UtilDate date) {
		date.validateTimeInMilliseconds();
		return compareTo(date.getTimeInMillis());
	}

	/**
	 * Compares this to the given date.
	 * @param date the date.
	 * @return the comparison.
	 */
	public final int compareTo(Date date) {
		return compareTo(date.getTime());
	}

	/**
	 * Compares this to the given date.
	 * @param date the date.
	 * @return the comparison.
	 */
	public final int compareTo(long timeInMillis) {
		this.validateTimeInMilliseconds();
		long thisInMillis = timeInMilliseconds;
		return (thisInMillis > timeInMillis) ? 1 : (thisInMillis == timeInMillis) ? 0 : -1;
	}

	public static void main(String[] args) {
		int month = 0; // January (index month)
		UtilDate date = new UtilDate();
		UtilDate1 date1 = new UtilDate1();
		System.out.println("before=" + date);
		System.out.println("before=" + date1);
		date.set(Calendar.MONTH, month);
		date1.set(Calendar.MONTH, month);
		System.out.println("after=" + date);
		System.out.println("after=" + date1);
	}
	
	/**
	 * Converts the UtilDate to a java.util.Date instance
	 * @return java.util.Date instance of this date
	 */
	public Date getDate() {
		return new Date(this.getTimeInMillis());
	}
	
	/**
	 * Clears time and leaves only date
	 */
	public final void clearTime() {
		validateDate();
		minute = 0;
		hour = 0;
		second = 0;
		millisecond = 0;
		invalidateTimeInMilliseconds();
	}
}
