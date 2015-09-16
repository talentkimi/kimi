package core.lang;

import java.util.Arrays;

/**
 * A Hexidecimal Number.
 */
public final class Hex {

	/** Mapping from integers to hex. * */
	private static final char[] HEX_MAP = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

	/**
	 * Returns true if the given character is a HEX digit.
	 * @param c the character.
	 * @return true if the given character is a HEX digit.
	 */
	public static final boolean isHexDigit(char c) {
		c = Character.toLowerCase(c);
		for (int i = 0; i < HEX_MAP.length; i++) {
			if (HEX_MAP[i] == c) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Converts the given string from hex to bytes.
	 */
	private static final byte[] convert(String s) {
		byte[] b = new byte[s.length() / 2];
		int bind = 0;
		int sind = 0;
		for (; bind < b.length;) {
			b[bind] = convert(s, sind);
			bind += 1;
			sind += 2;
		}
		return b;
	}

	/**
	 * Converts the given byte array to a hex string.
	 * @param s the string.
	 * @param index the string offset.
	 */
	private static final byte convert(String s, int index) {
		byte b1 = convert(s.charAt(index + 0));
		byte b0 = convert(s.charAt(index + 1));
		return (byte) (b0 | (b1 << 4));
	}

	/**
	 * Returns the byte for the give character.
	 */
	private static final byte convert(char c) {
		for (int i = 0; i < HEX_MAP.length; i++) {
			if (c == HEX_MAP[i]) {
				return (byte) i;
			}
		}
		throw new IllegalArgumentException("Illegal Hex Character: " + c);
	}

	/**
	 * Speed convenience function for converting a byte array to hex.
	 * @param bytes the value.
	 */
	public static final String convert(byte[] bytes) {
		char[] chars = new char[bytes.length * 2];
		int charsIndex = 0;
		for (int i = 0; i < bytes.length; i++) {
			int b = bytes[i];
			chars[charsIndex++] = HEX_MAP[(b & 0xf0) >>> 4];
			chars[charsIndex++] = HEX_MAP[b & 0x0f];
		}
		return new String(chars);
	}

	/** The value. * */
	private Object value = null;

	/**
	 * Returns a string representation of this hexidecimal number.
	 */
	public String toString() {
		if (value instanceof String) {
			return (String) value;
		} else {
			return convert((byte[]) value);
		}
	}

	/**
	 * Set the value as a string.
	 * @param s the string.
	 */
	public void set(String s) {
		if (s == null) {
			throw new NullPointerException();
		}
		if (s.length() == 0) {
			throw new IllegalArgumentException("empty string");
		}
		if (s.length() % 2 == 1) {
			throw new IllegalArgumentException("hex=\"" + s + "\"");
		}

		// Validate
		s = s.toLowerCase();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			boolean found = false;
			for (int k = 0; k < HEX_MAP.length; k++) {
				if (c == HEX_MAP[k]) {
					found = true;
					break;
				}
			}
			if (!found) {
				throw new IllegalArgumentException("hex=\"" + s + "\"");
			}
		}
		this.value = s;
	}

	/**
	 * Returns the bytes of this hexidecimal number.
	 */
	public byte[] getBytes() {
		if (value instanceof byte[]) {
			return (byte[]) value;
		} else {
			return convert((String) value);
		}
	}

	/**
	 * Set the value as a string.
	 * @param b the string.
	 */
	public void set(byte[] b) {
		if (b == null) {
			throw new NullPointerException();
		}
		if (b.length == 0) {
			throw new IllegalArgumentException("empty array");
		}

		// Validate
		this.value = b;
	}

	/**
	 * Creates a new sequence of hex digits.
	 * @param b the hexidecimal value
	 */
	public Hex(byte[] b) {
		set(b);
	}

	/**
	 * Creates a new sequence of hex digits.
	 * @param array the hexidecimal value
	 */
	public Hex(byte[] array, int offset, int length) {
		if (offset != 0 || array.length != length) {
			byte[] subarray = new byte[length];
			System.arraycopy(array, offset, subarray, 0, length);
			array = subarray;
		}
		set(array);
	}

	/**
	 * Creates a new sequence of hex digits.
	 * @param s the hexidecimal value
	 */
	public Hex(String s) {
		set(s);
	}

}