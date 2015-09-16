package core.lang.javac;

import java.io.IOException;

import core.io.file.TextFile;

/**
 * A Java File.
 */
public class JavaFile extends TextFile {

	/**
	 * Returns a java file for the given path and class name.
	 * @param path the path.
	 * @param className the class name.
	 * @return the java file.
	 */
	public static final JavaFile getJavaFile(String path, String className, String charset) {
		StringBuilder filename = new StringBuilder();
		if (path.length() > 0) {
			filename.append(path.replace('\\', '/'));
			filename.append('/');
		}
		filename.append(className.replace('.', '/'));
		filename.append(".java");
		return new JavaFile(filename.toString(), charset);
	}

	/**
	 * Returns the filename with the given extension.
	 * @param extension the extension.
	 * @return the filename.
	 */
	public String getFilename(String extension) {
		String path = getParent();
		String name = getName();
		int index = name.lastIndexOf('.');
		name = name.substring(0, index) + extension;
		return path.replace('\\', '/') + '/' + name;
	}

	/**
	 * Returns the filename for the given version.
	 * @param version the version.
	 * @return the filename.
	 */
	public String getJavaFilename(JavaVersion version) {
		if (version.isStartVersion()) {
			return getFilename(".java");
		}
		return getFilename(version + ".java");
	}

	/**
	 * Returns the java filename.
	 * @return the java filename.
	 */
	public String getJavaFilename() {
		return getFilename(".java");
	}

	/**
	 * Returns the class filename.
	 * @return the class filename.
	 */
	public String getClassFilename() {
		return getFilename(".class");
	}

	/**
	 * Returns the next version of this file.
	 * @return the next version.
	 */
	public JavaVersion getNextVersion() {
		JavaVersion version = new JavaVersion().next();
		while (true) {
			String filename = getJavaFilename(version);
			if (!new JavaFile(filename, getCharset()).exists()) {
				break;
			}
			version = version.next();
		}
		return version;
	}

	/**
	 * Reads the code from this file.
	 * @return the code.
	 */
	public JavaCode readCode() throws IOException {
		return new JavaCode(readToString());
	}

	/**
	 * Writes the given code to the appropriately versioned file.
	 * @param code the code.
	 */
	public void writeCode(JavaCode code) throws IOException {
		String filename = getJavaFilename(code.getVersion());
		JavaFile file = new JavaFile(filename, getCharset());
		file.write(code.toString());
	}

	/**
	 * Creates a new java file.
	 * @param filename the filename.
	 * @param charset the character set.
	 */
	public JavaFile(String filename, String charset) {
		super(filename);
		setCharset(charset);
	}
}
