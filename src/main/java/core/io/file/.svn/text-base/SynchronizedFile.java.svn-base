package core.io.file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

import core.text.Charsets;
import core.util.UtilDate;

/**
 * A Synchronized File.
 */
public final class SynchronizedFile {

	/** The cache. */
	private static final Hashtable cache = new Hashtable();

	/**
	 * Returns the named file.
	 * @param name the name.
	 * @return the file.
	 */
	public static final synchronized SynchronizedFile get(String name) throws IOException {
		if (name == null) {
			throw new NullPointerException();
		}
		SynchronizedFile file = (SynchronizedFile) cache.get(name);
		if (file == null) {
			file = new SynchronizedFile("logs/" + name);
		}
		return file;
	}

	/** The output stream. */
	private final OutputStream output;

	/**
	 * Creates a new synchronized file.
	 * @param name the name.
	 */
	private SynchronizedFile(String name) throws IOException {
		File file = new File(name);
		FileOutputStream fileOutput = new FileOutputStream(file, true);
		this.output = new BufferedOutputStream(fileOutput);
	}

	/**
	 * Write the given text.
	 * @param text the text.
	 */
	private synchronized final void write(String text) throws IOException {
		byte[] bytes = text.getBytes(Charsets.UTF_8);
		output.write(bytes);
		output.flush();
	}

	/**
	 * Append the given text to the file.
	 * @param text the text.
	 */
	public synchronized void append(String text) throws IOException {
		if (text == null) {
			text = "";
		}
		long time = System.nanoTime();
		long time1 = time / 1000000;
		long time2 = time % 1000000;
		write(new UtilDate(time1).toString("[yy/MM/dd-HH:mm:ss SSS:") + time2 + "]" + TextFile.newLine);
		write(text + TextFile.newLine);
	}

	/**
	 * Close the file.
	 */
	public synchronized void close() throws IOException {
		output.close();
	}

	public static void main(String[] args) throws Exception {
		get("yo").append("fred");
		get("yo").append("fred");
		get("yo").append("fred");
		get("yo").append("fred");
	}

}
