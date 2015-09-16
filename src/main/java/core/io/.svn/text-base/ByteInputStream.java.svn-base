package core.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public final class ByteInputStream extends FilterInputStream implements ByteIOConstants {

	/** The character set. */
	private Charset charset = Charset.forName("UTF-8");

	/**
	 * Read a byte.
	 * @return the byte read.
	 */
	public final byte readByte() throws IOException {
		int value = read();
		if (value == -1) {
			throw new IOException("end of stream");
		}
		return (byte) value;
	}

	/**
	 * Read a boolean.
	 * @return the boolean read.
	 */
	public final boolean readBoolean() throws IOException {
		return readByte() == TRUE ? true : false;
	}

	/**
	 * Read a short.
	 * @return the short read.
	 */
	public final short readShort() throws IOException {
		int b0 = readByte();
		int b1 = readByte();
		return (short) (((b0 & 0xff) << 8) | ((b1 & 0xff) << 0));
	}

	/**
	 * Read an integer.
	 * @return the integer read.
	 */
	public final int readInt() throws IOException {
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
	public final long readNumber() throws IOException {
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
	public final char readChar() throws IOException {
		return (char) readInt();
	}

	/**
	 * Read a long.
	 * @return the long read.
	 */
	public final long readLong() throws IOException {
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
	public final float readFloat() throws IOException {
		return Float.intBitsToFloat(readInt());
	}

	/**
	 * Read a double.
	 * @return the double read.
	 */
	public final double readDouble() throws IOException {
		return Double.longBitsToDouble(readLong());
	}

	/**
	 * Read a byte array.
	 * @param length the length of the array.
	 * @return the byte array read.
	 */
	public final byte[] readByteArray(int length) throws IOException {
		byte[] newArray = new byte[length];
		read(newArray);
		return newArray;
	}

	/**
	 * Read a byte array.
	 * @return the byte array read.
	 */
	public final byte[] readByteArray() throws IOException {
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
	public final String readString() throws IOException {
		byte[] array = readByteArray();
		return new String(array, charset);
	}

	public ByteInputStream(InputStream in) {
		super(in);
	}

}
