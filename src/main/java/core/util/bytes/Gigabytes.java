package core.util.bytes;

/**
 * Megabytes.
 */
public class Gigabytes extends Bytes {

	/**
	 * Creates a new gigabytes.
	 * @param gigabytes the gigabytes.
	 */
	public Gigabytes(long gigabytes) {
		super(gigabytes * GIGABYTE);
	}

}
