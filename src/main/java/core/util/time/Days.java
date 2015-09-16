package core.util.time;

/**
 * Days.
 */
public class Days extends Milliseconds {

	/**
	 * Creates a new days.
	 * @param days the days.
	 */
	public Days(long days) {
		super(days * DAY);
	}

	public Days() {
	}
	
}
