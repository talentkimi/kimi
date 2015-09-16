package core.mime;

/**
 * A Base 64 Encoder. <i>Encoding complete, decoding unfinished. </i>
 * <p>
 * To summarise, the encoding works like this:
 * <p>
 * Every three 8-bit characters are encoded to four 6-bit characters. If there is only one or two 8-bit characters at the end of the encoding, a number of padding (=) characters are appended.
 * <p>
 * 100 x faster than the sun.misc encoder...
 */
public final class Base64Encoder {

	/** The padding. */
	private static final byte PADDING = 64;
	/** The character set. */
	private static final char[] SET = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/', '='};

	/**
	 * Returns the encoded length.
	 * @param from the from.
	 * @return the encoded length.
	 */
	private final int getEncodedLength(byte[] from) {
		if (from.length == 0) {
			return 0;
		}
		int length = (from.length / 3) * 4;
		int remainder = from.length % 3;
		if (remainder > 0) {
			length += 4;
		}
		return length;
	}

	/**
	 * Appends a set of bytes.
	 * @param b the byte array.
	 * @param offset the offset.
	 * @param b0 the first byte.
	 * @param b1 the second byte.
	 * @param b2 the third byte.
	 * @param b3 the forth byte.
	 */
	private final void append(byte[] b, int offset, byte b0, byte b1, byte b2, byte b3) {
		b[0 + offset] = (byte) SET[b0];
		b[1 + offset] = (byte) SET[b1];
		b[2 + offset] = (byte) SET[b2];
		b[3 + offset] = (byte) SET[b3];
	}

	/**
	 * Append bytes from one byte array to another.
	 * @param from the byte array to append from.
	 * @param fromOffset the offset in the byte array.
	 * @param to the byte array to append to.
	 * @param toOffset the offset in the byte array.
	 */
	private final void append(byte[] from, int fromOffset, byte[] to, int toOffset) {
		int diff = from.length - fromOffset;
		byte b0 = (byte) ((from[fromOffset] & 0xFC) >> 2);
		byte b1;
		byte b2;
		byte b3;
		if (diff > 1) {
			b1 = (byte) (((from[fromOffset] & 0x03) << 4) | ((from[fromOffset + 1] & 0xF0) >> 4));
			if (diff > 2) {
				b2 = (byte) (((from[fromOffset + 1] & 0x0F) << 2) | ((from[fromOffset + 2] & 0xC0) >> 6));
				b3 = (byte) (from[fromOffset + 2] & 0x3F);
			} else {
				b2 = (byte) (((from[fromOffset + 1] & 0x0F) << 2));
				b3 = PADDING;
			}
		} else {
			b1 = (byte) ((from[fromOffset] & 0x03) << 4);
			b2 = PADDING;
			b3 = PADDING;
		}
		append(to, toOffset, b0, b1, b2, b3);
	}

	/**
	 * Convert the given bytes.
	 * @param from the bytes to endcode from.
	 * @return the encode bytes.
	 */
	public final byte[] encode(byte[] from) {
		if (from == null) {
			throw new NullPointerException();
		}
		int toLength = getEncodedLength(from);
		byte[] to = new byte[toLength];
		int fromOffset = 0;
		for (int toOffset = 0; toOffset < toLength; toOffset += 4) {
			append(from, fromOffset, to, toOffset);
			fromOffset += 3;
		}
		return to;
	}

	/**
	 * Convert the given bytes.
	 * @param from the bytes to endcode from.
	 * @return the encode bytes.
	 */
	public final String encode(String from) {
		return new String(encode(from.getBytes()));
	}

	/**
	 * Decode the given bytes.
	 * @param from the bytes do decode from.
	 * @return the decoded bytes.
	 */
	public final byte[] decode(byte[] from) {
		throw new IllegalStateException("MIME decoding not implemented");
	}

	public static void main(String[] args) {
		System.out.println(new Base64Encoder().encode("Robin"));
	}

}