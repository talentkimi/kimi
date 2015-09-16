package core.io;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;

import core.io.file.TextFile;
import core.text.Charsets;

/**
 * A Stream Writer.
 */
public class StreamWriter extends StreamOutput {

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
	 * Creates a new output stream.
	 * @param os the output stream.
	 */
	public StreamWriter(OutputStream os) throws IOException {
		super(os);
	}

	/**
	 * Creates a new output stream.
	 * @param f the file.
	 */
	public StreamWriter(File f) throws IOException {
		super(f);
	}

	/**
	 * Creates a new output stream.
	 * @param f the file.
	 */
	public StreamWriter(File f, boolean append) throws IOException {
		super(f, append);
	}

	/**
	 * Creates a new output stream.
	 * @param s the socket.
	 */
	public StreamWriter(Socket s) throws IOException {
		super(s);
	}

	/**
	 * Writes a newline to the stream.
	 */
	public void newLine() throws IOException {
		byte[] bytes = TextFile.newLine.getBytes(getCharset());
		// Default behaviour should be multi-byte write
		if (isMultibyteWrite == null || isMultibyteWrite.booleanValue()) {
			// Call the BufferedOutpuStream write
			write(bytes, 0, bytes.length);
		} else {
			write(TextFile.newLine.getBytes(getCharset()));
		}
	}

	/**
	 * Writes the given string to the stream.
	 * @param s the string to write.
	 * @param off the offset.
	 * @param len the length.
	 */
	public void write(String s, int off, int len) throws IOException {
		byte[] bytes = s.substring(off, off + len).getBytes(getCharset());
		// Default behaviour should be multi-byte write
		if (isMultibyteWrite == null || isMultibyteWrite.booleanValue()) {
			// Call the BufferedOutpuStream write
			write(bytes, 0, bytes.length);
		} else {
			// Call the FilterOutpuStream write
			write(bytes);
		}
	}

	/**
	 * Writes the given string to the stream.
	 * @param s the string to write.
	 */
	public void write(String s) throws IOException {
		byte[] bytes = s.getBytes(getCharset());
		// Default behaviour should be multi-byte write
		if (isMultibyteWrite == null || isMultibyteWrite.booleanValue()) {
			// Call the BufferedOutpuStream write
			write(bytes, 0, bytes.length);
		} else {
			// Call the FilterOutpuStream write
			write(bytes);
		}
	}
}
