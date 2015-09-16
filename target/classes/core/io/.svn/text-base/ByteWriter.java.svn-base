package core.io;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * A Byte Writer
 */
public class ByteWriter implements ByteIOConstants {

	/** The size. */
	private int size = 0;
	/** The array. */
	private byte[] array;

	/** The character set. */
	private Charset charset = Charset.forName("UTF-8");

	/**
	 * Set the charset.
	 * @param charset the charset.
	 */
	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	/**
	 * Creates a new byte writer.
	 * @param initialCapacity the initial capacity.
	 */
	public ByteWriter(int initialCapacity) {
		if (initialCapacity < 1) {
			throw new IllegalArgumentException("initialCapacity=" + initialCapacity);
		}
		array = new byte[initialCapacity];
	}

	/**
	 * Creates a new byte writer.
	 */
	public ByteWriter() {
		this(64);
	}

	/**
	 * Clear the writer.
	 */
	public final void clear() {
		size = 0;
		array = new byte[64];
	}

	/**
	 * Returns the size.
	 * @return the size.
	 */
	public final int size() {
		return size;
	}

	/**
	 * Returns the byte array.
	 * @return the byte array.
	 */
	public final byte[] toByteArray() {
		byte[] newArray = new byte[size];
		System.arraycopy(array, 0, newArray, 0, size);
		return newArray;
	}

	/**
	 * Returns the byte array.
	 * @return the byte array.
	 */
	public final byte[] toByteArray(boolean match) {
		if (match && size == array.length) {
			return array;
		}
		return toByteArray();
	}

	/**
	 * Dynamically grow the array if necessary.
	 * @param bytes the number of bytes to grow by.
	 */
	private final void grow(int bytes) {
		if (size + bytes > array.length) {
			int newLength = array.length * 2;
			if (newLength < size + bytes) {
				newLength = size + bytes;
			}
			byte[] newArray = new byte[newLength];
			System.arraycopy(array, 0, newArray, 0, size);
			array = newArray;
		}
	}

	/**
	 * Write the given byte array.
	 * @param b the byte array to write.
	 * @param offset the offset.
	 * @param length the size.
	 */
	public final void write(byte[] b, int offset, int length) {
		grow(length);
		System.arraycopy(b, offset, this.array, this.size, length);
		this.size += length;
	}

	/**
	 * Write the given byte array.
	 * @param b the byte array to write.
	 */
	public final void write(byte[] b) {
		write(b, 0, b.length);
	}

	/**
	 * Write the given byte array.
	 * @param b the byte array to write.
	 */
	public final void writeByteArray(byte[] b) {
		if (b == null) {
			writeNumber(-1);
		} else {
			int length = b.length;
			writeNumber(length);

			// Write Byte Array
			if (length > 0) {
				grow(length);
				System.arraycopy(b, 0, this.array, size, length);
				size += length;
			}
		}
	}

	/**
	 * Write the given byte.
	 * @param b the byte to write.
	 */
	public final void writeByte(byte b) {
		grow(1);
		array[size++] = b;
	}

	/**
	 * Write the given number.
	 * @param number the number.
	 */
	public final void writeNumber(long number) {
		if (number > TYPE_LONG && number <= Byte.MAX_VALUE) {
			writeByte((byte) number);
		} else if (number >= Short.MIN_VALUE && number <= Short.MAX_VALUE) {
			writeByte(TYPE_SHORT);
			writeShort((short) number);
		} else {
			if (number >= Integer.MIN_VALUE && number <= Integer.MAX_VALUE) {
				writeByte(TYPE_INTEGER);
				writeInt((int) number);
			} else {
				writeByte(TYPE_LONG);
				writeLong(number);
			}
		}
	}

	/**
	 * Write the given boolean.
	 * @param b the boolean to write.
	 */
	public final void writeBoolean(boolean b) {
		writeByte(b ? TRUE : FALSE);
	}

	/**
	 * Write the given short.
	 * @param s the short to write.
	 */
	public final void writeShort(short s) {
		grow(2);
		array[size++] = (byte) (s >> 8);
		array[size++] = (byte) (s >> 0);
	}

	/**
	 * Write the given integer.
	 * @param i the integer to write.
	 */
	public final void writeInt(int i) {
		grow(4);
		array[size++] = (byte) (i >> 24);
		array[size++] = (byte) (i >> 16);
		array[size++] = (byte) (i >> 8);
		array[size++] = (byte) (i >> 0);
	}

	/**
	 * Write the given char.
	 * @param c the char to write.
	 */
	public final void writeChar(char c) {
		writeInt(c);
	}

	/**
	 * Write the given long.
	 * @param l the long to write.
	 */
	public final void writeLong(long l) {
		grow(8);
		array[size++] = (byte) (l >> 56);
		array[size++] = (byte) (l >> 48);
		array[size++] = (byte) (l >> 40);
		array[size++] = (byte) (l >> 32);
		array[size++] = (byte) (l >> 24);
		array[size++] = (byte) (l >> 16);
		array[size++] = (byte) (l >> 8);
		array[size++] = (byte) (l >> 0);
	}

	/**
	 * Write the given float.
	 * @param f the float to write.
	 */
	public final void writeFloat(float f) {
		int i = Float.floatToIntBits(f);
		writeInt(i);
	}

	/**
	 * Write the given double.
	 * @param d the double to write.
	 */
	public final void writeDouble(double d) {
		long l = Double.doubleToLongBits(d);
		writeLong(l);
	}

	/**
	 * Write the given string.
	 * @param s the string to write.
	 */
	public final void writeString(String s) {
		byte[] array = s == null ? null : s.getBytes(charset);
		writeByteArray(array);
	}

}
