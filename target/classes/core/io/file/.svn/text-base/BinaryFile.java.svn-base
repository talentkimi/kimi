package core.io.file;

import java.io.FileNotFoundException;
import java.io.IOException;

import core.io.StreamInput;
import core.io.StreamOutput;

/**
 * A Binary File. Includes methods for checking for file modification.
 */
public class BinaryFile extends File {

	/**
	 * Clear this file.
	 */
	public final void clear() throws IOException {
		write(new byte[0]);
	}

	/**
	 * Creates a new file.
	 * @param filename the filename.
	 */
	public BinaryFile(File parent, String filename) {
		super(parent, filename);
		modify();
	}

	/**
	 * Creates a new file.
	 * @param filename the filename.
	 */
	public BinaryFile(String filename) {
		super(filename);
		modify();
	}

	/**
	 * Writes the contents of this file from the given byte array.
	 * @param b the byte array.
	 * @throws IOException if an IO error occurs during writing.
	 */
	public void write(byte[] b, boolean append) throws IOException {
		if (b.length == 0) return;
		StreamOutput stream = new StreamOutput(this, append);
		try {
			stream.write(b);
			stream.flush();
		} catch (IOException ioe) {
			throw ioe;
		} finally {
			modify();
			stream.close();
		}
	}

	/**
	 * Writes the contents of this file from the given byte array.
	 * @param b the byte array.
	 * @throws IOException if an IO error occurs during writing.
	 */
	public void write(byte[] b) throws IOException {
		write(b, false);
	}

	/**
	 * Writes the contents of this file from the given byte array.
	 * @param b the byte array.
	 * @throws IOException if an IO error occurs during writing.
	 */
	public void write(byte[] b, int blockSize) throws IOException {
		if (blockSize < 1) throw new IllegalArgumentException("blockSize=" + blockSize);
		if (b.length == 0) return;
		StreamOutput stream = new StreamOutput(this);
		try {
			int index = 0;
			boolean finished = false;
			while (!finished) {
				int length = blockSize;
				if (length > b.length - index) {
					length = b.length - index;
					finished = true;
				}
				stream.write(b, index, length);
				stream.flush();
				index += length;
			}
		} catch (IOException ioe) {
			throw ioe;
		} finally {
			modify();
			stream.close();
		}
	}

	/**
	 * Read this to the given file.
	 */
	public void copyTo(File file) throws IOException {
		int bufferSize = 8192;
		StreamInput input = new StreamInput(this, bufferSize);
		try {
			StreamOutput output = new StreamOutput(file, bufferSize);
			try {
				long length = length();
				// if (log.isDebugEnabled()) log.debug ("copying " + getName() + " to " +
				// file.getPath());
				// String msg = "copying file";
				// Print.percent(msg, 0);
				int nextCheckI = bufferSize;
				for (long i = 0; i < length; i++) {
					if (i >= nextCheckI) {
						int percentDone = (int) ((i * 100) / length);
						// Print.percent(msg, percentDone);
						nextCheckI += bufferSize;
					}
					int b = input.read();
					output.write(b);
				}
				// Print.percent(msg, 100);
			} finally {
				output.close();
			}
		} finally {
			input.close();
		}
	}

	/**
	 * Returns the contents of this file as a byte array.
	 * @return the contents of this file as a byte array.
	 * @throws IOException if an IO error occurs during reading.
	 */
	public byte[] readToByteArray() throws IOException {
		if (!isFile()) {
			throw new FileNotFoundException(toString());
		}
		int length = (int) length();
		if (length == 0) {
			return new byte[0];
		}
		StreamInput stream = new StreamInput(this);
		try {
			return stream.readToByteArray(length);
		} catch (IOException ioe) {
			throw ioe;
		} finally {
			modify();
			stream.close();
		}
	}
	
	protected BinaryFile newFile(File dir, String filename) {
		return new BinaryFile(dir, filename);
	}
	
	@Override
	protected BinaryFile[] newFileArray(int length) {
		return new BinaryFile[length];
	}
}
