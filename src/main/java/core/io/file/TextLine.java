package core.io.file;

/**
 * A Line of Text
 */
public class TextLine {

	/** The text. * */
	private final String text;

	/**
	 * Returns the text.
	 * @return the text.
	 */
	public String getText() {
		return text;
	}

	/**
	 * Returns the length.
	 * @return the length.
	 */
	public int length() {
		return text.length();
	}
	
	/**
	 * Returns the text.
	 * @return the text.
	 */
	public String toString() {
		return text;
	}

	/**
	 * Returns true if this is empty.
	 * @return true if this is empty.
	 */
	public boolean isEmpty() {
		return text.trim().length() == 0;
	}

	/**
	 * Returns the comment.
	 * @return the comment.
	 */
	public boolean isComment() {
		String trimmed = text.trim();
		return trimmed.startsWith("//") || trimmed.startsWith("#");
	}

	/**
	 * Creates a line of text.
	 * @param line the line.
	 */
	public TextLine(String line) {
		if (line == null)
			throw new NullPointerException();
		this.text = line;
	}

	/**
	 * Splits this text using the given regular expression.
	 * @param regex the regular expression.
	 * @return the split strings.
	 */
	public String[] split(String regex) {
		return text.split(regex);
	}

}