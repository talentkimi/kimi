package core.util.time;

/**
 * Weeks.
 */
public class Weeks extends Milliseconds {

	/**
	 * Creates a new weeks.
	 * @param weeks the weeks.
	 */
	public Weeks(long weeks) {
		super(weeks * WEEK);
	}

	public Weeks() {
	}
	
}
