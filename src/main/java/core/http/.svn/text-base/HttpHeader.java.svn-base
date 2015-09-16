package core.http;

/**
 * An HTTP Message Header.
 */
public final class HttpHeader implements HttpMessageHeaderList {

	/** The name. * */
	private final String name;
	/** The value. * */
	private final String value;

	/**
	 * Returns the name.
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the value.
	 * @return the value.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Creates a new header.
	 * @param name the name.
	 * @param value the value.
	 */
	public HttpHeader(String name, String value) {
		if (name == null || value == null)
			throw new NullPointerException();
		this.name = name;
		this.value = value;
	}

	/**
	 * Creates a new header.
	 * @param name the name.
	 * @param value the value.
	 */
	public HttpHeader(String name, Object value) {
		this(name, value.toString());
	}

	/**
	 * Returns true if this header has the given name.
	 * @param name the name.
	 * @return true if this header has the given name.
	 */
	public boolean hasName(String name) {
		return this.name.equalsIgnoreCase(name);
	}

	/**
	 * Returns a string representation of this header.
	 */
	public String toString() {
		return getName() + ": " + getValue();
	}

}
