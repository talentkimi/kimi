package core.io.file;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import core.io.StreamInput;
import core.io.StreamOutput;
import core.io.StreamReader;
import core.io.StreamWriter;
import core.text.Charsets;
import core.text.Text;


/**
 * A Text File. Includes methods for reading and writing strings.
 */
public class TextFile extends BinaryFile {

	/** The DOS new-line */
	public static final String DOS_NEW_LINE = "\r\n";
	/** The UNIX new-line */
	public static final String UNIX_NEW_LINE = "\n";
	/** The MAC new-line */
	public static final String MAC_NEW_LINE = "\r";

	/** The system dependent newline characters. */
	public static final String newLine = System.getProperty("line.separator");

	/** The character set. */
	private String charset = Charsets.SYSTEM;

	/**
	 * Sets the character set to use for reading and writing.
	 * @param charset the character set.
	 */
	public final void setCharset(String charset) {
		Charset.forName(charset);
		this.charset = charset;
	}

	/**
	 * Returns the character set used when reading and writing.
	 * @return the character set used when reading and writing.
	 */
	public final String getCharset() {
		return charset;
	}

	/**
	 * Creates a new file.
	 * @param filename the filename.
	 */
	public TextFile(File parent, String filename) {
		super(parent, filename);
	}

	/**
	 * Creates a new file.
	 * @param filename the filename.
	 */
	public TextFile(String filename) {
		super(filename);
	}

	/**
	 * Creates and returns a new file.
	 * @param dir the directory.
	 * @param filename the filename.
	 * @return the new file.
	 */
	protected TextFile newFile(File dir, String filename) {
		return new TextFile(dir, filename);
	}

	/**
	 * Creates and returns a new array of files.
	 * @param length the length of the array.
	 * @return the new file array.
	 */
	protected TextFile[] newFileArray(int length) {
		return new TextFile[length];
	}

	/**
	 * Returns the contents of this file as a string.
	 * @return the contents of this file as a string.
	 * @throws IOException if an IO error occurs during reading.
	 * @throws UnsupportedEncodingException if the character set is not supported.
	 */
	public String readToString() throws IOException {
		byte[] b = readToByteArray();
		return new String(b, getCharset());
	}

	/**
	 * Returns the contents of this file as a string array.
	 * @return the contents of this file as a string array.
	 * @throws IOException if an IO error occurs during reading.
	 * @throws UnsupportedEncodingException if the character set is not supported.
	 */
	public String[] readToStringArray() throws IOException {
		String string = readToString();
		string = Text.remove(string, "\r");
		if (string.indexOf(UNIX_NEW_LINE) != -1) {
			return Text.split(string, UNIX_NEW_LINE);
		}
		return new String[]{string};
	}

	/**
	 * Returns the contents of this file as a string array.
	 * @return the contents of this file as a string array.
	 * @throws IOException if an IO error occurs during reading.
	 * @throws UnsupportedEncodingException if the character set is not supported.
	 */
	public String[][] readToStringArray(String delimiter) throws IOException {
		if (delimiter == null) {
			throw new NullPointerException();
		}
		if (delimiter.length() == 0) {
			throw new IllegalArgumentException("empty delimiter");
		}
		String[] lines = readToStringArray();
		return toStringArray(lines, delimiter);
	}

	/**
	 * Returns the given lines split on the given delimiter.
	 * @param lines the lines.
	 * @param delimiter the delimiter.
	 * @return the lines.
	 */
	private String[][] toStringArray(String[] lines, String delimiter) {
		String[][] array = new String[lines.length][];
		for (int i = 0; i < array.length; i++) {
			array[i] = Text.split(lines[i], delimiter);
		}
		return array;
	}

	/**
	 * Writes the contents of this file from the given string.
	 * @param s the string.
	 * @throws IOException if an IO error occurs during writing.
	 */
	public void write(String s, boolean append) throws IOException {
		StreamWriter writer = new StreamWriter(this, append);
		writer.setCharset(getCharset());
		try {
			writer.write(s);
			writer.flush();
		} catch (IOException ioe) {
			throw ioe;
		} finally {
			modify();
			writer.close();
		}
	}

	/**
	 * Writes the contents of this file from the given string.
	 * @param s the string.
	 * @throws IOException if an IO error occurs during writing.
	 */
	public void write(String s) throws IOException {
		write(s, false);
	}

	/**
	 * Writes the contents of this file from the given string.
	 * @param s the string.
	 * @throws IOException if an IO error occurs during writing.
	 */
	public void write(String s, int blockSize) throws IOException {
		if (blockSize < 1)
			throw new IllegalArgumentException("blockSize=" + blockSize);
		if (s.length() == 0)
			return;
		StreamOutput stream = new StreamOutput(this);
		try {
			int beginIndex = 0;
			int endIndex = 0;
			int previousPercent = 0;
			while (endIndex < s.length()) {
				endIndex = beginIndex + blockSize;
				if (endIndex > s.length()) {
					endIndex = s.length();
				}
				byte[] b = s.substring(beginIndex, endIndex).getBytes(getCharset());
				stream.write(b);
				stream.flush();
				beginIndex = endIndex;
			}
		} catch (IOException ioe) {
			throw ioe;
		} finally {
			modify();
			stream.close();
		}
	}

	/**
	 * Writes the contents of this file from the given string buffer.
	 * @param sb the string buffer.
	 * @throws IOException if an IO error occurs during writing.
	 */
	public void write(StringBuilder sb) throws IOException {
		write(sb.toString());
	}

	/**
	 * Writes the contents of this file from the given string array
	 * @param lineArray the string array.
	 * @throws IOException if an IO error occurs during writing.
	 */
	public void write(String[] lineArray) throws IOException {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < lineArray.length; i++) {
			sb.append(lineArray[i]).append(newLine);
		}
		write(sb);
	}

	/**
	 * Returns the contents of this file as a string array.
	 * @return the contents of this file as a string array.
	 * @throws IOException if an IO error occurs during reading.
	 * @throws UnsupportedEncodingException if the character set is not supported.
	 */
	public TextLine[] readToLineArray() throws IOException {
		String[] stringArray = readToStringArray();
		TextLine[] lineArray = new TextLine[stringArray.length];
		for (int i = 0; i < lineArray.length; i++) {
			lineArray[i] = new TextLine(stringArray[i]);
		}
		return lineArray;
	}

	/**
	 * Count the number of lines in this file.
	 * @return the number of lines.
	 */
	public int countLines() throws IOException {
		int count = 0;
		StreamInput input = new StreamInput(this);
		try {
			long fileLength = length();
			for (int i = 0; i < fileLength; i++) {
				if (input.read() == 10)
					count++;
			}
		} finally {
			input.close();
		}
		return count;
	}

	/**
	 * Count the number of lines in this file.
	 * @return the number of lines.
	 */
	public String[][] readToStringArray(String delimiter, int offset, int length) throws IOException {
		StreamReader reader = new StreamReader(this);
		try {
			if (offset != 0) {
				long fileLength = length();
				for (int i = 0; i < fileLength; i++) {
					if (reader.read() == 10) {
						offset--;
						if (offset == 0) {
							break;
						}
					}
				}
			}
			String[] array = new String[length];
			for (int i = 0; i < length; i++) {
				array[i] = reader.readLine();
			}
			return toStringArray(array, delimiter);
		} finally {
			reader.close();
		}
	}

}