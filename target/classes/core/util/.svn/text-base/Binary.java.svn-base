package core.util;

/**
 * A collection of useful byte array-manipulation functions.
 */
public final class Binary {

	/** The invalid index. * */
	private static final int INVALID_INDEX = -1;

	/**
	 * Inaccessible Constructor.
	 */
	private Binary() {
	}

	/**
	 * Returns the index of the needle in the haystack!
	 * @param haystack the haystack.
	 * @param needle the needle.
	 * @param index the index to search from.
	 * @return the index.
	 */
	public static int indexOf(byte[] haystack, byte[] needle, int index) {
		int needleIndex = 0;
		for (int haystackIndex = index; haystackIndex < haystack.length; haystackIndex++) {
			if (needle[needleIndex] == haystack[haystackIndex]) {
				needleIndex++;
				if (needleIndex == needle.length) {
					return haystackIndex - needle.length + 1;
				}
			} else {
				needleIndex = 0;
			}
		}
		return -1;
	}

	/**
	 * Returns the index of the needle in the haystack!
	 * @param haystack the haystack.
	 * @param needle the needle.
	 * @return the index.
	 */
	public static int indexOf(byte[] haystack, byte[] needle) {
		return indexOf(haystack, needle, 0);
	}

	/**
	 * Optimized method for inserting a boolean into a byte array.
	 */
	public static final void putBoolean(boolean bool, byte[] b, int offset) {
		b[offset] = (bool ? (byte) 0 : (byte) 1);
	}

	/**
	 * Optimized method for inserting a double into a byte array.
	 */
	public static final void putDouble(double d, byte[] b, int offset) {
		long l = Double.doubleToLongBits(d);
		putLong(l, b, offset);
	}

	/**
	 * Optimized method for inserting a float into a byte array.
	 */
	public static final void putFloat(float f, byte[] b, int offset) {
		int i = Float.floatToIntBits(f);
		putInt(i, b, offset);
	}

	/**
	 * Optimized method for inserting a long into a byte array.
	 */
	public static final void putLong(long l, byte[] b, int offset) {
		b[offset + 0] = (byte) (l >> 56);
		b[offset + 1] = (byte) (l >> 48);
		b[offset + 2] = (byte) (l >> 40);
		b[offset + 3] = (byte) (l >> 32);
		b[offset + 4] = (byte) (l >> 24);
		b[offset + 5] = (byte) (l >> 16);
		b[offset + 6] = (byte) (l >> 8);
		b[offset + 7] = (byte) (l >> 0);
	}

	/**
	 * Optimized method for inserting a long into a byte array.
	 */
	public static final void putLong(long l, byte[] b) {
		putLong(l, b, 0);
	}

	/**
	 * Optimized method for inserting an integer into a byte array.
	 */
	public static final void putInt(int i, byte[] b, int offset) {
		b[offset + 0] = (byte) (i >> 24);
		b[offset + 1] = (byte) (i >> 16);
		b[offset + 2] = (byte) (i >> 8);
		b[offset + 3] = (byte) (i >> 0);
	}

	/**
	 * Optimized method for inserting a integer into a byte array.
	 */
	public static final void putInt(int i, byte[] b) {
		putInt(i, b, 0);
	}

	/**
	 * Optimized method for inserting an short into a byte array.
	 */
	public static final void putShort(short s, byte[] b, int offset) {
		b[offset + 0] = (byte) (s >> 8);
		b[offset + 1] = (byte) (s >> 0);
	}

	/**
	 * Optimized method for inserting a short into a byte array.
	 */
	public static final void putShort(short s, byte[] b) {
		putShort(s, b, 0);
	}

	/**
	 * Optimized method for inserting a character into a byte array.
	 */
	public static final void putChar(char c, byte[] b, int offset) {
		putInt(c, b, offset);
	}

	/**
	 * Optimized method for inserting a character into a byte array.
	 */
	public static final void putChar(char c, byte[] b) {
		putInt(c, b);
	}

	/**
	 * Optimized method for converting a byte to a boolean.
	 */
	public static final boolean getBoolean(byte b) {
		return b == 0;
	}

	/**
	 * Optimized method for converting 8 bytes to a double.
	 */
	public static final double getDouble(byte b0, byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7) {
		return Double.longBitsToDouble(getLong(b0, b1, b2, b3, b4, b5, b6, b7));
	}

	/**
	 * Optimized method for converting 8 bytes to a double.
	 */
	public static final double getDouble(byte[] b, int offset) {
		return getDouble(b[offset + 0], b[offset + 1], b[offset + 2], b[offset + 3], b[offset + 4], b[offset + 5], b[offset + 6], b[offset + 7]);
	}

	/**
	 * Optimized method for converting 8 bytes to a double.
	 */
	public static final double getDouble(byte[] b) {
		return getDouble(b, 0);
	}

	/**
	 * Optimized method for converting 4 bytes to a float.
	 */
	public static final float getFloat(byte b0, byte b1, byte b2, byte b3) {
		return Float.intBitsToFloat(getInt(b0, b1, b2, b3));
	}

	/**
	 * Optimized method for converting 4 bytes to a float.
	 */
	public static final float getFloat(byte[] b, int offset) {
		return getFloat(b[offset + 0], b[offset + 1], b[offset + 2], b[offset + 3]);
	}

	/**
	 * Optimized method for converting 4 bytes to a float.
	 */
	public static final float getFloat(byte[] b) {
		return getFloat(b, 0);
	}

	/**
	 * Optimized method for converting 8 bytes to a long.
	 */
	public static final long getLong(byte b0, byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7) {
		return ((((long) b0 & 0xff) << 56) | (((long) b1 & 0xff) << 48) | (((long) b2 & 0xff) << 40) | (((long) b3 & 0xff) << 32) | (((long) b4 & 0xff) << 24) | (((long) b5 & 0xff) << 16) | (((long) b6 & 0xff) << 8) | (((long) b7 & 0xff) << 0));
	}

	/**
	 * Optimized method for converting 8 bytes to a long.
	 */
	public static final long getLong(byte[] b, int offset) {
		return getLong(b[offset + 0], b[offset + 1], b[offset + 2], b[offset + 3], b[offset + 4], b[offset + 5], b[offset + 6], b[offset + 7]);
	}

	/**
	 * Optimized method for converting 8 bytes to a long.
	 */
	public static final long getLong(byte[] b) {
		return getLong(b, 0);
	}

	/**
	 * Optimized method for converting 4 bytes to an integer.
	 */
	public static final int getInt(byte b0, byte b1, byte b2, byte b3) {
		return ((b0 & 0xff) << 24) | ((b1 & 0xff) << 16) | ((b2 & 0xff) << 8) | ((b3 & 0xff) << 0);
	}

	/**
	 * Optimized method for converting 4 bytes to an integer.
	 */
	public static final int getInt(byte[] b, int offset) {
		return getInt(b[offset + 0], b[offset + 1], b[offset + 2], b[offset + 3]);
	}

	/**
	 * Optimized method for converting 4 bytes to an integer.
	 */
	public static final int getInt(byte[] b) {
		return getInt(b, 0);
	}

	/**
	 * Optimized method for converting 2 bytes to a short.
	 */
	public static final short getShort(byte b0, byte b1) {
		return (short) (((b0 & 0xff) << 8) | ((b1 & 0xff) << 0));
	}

	/**
	 * Optimized method for converting 2 bytes to a short.
	 */
	public static final short getShort(byte[] b, int offset) {
		return getShort(b[offset + 0], b[offset + 1]);
	}

	/**
	 * Optimized method for converting 2 bytes to a short.
	 */
	public static final short getShort(byte[] b) {
		return getShort(b, 0);
	}

	/**
	 * Optimized method for converting 4 bytes to a character.
	 */
	public static final char getChar(byte b0, byte b1, byte b2, byte b3) {
		return (char) getInt(b0, b1, b2, b3);
	}

	/**
	 * Optimized method for converting 4 bytes to an integer.
	 */
	public static final char getChar(byte[] b, int offset) {
		return (char) getInt(b, offset);
	}

	/**
	 * Optimized method for converting 4 bytes to an integer.
	 */
	public static final char getChar(byte[] b) {
		return (char) getInt(b);
	}
}