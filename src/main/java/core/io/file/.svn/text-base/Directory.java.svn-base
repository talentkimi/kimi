package core.io.file;

import java.io.File;
import java.io.IOException;

/**
 * A Directory.
 */
public class Directory extends BinaryFile {

	/**
	 * Creates a new directory.
	 * @param filename the filename.
	 */
	public Directory(String filename, boolean create) throws IOException {
		super(filename);
		check(create);
	}

	/**
	 * Check the this directory.
	 * @param create true to create.
	 */
	private final void check(boolean create) throws IOException {
		String filename = getPath();
		if (exists()) {
			if (!isDirectory()) {
				throw new IOException("File is not a directory: '" + filename + "'");
			}
		} else {
			if (create) {
				if (!mkdirs()) {
					throw new IOException("Unable to create directory: '" + filename + "'");
				}
				if (!exists()) {
					throw new IOException("Unable to create directory: '" + filename + "'");
				}
				if (!isDirectory()) {
					throw new IOException("File is not a directory: '" + filename + "'");
				}
			} else {
				throw new IOException("Directory does not exist: '" + filename + "'");
			}
		}
	}

	/**
	 * Creates a new directory.
	 * @param filename the filename.
	 */
	public Directory(BinaryFile parent, String filename, boolean create) throws IOException {
		super(parent, filename);
		check(create);
	}

	/**
	 * Return the path for the given filename.
	 * @param filename the filename.
	 * @return the path.
	 */
	public String getPath(String filename) {
		return getFile(filename).getPath();
	}

	/**
	 * Return the file for the given filename.
	 * @param filename the filename.
	 * @return the file.
	 */
	public File getFile(String filename) {
		return new BinaryFile(this, filename);
	}

	/**
	 * Return the file for the given filename.
	 * @param filename the filename.
	 * @return the file.
	 */
	public Directory getDirectory(String filename, boolean create) throws IOException {
		return new Directory(toString() + filename, create);
	}

	/**
	 * Returns the space used by this directory and its contents!
	 * @return the space used.
	 */
	public long used() {
		long used = 0;
		File[] files = listFiles(true);
		for (int i = 0; i < files.length; i++) {
			used += files[i].length();
		}
		return used;
	}
}
