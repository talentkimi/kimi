package core.util.bytes;

/**
 * Kilobytes.
 */
public class Kilobytes extends Bytes {

	/**
	 * Creates a new kilobytes.
	 * @param kilobytes the kilobytes.
	 */
	public Kilobytes(long kilobytes) {
		super(kilobytes * KILOBYTE);
	}

}
