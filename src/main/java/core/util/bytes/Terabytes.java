package core.util.bytes;

/**
 * Megabytes.
 */
public class Terabytes extends Bytes {

	/**
	 * Creates a new terabytes.
	 * @param terabytes the terabytes.
	 */
	public Terabytes(long terabytes) {
		super(terabytes * TERABYTE);
	}

}
