package core.util;

import core.util.bytes.Bytes;

/**
 * A Utilility Byte.
 */
public class UtilBytes extends Bytes {

	/**
	 * Creates a new util time.
	 * @param bytes the bytes.
	 */
	public UtilBytes(long bytes) {
		super(bytes);
	}

	/**
	 * Returns this as a string.
	 * @return this as a string.
	 */
	public String toString() {
		long bytes = getBytes();
		StringBuilder buffer = new StringBuilder();
		bytes = append(bytes, buffer, TERABYTE, "terabyte", true);
		bytes = append(bytes, buffer, GIGABYTE, "gigabyte", true);
		bytes = append(bytes, buffer, MEGABYTE, "megabyte", true);
		bytes = append(bytes, buffer, KILOBYTE, "kilobyte", true);
		bytes = append(bytes, buffer, BYTE, "byte", false);
		return buffer.toString();
	}

	/**
	 * @param buffer
	 * @param year
	 * @param string
	 * @return
	 */
	private long append(long bytes, StringBuilder buffer, long timeUnit, String description, boolean comma) {
		long time = bytes / timeUnit;
		if (!comma || time > 0) {
			buffer.append(time).append(' ').append(description).append(time == 1 ? "" : "s").append(comma ? ", " : "");
			bytes -= time * timeUnit;
		}
		return bytes;
	}

}
