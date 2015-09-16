package core.text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.io.StreamReader;
import core.lang.StackTrace;

/**
 * A collection of useful methods for string manipulation.
 */
public class Text {

	private static final Logger log = LoggerFactory.getLogger(Text.class);
	
	private static final Map<Integer, String> twoDigitMap = new Hashtable<Integer, String>();

	/**
	 * Inaccessible Constructor.
	 */
	private Text() {
	}

	// This is a frickin weird function. Why offer the user to do a new string()? 
	// Why not leave that to the user to do it at their code level to avoid confusion.
	// This led to a major bug around 14/9/2010
	public static final String intern(String s, int maxLength, boolean newString) {
		if (s == null) {
			return null;
		}
		int length = s.length();
		if ((length > 0) && (length <= maxLength)) {
			// We are assuming here that intern ALWAYS returns a new string because the caller may have asked for one.  
			return s.intern();
		}
		// THIS IS NEEDED
		if (newString) {
			// THIS IS NEEDED
			/* THIS IS NEEDED */return new String(s);
		}
		return s;
	}

	
	// This is a BAD function. Why do a new string() when the user may not even realise it is being done?
	// Also this is called from trim(), which seems to expect it to also trim the string.
	// This led to a major bug around 14/9/2010	
//	public static final String intern(String s, int maxLength) {
//		return intern(s, maxLength, true);
//	}

	/**
	 * Returns the simple name of the given class.
	 * @param c the class.
	 * @return the simple name.
	 */
	public static final String getSimpleName(Class c) {
		String name = c.getName();
		int dotIndex = name.lastIndexOf('.') + 1;
		if (dotIndex > 0) {
			name = name.substring(dotIndex, name.length());
		}
		return name;
	}

	/**
	 * Returns the simple name of the given object.
	 * @param obj the object.
	 * @return the simple name.
	 */
	public static final String getSimpleName(Object obj) {
		return getSimpleName(obj.getClass());
	}

	/**
	 * Returns a two digit string parsed from the given number.
	 * @param number the number.
	 * @return a two digit string.
	 */
	public static final String getTwoDigitString(int number) {
		String value = twoDigitMap.get(number);
		if (value != null) {
			return value;
		}
		if (number >= 10 && number < 100) {
			value = String.valueOf(number);
			twoDigitMap.put(number, value);
			return value;
		}
		if (number >= 0 && number < 10) {
			value = "0" + number;
			twoDigitMap.put(number, value);
			return value;
		}
		if (number >= 100) {
			value = String.valueOf(number);
			return value.substring(value.length() - 2, value.length());
		}
		throw new IllegalArgumentException("number=" + number);
	}

	/**
	 * Returns the stack trace of the given throwable.
	 * @param t the throwable.
	 * @return the stack trace.
	 */
	public static final String getStackTrace(Throwable t) {
		StringBuilder buffer = new StringBuilder();
		StackTrace.appendTo(t, buffer);
		return buffer.toString();
	}

	/**
	 * Returns the stack trace of the given throwable.
	 * @param t the throwable.
	 * @return the stack trace.
	 */
	public static final String getStackTrace(StackTraceElement[] trace) {
		return getStackTrace(trace, -1);
	}
	
	/**
	 * Returns the stack trace of the given throwable.
	 * @param t the throwable.
	 * @return the stack trace.
	 */
	public static final String getStackTrace(StackTraceElement[] trace, int maxElements) {
		StringBuilder builder = new StringBuilder();
		StackTrace.appendTo(trace, builder, maxElements);
		return builder.toString();
	}

	/**
	 * Returns a two digit string parsed from the given number.
	 * @param number the number.
	 * @return a two digit string.
	 */
	public static final String getTwoDigitString(String number) {
		return getTwoDigitString(Integer.parseInt(number));
	}

	/**
	 * Splits the given string into a string array, breaking on newlines.
	 * @param reader the reader.
	 * @return the lines.
	 */
	public static final String[] splitToLines(StreamReader reader) throws IOException {
		ArrayList<String> list = new ArrayList<String>();
		while (true) {
			String line = reader.readLine();
			if (line == null)
				break;
			list.add(line);
		}
		String[] lines = new String[list.size()];
		list.toArray(lines);
		return lines;
	}

	/**
	 * Splits the given string into a string array, breaking on newlines.
	 * @param s the string.
	 * @return the lines.
	 */
	public static final String[] splitToLines(String s) {
		try {
			return splitToLines(new StreamReader(s));
		} catch (IOException ioe) {
			if (log.isErrorEnabled()) log.error(ioe.getMessage(),ioe);
			throw new IllegalStateException();
		}
	}

	/**
	 * Split the given string on the delimiter.
	 * @param toSplit the string to split.
	 * @param delimiter the delimiter.
	 * @return the split string.
	 */
	public static final String[] split(String toSplit, String delimiter) {
		if (toSplit == null || delimiter == null)
			throw new NullPointerException();
		if (delimiter.length() == 0)
			throw new IllegalArgumentException("delimiter cannot be an empty string");
		int count = count(toSplit, delimiter);
		if (count == 0)
			return new String[]{toSplit};
		String[] splitted = new String[count + 1];
		int indexStart = 0;
		int indexEnd = 0;
		for (int i = 0; i < splitted.length; i++) {
			indexEnd = toSplit.indexOf(delimiter, indexStart);
			if (indexEnd == -1)
				indexEnd = toSplit.length();
			splitted[i] = toSplit.substring(indexStart, indexEnd);
			indexStart = indexEnd + delimiter.length();
		}
		return splitted;
	}

	/**
	 * Counts and returns the number of occurances of the given string in the string.
	 * @param countIn the string to count in.
	 * @param delimiter the delimiter string.
	 * @return the number.
	 */
	public static final int count(String countIn, String delimiter) {
		int number = 0;
		int index = 0;
		while (true) {
			index = countIn.indexOf(delimiter, index);
			if (index == -1)
				break;
			number++;
			index += delimiter.length();
		}
		return number;
	}

	/**
	 * Returns the index of the given substring from the given index.
	 * @param cs the character sequence to search.
	 * @param sub the substring to find the index of.
	 * @param index the index.
	 * @return the index or the substring of -1 if not found.
	 */
	public static int indexOfIgnoreCase(CharSequence cs, String sub, int index) {
		if (sub.length() == 0) {
			return index;
		}
		if (sub.length() > cs.length()) {
			return -1;
		}
		int subIndex = 0;
		for (int i = index; i < cs.length(); i++) {
			char c1 = Character.toLowerCase(cs.charAt(i));
			char c2 = Character.toLowerCase(sub.charAt(subIndex));
			if (c1 == c2) {
				subIndex++;
				if (subIndex == sub.length()) {
					return i - subIndex + 1;
				}
			} else {
				subIndex = 0;
			}
		}
		return -1;
	}

	/**
	 * Returns the index of the first whitespace from the given index.
	 * @param cs the character sequence to search.
	 * @param index the index.
	 * @return the index of the whitespace or -1 if not found.
	 */
	public static int indexOfWhitespace(CharSequence cs, int index) {
		for (int i = index; i < cs.length(); i++) {
			char c = cs.charAt(i);
			if (Character.isWhitespace(c)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Returns the index of the first non-whitespace from the given index.
	 * @param cs the character sequence to search.
	 * @param index the index.
	 * @return the index of the whitespace or -1 if not found.
	 */
	public static int indexOfNonWhitespace(CharSequence cs, int index) {
		for (int i = index; i < cs.length(); i++) {
			char c = cs.charAt(i);
			if (!Character.isWhitespace(c)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Returns true if the first character sequence starts with the second (ignoring case).
	 * @param cs1 the first character sequence.
	 * @param cs2 the second character sequence.
	 * @return true if the first character sequence starts with the second (ignoring case).
	 */
	public static final boolean startsWithIgnoreCase(CharSequence cs1, CharSequence cs2) {
		if (cs1.length() == 0 || cs2.length() == 0)
			return false;
		if (cs1.length() < cs2.length())
			return false;
		for (int i = 0; i < cs2.length(); i++) {
			char c1 = Character.toLowerCase(cs1.charAt(i));
			char c2 = Character.toLowerCase(cs2.charAt(i));
			if (c1 != c2)
				return false;
		}
		return true;
	}

	/**
	 * Replaces all occurances of the given string with the replacement.
	 * @param replaceIn the string to replace in.
	 * @param toReplace the string to replace.
	 * @param replaceWith the string to replace with.
	 * @return the result.
	 */
	public static String replace(String replaceIn, String toReplace, String replaceWith) {
		if (replaceIn == null || toReplace == null || replaceWith == null) {
			throw new NullPointerException();
		}
		if (replaceIn.length() == 0 || toReplace.length() == 0) {
			return replaceIn;
		}
		if (toReplace.length() > replaceIn.length()) {
			return replaceIn;
		}
		int index = replaceIn.indexOf(toReplace);
		int length = toReplace.length();
		if (index == -1) {
			return replaceIn;
		}
		StringBuilder sb = new StringBuilder(replaceIn.length());
		for (int i = 0; i < replaceIn.length(); i++) {
			if (index == i) {
				sb.append(replaceWith);
				i += length - 1;
				index = replaceIn.indexOf(toReplace, index + length);
			} else {
				char c = replaceIn.charAt(i);
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static final int indexOfUnescaped(String haystack, char needle, int indexFrom) {
		for (int i = indexFrom; i < haystack.length(); i++) {
			char c = haystack.charAt(i);
			if (c == '\\') {
				i++;
			}
			if (c == needle) {
				return i;
			}
		}
		return -1;
	}

	public static final int indexOfUnescaped(String haystack, char needle) {
		return indexOfUnescaped(haystack, needle, 0);
	}

	public static final int countUnescapedChars(String s, char unescaped) {
		int count = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '\\') {
				i++;
			} else {
				if (c == unescaped) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * Inserts escaped characters into the given string.
	 * @param unescaped the string to insert into.
	 * @param chars the characters to escape.
	 * @return the resulting string.
	 */
	public static final String insertEscapedChars(String unescaped, char... chars) {
		if (unescaped == null || chars == null) {
			throw new NullPointerException();
		}
		boolean found = false;
		for (int index = 0; index < unescaped.length(); index++) {
			if (contains(chars, unescaped.charAt(index))) {
				found = true;
				break;
			}
		}
		if (!found) {
			return unescaped;
		}
		StringBuilder b = new StringBuilder(unescaped.length() * 2);
		for (int index = 0; index < unescaped.length(); index++) {
			if (contains(chars, unescaped.charAt(index))) {
				b.append('\\');
			}
			b.append(unescaped.charAt(index));
		}
		return b.toString();
	}

	/**
	 * Removes escaped characters from the given string.
	 * @param escaped the string to remove from.
	 * @return the resulting string.
	 */
	public static final String removeEscapedChars(String escaped) {
		if (escaped == null) {
			throw new NullPointerException();
		}
		int endIndex = escaped.indexOf('\\');
		if (endIndex == -1) {
			return escaped;
		}
		StringBuilder b = new StringBuilder();
		int startIndex = 0;
		while (true) {
			b.append(escaped.substring(startIndex, endIndex++));
			int tempIndex = escaped.indexOf('\\', endIndex + 1);
			if (tempIndex == -1) {
				break;
			} else {
				startIndex = endIndex;
				endIndex = tempIndex;
			}
		}
		b.append(escaped.substring(endIndex, escaped.length()));
		return b.toString();
	}

	/**
	 * Returns true if the given character array contains the given character!
	 * @param haystack the character array.
	 * @param needle the character.
	 */
	public static final boolean contains(char[] haystack, char needle) {
		if (haystack == null) {
			throw new NullPointerException();
		}
		for (int i = 0; i < haystack.length; i++) {
			if (haystack[i] == needle) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns a string representation of the given array.
	 * @param array the character array.
	 */
	public static final String toString(Object[] array) {
		if (array == null) {
			throw new NullPointerException();
		}
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < array.length; i++) {
			if (i > 0) {
				sb.append(",");
			}
			sb.append(array[i]);
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Returns a string representation of the given array.
	 * @param array the character array.
	 */
	public static final String toString(Object object) {
		if (object == null) {
			return "null";
		}
		if (object instanceof Object[]) {
			return toString((Object[]) object);
		}
		return object.toString();
	}

	/**
	 * Remove the given string from another.
	 * @param removeFrom the string to remove from.
	 * @param toRemove the string to remove.
	 * @return the resulting string.
	 */
	public static final String remove(String removeFrom, String toRemove) {
		return replace(removeFrom, toRemove, "");
	}

	/**
	 * Removes substrings from a string.
	 * @param removeFrom the string to remove from.
	 * @param from the start of the substring to remove.
	 * @param to the end of the substring to remove.
	 * @return the resulting string.
	 */
	public static final String remove(String removeFrom, String from, String to) {
		if (removeFrom == null || from == null || to == null) {
			throw new NullPointerException();
		}
		if (from.length() == 0) {
			throw new IllegalArgumentException("from is empty");
		}
		if (to.length() == 0) {
			throw new IllegalArgumentException("to is empty");
		}
		int indexFrom = removeFrom.indexOf(from);
		if (indexFrom == -1) {
			return removeFrom;
		}
		int indexTo = removeFrom.indexOf(to, indexFrom + from.length());
		if (indexTo == -1) {
			return removeFrom;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < removeFrom.length(); i++) {
			if (i == indexFrom) {
				i = indexTo + to.length();
				indexFrom = removeFrom.indexOf(from, indexTo + to.length());
				if (indexFrom != -1) {
					indexTo = removeFrom.indexOf(to, indexFrom + from.length());
					if (indexTo == -1) {
						indexFrom = -1;
					}
				}
				if (i == removeFrom.length()) {
					break;
				}
			}
			char c = removeFrom.charAt(i);
			sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * Removes whitespace from the given character sequence.
	 * @param cs the character sequence.
	 * @return the resulting character sequence.
	 */
	public static String removeWhitespace(CharSequence cs) {
		if (cs == null) {
			throw new NullPointerException();
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cs.length(); i++) {
			char c = cs.charAt(i);
			if (!Character.isWhitespace(c)) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * Strip all whitespace and convert uppercase characters.
	 * @param toStrip the text to strip.
	 * @return the stripped code.
	 */
	public static final String strip(String toStrip) {
		if (toStrip == null) {
			throw new NullPointerException();
		}
		StringBuilder stripped = new StringBuilder();
		for (int i = 0; i < toStrip.length(); i++) {
			char c = toStrip.charAt(i);
			if (Character.isLetterOrDigit(c)) {
				c = Character.toLowerCase(c);
				stripped.append(c);
			}
		}
		return stripped.toString();
	}

	/**
	 * Returns the given text in name case.
	 * @param text the text to convert.
	 * @return the converted text.
	 */
	public static final String toNameCase(String text) {
		StringBuilder buffer = new StringBuilder();
		boolean upper = true;
		for (int k = 0; k < text.length(); k++) {
			char c = text.charAt(k);
			if (Character.isLetter(c)) {
				if (upper) {
					c = Character.toUpperCase(c);
					upper = false;
				} else {
					c = Character.toLowerCase(c);
				}
			} else {
				upper = true;
			}
			buffer.append(c);
		}
		return buffer.toString();
	}

	/**
	 * Insert commas into the given number.
	 * @param l the number.
	 * @return the string.
	 */
	public static String insertCommas(long l) {
		String s = String.valueOf(l);
		if (-999 < l && l < 999) {
			return s;
		}
		return insertCommas(s);
	}

	/**
	 * Insert commas into the given number.
	 * @param number the number.
	 * @return the string.
	 */
	public static String insertCommas(String number) {
		if (number.length() < 4) {
			return number;
		}
		if (number.length() == 4 && number.startsWith("-")) {
			return number;
		}
		int count = 0;
		StringBuilder sb = new StringBuilder();
		for (int i = number.length() - 1; i >= 0; i--) {
			if (count == 3) {
				sb.insert(0, ',');
				count = 1;
			} else {
				count++;
			}
			sb.insert(0, number.charAt(i));
		}
		return sb.toString();
	}

	/**
	 * Returns the given long as a formatted string.
	 * @param bytes the bytes.
	 * @return the long.
	 */
	public static String bytes(long bytes) {
		if (bytes >= 1000000000) {
			return bytes(bytes / 100000000, "Gigabyte");
		}
		if (bytes >= 1000000) {
			return bytes(bytes / 100000, "Megabyte");
		}
		if (bytes >= 1000) {
			return bytes(bytes / 100, "Kilobyte");
		}
		return bytes + " Bytes";
	}

	/**
	 * Return a bytes string in a human readable form.
	 * @param bytes the bytes to format.
	 * @param type the type.
	 * @return the string.
	 */
	private static final String bytes(long bytes, String type) {
		StringBuilder text = new StringBuilder();
		text.append(bytes);
		boolean plural = true;
		if (text.charAt(text.length() - 1) == '0') {
			text.deleteCharAt(text.length() - 1);
			if (text.charAt(text.length() - 1) == '1') {
				plural = false;
			}
		} else {
			text.insert(text.length() - 1, '.');
		}
		text.append(' ');
		text.append(type);
		if (plural) {
			text.append('s');
		}
		return text.toString();
	}
}