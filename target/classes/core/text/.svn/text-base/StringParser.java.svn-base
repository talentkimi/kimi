package core.text;

/**
 * An object for improved string parsing.
 */
public class StringParser implements CharSequence {

	/** The Invalid Index. */
	public static final int INVALID_INDEX = -1;

	/** The string. */
	private String string;
	/** Length of the string. */
	private int lengthIndex;
	/** The mark index. */
	private int markIndex = 0;

	/**
	 * Set the underlying string of this parser.
	 * @param s the string.
	 */
	public void setString(String s) {
		if (s == null) {
			throw new NullPointerException("StringParser.setString( null )");
		}
		string = s;
		lengthIndex = string.length();
		markIndex = 0;
	}

	/**
	 * Set the underlying string of this parser.
	 * @param beginIndex the beginnning index.
	 * @param endIndex the end index.
	 */
	public void setString(int beginIndex, int endIndex) {
		checkIndexes(beginIndex, endIndex);
		if (beginIndex != 0 || endIndex != lengthIndex) {
			setString(string.substring(beginIndex, endIndex));
		}
	}

	/**
	 * Returns the string.
	 * @return the string.
	 */
	public final String getString() {
		return string;
	}

	/**
	 * Returns the length of the string.
	 * @return the length of the string.
	 */
	public final int length() {
		return lengthIndex;
	}

	/**
	 * Returns the mark index.
	 */
	public final int getMark() {
		return markIndex;
	}

	/**
	 * Sets the mark index.
	 * @param index the mark to set.
	 */
	public final void setMark(int index) {
		if (index < 0 || index > lengthIndex) {
			throw new IllegalArgumentException("index=" + index);
		}
		this.markIndex = index;
	}

	/**
	 * Returns the true if the mark is at the end of the string.
	 */
	public final boolean isMarkAtEnd() {
		return markIndex == lengthIndex;
	}

	/**
	 * Returns the true if the mark is at the beginning of the string.
	 */
	public final boolean isMarkAtBeginning() {
		return markIndex == 0;
	}

	/**
	 * Returns a string represenation of this parser.
	 * @return a string represenation of this parser.
	 */
	public String toString() {
		return string;
	}

	/**
	 * Returns true if this object is equal to the one given.
	 * @param obj the object.
	 * @return true if this object is equal to the one given.
	 */
	public boolean equals(Object obj) {
		if (obj instanceof StringParser) {
			return string.equals(((StringParser) obj).toString());
		}
		if (obj instanceof String) {
			return string.equals(obj);
		}
		return false;
	}

	/**
	 * Creates a new parser over the given string.
	 * @param s the string.
	 */
	public StringParser(String s) {
		setString(s);
	}

	/**
	 * Returns true if the string contains the given index.
	 * @param index the index.
	 */
	public final boolean containsIndex(int index) {
		return index >= 0 && index < lengthIndex;
	}

	/**
	 * Returns true if the string contains the given index.
	 * @param beginIndex the begin index.
	 * @param endIndex the end index.
	 */
	public final boolean containsIndexes(int beginIndex, int endIndex) {
		return beginIndex >= 0 && beginIndex <= endIndex && endIndex < lengthIndex;
	}

	/**
	 * Throws an exception if the given index is invalid.
	 * @param index the index.
	 */
	public final void checkIndex(int index) {
		if (!containsIndex(index)) {
			throw new StringIndexOutOfBoundsException("Index: " + index + " (length: " + lengthIndex + ")");
		}
	}

	/**
	 * Throws an exception if the given index is invalid.
	 * @param beginIndex the begin index.
	 * @param endIndex the end index.
	 */
	public final void checkIndexes(int beginIndex, int endIndex) {
		if (!containsIndexes(beginIndex, endIndex)) {
			throw new StringIndexOutOfBoundsException("Indexes: " + beginIndex + "," + endIndex + " (length: " + lengthIndex + ")");
		}
	}

	/**
	 * Returns a substring of the string.
	 * @param beginIndex the start index.
	 */
	public final String getString(int beginIndex) {
		return getString(beginIndex, lengthIndex, false);
	}

	/**
	 * Returns a substring of the string.
	 * @param begin the start string.
	 */
	public final String getString(String begin) {
		int beginIndex = indexOf(begin);
		return getString(beginIndex, lengthIndex, false);
	}

	/**
	 * Returns a substring of the string.
	 * @param beginIndex the start index.
	 * @param endIndex the end index.
	 */
	public final String getString(int beginIndex, int endIndex) {
		return getString(beginIndex, endIndex, false);
	}

	/**
	 * Returns a substring of the string.
	 * @param begin the begin string.
	 * @param end the end string.
	 */
	public final String getString(String begin, String end) {
		return getString(begin, end, true);
	}

	/**
	 * Returns a substring of the string.
	 * @param begin the begin string.
	 * @param end the end string.
	 * @param includeDelimiters true to include the delimiters.
	 */
	public final String getString(String begin, String end, boolean includeDelimiters) {
		int beginIndex = indexOf(begin);
		int endIndex = indexOf(end);
		if (includeDelimiters) {
			beginIndex += begin.length();
		} else {
			endIndex += end.length();
		}
		return getString(beginIndex, endIndex, false);
	}

	/**
	 * Returns a substring of the string.
	 * @param beginIndex the start index.
	 * @param endIndex the end index.
	 */
	public final String getString(int beginIndex, int endIndex, boolean ignoreEscaped) {
		StringBuilder sb = new StringBuilder();
		for (int i = beginIndex; i < endIndex; i++) {
			if (ignoreEscaped) {
				if (charAt(i) == '\\') {
					i++;
				}
			}
			sb.append(charAt(i));
		}
		return sb.toString();
	}

	/**
	 * Returns a substring of the string.
	 * @param beginIndex the start index.
	 * @param endIndex the end index.
	 */
	public final String getXmlString(int beginIndex, int endIndex) {
		StringBuilder buffer = new StringBuilder();
		for (int i = beginIndex; i < endIndex; i++) {
			char c = charAt(i);
			if (c == '&') {
				if (regionMatches(false, i, "&amp;")) {
					buffer.append('&');
					i += 4;
				} else {
					if (regionMatches(false, i, "&gt;")) {
						buffer.append('>');
						i += 3;
					} else {
						if (regionMatches(false, i, "&lt;")) {
							buffer.append('<');
							i += 3;
						} else {
							if (regionMatches(false, i, "&quot;")) {
								buffer.append('\"');
								i += 5;
							} else {
								if (regionMatches(false, i, "&apos;")) {
									buffer.append('\'');
									i += 5;
								} else {
									buffer.append(c);
									// int index = indexOf(';', i);
									// if (index == -1) {
									// throw new IllegalStateException("Unable to match " + substring(i, index + 1));
									// }
									// int character = Integer.parseInt(substring(i + 2, index));
									// buffer.append((char) character);
									// i = index;
								}
							}
						}
					}
				}
			} else {
				buffer.append(c);
			}
		}
		return buffer.toString();
	}

	/**
	 * Returns true if the given substring exists from the index.
	 * @param index the index.
	 * @param string the string.
	 * @return true if the given substring exists from the index.
	 */
	public boolean isSubstring(int index, String string) {
		if (length() - index > string.length()) {
			return false;
		}
		for (int i = 0; i < string.length(); i++) {
			char c = charAt(i + index);
			if (c != string.charAt(i)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns a substring of the string.
	 * @param beginIndex the start index.
	 */
	public final String substring(int beginIndex) {
		return string.substring(beginIndex);
	}

	/**
	 * Returns a substring of the string.
	 * @param beginIndex the start index.
	 * @param endIndex the end index.
	 */
	public final String substring(int beginIndex, int endIndex) {
		return string.substring(beginIndex, endIndex);
	}

	/**
	 * Returns the character at the given index.
	 * @param index the index.
	 */
	public final char charAt(int index) {
		return string.charAt(index);
	}

	/**
	 * Returns true if the character at the given index is whitespace.
	 * @param index the index.
	 */
	public final boolean isWhitespace(int index) {
		return Character.isWhitespace(string.charAt(index));
	}

	/**
	 * Returns true if the character at the this index is the one given.
	 * @param index the index.
	 * @param c the character.
	 */
	public final boolean isChar(int index, char c) {
		return string.charAt(index) == c;
	}

	/**
	 * Returns true if the character at the this index is one of the ones given.
	 * @param index the index.
	 * @param c the character.
	 */
	public final boolean isChar(int index, char[] c) {
		return Text.contains(c, string.charAt(index));
	}

	/**
	 * Tests if two string regions are equal.
	 * @param ignoreCase if true, ignore case when comparing characters.
	 * @param toffset the starting offset of the subregion in this string.
	 * @param other the string argument.
	 * @param ooffset the starting offset of the subregion in the string argument.
	 * @param len the number of characters to compare.
	 * @return true if the two string regions are equal.
	 */
	public final boolean regionMatches(boolean ignoreCase, int toffset, String other, int ooffset, int len) {
		return string.regionMatches(ignoreCase, toffset, other, ooffset, len);
	}

	/**
	 * Tests if two string regions are equal.
	 * @param ignoreCase if true, ignore case when comparing characters.
	 * @param toffset the starting offset of the subregion in this string.
	 * @param other the string argument.
	 * @return true if the two string regions are equal.
	 */
	public final boolean regionMatches(boolean ignoreCase, int toffset, String other) {
		return string.regionMatches(ignoreCase, toffset, other, 0, other.length());
	}

	/**
	 * Tests if two string regions are equal.
	 * @param toffset the starting offset of the subregion in this string.
	 * @param other the string argument.
	 * @param ooffset the starting offset of the subregion in the string argument.
	 * @param len the number of characters to compare.
	 * @return true if the two string regions are equal.
	 */
	public final boolean regionMatches(int toffset, String other, int ooffset, int len) {
		return string.regionMatches(toffset, other, ooffset, len);
	}

	/**
	 * Tests if two string regions are equal.
	 * @param toffset the starting offset of the subregion in this string.
	 * @param other the string argument.
	 * @return true if the two string regions are equal.
	 */
	public final boolean regionMatches(int toffset, String other) {
		return string.regionMatches(toffset, other, 0, other.length());
	}

	/**
	 * Trims this string, removing leading and trailing whitespace.
	 */
	public final void trim() {
		string = string.trim();
	}

	/**
	 * Converts all letters in this string to lower case.
	 */
	public final void toLowerCase() {
		string = string.toLowerCase();
	}

	/**
	 * Converts letters in the given bounds to lower case.
	 * @param beginIndex the start index.
	 * @param endIndex the end index.
	 * @param upper true to set the remaining letters to upper case.
	 */
	public final void toLowerCase(int beginIndex, int endIndex, boolean upper) {
		char[] c = string.toCharArray();
		if (upper) {
			for (int i = 0; i < lengthIndex; i++) {
				if (i >= beginIndex && i < endIndex) {
					c[i] = Character.toLowerCase(c[i]);
				} else {
					c[i] = Character.toUpperCase(c[i]);
				}
			}
		} else {
			for (int i = beginIndex; i < endIndex; i++) {
				c[i] = Character.toLowerCase(c[i]);
			}
		}
		string = new String(c);
	}

	/**
	 * Converts letters in the given bounds to lower case.
	 * @param beginIndex the start index.
	 * @param endIndex the end index.
	 */
	public final void toLowerCase(int beginIndex, int endIndex) {
		toLowerCase(beginIndex, endIndex, false);
	}

	/**
	 * Converts all letters in this string to upper case.
	 */
	public final void toUpperCase() {
		string = string.toUpperCase();
	}

	/**
	 * Converts letters in the given bounds to upper case.
	 * @param beginIndex the start index.
	 * @param endIndex the end index.
	 * @param lower true to set the remaining letters to lower case.
	 */
	public final void toUpperCase(int beginIndex, int endIndex, boolean lower) {
		char[] c = string.toCharArray();
		if (lower) {
			for (int i = 0; i < lengthIndex; i++) {
				if (i >= beginIndex && i < endIndex) {
					c[i] = Character.toUpperCase(c[i]);
				} else {
					c[i] = Character.toLowerCase(c[i]);
				}
			}
		} else {
			for (int i = beginIndex; i < endIndex; i++) {
				c[i] = Character.toUpperCase(c[i]);
			}
		}
		string = new String(c);
	}

	/**
	 * Converts letters in the given bounds to upper case.
	 * @param beginIndex the start index.
	 * @param endIndex the end index.
	 */
	public final void toUpperCase(int beginIndex, int endIndex) {
		toUpperCase(beginIndex, endIndex, false);
	}

	/**
	 * Returns the index of the given character.
	 * @param c the character.
	 */
	public final int indexOf(char c) {
		return string.indexOf(c);
	}

	/**
	 * Returns the index of the given character.
	 * @param c the character.
	 * @param index the begin index.
	 */
	public final int indexOf(char c, int index) {
		return string.indexOf(c, index);
	}

	/**
	 * Returns the index of the given character.
	 * @param c the character.
	 * @param index the begin index.
	 * @param notEscaped true to skip the character if escaped.
	 */
	public final int indexOf(char c, int index, boolean notEscaped) {
		return indexOf(c, index, lengthIndex, notEscaped);
	}

	/**
	 * Returns the index of the given character.
	 * @param c the character.
	 * @param beginIndex the begin index.
	 * @param endIndex the end index.
	 */
	public final int indexOf(char c, int beginIndex, int endIndex) {
		int index = string.indexOf(c, beginIndex);
		if (index > endIndex) {
			return INVALID_INDEX;
		}
		return index;
	}

	/**
	 * Returns the index of the given character.
	 * @param c the character.
	 * @param beginIndex the begin index.
	 * @param endIndex the end index.#
	 * @param notEscaped true to skip the character if escaped.
	 */
	public final int indexOf(char c, int beginIndex, int endIndex, boolean notEscaped) {
		if (!notEscaped) {
			return indexOf(c, beginIndex, endIndex);
		}
		while (true) {
			int index = string.indexOf(c, beginIndex);
			if (index > endIndex || index == INVALID_INDEX) {
				return INVALID_INDEX;
			}
			int escapeIndex = index - 1;
			while (string.charAt(escapeIndex) == '\\' && escapeIndex >= beginIndex) {
				escapeIndex--;
			}
			if ((index - escapeIndex) % 2 == 1) {
				return index;
			}
			beginIndex = index + 1;
		}
	}

	/**
	 * Returns the last index of the given character.
	 * @param c the character.
	 * @return the index.
	 */
	public final int lastIndexOf(char c) {
		return string.lastIndexOf(c);
	}

	/**
	 * Returns the last index of the given character.
	 * @param c the character.
	 * @param index the begin index.
	 */
	public final int lastIndexOf(char c, int index) {
		return string.lastIndexOf(c, index);
	}

	/**
	 * Returns the last index of the given character.
	 * @param c the character.
	 * @param beginIndex the begin index.
	 * @param endIndex the end index.
	 */
	public final int lastIndexOf(char c, int beginIndex, int endIndex) {
		int index = string.lastIndexOf(c, endIndex);
		if (index < beginIndex) {
			return INVALID_INDEX;
		}
		return index;
	}

	/**
	 * Returns the index of the given string.
	 * @param s the string.
	 */
	public final int indexOf(String s) {
		return string.indexOf(s);
	}

	/**
	 * Returns the index of the given string.
	 * @param s the string.
	 * @param index the begin index.
	 */
	public final int indexOf(String s, int index) {
		return string.indexOf(s, index);
	}

	/**
	 * Returns the index of the given string.
	 * @param s the string.
	 * @param beginIndex the begin index.
	 * @param endIndex the end index.
	 */
	public final int indexOf(String s, int beginIndex, int endIndex) {
		int index = string.indexOf(s, beginIndex);
		if (index > endIndex) {
			return INVALID_INDEX;
		}
		return index;
	}

	/**
	 * Returns the last index of the given string.
	 * @param s the string.
	 */
	public final int lastIndexOf(String s) {
		return string.lastIndexOf(s);
	}

	/**
	 * Returns the last index of the given string.
	 * @param s the string.
	 * @param index the begin index.
	 */
	public final int lastIndexOf(String s, int index) {
		return string.lastIndexOf(s, index);
	}

	/**
	 * Returns the index of the first of the given characters.
	 * @param c the characters.
	 * @param beginIndex the begin index.
	 * @param endIndex the end index.
	 */
	public final int indexOf(char[] c, int beginIndex, int endIndex) {
		for (int index = beginIndex; index < endIndex; index++) {
			if (Text.contains(c, string.charAt(index))) {
				return index;
			}
		}
		return INVALID_INDEX;
	}

	/**
	 * Returns the first index of the first of the given characters.
	 * @param c the characters.
	 * @param index the begin index.
	 */
	public final int indexOf(char[] c, int index) {
		return indexOf(c, index, lengthIndex);
	}

	/**
	 * Returns the first index of the first of the given characters.
	 * @param c the characters.
	 */
	public final int indexOf(char[] c) {
		return indexOf(c, 0, lengthIndex);
	}

	/**
	 * Returns the index of the first whitespace encounted.
	 * @param beginIndex the begin index.
	 * @param endIndex the end index.
	 */
	public final int indexOfWhitespace(int beginIndex, int endIndex) {
		for (int index = beginIndex; index < endIndex; index++) {
			if (Character.isWhitespace(string.charAt(index))) {
				return index;
			}
		}
		return INVALID_INDEX;
	}

	/**
	 * Returns the index of the first whitespace encounted.
	 * @param index the begin index.
	 */
	public final int indexOfWhitespace(int index) {
		return indexOfWhitespace(index, lengthIndex);
	}

	/**
	 * Returns the index of the first non-digit character.
	 * @param beginIndex the begin index.
	 * @param endIndex the end index.
	 */
	public int skipDigits(int beginIndex, int endIndex) {
		for (int i = beginIndex; i < endIndex; i++) {
			if (!Character.isDigit(string.charAt(i))) {
				return i;
			}
		}
		return endIndex;
	}

	/**
	 * Returns the index of the first non-digit character.
	 * @param index the begin index.
	 */
	public int skipDigits(int index) {
		return skipDigits(index, lengthIndex);
	}

	/**
	 * Returns the index of the first non-digit character.
	 * @param beginIndex the begin index.
	 * @param endIndex the end index.
	 */
	public int skipDecimals(int beginIndex, int endIndex) {
		for (int i = beginIndex; i < endIndex; i++) {
			char c = string.charAt(i);
			if (!Character.isDigit(c) && c != '.') {
				return i;
			}
		}
		return endIndex;
	}

	/**
	 * Returns the index of the first non-digit character.
	 * @param index the begin index.
	 */
	public int skipDecimals(int index) {
		return skipDecimals(index, lengthIndex);
	}

	/**
	 * Returns the index of the first non-letter character.
	 * @param beginIndex the begin index.
	 * @param endIndex the end index.
	 */
	public int skipLetters(int beginIndex, int endIndex) {
		for (int i = beginIndex; i < endIndex; i++) {
			if (!Character.isLetter(string.charAt(i))) {
				return i;
			}
		}
		return endIndex;
	}

	/**
	 * Returns the index of the first non-letter character.
	 * @param index the begin index.
	 */
	public int skipLetters(int index) {
		return skipLetters(index, lengthIndex);
	}

	/**
	 * Returns the index of the first non-alphanumeric character.
	 * @param beginIndex the begin index.
	 * @param endIndex the end index.
	 */
	public int skipAlpha(int beginIndex, int endIndex, String chars) {
		for (int i = beginIndex; i < endIndex; i++) {
			char c = string.charAt(i);
			if (!Character.isLetterOrDigit(c)) {
				if (chars == null) {
					return i;
				} else {
					if (chars.indexOf(c) == -1) {
						return i;
					}
				}
			}
		}
		return endIndex;
	}

	/**
	 * Returns the index of the first non-alphanumeric character.
	 * @param index the begin index.
	 */
	public int skipAlpha(int index) {
		return skipAlpha(index, lengthIndex, null);
	}

	/**
	 * Returns the index of the first non-alphanumeric character.
	 * @param beginIndex the begin index.
	 * @param endIndex the end index.
	 */
	public int skipAlpha(int beginIndex, int endIndex) {
		return skipAlpha(beginIndex, endIndex, null);
	}

	/**
	 * Returns the index of the first non-alphanumeric character.
	 * @param index the begin index.
	 */
	public int skipAlpha(int index, String chars) {
		return skipAlpha(index, lengthIndex, chars);
	}

	/**
	 * Returns the index of the first non-whitespace character.
	 * @param beginIndex the begin index.
	 * @param endIndex the end index.
	 */
	public int skipWhitespace(int beginIndex, int endIndex) {
		for (int i = beginIndex; i < endIndex; i++) {
			if (!Character.isWhitespace(string.charAt(i))) {
				return i;
			}
		}
		return endIndex;
	}

	/**
	 * Returns the index of the first non-whitespace character.
	 * @param index the begin index.
	 */
	public int skipWhitespace(int index) {
		return skipWhitespace(index, lengthIndex);
	}

	/**
	 * Returns the first index that doesn't hold the given character.
	 * @param c the character to skip.
	 * @param beginIndex the begin index.
	 * @param endIndex the end index.
	 * @param ignoreEscaped true to ignore escaped characters.
	 */
	public int skipChars(char c, int beginIndex, int endIndex, boolean ignoreEscaped) {
		for (int i = beginIndex; i < endIndex; i++) {
			char ca = string.charAt(i);
			if (ignoreEscaped && ca == '\\') {
				i++;
			} else if (ca != c) {
				return i;
			}
		}
		return endIndex;
	}

	/**
	 * Returns the first index that doesn't hold the given character.
	 * @param c the character to skip.
	 * @param beginIndex the begin index.
	 * @param endIndex the end index.
	 */
	public int skipChars(char c, int beginIndex, int endIndex) {
		return skipChars(c, beginIndex, endIndex, false);
	}

	/**
	 * Returns the first index that doesn't hold the given character.
	 * @param c the character to skip.
	 * @param index the begin index.
	 */
	public int skipChars(char c, int index) {
		return skipChars(c, index, lengthIndex, false);
	}

	/**
	 * Returns the first index that doesn't hold one of the given characters.
	 * @param c the characters to skip.
	 * @param beginIndex the begin index.
	 * @param endIndex the end index.
	 * @param ignoreEscaped true to ignore escaped characters.
	 */
	public int skipChars(char[] c, int beginIndex, int endIndex, boolean ignoreEscaped) {
		for (int i = beginIndex; i < endIndex; i++) {
			char ca = string.charAt(i);
			if (ignoreEscaped && ca == '\\') {
				i++;
			} else if (!Text.contains(c, ca)) {
				return i;
			}
		}
		return endIndex;
	}

	/**
	 * Returns the first index that doesn't hold one of the given characters.
	 * @param c the characters to skip.
	 * @param beginIndex the begin index.
	 * @param endIndex the end index.
	 */
	public int skipChars(char[] c, int beginIndex, int endIndex) {
		return skipChars(c, beginIndex, endIndex, false);
	}

	/**
	 * Returns the first index that doesn't hold the given character.
	 * @param c the characters to skip.
	 * @param index the begin index.
	 */
	public int skipChars(char[] c, int index) {
		return skipChars(c, index, lengthIndex, false);
	}

	/**
	 * Returns a subsequence of this character sequence.
	 * @param beginIndex the begin index.
	 * @param endIndex the end index.
	 * @return the subsequence.
	 */
	public CharSequence subSequence(int beginIndex, int endIndex) {
		return substring(beginIndex, endIndex);
	}

}