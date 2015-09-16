package core.util.time;

/**
 * Minutes.
 */
public class Minutes extends Milliseconds {

	/**
	 * Creates a new minutes.
	 * @param minutes the minutes.
	 */
	public Minutes(long minutes) {
		super(minutes * MINUTE);
	}

	public Minutes() {
	}
	
}
