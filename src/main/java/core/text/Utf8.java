package core.text;

import java.io.UnsupportedEncodingException;

public class Utf8 {

	/** The character set. */
	// public static final Charset CHARSET = Charset.forName("UTF-8"); // Version 6
	public static final String CHARSET = "UTF-8";

	public static final byte[] toByteArray(String text) {
		if (text == null) {
			throw new NullPointerException();
		}
		if (text.length() == 0) {
			return new byte[0];
		}
		try {
			return text.getBytes(CHARSET);
		} catch (UnsupportedEncodingException e) {
			return text.getBytes();
		}
	}

	public static final String toString(byte[] bytes) {
		if (bytes == null) {
			throw new NullPointerException();
		}
		if (bytes.length == 0) {
			return "";
		}
		try {
			return new String(bytes, CHARSET);
		} catch (UnsupportedEncodingException e) {
			return new String(bytes);
		}
	}

}
