package core.io;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * A Byte Reader.
 */
public class ByteReader implements ByteIOConstants {

	/** The offset. */
	private int offset = 0;
	/** The array. */
	private final byte[] array;

	/** The character set. */
	private Charset charset = Charset.forName("UTF-8");

	/**
	 * Returns true if the reader is at the end.
	 * @return true if the reader is at the end.
	 */
	public final boolean atEnd() {
		return offset == array.length;
	}

	/**
	 * Returns the offset.
	 * @return the offset.
	 */
	public final int getOffset() {
		return offset;
	}

	/**
	 * Set the charset.
	 * @param charset the charset.
	 */
	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	/**
	 * Returns the length.
	 * @return the length.
	 */
	public final int length() {
		return array.length;
	}

	/**
	 * Decrement the offset.
	 */
	public final void decrementOffset() {
		if (offset == 0) {
			throw new IllegalStateException("offset already at zero");
		}
		offset--;
	}

	/**
	 * Increment the offset.
	 */
	public final void incrementOffset() {
		if (offset == array.length) {
			throw new IllegalStateException("offset already at maximum");
		}
		offset++;
	}

	/**
	 * Set the offset.
	 * @param offset the offset.
	 */
	public final void setOffset(int offset) {
		if (offset < 0 || offset > array.length) {
			throw new IllegalArgumentException("offset=" + offset);
		}
		this.offset = offset;
	}

	/**
	 * Creates a new reader of the given array.
	 * @param array the array.
	 */
	public ByteReader(byte[] array) {
		if (array == null) {
			throw new NullPointerException();
		}
		this.array = array;
	}

	/**
	 * Read into the given byte array.
	 * @param b the array to read into.
	 */
	public final void read(byte[] b) {
		System.arraycopy(array, offset, b, 0, b.length);
		offset += b.length;
	}

	/**
	 * Read a byte.
	 * @return the byte read.
	 */
	private final byte read() {
		return array[offset++];
	}

	/**
	 * Read a byte.
	 * @return the byte read.
	 */
	public final byte readByte() {
		return read();
	}

	/**
	 * Read a boolean.
	 * @return the boolean read.
	 */
	public final boolean readBoolean() {
		return readByte() == TRUE ? true : false;
	}

	/**
	 * Read a short.
	 * @return the short read.
	 */
	public final short readShort() {
		int b0 = readByte();
		int b1 = readByte();
		return (short) (((b0 & 0xff) << 8) | ((b1 & 0xff) << 0));
	}

	/**
	 * Read an integer.
	 * @return the integer read.
	 */
	public final int readInt() {
		int b0 = readByte();
		int b1 = readByte();
		int b2 = readByte();
		int b3 = readByte();
		return ((b0 & 0xff) << 24) | ((b1 & 0xff) << 16) | ((b2 & 0xff) << 8) | ((b3 & 0xff) << 0);
	}

	/**
	 * Read a number.
	 * @return the number read.
	 */
	public final long readNumber() {
		byte type = readByte();
		switch (type) {
			case TYPE_LONG :
				return readLong();
			case TYPE_INTEGER :
				return readInt();
			case TYPE_SHORT :
				return readShort();
			default :
				return type;
		}
	}

	/**
	 * Read a char.
	 * @return the char read.
	 */
	public final char readChar() {
		return (char) readInt();
	}

	/**
	 * Read a long.
	 * @return the long read.
	 */
	public final long readLong() {
		long b0 = readByte();
		long b1 = readByte();
		long b2 = readByte();
		long b3 = readByte();
		long b4 = readByte();
		long b5 = readByte();
		long b6 = readByte();
		long b7 = readByte();
		return ((b0 & 0xff) << 56) | ((b1 & 0xff) << 48) | ((b2 & 0xff) << 40) | ((b3 & 0xff) << 32) | ((b4 & 0xff) << 24) | ((b5 & 0xff) << 16) | ((b6 & 0xff) << 8) | ((b7 & 0xff) << 0);
	}

	/**
	 * Read a float.
	 * @return the float read.
	 */
	public final float readFloat() {
		return Float.intBitsToFloat(readInt());
	}

	/**
	 * Read a double.
	 * @return the double read.
	 */
	public final double readDouble() {
		return Double.longBitsToDouble(readLong());
	}

	/**
	 * Read a byte array.
	 * @param length the length of the array.
	 * @return the byte array read.
	 */
	public final byte[] readByteArray(int length) {
		byte[] newArray = new byte[length];
		read(newArray);
		return newArray;
	}

	/**
	 * Read a byte array.
	 * @return the byte array read.
	 */
	public final byte[] readByteArray() {
		long length = readNumber();
		if (length == -1) {
			return null;
		}
		return readByteArray((int) length);
	}

	/**
	 * Read a string.
	 * @return the string read.
	 */
	public final String readString() {
		byte[] array = readByteArray();
		return new String(array, charset);
	}

}
