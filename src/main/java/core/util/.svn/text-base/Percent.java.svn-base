package core.util;

/**
 * A Percentage.
 */
public final class Percent {

	/** The percent. */
	private final byte percent;

	/**
	 * Returns the percentage.
	 * @return the percentage.
	 */
	public int get() {
		return percent;
	}

	/**
	 * Returns true if this is less than the given percent.
	 * @param percent the percent.
	 * @return true if this is less than the given percent.
	 */
	public boolean isGreaterThan(int percent) {
		if (percent < 0 || percent > 100) {
			throw new IllegalArgumentException("percent=" + percent);
		}
		return this.percent > percent;
	}

	/**
	 * Returns true if this is less than the given percent.
	 * @param percent the percent.
	 * @return true if this is less than the given percent.
	 */
	public boolean isLessThan(int percent) {
		if (percent < 0 || percent > 100) {
			throw new IllegalArgumentException("percent=" + percent);
		}
		return this.percent < percent;
	}

	/**
	 * Creates a new Percent.
	 * @param percent the percent.
	 */
	public Percent(int percent) {
		if (percent < 0 || percent > 100) {
			throw new IllegalArgumentException("percent=" + percent);
		}
		this.percent = (byte) percent;
	}

}
