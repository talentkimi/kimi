package core.text;

import java.math.BigDecimal;

public class StringDigestor {

	private int index;
	private final String string;

	public void reset() {
		index = 0;
	}

	public String character() {
		if (index >= string.length()) {
			return "";
		}
		index++;
		return string.substring(index - 1, index);
	}

	public String find(char c, boolean includeCharacter) {
		int beginIndex = index;
		int endIndex = string.indexOf(c, beginIndex);
		if (endIndex == -1) {
			return null;
		}
		if (includeCharacter) {
			endIndex++;
		}
		index = endIndex;
		return string.substring(beginIndex, endIndex);
	}

	public String find(String text, boolean includeText) {
		int beginIndex = index;
		int endIndex = string.indexOf(text, beginIndex);
		if (endIndex == -1) {
			return null;
		}
		if (includeText) {
			endIndex += text.length();
		}
		index = endIndex;
		return string.substring(beginIndex, endIndex);
	}

	public String whitespace() {
		int beginIndex = index;
		int endIndex = index;
		for (; endIndex < string.length(); endIndex++) {
			char c = string.charAt(endIndex);
			if (!Character.isWhitespace(c)) {
				break;
			}
		}
		index = endIndex;
		return string.substring(beginIndex, endIndex);
	}

	public String digits() {
		int beginIndex = index;
		int endIndex = index;
		for (; endIndex < string.length(); endIndex++) {
			char c = string.charAt(endIndex);
			if (!Character.isDigit(c)) {
				break;
			}
		}
		index = endIndex;
		return string.substring(beginIndex, endIndex);
	}

	public String letters() {
		int beginIndex = index;
		int endIndex = index;
		for (; endIndex < string.length(); endIndex++) {
			char c = string.charAt(endIndex);
			if (!Character.isLetter(c)) {
				break;
			}
		}
		index = endIndex;
		return string.substring(beginIndex, endIndex);
	}

	public String alpha() {
		int beginIndex = index;
		int endIndex = index;
		for (; endIndex < string.length(); endIndex++) {
			char c = string.charAt(endIndex);
			if (!Character.isLetterOrDigit(c)) {
				break;
			}
		}
		index = endIndex;
		return string.substring(beginIndex, endIndex);
	}

	public String text() {
		int beginIndex = index;
		int endIndex = index;
		for (; endIndex < string.length(); endIndex++) {
			char c = string.charAt(endIndex);
			if (Character.isWhitespace(c)) {
				break;
			}
		}
		index = endIndex;
		return string.substring(beginIndex, endIndex);
	}

	public String decimal() {
		int beginIndex = index;
		int endIndex = index;
		for (; endIndex < string.length(); endIndex++) {
			char c = string.charAt(endIndex);
			if (!Character.isDigit(c) && c != '.') {
				break;
			}
		}
		index = endIndex;
		return string.substring(beginIndex, endIndex);
	}

	public BigDecimal parseBigDecimal() {
		return new BigDecimal(decimal());
	}

	public long parseLong() {
		return Long.parseLong(digits());
	}

	public double parseDouble() {
		return Double.parseDouble(decimal());
	}

	public String get() {
		return string.substring(index);
	}

	public StringDigestor(String string) {
		if (string == null) {
			throw new NullPointerException();
		}
		this.string = string;
		this.index = 0;
	}

}
