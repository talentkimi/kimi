package core.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public final class ByteOutputStream extends FilterOutputStream implements ByteIOConstants {

	/** The character set. */
	private Charset charset = Charset.forName("UTF-8");

	/**
	 * Write the given byte.
	 * @param b the byte to write.
	 */
	public final void writeByte(byte b) throws IOException {
		write(b);
	}

	/**
	 * Write the given number.
	 * @param number the number.
	 */
	public final void writeNumber(long number) throws IOException {
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
	public final void writeBoolean(boolean b) throws IOException {
		writeByte(b ? TRUE : FALSE);
	}

	/**
	 * Write the given short.
	 * @param s the short to write.
	 */
	public final void writeShort(short s) throws IOException {
		write((byte) (s >> 8));
		write((byte) (s >> 0));
	}

	/**
	 * Write the given integer.
	 * @param i the integer to write.
	 */
	public final void writeInt(int i) throws IOException {
		write((byte) (i >> 24));
		write((byte) (i >> 16));
		write((byte) (i >> 8));
		write((byte) (i >> 0));
	}

	/**
	 * Write the given char.
	 * @param c the char to write.
	 */
	public final void writeChar(char c) throws IOException {
		writeInt(c);
	}

	/**
	 * Write the given long.
	 * @param l the long to write.
	 */
	public final void writeLong(long l) throws IOException {
		write((byte) (l >> 56));
		write((byte) (l >> 48));
		write((byte) (l >> 40));
		write((byte) (l >> 32));
		write((byte) (l >> 24));
		write((byte) (l >> 16));
		write((byte) (l >> 8));
		write((byte) (l >> 0));
	}

	/**
	 * Write the given float.
	 * @param f the float to write.
	 */
	public final void writeFloat(float f) throws IOException {
		int i = Float.floatToIntBits(f);
		writeInt(i);
	}

	/**
	 * Write the given double.
	 * @param d the double to write.
	 */
	public final void writeDouble(double d) throws IOException {
		long l = Double.doubleToLongBits(d);
		writeLong(l);
	}

	/**
	 * Write the given byte array.
	 * @param b the byte array to write.
	 */
	public final void writeByteArray(byte[] b) throws IOException {
		if (b == null) {
			writeNumber(-1);
		} else {
			int length = b.length;
			writeNumber(length);
			write(b);
		}
	}

	/**
	 * Write the given string.
	 * @param s the string to write.
	 */
	public final void writeString(String s) throws IOException {
		byte[] array = s == null ? null : s.getBytes(charset);
		writeByteArray(array);
	}

	public ByteOutputStream(OutputStream out) {
		super(out);
	}

}
