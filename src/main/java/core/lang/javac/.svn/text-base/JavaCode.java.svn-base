package core.lang.javac;

/**
 * A Java Code Block.
 */
public class JavaCode {

	/** The code. */
	private final String code;
	/** The version. */
	private final JavaVersion version;

	/**
	 * Returns the version.
	 * @return the version.
	 */
	public final JavaVersion getVersion() {
		return version;
	}

	/**
	 * Remove comments from the code.
	 * @param code the code.
	 * @return the code.
	 */
	public static final String removeComments(String code) {
		StringBuilder buffer = new StringBuilder();
		int index = 0;
		while (true) {
			int startCommentIndex = code.indexOf("/*", index);
			if (startCommentIndex == -1) {
				buffer.append(code.substring(index, code.length()));
				break;
			}
			int endCommentIndex = code.indexOf("*/", startCommentIndex + 2);
			if (endCommentIndex == -1) {
				buffer.append(code.substring(index, startCommentIndex));
				break;
			}
			buffer.append(code.substring(index, startCommentIndex));
			index = endCommentIndex + 2;
		}
		return buffer.toString();
	}

	/**
	 * Returns the class name.
	 * @param code the code.
	 * @return the class name.
	 */
	public static final String getPackageName(String code) {
		int index = code.indexOf("package");
		if (index != -1) {
			int colonIndex = code.indexOf(";", index);
			if (colonIndex != -1) {
				return code.substring(index + 7, colonIndex).trim();
			}
		}
		return "";
	}

	/**
	 * Returns the class name.
	 * @param code the code.
	 * @return the class name.
	 */
	public static final String getName(String code) {
		int index = code.indexOf("class");
		if (index == -1) {
			index = code.indexOf("interface");
			index += 4;
		}
		index += 5;
		StringBuilder name = new StringBuilder();
		for (; index < code.length(); index++) {
			char c = code.charAt(index);
			if (!Character.isWhitespace(c)) {
				break;
			}
		}
		for (; index < code.length(); index++) {
			char c = code.charAt(index);
			if (Character.isWhitespace(c)) {
				break;
			}
			name.append(c);
		}
		return name.toString();
	}

	/**
	 * Returns the code as a string.
	 * @return the code as a string.
	 */
	public String toString() {
		return code;
	}

	/**
	 * Returns the package and class names.
	 * @return the package and class names.
	 */
	public String getClassName() {
		return getPackageName() + '.' + getName();
	}

	/**
	 * Returns the package name.
	 * @return the package name.
	 */
	public String getPackageName() {
		return getPackageName(code);
	}

	/**
	 * Returns the class name.
	 * @return the class name.
	 */
	public String getName() {
		return getName(code);
	}

	/**
	 * Returns the class name for the given version.
	 * @param version the version.
	 * @return the class name.
	 */
	public String getClassName(JavaVersion version) {
		return getClassName() + version;
	}

	/**
	 * Modify this java code.
	 * @param version the version with which to modify the code.
	 */
	public final JavaCode newVersion(JavaVersion version) {
		String code = this.code; // removeComments(code);
		String className = getName(code);
		StringBuilder newCode = new StringBuilder();
		for (int i = 0; i < code.length(); i++) {
			char c = code.charAt(i);
			if (code.regionMatches(i, className, 0, className.length())) {
				char c1 = code.charAt(i - 1);
				char c2 = code.charAt(i + className.length());
				if (!Character.isLetter(c1) && !Character.isLetterOrDigit(c2)) {
					newCode.append(className);
					newCode.append(version);
					i += className.length() - 1;
				} else {
					newCode.append(c);
				}
			} else {
				newCode.append(c);
			}
		}
		return new JavaCode(newCode.toString(), version);
	}

	/**
	 * Creates a new JavaCode.
	 * @param code the code.
	 */
	private JavaCode(String code, JavaVersion version) {
		if (version == null) {
			throw new NullPointerException();
		}
		this.code = code; // removeComments(code);
		this.version = version;
	}

	/**
	 * Creates a new JavaCode.
	 * @param code the code.
	 */
	public JavaCode(String code) {
		this(code, new JavaVersion());
	}

}
