package core.lang.javac;

import java.io.File;

import core.util.UtilList;

/**
 * A Javac Class Path.
 */
public final class JavacClasspath {

	/** The list. */
	private final UtilList list = new UtilList();

	/**
	 * Clear the classpath.
	 */
	public void clear() {
		list.clear();
	}

	/**
	 * Add the given path.
	 * @param path the path.
	 */
	public void addDirectory(String path) {
		File directory = new File(path);
		if (!directory.exists()) {
			throw new IllegalArgumentException("directory does not exist: '" + path + "'");
		}
		if (!directory.isDirectory()) {
			throw new IllegalArgumentException("file is not a directory: '" + path + "'");
		}
		list.add(directory.toString());
	}

	/**
	 * Add the given path.
	 * @param path the path.
	 */
	public void addJar(String path) {
		File jarFile = new File(path);
		if (!jarFile.exists()) {
			throw new IllegalArgumentException("jar file does not exist: '" + path + "'");
		}
		String jarFilename = jarFile.toString();
		if (!jarFilename.toLowerCase().endsWith(".jar")) {
			throw new IllegalArgumentException("file is not a jar file: '" + path + "'");
		}
		list.add(jarFilename);
	}

	/**
	 * Add the given path.
	 * @param path the path.
	 */
	public void addJars(String path) {
		File directory = new File(path);
		if (!directory.exists()) {
			throw new IllegalArgumentException("directory does not exist: '" + path + "'");
		}
		if (!directory.isDirectory()) {
			throw new IllegalArgumentException("file is not a directory: '" + path + "'");
		}
		File[] files = directory.listFiles();
		boolean found = false;
		for (int i = 0; i < files.length; i++) {
			String jarFilename = files[i].toString();
			if (jarFilename.toLowerCase().endsWith(".jar")) {
				list.add(jarFilename);
				found = true;
			}
		}
		if (!found) {
			throw new IllegalArgumentException("directory contains no jar files: '" + path + "'");
		}
	}

	/**
	 * Returns true if this is empty.
	 * @return true if this is empty.
	 */
	public boolean isEmpty() {
		return list.size() == 0;
	}

	/**
	 * Returns this as a string.
	 * @return this as a string.
	 */
	public String toString() {
		String pathSeparator = System.getProperty("path.separator");
		StringBuilder path = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			if (i > 0) {
				path.append(pathSeparator);
			}
			path.append(list.get(i));
		}
		return path.toString();
	}

	/**
	 * Returns this as an array.
	 * @return this as an array.
	 */
	public String[] toArray() {
		String[] array = new String[list.size()];
		list.toArray(array);
		return array;
	}
	
}
