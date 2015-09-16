package core.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * An Output Stream.
 */
public class StreamOutput extends BufferedOutputStream {
	
	private static final Logger log = LoggerFactory.getLogger(StreamOutput.class);

	protected Boolean isMultibyteWrite = null;

	public static final String IS_MULTIBYTE_WRITE_SYSVAR_NAME = "Core.writeMultipleBytes";
	
	/** Indicates if this is closed. */
	private boolean closed = false;

	/**
	 * Returns true if closed.
	 * @return true if closed.
	 */
	public boolean isClosed() {
		return closed;
	}

	/**
	 * Close this stream.
	 */
	public void close() throws IOException {
		this.closed = true;
		try {
			super.close();
		} catch (IOException ioe) {
			if (log.isErrorEnabled()) log.error(ioe.getMessage(),ioe);
		}
	}

	/**
	 * Creates a new output stream.
	 * @param os the output stream to wrap.
	 */
	public StreamOutput(OutputStream os) {
		super(os);
		this.initialiseMultiWrite();
	}

	/**
	 * Creates a new output stream.
	 * @param f the file.
	 */
	public StreamOutput(File f) throws FileNotFoundException {
		this(new FileOutputStream(f));
	}


	/**
	 * Creates a new output stream.
	 * @param f the file.
	 * @param append true to append.
	 */
	public StreamOutput(File f, boolean append) throws FileNotFoundException {
		this(new FileOutputStream(f, append));
	}

	/**
	 * Creates a new output stream.
	 * @param f the file.
	 */
	public StreamOutput(File f, int size) throws FileNotFoundException {
		super(new FileOutputStream(f), size);
		this.initialiseMultiWrite();
	}

	/**
	 * Creates a new output stream.
	 * @param fd the file descriptor.
	 */
	public StreamOutput(FileDescriptor fd) throws FileNotFoundException {
		super(new FileOutputStream(fd));
		this.initialiseMultiWrite();
	}

	/**
	 * Creates a new output stream.
	 * @param s the socket.
	 */
	public StreamOutput(Socket s) throws IOException {
		this(s.getOutputStream());
	}

	private void initialiseMultiWrite() {
//		if (TripPlanner.getTripPlanner().getSystemVariableManager() != null) {
//			isMultibyteWrite = TripPlanner.getTripPlanner().getSystemVariableManager().getVariableAsBoolean(IS_MULTIBYTE_WRITE_SYSVAR_NAME);
//		}
	}

	/**
	 * Writes the given string to the output stream.
	 * @param s the string.
	 */
	public void write(String s) throws IOException {
		byte[] bytes = s.getBytes();
		// Default behaviour should be multi-byte write
		if (isMultibyteWrite == null || isMultibyteWrite.booleanValue()) {
			// Call the BufferedOutpuStream write
			super.write(bytes, 0, bytes.length);
		} else {
			// Call the FilterOutpuStream write
			write(s.getBytes());
		}
	}

}
