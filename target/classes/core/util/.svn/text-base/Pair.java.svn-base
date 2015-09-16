package core.util;

/**
 * A Key-Value Pair.
 */
public class Pair {

	/** The key. * */
	private Object key = null;
	/** The value. * */
	private Object value = null;

	/**
	 * Returns the key.
	 * @return the key.
	 */
	public final Object getKey() {
		return key;
	}

	/**
	 * Returns the value.
	 * @return the value.
	 */
	public final Object getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 * @param value the value.
	 */
	public final void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Returns true if this xml has a value.
	 */
	public final boolean hasAValue() {
		return value != null;
	}

	/**
	 * Creates a new key-value pair.
	 * @param key the key.
	 */
	public Pair(Object key) {
		if (key == null)
			throw new NullPointerException();
		this.key = key;
	}

	/**
	 * Creates a new key-value pair.
	 * @param key the key.
	 * @param value the value.
	 */
	public Pair(Object key, Object value) {
		this(key);
		if (value == null)
			throw new NullPointerException();
		this.value = value;
	}

	public Pair() {
	}

	/**
	 * Returns true if this pair equals the given object.
	 * @param object the object.
	 * @return true if this pair equals the given object.
	 */
	public boolean equals(Object object) {
		if (object instanceof Pair) {
			Pair pair = (Pair) object;
			if (this.key.equals(pair.key)) {
				if (value != null) {
					return this.value.equals(pair.value);
				} else {
					return pair.value == null;
				}
			}
		}
		return super.equals(object);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	/**
	 * Returns a string representation of this pair.
	 */
	public String toString() {
		return getKey() + "=" + getValue();
	}

}