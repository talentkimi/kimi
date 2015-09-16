package core.text;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringEscaper {

	private static final Logger log = LoggerFactory.getLogger(StringEscaper.class);
	
	private static final void escape(String unescaped, char separator, StringBuilder escaped) {
		for (int i = 0; i < unescaped.length(); i++) {
			char c = unescaped.charAt(i);
			if (c == separator) {
				escaped.append('\\');
			}
			escaped.append(c);
		}
	}

	public String[] unescape(String escaped, char separator) {
		if (separator == '\\') {
			throw new IllegalArgumentException("separator cannot be the character '\\'");
		}
		int length = Text.countUnescapedChars(escaped, separator) + 1;
		String[] array = new String[length];
		StringBuilder builder = new StringBuilder();
		int index = 0;
		for (int i = 0; i < escaped.length(); i++) {
			char c = escaped.charAt(i);
			if (c != '\\') {
				if (c == separator) {
					array[index++] = builder.toString();
					builder = new StringBuilder();
				} else {
					builder.append(c);
				}
			} else {
				c = escaped.charAt(++i);
				builder.append(c);
			}
		}
		array[index++] = builder.toString();
		if (index != array.length) {
			throw new IllegalArgumentException("escaped: '" + escaped + "'");
		}
		return array;
	}

	public String escape(char separator, String... unescaped) {
		if (unescaped.length == 0) {
			return "";
		}
		StringBuilder escaped = new StringBuilder();
		for (int i = 0; i < unescaped.length; i++) {
			if (i > 0) {
				escaped.append(separator);
			}
			escape(unescaped[i], separator, escaped);
		}
		return escaped.toString();
	}

	public static void main(String... args) {
		try {
			StringEscaper escaper = new StringEscaper();
			String[] unescaped = {"robin", "john", "f,red"};
			String escaped = escaper.escape(',', unescaped);
			System.out.println(Arrays.asList(unescaped));
			System.out.println(escaped);
			unescaped = escaper.unescape(escaped, ',');
			System.out.println(Arrays.asList(unescaped));

		} catch (Exception e) {
			if (log.isErrorEnabled()) log.error(e.getMessage(),e);
		}
	}

}
