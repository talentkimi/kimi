package core.io;

/**
 * A Compressed Object.
 */
public final class CompressedObject<O extends Object> {

	/** The object. */
	private O object = null;

	public final boolean isEmpty() {
		return (object == null);
	}

	/**
	 * Returns the object.
	 * @return the object.
	 */
	public synchronized O getObject() {
		return this.object;
	}

	/**
	 * Set this object.
	 * @param object the object.
	 */
	public synchronized void setObject(O object) {
		this.object = object;
	}

	/**
	 * Compress this object.
	 */
	public void compress() {
	}

	/**
	 * Creates a new container.
	 * @param gzip
	 */
	public CompressedObject(boolean gzip) {
	}

	/**
	 * Creates a new container.
	 * @param gzip
	 */
	public CompressedObject(boolean gzip, O object) {
		setObject(object);
	}
}
