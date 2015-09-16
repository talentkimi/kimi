package core.util.time;

/**
 * Years.
 */
public class Years extends Milliseconds {

	/**
	 * Creates a new years.
	 * @param years the years.
	 */
	public Years(long years) {
		super(years * YEAR);
	}

	public Years() {
	}

}
