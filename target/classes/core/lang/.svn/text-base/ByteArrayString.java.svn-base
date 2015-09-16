package core.lang;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * A Byte Array (based on java.lang.String).
 */
public final class ByteArrayString {

	/** The array. */
	private final byte[] value;
	/** The offset. */
	private int offset;
	/** The length. */
	private int count;
	/** The hashcode. */
	private int hash = 0;

	/** The string value. */
	private String stringValue = null;
	/** The character set. */
	// private final Charset charset; // Version 6
	private final String charset;

	public int length() {
		return count;
	}

	public byte[] getBytes() {
		byte[] newValue = new byte[count];
		System.arraycopy(value, offset, newValue, 0, count);
		return newValue;
	}

	public int hashCode() {
		int h = hash;
		if (h == 0) {
			int off = offset;
			byte[] val = value;
			int len = count;

			for (int i = 0; i < len; i++) {
				h = 31 * h + val[off++];
			}
			hash = h;
		}
		return h;
	}

	public boolean equals(Object anObject) {
		if (this == anObject) {
			return true;
		}
		if (anObject instanceof ByteArrayString) {
			ByteArrayString anotherArray = (ByteArrayString) anObject;
			int n = count;
			if (n == anotherArray.count) {
				byte v1[] = value;
				byte v2[] = anotherArray.value;
				int i = offset;
				int j = anotherArray.offset;
				while (n-- != 0) {
					if (v1[i++] != v2[j++])
						return false;
				}
				return true;
			}
		}
		return false;
	}

	public String toString() {
		if (stringValue == null) {
			if (count == 0) {
				stringValue = "";
			} else {
				try {
					stringValue = new String(value, charset);
				} catch (UnsupportedEncodingException e) {
					stringValue = new String(value);
				}
			}
		}
		return stringValue;
	}

	public ByteArrayString(byte[] value, String charset) {
		if (value == null || charset == null) {
			throw new NullPointerException();
		}
		int size = value.length;
		this.offset = 0;
		this.count = size;
		this.value = new byte[value.length];
		System.arraycopy(value, 0, this.value, 0, size);
		this.charset = charset;
	}

	public ByteArrayString() {
		this.offset = 0;
		this.count = 0;
		this.value = new byte[0];
		this.charset = null;
	}

	public ByteArrayString(String text, String charset) {
		if (text == null || charset == null) {
			throw new NullPointerException();
		}
		try {
			this.value = text.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("unsupported charset: " + charset);
		}
		this.stringValue = text;
		this.charset = charset;
		this.offset = 0;
		this.count = value.length;
	}

}
