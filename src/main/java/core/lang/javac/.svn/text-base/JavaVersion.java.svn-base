package core.lang.javac;

/**
 * The Java Version.
 */
public final class JavaVersion {

	/** The start version. */
	private static final Integer START_VERSION = new Integer(1000);

	/** The version. */
	private final Integer version;

	/**
	 * Returns this as a string.
	 * @return this as a string.
	 */
	public String toString() {
		return version.toString();
	}
	
	/**
	 * Returns true if this is the start version.
	 * @return true if this is the start version.
	 */
	public boolean isStartVersion() {
		return version.equals(START_VERSION);
	}

	/**
	 * Returns the next version.
	 * @return the next version.
	 */
	public JavaVersion next() {
		return new JavaVersion(version + 1);
	}

	/**
	 * Creates a new JavaVersion.
	 * @param version the version.
	 */
	public JavaVersion(Integer version) {
		this.version = version;
	}

	/**
	 * Creates a new JavaVersion.
	 */
	public JavaVersion() {
		this(START_VERSION);
	}

}