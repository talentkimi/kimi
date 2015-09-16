package core.io.file;

import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;

import core.util.UtilList;

/**
 * Overrides java.io.File and adds a root dir facility.
 */
public class File extends java.io.File {

	private static String rootDir = null;
	
	static {
		setRootDir(System.getProperty("c5.home"));
	}

	public static String getRootDir() {
		return rootDir;
	}

	private static void setRootDir(String rootDir) {
		if (rootDir != null) {
			rootDir = rootDir.trim();
			if (rootDir.length() == 0) {
				rootDir = null;
			} else if (!rootDir.endsWith("/") && !rootDir.endsWith("\"")) {
				rootDir = rootDir + "/";
			}
		}
		File.rootDir = rootDir;
	}

	/** The time the file was last modified. */
	private long timeLastModified;

	/**
	 * Creates a new file.
	 * @param filename the filename.
	 */
	public File(File parent, String filename) {
		super(parent, filename);
		modify();
	}

	/**
	 * Creates a new file.
	 * @param filename the filename.
	 */
	public File(String filename) {
		super(rootDir, filename);
		modify();
	}

	/**
	 * Specify that the file has been modified.
	 */
	public void modify() {
		this.timeLastModified = lastModified();
	}

	/**
	 * Returns true if this file has been modified.
	 * @return true if this file has been modified.
	 */
	public boolean isModified() {
		long modified = lastModified();
		if (timeLastModified != modified) {
			timeLastModified = modified;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Create this directory and check it exists.
	 */
	public final void createNewDirectoryAndCheckExists() throws FileNotFoundException {
		mkdir();
		if (!isDirectory()) throw new FileNotFoundException(toString());
	}

	/**
	 * Create this file and check it has read and write access.
	 */
	public final void createNewFileAndCheckIsReadWrite() throws IOException {
		createNewFile();
		if (!isFile()) throw new FileNotFoundException(toString());
		if (!canRead()) throw new IOException("Unable to read from file '" + this + "'");
		if (!canWrite()) throw new IOException("Unable to write to file '" + this + "'");
	}

	/**
	 * Adds files from the given directory into the list.
	 * @param list the file list.
	 * @param dir the directory.
	 * @param recursive true to list files recursively.
	 * @param filter the filter to use.
	 */
	private void addFilesToList(UtilList list, File dir, boolean recursive, FileFilter filter) {
		String[] filenames = dir.list();
		for (int i = 0; i < filenames.length; i++) {
			File file = (File) newFile(dir, filenames[i]);

			// Recursive
			if (recursive) {
				if (file.isDirectory()) {
					addFilesToList(list, file, recursive, filter);
				}
			}

			// Filter
			if (filter != null) {
				if (!filter.accept(file)) {
					continue;
				}
			}

			// Add
			list.add(file);
		}
	}

	/**
	 * List all files in this directory.
	 * @param recursive true to list files recursively.
	 * @param filter the filter to use.
	 */
	public File[] listFiles(boolean recursive, FileFilter filter) {
		if (!isDirectory()) return null;

		// Simple list
		if (!recursive && filter == null) {
			String[] filenames = list();
			File[] files = newFileArray(filenames.length);
			for (int i = 0; i < files.length; i++) {
				files[i] = newFile(this, filenames[i]);
			}
			return files;
		}

		// Complex list
		UtilList list = new UtilList();
		addFilesToList(list, this, recursive, filter);
		File[] files = newFileArray(list.size());
		list.toArray(files);
		return files;
	}

	/**
	 * List all files in this directory.
	 * @param recursive true to list files recursively.
	 * @param filter the filter to use.
	 */
	public File[] listFiles(boolean recursive, String regex) {
		return listFiles(recursive, new SimpleFileFilter(regex));
	}

	/**
	 * List all files in this directory.
	 * @param recursive true to list files recursively.
	 */
	public File[] listFiles(boolean recursive) {
		return listFiles(recursive, (FileFilter) null);
	}

	/**
	 * Creates and returns a new file.
	 * @param dir the directory.
	 * @param filename the filename.
	 * @return the new file.
	 */
	protected File newFile(File dir, String filename) {
		return new File(dir, filename);
	}

	/**
	 * Creates and returns a new array of files.
	 * @param length the length of the array.
	 * @return the new file array.
	 */
	protected File[] newFileArray(int length) {
		return new File[length];
	}

	/**
	 * Deletes this file and if it is a directory, recursively deletes all files and subdirectories!
	 */
	public void deleteAll() {
		File[] files = listFiles(false);
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				File file = (File) files[i];
				file.deleteAll();
			}
		}
		delete();
	}
}
