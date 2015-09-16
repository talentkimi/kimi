package core.util.bytes;


/**
 * Bytes.
 */
public class Bytes {

	/** The bytes per byte! */
	public static final long BYTE = 1;
	/** The bytes per kilobyte. */
	public static final long KILOBYTE = BYTE * 1024;
	/** The bytes per megabyte. */
	public static final long MEGABYTE = KILOBYTE * 1024;
	/** The bytes per gigabyte. */
	public static final long GIGABYTE = MEGABYTE * 1024;
	/** The bytes per terabyte. */
	public static final long TERABYTE = GIGABYTE * 1024;

	/** The bytes. */
	private final long bytes;

	/**
	 * Returns the byte.
	 * @return the byte.
	 */
	public final long getBytes() {
		return bytes;
	}

	/**
	 * Creates a new byte.
	 * @param bytes the bytes.
	 */
	public Bytes(long bytes) {
		if (bytes < 0) {
			throw new IllegalArgumentException("bytes=" + bytes);
		}
		this.bytes = bytes;
	}

	/**
	 * Returns this as a string.
	 * @return this as a string.
	 */
	public String toString() {
		return String.valueOf(bytes);
	}

	public static void main(String[] args) {
		System.out.println(new Bytes(1));
		System.out.println(new Kilobytes(1));
		System.out.println(new Megabytes(1));
		System.out.println(new Gigabytes(1));
		System.out.println(new Terabytes(1));
	}

}
