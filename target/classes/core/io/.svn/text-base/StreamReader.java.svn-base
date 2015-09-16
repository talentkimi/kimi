package core.io;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.Charset;

import core.text.Charsets;

/**
 * A Stream Reader.
 */
public class StreamReader extends StreamInput {

	/** The character set. */
	private String charset = Charsets.SYSTEM;

	/**
	 * The character set.
	 * @return the character set.
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * Sets the character set.
	 * @param charset the character set.
	 */
	public void setCharset(String charset) {
		Charset.forName(charset);
		this.charset = charset;
	}

	/**
	 * Creates a new input stream.
	 * @param f the file.
	 */
	public StreamReader(File f) throws FileNotFoundException {
		super(f);
	}

	/**
	 * Creates a new input stream.
	 * @param s the socket.
	 */
	public StreamReader(Socket s) throws IOException {
		super(s);
	}

	/**
	 * Creates a new input stream.
	 * @param input the input.
	 */
	public StreamReader(InputStream input) {
		super(input);
	}

	/**
	 * Creates a new input stream.
	 * @param b the byte array.
	 */
	public StreamReader(byte[] b) {
		super(new ByteArrayInputStream(b));
	}

	/**
	 * Creates a new input stream.
	 * @param s the string.
	 */
	public StreamReader(String s) {
		this(s.getBytes());
	}

	/**
	 * Reads the contents of this reader to a string.
	 * @return the string.
	 * @throws IOException if an IO error occurs reading the string.
	 */
	public String readToString() throws IOException {
		byte[] b = readToByteArray();
		return new String(b, getCharset());
	}

	/**
	 * Reads the the given number of bytes and converts them to a string.
	 * @return the string.
	 * @throws IOException if an IO error occurs reading the string.
	 */
	public String readToString(int bytes) throws IOException {
		byte[] b = readToByteArray(bytes);
		return new String(b, getCharset());
	}

	/**
	 * Reads and returns a line of text.
	 * @return a line of text or null if the end of stream has been reached.
	 */
	public String readLine() throws IOException {
		byte[] line = readLineBytes();
		if (line == null) {
			return null;
		}
		return new String(line, getCharset());
	}
}