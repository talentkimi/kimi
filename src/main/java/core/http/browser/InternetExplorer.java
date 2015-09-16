package core.http.browser;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import core.http.request.HttpRequest;

/**
 * The Internet Explorer Browser.
 */
public class InternetExplorer extends HttpBrowser {

	/** Hexidecimal Digits. * */
	private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

	/**
	 * Sets the default headers for the given request.
	 * @param request the request.
	 */
	public HttpRequest setHeaders(HttpRequest request) {
		if (request == null) {
			throw new NullPointerException();
		}

		// Headers
		request.addHeader(HEADER_ACCEPT, "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, */*");
		request.addHeader(HEADER_ACCEPT_LANGUAGE, "en-gb");
		request.addHeader(HEADER_USER_AGENT, "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)");

		// Host
		setHostHeader(request, 80);

		// Done
		return request;
	}

	/**
	 * Returns the hexidecimal value of the given decimal.
	 * @param decimal the decimal.
	 * @return the hexidecimal value of the given decimal.
	 */
	private static final char toHex(int decimal) {
		return HEX_DIGITS[decimal];
	}

	/**
	 * Returns the decimal value of the given hexadecimal.
	 * @param hexadecimal the hexadecimal.
	 * @return the decimal value of the given hexadecimal.
	 */
	private static final int toDecimal(char hexadecimal) {
		for (int i = 0; i < HEX_DIGITS.length; i++) {
			if (HEX_DIGITS[i] == Character.toUpperCase(hexadecimal)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Decodes the given string.
	 * @param toDecode the string to encode.
	 * @return the decoded string.
	 */
	public String urlDecode(String toDecode, String charset) {
		try {
			return URLDecoder.decode(toDecode, charset);
		} catch (Exception e) {
			throw new RuntimeException("'" + toDecode + "' for charset '" + charset + "'", e);
		}
	}

	/**
	 * Encodes the given string.
	 * @param toEncode the string to encode.
	 * @return the encoded string.
	 */
	public String urlEncode(String toEncode, String charset) {
		StringBuilder encoded = new StringBuilder();
		for (int i = 0; i < toEncode.length(); i++) {
			char c = toEncode.charAt(i);
			encodeChar(c, encoded, charset);
		}
		return encoded.toString();
	}

	/**
	 * Encodes the given character.
	 * @param c the character.
	 * @param encoded the buffer of encoded characters.
	 * @param charset the character set.
	 * @return the buffer.
	 */
	private static final StringBuilder encodeChar(char c, StringBuilder encoded, String charset) {
		int decimal = (int) c;

		// Ignore Unencodable Characters
		if (decimal < 0 || decimal > 255) {
			try {
				String encodedChar = URLEncoder.encode(String.valueOf(c), charset);
				return encoded.append(encodedChar);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}

		// Ignore Letters & Digits
		if (c >= 'a' && c <= 'z') {
			return encoded.append(c);
		}
		if (c >= 'A' && c <= 'Z') {
			return encoded.append(c);
		}
		if (c >= '0' && c <= '9') {
			return encoded.append(c);
		}

		// Ignore Some Characters
		switch (c) {
			case ' ' :
				return encoded.append('+');
			case '_' :
				return encoded.append(c);
			case '-' :
				return encoded.append(c);
			case '*' :
				return encoded.append(c);
			case '.' :
				return encoded.append(c);
			case '@' :
				return encoded.append(c);
			case '\'' :
				return encoded.append(c);
		}

		// Encode All Other Characters
		int digit1 = decimal / 16;
		int digit2 = decimal % 16;
		char char1 = toHex(digit1);
		char char2 = toHex(digit2);
		encoded.append('%');
		encoded.append(char1);
		encoded.append(char2);
		return encoded;
	}
}