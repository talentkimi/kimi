package core.io.file;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

import core.util.UtilList;

/**
 * A Simple File Filter.
 */
public final class SimpleFileFilter implements FilenameFilter, FileFilter {

	/** Indicates if matches should be accepted. */
	private final boolean accept;

	/**
	 * Creates a new simple file filter.
	 * @param pattern the file matching regular expression.
	 * @param accept true to accept matches.
	 */
	public SimpleFileFilter(String pattern, boolean accept) {
		this.accept = accept;
		addPattern(pattern);
	}

	/**
	 * Creates a new simple file filter.
	 * @param pattern the file matching regular expression.
	 */
	public SimpleFileFilter(String pattern) {
		this(pattern, true);
	}

	/* The pattern list. */
	private UtilList patternList = new UtilList();

	/**
	 * Add the given pattern.
	 * @param pattern the pattern.
	 */
	public SimpleFileFilter addPattern(String pattern) {
		if (pattern == null) {
			throw new NullPointerException();
		}
		this.patternList.add(pattern);
		return this;
	}

	/**
	 * Returns true if the given file is accepted.
	 * @param dir the directory.
	 * @param name the name.
	 * @return true if the given file is accepted.
	 */
	public boolean accept(File dir, String name) {
		for (int i = 0; i < patternList.size(); i++) {
			String pattern = (String) patternList.get(i);
			if (name.matches(pattern)) {
				return accept;
			}
		}
		return !accept;
	}

	/**
	 * Returns true if the given pathname is accepted.
	 * @param pathname the pathname.
	 * @return true if the given pathname is accepted.
	 */
	public boolean accept(File pathname) {
		return accept(pathname.getParentFile(), pathname.getName());
	}
}