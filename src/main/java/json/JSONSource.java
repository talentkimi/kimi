package json;

/**
 * JSON Source
 * 
 * @author vesko
 */
final public class JSONSource {
	/**
	 * source buffer index
	 */
	private int index = 0;
	/**
	 * source buffer
	 */
	final private StringBuffer source = new StringBuffer();

	/**
	 * constructor
	 * 
	 * @param source
	 * @throws NullPointerException
	 */
	public JSONSource(StringBuffer source) throws NullPointerException {
		if (source == null) {
			throw new NullPointerException();
		}
		index = 0;
		this.source.append(source.toString());
	}

	/**
	 * go back one character
	 * 
	 */
	final public void back() {
		if (index > 0) {
			index -= 1;
		}
	}

	/**
	 * is empty
	 * 
	 * @return boolean [true if there are no more characters]
	 */
	final public boolean empty() {
		return (index >= source.length());
	}

	/**
	 * get next character
	 * 
	 * @return char [next character]
	 */
	final public char next() {
		if (!this.empty()) {
			char character = source.charAt(index);
			index += 1;
			return character;
		}
		return 0;
	}

	/**
	 * get next character and check does match the searched one
	 * 
	 * @param match
	 * @return char [next character]
	 * @throws IllegalArgumentException
	 */
	final public char next(char match) throws IllegalArgumentException {
		char character = this.next();
		if (character != match) {
			throw new IllegalArgumentException("Expected '" + match + "' instead '" + character + "'");
		}
		return character;
	}

	/**
	 * get next 'length' characters
	 * 
	 * @param length
	 * @return string [next 'size' characters]
	 * @throws IllegalArgumentException
	 */
	final public String next(int length) throws IllegalArgumentException {
		if (length < 1) {
			throw new IllegalArgumentException("Invalid length: " + length);
		}
		int bIdx = index;
		int eIdx = bIdx + length;
		if (eIdx >= source.length()) {
			throw new IllegalArgumentException("Index out of bounds: " + eIdx);
		}
		index += length;
		return source.substring(bIdx, eIdx);
	}

	/**
	 * get the rest of the source
	 * 
	 * @return string [rest of the source]
	 */
	final public String restSource() {
		return source.substring(index);
	}

	/**
	 * get next character, skipping whitespace's and comment's
	 * 
	 * @return char [next character]
	 * @throws IllegalArgumentException
	 */
	final public char nextCharacter() throws IllegalArgumentException {
		for (;;) {
			char character = this.next();
			if (character == '/') {
				switch (this.next()) {
				case '/':
					do {
						character = this.next();
					} while (character != '\n' && character != '\r' && character != 0);
					break;
				case '*':
					for (;;) {
						character = this.next();
						if (character == 0) {
							throw new IllegalArgumentException("Unclosed comment");
						}
						if (character == '*') {
							if (this.next() == '/') {
								break;
							}
							this.back();
						}
					}
					break;
				default:
					this.back();
					return '/';
				}
			} else if (character == '#') {
				do {
					character = this.next();
				} while (character != '\n' && character != '\r' && character != 0);
			} else if (character == 0 || character > ' ') {
				return character;
			}
		}
	}

	/**
	 * get next value
	 * 
	 * @param quote
	 * @return string [next value]
	 * @throws IllegalArgumentException
	 */
	final public JSONString nextValue(char quote) throws IllegalArgumentException {
		StringBuffer buffer = new StringBuffer();
		for (;;) {
			char character = this.next();
			switch (character) {
			case 0:
			case '\n':
			case '\r':
				throw new IllegalArgumentException("Unterminated string");
			case '\\':
				character = this.next();
				switch (character) {
				case 'b':
					buffer.append('\b');
					break;
				case 't':
					buffer.append('\t');
					break;
				case 'n':
					buffer.append('\n');
					break;
				case 'f':
					buffer.append('\f');
					break;
				case 'r':
					buffer.append('\r');
					break;
				case 'u':
					buffer.append((char) Integer.parseInt(this.next(4), 16));
					break;
				case 'x':
					buffer.append((char) Integer.parseInt(this.next(2), 16));
					break;
				default:
					buffer.append(character);
					break;
				}
				break;
			default:
				if (character == quote) {
					return new JSONString(buffer);
				}
				buffer.append(character);
				break;
			}
		}
	}

	/**
	 * get next object
	 * 
	 * @return JSONObject [next object]
	 * @throws IllegalArgumentException
	 */
	final public JSONObject nextObject() throws IllegalArgumentException {
		char character = this.nextCharacter();
		switch (character) {
		case '"':
		case '\'':
			return this.nextValue(character);
		case '{':
			this.back();
			return new JSONElement(this, false);
		case '[':
			this.back();
			return new JSONElementList(this, false);
		}
		StringBuffer buffer = new StringBuffer();

		while (character >= ' ' && ",:]}/\\\"[{;=#".indexOf(character) < 0) {
			buffer.append(character);
			character = this.next();
		}
		this.back();

		if (buffer.length() == 0) {
			throw new IllegalArgumentException("element value is missing");
		}
		return new JSONString(buffer);
	}

	/**
	 * close object
	 * 
	 */
	final public void close() {
		source.setLength(0);
		index = 0;
	}
}