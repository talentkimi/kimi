package core.util.time;

/**
 * Milliseconds.
 */
public class Milliseconds implements Comparable<Milliseconds> {

	/** The milliseconds per millisecond! */
	public static final long MILLISECOND = 1;
	/** The milliseconds per second. */
	public static final long SECOND = 1000;
	/** The milliseconds per minute. */
	public static final long MINUTE = SECOND * 60;
	/** The milliseconds per hour. */
	public static final long HOUR = MINUTE * 60;
	/** The milliseconds per day. */
	public static final long DAY = HOUR * 24;
	/** The milliseconds per week. */
	public static final long WEEK = DAY * 7;
	/** The milliseconds per month (assumption: 30 days per month). */
	public static final long MONTH = DAY * 30;
	/** The milliseconds per year (assumption: not a leap year). */
	public static final long YEAR = DAY * 365;

	/** The millis. */
	private final long millis;

	@Override
	public int compareTo(Milliseconds other) {
		return (this.millis > other.millis ? 1 : this.millis < other.millis ? -1 : 0);
	}
	
	/**
	 * Returns the millisecond.
	 * @return the millisecond.
	 */
	public final long getMillis() {
		return millis;
	}

	/**
	 * Returns the seconds.
	 * @return the seconds.
	 */
	public final long getSeconds() {
		return millis / SECOND;
	}

	/**
	 * Returns the hashcode.
	 * @return the hashcode.
	 */
	public final int hashCode() {
		return (int) millis;
	}

	/**
	 * Returns true if this equals the given object.
	 * @param object the object.
	 * @return true if this equals the given object.
	 */
	public final boolean equals(Object object) {
		if (object instanceof Milliseconds) {
			return ((Milliseconds) object).getMillis() == millis;
		}
		return false;
	}

	/**
	 * Returns this as a string.
	 * @return this as a string.
	 */
	public String toString() {
		return String.valueOf(millis);
	}

	/**
	 * Creates a new millisecond.
	 * @param millis the millis.
	 */
	public Milliseconds(long millis) {
		if (millis < 0) {
			throw new IllegalArgumentException("millis=" + millis);
		}
		this.millis = millis;
	}

	/**
	 * Creates a new millisecond.
	 * @param millis the millis.
	 */
	public Milliseconds(String millis) {
		this(Long.parseLong(millis));
	}

	public Milliseconds() {
		this(System.currentTimeMillis());
	}

	public Milliseconds subtract(Milliseconds millis) {
		return new Milliseconds(this.millis - millis.millis);
	}

	public Milliseconds add(Milliseconds millis) {
		return new Milliseconds(this.millis + millis.millis);
	}

	public Milliseconds subtract(long millis) {
		return new Milliseconds(this.millis - millis);
	}

	public Milliseconds add(long millis) {
		return new Milliseconds(this.millis + millis);
	}

	public boolean isLessThan(Milliseconds millis) {
		return this.millis < millis.millis;
	}

	public boolean isGreaterThan(Milliseconds millis) {
		return this.millis > millis.millis;
	}

	public boolean isLessThan(long millis) {
		return this.millis < millis;
	}

	public boolean isGreaterThan(long millis) {
		return this.millis > millis;
	}

	public void sleep() throws InterruptedException {
		Thread.sleep(this.millis);
	}

	public static void main(String[] args) {
		System.out.println(new Milliseconds(1));
		System.out.println(new Seconds(1));
		System.out.println(new Minutes(1));
		System.out.println(new Hours(1));
		System.out.println(new Days(1));
		System.out.println(new Weeks(1));
		System.out.println(new Months(1));
		System.out.println(new Years(1));
	}

}
